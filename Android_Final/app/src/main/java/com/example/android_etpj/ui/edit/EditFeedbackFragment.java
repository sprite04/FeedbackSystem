package com.example.android_etpj.ui.edit;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Feedback;
import com.example.android_etpj.models.Module;
import com.example.android_etpj.models.Question;
import com.example.android_etpj.models.Topic;
import com.example.android_etpj.models.TypeFeedback;
import com.example.android_etpj.ui.add.AddFeedbackFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFeedbackFragment extends Fragment {
    public static final String TAG= EditFeedbackFragment.class.getName();

    private MainActivity mainActivity;

    private Spinner spTypeFeedback;
    private EditText edtFeedbackTitle;

    private Button btnReview;
    private Button btnBack;

    private LinearLayout layoutInsert;

    private Feedback feedback;

    private TextView tvTitle;

    private int type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feedback,container,false);

        Bundle bundle=getArguments();
        feedback= (Feedback) bundle.get("FEEDBACK_EDIT");
        type=bundle.getInt("TYPE");

        mainActivity=(MainActivity)getActivity();

        tvTitle=view.findViewById(R.id.tv_title);
        tvTitle.setText("Edit New Feedback");

        spTypeFeedback=view.findViewById(R.id.sp_type_feedback);
        edtFeedbackTitle=view.findViewById(R.id.edt_feedback_title);
        edtFeedbackTitle.setText(feedback.getTitle());

        btnBack=view.findViewById(R.id.btn_back);
        btnReview=view.findViewById(R.id.btn_review);

        layoutInsert=view.findViewById(R.id.layout_insert);


        loadData();
        setSpinnerTypeFeedback();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager()!=null){
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        return view;
    }

    private void save() {
        ApiService.apiService.getTopics().enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                List<Topic> topics=response.body();
                String title=edtFeedbackTitle.getText().toString();



                //Lấy giá trị user admin gán vào đây

                feedback.setAdminID("admin1");

                if(title.isEmpty()==true || title.trim().equals("")||title==null){
                    Dialog dialogFail=new Dialog(mainActivity);
                    dialogFail.setContentView(R.layout.dialog_notification_2);
                    dialogFail.setCancelable(false);

                    Window window=dialogFail.getWindow();
                    if(window==null)
                        return ;
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView tvTitleFail=dialogFail.findViewById(R.id.tv_title);
                    tvTitleFail.setText("Feedback title cannot be empty!");

                    Button btnOk=dialogFail.findViewById(R.id.btn_ok);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogFail.cancel();
                        }
                    });
                    dialogFail.show();
                    return;
                }
                feedback.setTitle(title);
                for(int i=0; i<topics.size();i++){
                    int count=0;
                    for(int j=0; j<feedback.getQuestions().size();j++){
                        Question question=feedback.getQuestions().get(j);
                        if(question.getTopicID()==topics.get(i).getTopicID()){
                            count++;
                        }
                    }
                    if(count==0){
                        Dialog dialogFail=new Dialog(mainActivity);
                        dialogFail.setContentView(R.layout.dialog_notification_2);
                        dialogFail.setCancelable(false);

                        Window window=dialogFail.getWindow();
                        if(window==null)
                            return ;
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView tvTitleFail=dialogFail.findViewById(R.id.tv_title);
                        tvTitleFail.setText("Choose at least 1 question for each topic!");

                        Button btnOk=dialogFail.findViewById(R.id.btn_ok);
                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFail.cancel();
                            }
                        });
                        dialogFail.show();
                        return;
                    }


                }

                mainActivity.reviewEditFeedbackFragment(feedback,type);

            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {

            }
        });
    }

    private void setSpinnerTypeFeedback() {
        ApiService.apiService.getTypeFeedbacks().enqueue(new Callback<List<TypeFeedback>>() {
            @Override
            public void onResponse(Call<List<TypeFeedback>> call, Response<List<TypeFeedback>> response) {
                List<TypeFeedback> arrayList=(ArrayList<TypeFeedback>) response.body();
                List<Object> typefeedbacks=new ArrayList<>();
                typefeedbacks.addAll(arrayList);

                SpinnerAdapter spTypeFeedbackAdapter=new SpinnerAdapter(getContext(),R.layout.item_sp_selected,typefeedbacks);
                spTypeFeedback.setAdapter(spTypeFeedbackAdapter);

                boolean existTypeFeedback=false;
                for(int i=0; i<arrayList.size(); i++){
                    TypeFeedback typeFeedback=arrayList.get(i);
                    if(typeFeedback.getTypeID()==feedback.getTypeFeedbackID()){
                        existTypeFeedback=true;
                        spTypeFeedback.setSelection(spTypeFeedbackAdapter.getPosition(arrayList.get(i)));
                        break;
                    }
                }
                if(arrayList.size()>0&& existTypeFeedback==false){
                    TypeFeedback typeFeedback=arrayList.get(0);
                    feedback.setTypeFeedbackID(typeFeedback.getTypeID());
                }

                spTypeFeedback.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spTypeFeedbackAdapter.getItem(position);
                        if(object instanceof TypeFeedback){
                            TypeFeedback typeFeedback=(TypeFeedback) object;
                            feedback.setTypeFeedbackID(typeFeedback.getTypeID());
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<TypeFeedback>> call, Throwable t) {

            }
        });
    }

    private void loadData() {
        ApiService.apiService.getTopics().enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                List<Topic> topics=response.body();

                for(int i=0; i<topics.size(); i++){
                    for(int j=0; j<topics.get(i).getQuestions().size();j++){
                        for(int k=0; k<feedback.getQuestions().size(); k++){
                            if(feedback.getQuestions().get(k).getQuestionID()==topics.get(i).getQuestions().get(j).getQuestionID()){
                                feedback.getQuestions().get(k).setTopicID(topics.get(i).getTopicID());
                            }
                        }
                    }
                }

                for(int i=0; i<topics.size(); i++) {
                    Topic topic=new Topic();
                    topic=topics.get(i);
                    View viewTopic=getLayoutInflater().inflate(R.layout.item_topic_question,null,false);
                    LinearLayout layoutTopic=viewTopic.findViewById(R.id.layout_topic);
                    TextView tvTitle=new TextView(mainActivity);
                    tvTitle.setText(topic.getTopicName());


                    layoutTopic.addView(tvTitle);

                    for(int j=0; j<topic.getQuestions().size(); j++){
                        Question question=topic.getQuestions().get(j);
                        question.setTopicID(topic.getTopicID());
                        CheckBox chkQuestion=new CheckBox(mainActivity);
                        chkQuestion.setText(question.getQuestionContent());
                        for(int k=0; k<feedback.getQuestions().size(); k++){
                            if(feedback.getQuestions().get(k).getQuestionID()==question.getQuestionID()){
                                chkQuestion.setChecked(true);
                            }
                        }
                        layoutTopic.addView(chkQuestion);
                        chkQuestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked==true){
                                    List<Question> questionList=feedback.getQuestions();
                                    if(questionList==null){
                                        questionList=new ArrayList<>();
                                    }

                                    questionList.add(question);
                                    feedback.setQuestions(questionList);
                                    Log.e("them",String.valueOf(feedback.getQuestions().size()));
                                }
                                else {
                                    List<Question> questionList=feedback.getQuestions();
                                    if(questionList==null){
                                        questionList=new ArrayList<>();
                                    }
                                    for(int z=0; z<feedback.getQuestions().size();z++){
                                        if(feedback.getQuestions().get(z).getQuestionID()==question.getQuestionID()){
                                            questionList.remove(feedback.getQuestions().get(z));
                                        }
                                    }

                                    feedback.setQuestions(questionList);
                                    Log.e("xoa",String.valueOf(feedback.getQuestions().size()));
                                }
                            }
                        });

                    }

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 20, 10, 0);

                    layoutInsert.addView(layoutTopic,layoutParams);
                }

            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {

            }
        });
    }
}
