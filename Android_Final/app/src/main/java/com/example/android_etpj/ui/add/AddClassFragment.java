package com.example.android_etpj.ui.add;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.R;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Module;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClassFragment extends Fragment {
    public static final String TAG=AddClassFragment.class.getName();

    private ImageView btnStartDate;
    private ImageView btnEndDate;

    private TextView tvStartDate;
    private TextView tvEndDate;

    private SimpleDateFormat formatterDate;

    private Calendar calendarStartDate;
    private Calendar calendarEndDate;

    private Calendar calendarPresent;

    private TextView tvErrorName;
    private TextView tvErrorCapicity;
    private TextView tvErrorStartDate;
    private TextView tvErrorEndDate;

    private Button btnSave;
    private Button btnBack;

    private EditText edtName;
    private EditText edtCapicity;

    private Class clss;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_class,container,false);

        btnStartDate=view.findViewById(R.id.img_start_date);
        btnEndDate=view.findViewById(R.id.img_end_date);

        tvStartDate=view.findViewById(R.id.tv_start_date);
        tvEndDate=view.findViewById(R.id.tv_end_date);

        edtName=view.findViewById(R.id.edt_name);
        edtCapicity=view.findViewById(R.id.edt_capacity);


        formatterDate= new SimpleDateFormat("dd/MM/yyyy");

        calendarStartDate=Calendar.getInstance();
        calendarEndDate=Calendar.getInstance();
        calendarPresent=Calendar.getInstance();

        tvErrorName=view.findViewById(R.id.tv_error_name);
        tvErrorCapicity=view.findViewById(R.id.tv_error_capacity);
        tvErrorStartDate=view.findViewById(R.id.tv_error_start_date);
        tvErrorEndDate=view.findViewById(R.id.tv_error_end_date);

        tvErrorName.setVisibility(View.GONE);
        tvErrorCapicity.setVisibility(View.GONE);
        tvErrorStartDate.setVisibility(View.GONE);
        tvErrorEndDate.setVisibility(View.GONE);

        btnSave=view.findViewById(R.id.btn_save);
        btnBack=view.findViewById(R.id.btn_back);

        clss = new Class();

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(calendarStartDate,tvStartDate,formatterDate,1);
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(calendarEndDate,tvEndDate,formatterDate,2);

            }
        });

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
                boolean success=true;
                String strName=edtName.getText().toString().trim();
                String strCapicity = edtCapicity.getText().toString().trim();

                if(strName.isEmpty()==true||strName=="" ||strName==null){
                    tvErrorName.setVisibility(View.VISIBLE);
                    success=false;
                }
                else {
                    if(strName.length()>=255){
                        tvErrorName.setVisibility(View.VISIBLE);
                        success=false;
                    }
                    else {
                        clss.setClassName(strName);
                        tvErrorName.setVisibility(View.GONE);
                    }
                }

                if(strCapicity.isEmpty()==true||strCapicity=="" ||strCapicity==null){
                    tvErrorCapicity.setVisibility(View.VISIBLE);
                    success=false;
                }
                else {
                    if((Integer.parseInt(strCapicity) % 1 != 0) || Integer.parseInt(strCapicity) <= 0){
                        tvErrorCapicity.setVisibility(View.VISIBLE);
                        success=false;
                    }
                    else {
                        clss.setCapacity(Integer.parseInt(strCapicity));
                        tvErrorCapicity.setVisibility(View.GONE);
                    }
                }

                if(clss.getStartTime()==null){
                    tvErrorStartDate.setText("Please choose date or fill mm/dd/yyyy");
                    tvErrorStartDate.setVisibility(View.VISIBLE);
                    success=false;
                }
                else{
                    String strPresent=formatterDate.format(calendarPresent.getTime());
                    String strStartDate=formatterDate.format(clss.getStartTime());

                    Date present= null;
                    Date startDate= null;
                    try {
                        present = formatterDate.parse(strPresent);
                        startDate = formatterDate.parse(strStartDate);

                        if(startDate.compareTo(present)<0){
                            tvErrorStartDate.setText("Please choose date after now date");
                            tvErrorStartDate.setVisibility(View.VISIBLE);
                            success=false;
                        }
                        else {
                            tvErrorStartDate.setVisibility(View.GONE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        success=false;
                    }
                }

                if(clss.getEndTime()==null){
                    tvErrorEndDate.setText("Please choose date or fill mm/dd/yyyy");
                    tvErrorEndDate.setVisibility(View.VISIBLE);
                    success=false;
                }
                else{
                    if(clss.getStartTime()!=null)
                    {
                        String strStartDate=formatterDate.format(clss.getStartTime());
                        String strEndDate=formatterDate.format(clss.getEndTime());

                        Date startDate= null;
                        Date endDate=null;

                        try {

                            startDate = formatterDate.parse(strStartDate);
                            endDate = formatterDate.parse(strEndDate);

                            if(endDate.compareTo(startDate)<0){
                                tvErrorEndDate.setText("Please choose date after start date");
                                tvErrorEndDate.setVisibility(View.VISIBLE);
                                success=false;
                            }
                            else {
                                tvErrorEndDate.setVisibility(View.GONE);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            success=false;
                        }
                    }
                    else {
                        String strPresent=formatterDate.format(calendarPresent.getTime());
                        String strEndDate=formatterDate.format(clss.getEndTime());

                        Date present= null;
                        Date endDate= null;
                        try {
                            present = formatterDate.parse(strPresent);
                            endDate = formatterDate.parse(strEndDate);

                            if(endDate.compareTo(present)<0){
                                tvErrorEndDate.setText("Please choose date after now date");
                                tvErrorEndDate.setVisibility(View.VISIBLE);
                                success=false;
                            }
                            else {
                                tvErrorEndDate.setVisibility(View.GONE);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            success=false;
                        }
                    }

                }
                if(success==true){
                    ApiService.apiService.addClass(clss).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.body()==true){
                                Dialog dialogSuccess=new Dialog(getActivity());
                                dialogSuccess.setContentView(R.layout.dialog_notification);
                                dialogSuccess.setCancelable(false);

                                Window window=dialogSuccess.getWindow();
                                if(window==null)
                                    return ;
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView tvTitleSuccess=dialogSuccess.findViewById(R.id.tv_title);
                                tvTitleSuccess.setText("Add success!");

                                Button btnOk=dialogSuccess.findViewById(R.id.btn_ok);
                                btnOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialogSuccess.cancel();
                                        if(getActivity().getSupportFragmentManager()!=null){
                                            FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                            fragmentManager.popBackStack();
                                        }
                                    }
                                });
                                dialogSuccess.show();
                            }else{
                                Dialog dialog = new Dialog(getActivity());
                                dialog.setContentView(R.layout.dialog_error);
                                dialog.setCancelable(false);

                                Window window = dialog.getWindow();
                                if (window == null)
                                    return;
                                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView tvTitleSuccess=dialog.findViewById(R.id.tv_title);
                                tvTitleSuccess.setText("Add fail!");

                                Button btnOk=dialog.findViewById(R.id.btn_ok_error);
                                btnOk.setOnClickListener(new View.OnClickListener() {
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

                        }
                    });
                }
                Log.e("Success",String.valueOf(success));

            }
        });

        return view;
    }

    private void setDate(Calendar calendar, TextView textView, SimpleDateFormat simpleDateFormat,int type) {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(calendar.MONTH,month);
                calendar.set(calendar.YEAR,year);

                textView.setText(simpleDateFormat.format(calendar.getTime()));

                switch (type){
                    case 1:
                        clss.setStartTime(calendarStartDate.getTime());
                        break;
                    case 2:
                        clss.setEndTime(calendarEndDate.getTime());
                        break;
                }
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                listener,
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
