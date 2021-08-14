package com.example.android_etpj.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;

import android.util.Log;

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
import com.example.android_etpj.interfaces.ExchangeModule;
import com.example.android_etpj.models.*;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>{

    private List<Module> moduleList;
    private Activity activity;
    private ExchangeModule exchange;
    private Object user;


    public void setData(List<Module> list){
        this.moduleList=list;
        notifyDataSetChanged();
    }

    public ModuleAdapter(ExchangeModule exchange, Object user) {
        this.exchange=exchange;
        this.user=user;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity=new Activity();
        activity=(Activity) parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_common,parent,false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module=moduleList.get(position);
        if(module==null)
            return;

        SimpleDateFormat formatterDate= new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterDateTime= new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String displayText="";
        displayText="<b>" + "Module ID: " + "</b> " + module.getModuleID()+"<br>"+
                "<b>" + "Module Name: " + "</b> "+ module.getModuleName()+"<br>"+
                "<b>" + "Admin ID: " + "</b> " + module.getAdminID()+"<br>"+
                "<b>" + "Start Date: " + "</b> "+ (module.getStartTime()!=null ?formatterDate.format(module.getStartTime()):"" )+"<br>"+
                "<b>" + "End Date: " + "</b> " + (module.getEndTime()!=null ?formatterDate.format(module.getEndTime()):"" )+"<br>"+
                "<b>" + "Feedback Title: " + "</b> "+ module.getFeedback().getTitle()+"<br>"+
                "<b>" + "Feedback StartTime: " + "</b> " + (module.getFeedbackStartTime()!=null ? formatterDateTime.format(module.getFeedbackStartTime()):"" )+"<br>"+
                "<b>" + "Feedback EndTime: " + "</b> "+ (module.getFeedbackEndTime()!=null ? formatterDateTime.format(module.getFeedbackEndTime()):"" )
        ;


        holder.tvItem.setText(Html.fromHtml(displayText,1));

        if(user instanceof Admin){

            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setEdit(module);
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDelete(module);
                }
            });
        }
    }

    private void setDelete(Module module) {
        ApiService.apiService.checkModuleUsed(module.getModuleID()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean result=response.body();
                if(result==true){
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
                    tvContent.setText("This Module has been started. You really want to delete this Module?");

                    Button btnYes=dialog.findViewById(R.id.btn_yes);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApiService.apiService.deleteModule(module.getModuleID()).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Boolean deleted=response.body();
                                    if(deleted==true){
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
                    tvContent.setText("Do you want to delete this Module?");

                    Button btnYes=dialog.findViewById(R.id.btn_yes);
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApiService.apiService.deleteModule(module.getModuleID()).enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    Boolean deleted=response.body();
                                    if(deleted==true){
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
            public void onFailure(Call<Boolean> call, Throwable t) {
                dialogFail();
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

    private void dialogSuccess(){

        Dialog dialogSuccess=new Dialog(activity);
        dialogSuccess.setContentView(R.layout.dialog_notification);
        dialogSuccess.setCancelable(false);

        Log.e("hrer",String.valueOf(activity.toString()));

        Window window=dialogSuccess.getWindow();
        Log.e("hrer",String.valueOf(window==null));

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

    private void setEdit(Module module) {

        exchange.editData(module);
    }

    @Override
    public int getItemCount() {
        if(moduleList!=null)
            return moduleList.size();
        return 0;
    }

    public class ModuleViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItem;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tv_content);
            btnEdit=itemView.findViewById(R.id.btn_edit);
            btnDelete=itemView.findViewById(R.id.btn_delete);

            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);

        }
    }
}
