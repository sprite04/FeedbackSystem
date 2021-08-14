package com.example.android_etpj.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.adapter.QuestionAdapter;
import com.example.android_etpj.interfaces.ExchangeQuestion;
import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Admin;
import com.example.android_etpj.models.Question;
import com.example.android_etpj.models.Topic;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment implements ExchangeQuestion {

    private Object user;
    private MainActivity mainActivity;
    private RecyclerView rcvQuestion;
    private QuestionAdapter questionAdapter;
    private TextView tvTitle;
    private TextView tvSpinnerTitle;
    private ImageButton btnAdd;
    private List<Question> questionList;
    private LinearLayout questionLayout;
    private Spinner spSearch;

    public QuestionFragment(Object user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (user instanceof Admin) {
            return setAdminView(inflater, container, savedInstanceState);
        }

        View view=inflater.inflate(R.layout.fragment_access_forbidden_2,container,false);
        return view;
    }

    @Override
    public void loadData(int idTopic) {
        ApiService.apiService.getQuestionByIdTopic(idTopic).enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                questionList = (ArrayList<Question>) response.body();
                tvTitle.setText("Question List");

                questionAdapter.setData(questionList);
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

            }
        });
    }

    @Override
    public void editData(Question question) {
        mainActivity.editQuestionFragment(question);
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
                        loadData(topic.getTopicID());
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

    private View setAdminView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvQuestion=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        tvSpinnerTitle = view.findViewById(R.id.tv_title_search);
        btnAdd=view.findViewById(R.id.btn_add);
        questionLayout = view.findViewById(R.id.spinner_search);
        questionLayout.setVisibility(View.VISIBLE);
        //btnAdd.setVisibility(View.GONE);

        tvSpinnerTitle.setText("Topic Name:");

        spSearch = view.findViewById(R.id.sp_search);
        setSpinnerSearch();

        questionAdapter=new QuestionAdapter(this);
        loadData(1);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvQuestion.setLayoutManager(linearLayoutManager);
        rcvQuestion.setAdapter(questionAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addQuestionFragment();
            }
        });

        return view;
    }
}
