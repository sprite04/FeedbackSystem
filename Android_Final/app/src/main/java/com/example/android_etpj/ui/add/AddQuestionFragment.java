package com.example.android_etpj.ui.add;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Question;
import com.example.android_etpj.models.Topic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQuestionFragment extends Fragment {
    public static final String TAG=AddQuestionFragment.class.getName();

    private TextView tvErrorContent;

    private Spinner spSearch;

    private EditText edtContent;

    private Button btnSave;
    private Button btnBack;

    private Question question;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_question,container,false);

        tvErrorContent = view.findViewById(R.id.tv_error_content);
        spSearch = view.findViewById(R.id.sp_search);
        edtContent = view.findViewById(R.id.edt_content);
        btnBack = view.findViewById(R.id.btn_back);
        btnSave = view.findViewById(R.id.btn_save);
        question = new Question();

        tvErrorContent.setVisibility(View.GONE);

        setSpinnerSearch();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager()!=null){
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                boolean success=true;
                String strContent = edtContent.getText().toString().trim();


                if(strContent.isEmpty()==true||strContent=="" ||strContent==null){
                    tvErrorContent.setText("Please enter the question");
                    tvErrorContent.setVisibility(View.VISIBLE);
                    success=false;
                }
                else {
                    question.setQuestionContent(strContent);
                    tvErrorContent.setVisibility(View.GONE);
                }

                if(success==true){
                    ApiService.apiService.addQuestion(question).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.body()==true){
                                Dialog dialogSuccess=new Dialog(getActivity());
                                dialogSuccess.setContentView(R.layout.dialog_notification);
                                dialogSuccess.setCancelable(false);

                                Window window=dialogSuccess.getWindow();
                                if(window==null)
                                    return ;
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView tvTitleSuccess=dialogSuccess.findViewById(R.id.tv_title);
                                tvTitleSuccess.setText("Add Question success!");

                                Button btnOk=dialogSuccess.findViewById(R.id.btn_ok);
                                btnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogSuccess.cancel();
                                        if(getActivity().getSupportFragmentManager()!=null){
                                            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                            fragmentManager.popBackStack();
                                        }
                                    }
                                });
                                dialogSuccess.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
                }
                Log.e("Success",String.valueOf(success));

            }
        });

        return view;
    }

    private void setSpinnerSearch() {
        ApiService.apiService.getTopics().enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                List<Topic> arrayTopicList=(ArrayList<Topic>) response.body();
                List<Object> topics = new ArrayList<>();
                topics.addAll(arrayTopicList);

                SpinnerAdapter spTopicIDAdapter = new SpinnerAdapter(getContext(),R.layout.item_sp_selected,topics);
                spSearch.setAdapter(spTopicIDAdapter);

                //test thu sau nay doi thanh id cua admin
                if(arrayTopicList.size()>=2) {
                    spSearch.setSelection(spTopicIDAdapter.getPosition(arrayTopicList.get(0)));
                }

                spSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spTopicIDAdapter.getItem(position);
                        Topic topic = (Topic)object;
                        question.setTopicID(topic.getTopicID());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {

            }
        });
    }
}
