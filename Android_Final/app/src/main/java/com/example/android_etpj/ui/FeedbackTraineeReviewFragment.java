package com.example.android_etpj.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.*;
import com.example.android_etpj.ui.edit.ReviewEditFeedbackFragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackTraineeReviewFragment extends Fragment {
    public static final String TAG= FeedbackTraineeReviewFragment.class.getName();

    private ArrayList<Answer> answers;

    private TextView tvModule;
    private TextView tvClass;
    private TextView tvName;
    private Button btnSubmit;

    private LinearLayout linearLayout;
    private MainActivity mainActivity;

    private Assignment assignment;
    private Trainee trainee;
    private EditText edtComment;



    private TextView tvTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_trainee_feedback,container,false);

        tvModule=view.findViewById(R.id.tv_module);
        tvClass=view.findViewById(R.id.tv_class);
        tvName=view.findViewById(R.id.tv_name);
        edtComment=view.findViewById(R.id.edt_comment);

        btnSubmit=view.findViewById(R.id.btn_submit);

        Bundle bundle=getArguments();
        assignment= (Assignment) bundle.get("ASSIGNMENT");
        trainee= (Trainee) bundle.get("TRAINEE");

        if(assignment!=null) {
            tvClass.setText(assignment.getClss().getClassName());
            tvModule.setText(assignment.getModule().getModuleName());
        }

        if(trainee!=null){
            tvName.setText(trainee.getName());
        }


        answers=new ArrayList<Answer>();

        mainActivity=(MainActivity)getActivity();

        linearLayout=view.findViewById(R.id.layout_insert_question);

        loadData();



        return view;
    }

    private void loadData() {
        ApiService.apiService.getTopicsByFeedback(assignment.getModule().getFeedbackID()).enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                List<Topic> topics=response.body();


                for(int i=0; i<topics.size(); i++){
                    TextView tvTopic=new TextView(mainActivity);
                    String strTopic=(i+1)+". "+topics.get(i).getTopicName();
                    tvTopic.setText(strTopic);
                    linearLayout.addView(tvTopic);

                    for(int j=0; j<topics.get(i).getQuestions().size(); j++){
                        View view= getLayoutInflater().inflate(R.layout.item_question_answer,null,false);

                        TextView tvQuestion=view.findViewById(R.id.tv_question);
                        String strQuestion=(j+1)+". "+topics.get(i).getQuestions().get(j).getQuestionContent();
                        tvQuestion.setText(strQuestion);

                        RadioButton rdAnswer1= view.findViewById(R.id.rd_answer1);
                        RadioButton rdAnswer2= view.findViewById(R.id.rd_answer2);
                        RadioButton rdAnswer3= view.findViewById(R.id.rd_answer3);
                        RadioButton rdAnswer4= view.findViewById(R.id.rd_answer4);
                        RadioButton rdAnswer5= view.findViewById(R.id.rd_answer5);

                        Answer answer=new Answer();
                        answer.setClassID(assignment.getClassID());
                        answer.setModuleID(assignment.getModuleID());
                        answer.setQuestionID(topics.get(i).getQuestions().get(j).getQuestionID());
                        answer.setTraineeID(trainee.getUserId());


                        rdAnswer1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked==true){
                                    boolean exist=false;
                                    for(int i=0; i<answers.size();i++){
                                        if(answers.get(i).getClassID()==answer.getClassID()&& answers.get(i).getModuleID()==answer.getModuleID()&&answers.get(i).getQuestionID()==answer.getQuestionID()&&answers.get(i).getTraineeID()==answer.getTraineeID()){
                                            answers.get(i).setValue(0);
                                            exist=true;
                                            break;
                                        }
                                    }
                                    if(exist==false){
                                        answer.setValue(0);
                                        answers.add(answer);
                                    }
                                }
                            }
                        });

                        rdAnswer2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked==true){
                                    boolean exist=false;
                                    for(int i=0; i<answers.size();i++){
                                        if(answers.get(i).getClassID()==answer.getClassID()&& answers.get(i).getModuleID()==answer.getModuleID()&&answers.get(i).getQuestionID()==answer.getQuestionID()&&answers.get(i).getTraineeID()==answer.getTraineeID()){
                                            answers.get(i).setValue(1);
                                            exist=true;
                                            break;
                                        }
                                    }
                                    if(exist==false){
                                        answer.setValue(1);
                                        answers.add(answer);
                                    }
                                }
                            }
                        });

                        rdAnswer3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked==true){
                                    boolean exist=false;
                                    for(int i=0; i<answers.size();i++){
                                        if(answers.get(i).getClassID()==answer.getClassID()&& answers.get(i).getModuleID()==answer.getModuleID()&&answers.get(i).getQuestionID()==answer.getQuestionID()&&answers.get(i).getTraineeID()==answer.getTraineeID()){
                                            answers.get(i).setValue(2);
                                            exist=true;
                                            break;
                                        }
                                    }
                                    if(exist==false){
                                        answer.setValue(2);
                                        answers.add(answer);
                                    }
                                }
                            }
                        });

                        rdAnswer4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked==true){
                                    boolean exist=false;
                                    for(int i=0; i<answers.size();i++){
                                        if(answers.get(i).getClassID()==answer.getClassID()&& answers.get(i).getModuleID()==answer.getModuleID()&&answers.get(i).getQuestionID()==answer.getQuestionID()&&answers.get(i).getTraineeID()==answer.getTraineeID()){
                                            answers.get(i).setValue(4);
                                            exist=true;
                                            break;
                                        }
                                    }
                                    if(exist==false){
                                        answer.setValue(4);
                                        answers.add(answer);
                                    }
                                }
                            }
                        });

                        rdAnswer5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked==true){
                                    boolean exist=false;
                                    for(int i=0; i<answers.size();i++){
                                        if(answers.get(i).getClassID()==answer.getClassID()&& answers.get(i).getModuleID()==answer.getModuleID()&&answers.get(i).getQuestionID()==answer.getQuestionID()&&answers.get(i).getTraineeID()==answer.getTraineeID()){
                                            answers.get(i).setValue(4);
                                            exist=true;
                                            break;
                                        }
                                    }
                                    if(exist==false){
                                        answer.setValue(4);
                                        answers.add(answer);
                                    }
                                }
                            }
                        });

                        linearLayout.addView(view);

                    }

                }

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog dialogConfirm=new Dialog(mainActivity);
                        dialogConfirm.setContentView(R.layout.dialog_warning);
                        dialogConfirm.setCancelable(false);

                        Window window=dialogConfirm.getWindow();
                        if(window==null)
                            return ;
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView tvTitle=dialogConfirm.findViewById(R.id.tv_title);
                        tvTitle.setText("Are you sure?");

                        TextView tvContent=dialogConfirm.findViewById(R.id.tv_content);
                        tvContent.setText("Do you want to submit Feedback? ");


                        Button btnYes=dialogConfirm.findViewById(R.id.btn_yes);
                        btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogConfirm.cancel();
                                if(edtComment.getText().toString().isEmpty()){
                                    dialogWarning();
                                    return;
                                }

                                for(int i=0; i<topics.size(); i++){
                                    for(int k=0; k<topics.get(0).getQuestions().size(); k++){
                                        boolean exist=false;
                                        for(int j=0; j<answers.size(); j++){
                                            if(answers.get(j).getQuestionID()==topics.get(0).getQuestions().get(k).getQuestionID()){
                                                exist=true;
                                                break;
                                            }
                                        }
                                        if(exist==false){
                                            dialogWarning();
                                            return;
                                        }
                                    }

                                }

                                Log.e("thuuu",String.valueOf(answers.size()));

                                String strComment=edtComment.getText().toString();
                                Trainee_Comment traineeComment=new Trainee_Comment(assignment.getClassID(),assignment.getModuleID(),trainee.getUserId(),strComment);

                                Review review=new Review();
                                review.setAnswers(answers);
                                review.setTraineeComment(traineeComment);

                                ApiService.apiService.addAnswers(review).enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                        Boolean result=response.body();
                                        if(result==true){
                                            dialogSuccess();
                                        }
                                        else {
                                            dialogFail();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        dialogFail();
                                        Log.e("Error add answer, comment",t.getMessage());
                                    }
                                });


                            }
                        });

                        Button btnCancel=dialogConfirm.findViewById(R.id.btn_cancel);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogConfirm.cancel();
                            }
                        });
                        dialogConfirm.show();

                    }
                });


            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {
                Log.e("hhhh",t.getMessage());
            }
        });


    }




    private void dialogSuccess() {
        Dialog dialogSuccess=new Dialog(mainActivity);
        dialogSuccess.setContentView(R.layout.dialog_notification);
        dialogSuccess.setCancelable(false);

        Window window=dialogSuccess.getWindow();
        if(window==null)
            return ;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitleSuccess=dialogSuccess.findViewById(R.id.tv_title);
        tvTitleSuccess.setText("Submit Feedback Success");

        Button btnOk=dialogSuccess.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuccess.cancel();
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });
        dialogSuccess.show();
    }

    private void dialogWarning() {
        Dialog dialogWarning=new Dialog(mainActivity);
        dialogWarning.setContentView(R.layout.dialog_notification_2);
        dialogWarning.setCancelable(false);

        Window window=dialogWarning.getWindow();
        if(window==null)
            return ;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitleFail=dialogWarning.findViewById(R.id.tv_title);
        tvTitleFail.setText("Please comple your Feedback !");

        Button btnOk=dialogWarning.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWarning.cancel();
            }
        });
        dialogWarning.show();
    }

    private void dialogFail() {
        Dialog dialogFail=new Dialog(mainActivity);
        dialogFail.setContentView(R.layout.dialog_notification_2);
        dialogFail.setCancelable(false);

        Window window=dialogFail.getWindow();
        if(window==null)
            return ;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitleFail=dialogFail.findViewById(R.id.tv_title);
        tvTitleFail.setText("Add Fail!");

        Button btnOk=dialogFail.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFail.cancel();
            }
        });
        dialogFail.show();
    }
}
