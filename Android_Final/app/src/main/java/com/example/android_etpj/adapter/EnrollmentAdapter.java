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
import com.example.android_etpj.interfaces.ExchangeEnrollment;
import com.example.android_etpj.models.Enrollment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollmentAdapter extends RecyclerView.Adapter<EnrollmentAdapter.EnrollmentViewHolder> {
    private List<Enrollment> enrollmentList;
    private Activity activity;
    private ExchangeEnrollment exchange;

    private SimpleDateFormat formatterDate;

    public EnrollmentAdapter(ExchangeEnrollment exchange) {
        this.exchange = exchange;
    }

    @NonNull
    @Override
    public EnrollmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        formatterDate= new SimpleDateFormat("dd/MM/yyyy");

        activity = new Activity();
        activity = (Activity) parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_common, parent, false);
        return new EnrollmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrollmentViewHolder holder, int position) {
        Enrollment enrollment = enrollmentList.get(position);
        if (enrollment == null)
            return;

        String displayText = "";
        displayText = "<b>" + "Trainee ID: " + "</b> " + enrollment.getTraineeId() + "<br>" +
                "<b>" + "Trainee Name: " + "</b> " + enrollment.getTrainee().getName().toString().trim() + "<br>" +
                "<b>" + "Class ID: " + "</b> " + String.valueOf(enrollment.getClassId()) + "<br>" +
                "<b>" + "Class Name: " + "</b> " + enrollment.getaClass().getClassName().toString().trim() + "<br>";


        holder.tvItem.setText(Html.fromHtml(displayText, 1));

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdit(enrollment);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDelete(enrollment);
            }
        });
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setView(enrollment);
            }
        });
    }

    private void setView(Enrollment enrollment) {
        exchange.viewData(enrollment);
    }

    private void setDelete(Enrollment enrollment) {
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
        String strEndtime = formatterDate.format(enrollment.getaClass().getEndTime());
        String strCurrent=formatterDate.format(new Date());

        Date date;
        Date endTime;
        try {
            endTime=formatterDate.parse(strEndtime);
            date = formatterDate.parse(strCurrent);
            if (date.compareTo(endTime) < 0)
                tvContent.setText("Class is not over. Do you want to remove the trainee from the classroom?");
            else
                tvContent.setText("Do you want to delete this item ?");
        } catch (ParseException e) {
            tvContent.setText("Do you want to delete this item ?");
        }

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.apiService.deleteEnrollment(enrollment.getClassId(), enrollment.getTraineeId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Boolean result = response.body();
                        if (result == true) {
                            Boolean deleted = response.body();
                            if (deleted == true) {
                                dialog.cancel();
                                //create dialog success
                                dialogSuccess();
                                exchange.loadData();
                            }
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

    private void setEdit(Enrollment enrollment) {
        exchange.editData(enrollment);
    }

    @Override
    public int getItemCount() {
        if (enrollmentList != null)
            return enrollmentList.size();
        return 0;
    }

    public void setData(List<Enrollment> enrollmentList) {
        this.enrollmentList = enrollmentList;
        notifyDataSetChanged();
    }

    public class EnrollmentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItem;
        private ImageButton btnEdit;
        private ImageButton btnDelete;
        private ImageButton btnView;

        public EnrollmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_content);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnView = itemView.findViewById(R.id.btn_view);
            btnView.setVisibility(View.VISIBLE);


        }
    }

    private void dialogSuccess() {
        Dialog dialogSuccess = new Dialog(activity);
        dialogSuccess.setContentView(R.layout.dialog_notification);
        dialogSuccess.setCancelable(false);

        Window window = dialogSuccess.getWindow();
        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitleSuccess = dialogSuccess.findViewById(R.id.tv_title);
        tvTitleSuccess.setText("Delete Success!");

        Button btnOk = dialogSuccess.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSuccess.cancel();
            }
        });
    }
}
