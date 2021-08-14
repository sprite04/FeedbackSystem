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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.R;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeFeedback;
import com.example.android_etpj.models.Feedback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>{

    private List<Feedback> feedbackList;
    private Activity activity;
    private ExchangeFeedback exchange;

    public void setData(List<Feedback> list){
        this.feedbackList=list;
        notifyDataSetChanged();
    }

    public FeedbackAdapter(ExchangeFeedback exchange) {
        this.exchange=exchange;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity=new Activity();
        activity=(Activity) parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_common,parent,false);
        return new FeedbackAdapter.FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback=feedbackList.get(position);
        if(feedback==null)
            return;



        String displayText="";
        displayText="<b>" + "Feedback ID: " + "</b> " + feedback.getFeedbackID()+"<br>"+
                "<b>" + "Title: " + "</b> "+ feedback.getTitle()+"<br>"+
                "<b>" + "Admin ID: " + "</b> " + feedback.getAdminID()+"<br>";        ;


        holder.tvItem.setText(Html.fromHtml(displayText,1));
        holder.tvItem.setTextSize(20f);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdit(feedback);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelete(feedback);
            }
        });

        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView(feedback);
            }
        });

    }

    private void setView(Feedback feedback) {
        exchange.viewData(feedback);
    }

    private void setDelete(Feedback feedback) {

        ApiService.apiService.checkFeedbackUsed(feedback.getFeedbackID()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Integer result=response.body();
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
                    String message="This Feedback is being used by "+result+" module. Do you really want to delete this feedback?";
                    tvContent.setText(message);

                    Button btnYes=dialog.findViewById(R.id.btn_yes);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApiService.apiService.deleteFeedback(feedback.getFeedbackID()).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Boolean delete=response.body();
                                    if(delete==true){
                                        dialog.cancel();
                                        //create dialog success
                                        dialogSuccess();

                                        exchange.loadData();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {

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
                    tvContent.setText("Do you want to delete this item?");

                    Button btnYes=dialog.findViewById(R.id.btn_yes);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApiService.apiService.deleteFeedback(feedback.getFeedbackID()).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Boolean delete=response.body();
                                    if(delete==true){
                                        dialog.cancel();
                                        //create dialog success
                                        dialogSuccess();

                                        exchange.loadData();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {

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
                dialogFail();
            }
        });

    }

    private void dialogSuccess() {
        Dialog dialogSuccess=new Dialog(activity);
        dialogSuccess.setContentView(R.layout.dialog_notification);
        dialogSuccess.setCancelable(false);

        Window window=dialogSuccess.getWindow();
        if(window==null)
            return ;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitleSuccess=dialogSuccess.findViewById(R.id.tv_title);
        tvTitleSuccess.setText("Delete Success!");

        Button btnOk=dialogSuccess.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuccess.cancel();
            }
        });
        dialogSuccess.show();
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
        tvTitleFail.setText("Delete Fail!");

        Button btnOk=dialogFail.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFail.cancel();
            }
        });
        dialogFail.show();
    }

    private void setEdit(Feedback feedback) {
        exchange.editData(feedback);
    }

    @Override
    public int getItemCount() {
        if(feedbackList!=null)
            return feedbackList.size();
        return 0;
    }

    public class FeedbackViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItem;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        private ImageButton btnView;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tv_content);
            btnEdit=itemView.findViewById(R.id.btn_edit);
            btnDelete=itemView.findViewById(R.id.btn_delete);
            btnView=itemView.findViewById(R.id.btn_view);
            btnView.setVisibility(View.VISIBLE);

        }
    }
}
