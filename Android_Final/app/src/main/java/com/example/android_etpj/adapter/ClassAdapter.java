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

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.interfaces.ExchangeClass;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Admin;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Enrollment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private Object user;
    private List<Class> classList;
    private Activity activity;
    private ExchangeClass exchange;
    private MainActivity mainActivity;

    public void setData(List<Class> list){
        this.classList=list;
        notifyDataSetChanged();
    }

    public ClassAdapter(ExchangeClass exchange, Object user, MainActivity mainActivity) {
        this.exchange = exchange;
        this.user = user;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        activity=new Activity();
        activity=(Activity) parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_common,parent,false);
        return new ClassAdapter.ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class clss =classList.get(position);
        if(clss==null)
            return;

        //Phan quyen trainer va trainee
        if (!(user instanceof Admin)) {
            holder.btnView.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.GONE);



            holder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.viewClassFragment(clss);
                }
            });



            String displayText="";
            displayText="<b>" + "Class ID: " + "</b> " + clss.getClassID()+"<br>"+
                    "<b>" + "Class Name: " + "</b> "+ clss.getClassName()+"<br>"
            ;

            ApiService.apiService.getEnrollmentByIdClass(clss.getClassID()).enqueue(new Callback<List<Enrollment>>() {
                @Override
                public void onResponse(Call<List<Enrollment>> call, Response<List<Enrollment>> response) {
                    List<Enrollment> enrollmentList = (ArrayList<Enrollment>) response.body();
                    String displayText="";
                    displayText="<b>" + "Class ID: " + "</b> " + clss.getClassID()+"<br>"+
                            "<b>" + "Class Name: " + "</b> "+ clss.getClassName()+"<br>" +
                            "<b>" + "Number of Trainee: " + "</b> "+ enrollmentList.size() +"<br>"
                    ;

                    holder.tvItem.setText(Html.fromHtml(displayText,1));
                }

                @Override
                public void onFailure(Call<List<Enrollment>> call, Throwable t) {

                }
            });

            return;
        }

        SimpleDateFormat formatterDate= new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatterDateTime= new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String displayText="";
        displayText="<b>" + "Class ID: " + "</b> " + clss.getClassID()+"<br>"+
                "<b>" + "Class Name: " + "</b> "+ clss.getClassName()+"<br>"+
                "<b>" + "Capacity: " + "</b> " + clss.getCapacity()+"<br>"+
                "<b>" + "Start Date: " + "</b> "+ (clss.getStartTime()!=null ?formatterDate.format(clss.getStartTime()):"" )+"<br>"+
                "<b>" + "End Date: " + "</b> " + (clss.getEndTime()!=null ?formatterDate.format(clss.getEndTime()):"" )+"<br>"

        ;


        holder.tvItem.setText(Html.fromHtml(displayText,1));




        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdit(clss);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelete(clss);
            }
        });

    }

    private void setDelete(Class clss) {
        if (new Date().before(clss.getStartTime()) || new Date().after(clss.getEndTime())) {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_warning);
            dialog.setCancelable(false);

            Window window = dialog.getWindow();
            if (window == null)
                return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView tvTitle = dialog.findViewById(R.id.tv_title);
            tvTitle.setText("Are you sure?");

            TextView tvContent = dialog.findViewById(R.id.tv_content);
            tvContent.setText("Do you want to delete this item?");

            Button btnYes = dialog.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiService.apiService.deleteClass(clss.getClassID()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            Boolean deleted = response.body();
                            if (deleted == true) {
                                dialog.cancel();

                                //create dialog success
                                dialogSuccess();

                                exchange.loadData();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                            dialogFail();

                        }
                    });
                }
            });

            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();
        } else {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_warning);
            dialog.setCancelable(false);

            Window window = dialog.getWindow();
            if (window == null)
                return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView tvTitle = dialog.findViewById(R.id.tv_title);
            tvTitle.setText("Are you sure?");

            TextView tvContent = dialog.findViewById(R.id.tv_content);
            tvContent.setText("Class is opearting. Do you really want to delete this item?");

            Button btnYes = dialog.findViewById(R.id.btn_yes);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApiService.apiService.deleteClass(clss.getClassID()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            Boolean deleted = response.body();
                            if (deleted == true) {
                                dialog.cancel();

                                //create dialog success

                                dialogSuccess();

                                exchange.loadData();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                            dialogFail();

                        }
                    });
                }
            });

            Button btnCancel = dialog.findViewById(R.id.btn_cancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();
        }

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

    private void setEdit(Class clss) {

        exchange.editData(clss);
    }



    @Override
    public int getItemCount() {
        if(classList!=null)
            return classList.size();
        return 0;
    }

    public class ClassViewHolder extends  RecyclerView.ViewHolder{
        private TextView tvItem;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        private ImageButton btnView;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem=itemView.findViewById(R.id.tv_content);
            btnEdit=itemView.findViewById(R.id.btn_edit);
            btnDelete=itemView.findViewById(R.id.btn_delete);
            btnView=itemView.findViewById(R.id.btn_view);

        }
    }

}
