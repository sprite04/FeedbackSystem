package com.example.android_etpj.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.R;
import com.example.android_etpj.interfaces.ExchangeQuestion;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Question;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<Question> questionList;
    private Activity activity;
    private ExchangeQuestion exchange;
    private Date date;

    public void setData(List<Question> list){
        this.questionList=list;
        notifyDataSetChanged();
    }

    public QuestionAdapter(ExchangeQuestion exchange) {
        this.exchange = exchange;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity=new Activity();
        activity=(Activity) parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_common,parent,false);
        return new QuestionAdapter.QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        if(question == null)
            return;

        String displayText="";
        displayText="<b>" + "Topic ID: " + "</b> " + question.getTopicID()+"<br>"+
                "<b>" + "Topic Name: " + "</b> "+ question.getTopic().getTopicName()+"<br>"+
                "<b>" + "Question ID: " + "</b> " + question.getQuestionID()+"<br>"+
                "<b>" + "Question Content: " + "</b> "+ (question.getQuestionContent())+"<br>"
        ;


        holder.tvItem.setText(Html.fromHtml(displayText,1));

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdit(question);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelete(question);
            }
        });
    }

    private void setDelete(Question question) {
        ApiService.apiService.checkQuestionUsed(question.getQuestionID()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int result = response.body();
                if(result>0){
                    Dialog dialog=new Dialog(activity);
                    dialog.setContentView(R.layout.dialog_warning);
                    dialog.setCancelable(false);

                    Window window=dialog.getWindow();
                    if(window==null)
                        return ;
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView tvTitle=dialog.findViewById(R.id.tv_title);
                    tvTitle.setText("Are you sure?");

                    TextView tvContent=dialog.findViewById(R.id.tv_content);
                    tvContent.setText("This Question is in use with " + result + " Feedback. You really want to delete this Question?");

                    Button btnYes=dialog.findViewById(R.id.btn_yes);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApiService.apiService.deleteQuestion(question.getQuestionID()).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Boolean deleted=response.body();
                                    if(deleted==true){
                                        dialog.cancel();

                                        //create dialog success
                                        dialogSuccess();

                                        exchange.loadData(1);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {

                                    dialogFail();

                                }
                            });
                        }
                    });

                    Button btnCancel=dialog.findViewById(R.id.btn_cancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();
                }
                else {
                    Dialog dialog=new Dialog(activity);
                    dialog.setContentView(R.layout.dialog_warning);
                    dialog.setCancelable(false);

                    Window window=dialog.getWindow();
                    if(window==null)
                        return ;
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView tvTitle=dialog.findViewById(R.id.tv_title);
                    tvTitle.setText("Are you sure?");

                    TextView tvContent=dialog.findViewById(R.id.tv_content);
                    tvContent.setText("Do you want to delete this Question?");

                    Button btnYes=dialog.findViewById(R.id.btn_yes);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApiService.apiService.deleteQuestion(question.getQuestionID()).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Boolean deleted=response.body();
                                    if(deleted==true){
                                        dialog.cancel();

                                        //create dialog success
                                        dialogSuccess();

                                        exchange.loadData(1);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {

                                    dialogFail();

                                }
                            });
                        }
                    });

                    Button btnCancel=dialog.findViewById(R.id.btn_cancel);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }


    private void dialogFail() {
        Dialog dialogFail=new Dialog(activity);
        dialogFail.setContentView(R.layout.dialog_notification_2);
        dialogFail.setCancelable(false);

        Window window=dialogFail.getWindow();
        if(window==null)
            return ;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitleFail=dialogFail.findViewById(R.id.tv_title);
        tvTitleFail.setText("Add Question Fail!");

        Button btnOk=dialogFail.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFail.cancel();
            }
        });
        dialogFail.show();
    }



    private void dialogSuccess(){
        Dialog dialogSuccess=new Dialog(activity);
        dialogSuccess.setContentView(R.layout.dialog_notification);
        dialogSuccess.setCancelable(false);

        Window window=dialogSuccess.getWindow();
        if(window==null)
            return ;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitleSuccess=dialogSuccess.findViewById(R.id.tv_title);

        tvTitleSuccess.setText("Add Question Success!");


        Button btnOk=dialogSuccess.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuccess.cancel();
            }
        });

        dialogSuccess.show();

    }

    private void setEdit(Question question) {

        exchange.editData(question);
    }

    @Override
    public int getItemCount() {
        if(questionList!=null)
            return questionList.size();
        return 0;
    }

    public class QuestionViewHolder extends  RecyclerView.ViewHolder{
        private TextView tvItem;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tv_content);
            btnEdit=itemView.findViewById(R.id.btn_edit);
            btnDelete=itemView.findViewById(R.id.btn_delete);

        }
    }
}
