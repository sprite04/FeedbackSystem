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
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeFeedbackTrainee;
import com.example.android_etpj.models.*;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackTraineeAdapter extends RecyclerView.Adapter<FeedbackTraineeAdapter.FeedbackTraineeViewHolder>{

    private List<Assignment> assignmentList;
    private Activity activity;
    private ExchangeFeedbackTrainee exchange;
    private Trainee trainee;

    public void setData(List<Assignment> list){
        this.assignmentList=list;
        notifyDataSetChanged();
    }

    public FeedbackTraineeAdapter(ExchangeFeedbackTrainee exchange, Trainee trainee) {
        this.exchange=exchange;
        this.trainee=trainee;
    }

    @NonNull
    @Override
    public FeedbackTraineeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity=new Activity();
        activity=(Activity) parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_common,parent,false);
        return new FeedbackTraineeAdapter.FeedbackTraineeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackTraineeViewHolder holder, int position) {
        Assignment assignment=assignmentList.get(position);
        if(assignment==null)
            return;



        ApiService.apiService.checkAnswerUsed(assignment.getClassID(),assignment.getModuleID(),trainee.getUserId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean result=response.body();
                if(result==true){
                    SimpleDateFormat formatterDateTime= new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    String displayText="";
                    displayText="<b>" + "Feedback Title: " + "</b> " + assignment.getModule().getFeedback().getTitle()+"<br>"+
                            "<b>" + "Class ID: " + "</b> "+ assignment.getClassID()+"<br>"+
                            "<b>" + "Class Name: " + "</b> "+ assignment.getClss().getClassName()+"<br>"+
                            "<b>" + "Module ID: " + "</b> "+ assignment.getModuleID()+"<br>"+
                            "<b>" + "Module Name: " + "</b> "+ assignment.getModule().getModuleName()+"<br>"+
                            "<b>" + "End Time: " + "</b> "+ (assignment.getModule().getFeedbackEndTime()!=null ? formatterDateTime.format(assignment.getModule().getFeedbackEndTime()):"" )+"<br>"+
                            "<b>" + "Status: " + "</b> " + "Complete"+"<br>";    ;


                    holder.btnEditFB.setVisibility(View.GONE);
                    holder.tvItem.setText(Html.fromHtml(displayText,1));
                    holder.tvItem.setTextSize(20f);
                }
                else {
                    SimpleDateFormat formatterDateTime= new SimpleDateFormat("dd/MM/yyyy HH:mm");

                    String displayText="";
                    displayText="<b>" + "Feedback Title: " + "</b> " + assignment.getModule().getFeedback().getTitle()+"<br>"+
                            "<b>" + "Class ID: " + "</b> "+ assignment.getClassID()+"<br>"+
                            "<b>" + "Class Name: " + "</b> "+ assignment.getClss().getClassName()+"<br>"+
                            "<b>" + "Module ID: " + "</b> "+ assignment.getModuleID()+"<br>"+
                            "<b>" + "Module Name: " + "</b> "+ assignment.getModule().getModuleName()+"<br>"+
                            "<b>" + "End Time: " + "</b> "+ (assignment.getModule().getFeedbackEndTime()!=null ? formatterDateTime.format(assignment.getModule().getFeedbackEndTime()):"" )+"<br>"+
                            "<b>" + "Status: " + "</b> " + "InComplete"+"<br>";    ;


                    holder.tvItem.setText(Html.fromHtml(displayText,1));
                    holder.tvItem.setTextSize(20f);
                    holder.btnEditFB.setVisibility(View.VISIBLE);
                    holder.btnEditFB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            exchange.reviewData(assignment,trainee);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });



    }



    @Override
    public int getItemCount() {
        if(assignmentList!=null)
            return assignmentList.size();
        return 0;
    }

    public class FeedbackTraineeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItem;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        private ImageButton btnEditFB;


        public FeedbackTraineeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tv_content);
            btnEdit=itemView.findViewById(R.id.btn_edit);
            btnEdit.setVisibility(View.GONE);
            btnDelete=itemView.findViewById(R.id.btn_delete);
            btnDelete.setVisibility(View.GONE);
            btnEditFB=itemView.findViewById(R.id.btn_edit_fb);


        }
    }
}
