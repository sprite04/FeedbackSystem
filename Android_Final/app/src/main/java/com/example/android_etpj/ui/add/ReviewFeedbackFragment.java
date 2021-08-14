package com.example.android_etpj.ui.add;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Feedback;
import com.example.android_etpj.models.FeedbackQuestion;
import com.example.android_etpj.models.Question;
import com.example.android_etpj.models.Topic;
import com.example.android_etpj.ui.edit.ReviewEditFeedbackFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFeedbackFragment extends Fragment {
    public static final String TAG= ReviewFeedbackFragment.class.getName();

    private Feedback feedback;
    private Button btnSave;
    private Button btnBack;
    private TextView tvTitle;
    private LinearLayout linearLayout;
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feedback_review,container,false);

        Bundle bundle=getArguments();
        feedback= (Feedback) bundle.get("FEEDBACKREVIEW");

        btnSave=view.findViewById(R.id.btn_save);
        btnSave.setText("Edit");

        btnBack=view.findViewById(R.id.btn_back);

        mainActivity=(MainActivity)getActivity();

        tvTitle=view.findViewById(R.id.tv_title);
        tvTitle.setText("Detail Feedback");

        linearLayout=view.findViewById(R.id.layout_insert_review);

        loadData();
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
                //2 dùng để thoát 3 lần
                mainActivity.editFeedbackFragment(feedback,2);

            }
        });

        return view;
    }

    private void loadData() {
        TextView tvTitle=new TextView(mainActivity);
        String title="Feedback Title: "+"<span style=\"color: red;\">"+feedback.getTitle()+"</span>";
        tvTitle.setText(Html.fromHtml(title,1));
        tvTitle.setTypeface(Typeface.SERIF);
        linearLayout.addView(tvTitle);

        TextView tvAdminID=new TextView(mainActivity);
        String adminID="Admin ID: "+"<span style=\"color: red;\">"+feedback.getAdminID()+"</span>";
        tvAdminID.setText(Html.fromHtml(adminID,2));
        tvAdminID.setTypeface(Typeface.SERIF);
        linearLayout.addView(tvAdminID);

        ApiService.apiService.getTopics().enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                List<Topic> topics=response.body();
                StringBuilder stringBuilder=new StringBuilder();
                TextView tvContent=new TextView(mainActivity);
                tvContent.setTypeface(Typeface.SERIF);
                for(int i=0; i<topics.size(); i++){
                    Topic topic=new Topic();
                    topic=topics.get(i);
                    String strTopic="<b>" + topic.getTopicName() + "</b>" +"<br>";
                    stringBuilder.append(strTopic);


                    for(int j=0; j<topics.get(i).getQuestions().size();j++){
                        Question question=topic.getQuestions().get(j);
                        for(int t=0; t<feedback.getQuestions().size(); t++){
                            if(feedback.getQuestions().get(t).getQuestionID()==question.getQuestionID()){
                                String strQuestion="- "+question.getQuestionContent()+"<br>";
                                stringBuilder.append(strQuestion);

                            }
                        }


                    }
                }
                tvContent.setText(Html.fromHtml(stringBuilder.toString(),1));

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 20, 0, 0);

                linearLayout.addView(tvContent,layoutParams);
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {

            }
        });


    }


}
