package com.example.android_etpj.ui.add;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddModuleFragment extends Fragment {

    public static final String TAG=AddModuleFragment.class.getName();
    private ImageView btnStartDate;
    private ImageView btnEndDate;
    private ImageView btnFBStartDate;
    private ImageView btnFBEndDate;

    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvFBStartDate;
    private TextView tvFBEndDate;

    private SimpleDateFormat formatterDate;
    private SimpleDateFormat formatterFeedbackDate;

    private Calendar calendarStartDate;
    private Calendar calendarEndDate;
    private Calendar calendarFBStartDate;
    private Calendar calendarFBEndDate;
    private Calendar calendarPresent;

    private Spinner spAdminID;
    private Spinner spFeedbackTitle;

    private TextView tvErrorName;
    private TextView tvErrorStartDate;
    private TextView tvErrorEndDate;
    private TextView tvErrorFBStartDate;
    private TextView tvErrorFBEndDate;

    private EditText edtName;

    private Button btnSave;
    private Button btnBack;


    private Admin user;

    private Module module;
    Activity activity;

    public AddModuleFragment(Admin user) {
        this.user=user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_module,container,false);

        activity=(Activity) container.getContext();
        btnStartDate=view.findViewById(R.id.img_start_date);
        btnEndDate=view.findViewById(R.id.img_end_date);
        btnFBStartDate=view.findViewById(R.id.img_fb_start_date);
        btnFBEndDate=view.findViewById(R.id.img_fb_end_date);

        tvStartDate=view.findViewById(R.id.tv_start_date);
        tvEndDate=view.findViewById(R.id.tv_end_date);
        tvFBStartDate=view.findViewById(R.id.tv_fb_start_date);
        tvFBEndDate=view.findViewById(R.id.tv_fb_end_date);

        edtName=view.findViewById(R.id.edt_name);

        formatterDate= new SimpleDateFormat("dd/MM/yyyy");
        formatterFeedbackDate= new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

        calendarStartDate=Calendar.getInstance();
        calendarEndDate=Calendar.getInstance();
        calendarFBStartDate=Calendar.getInstance();
        calendarFBEndDate=Calendar.getInstance();
        calendarPresent=Calendar.getInstance();



        spAdminID=view.findViewById(R.id.sp_admin_id);
        spFeedbackTitle=view.findViewById(R.id.sp_feedback_title);

        tvErrorName=view.findViewById(R.id.tv_error_name);
        tvErrorStartDate=view.findViewById(R.id.tv_error_start_date);
        tvErrorEndDate=view.findViewById(R.id.tv_error_end_date);
        tvErrorFBStartDate=view.findViewById(R.id.tv_error_fb_start_date);
        tvErrorFBEndDate=view.findViewById(R.id.tv_error_fb_end_date);

        tvErrorName.setVisibility(View.GONE);
        tvErrorStartDate.setVisibility(View.GONE);
        tvErrorEndDate.setVisibility(View.GONE);
        tvErrorFBStartDate.setVisibility(View.GONE);
        tvErrorFBEndDate.setVisibility(View.GONE);

        btnSave=view.findViewById(R.id.btn_save);
        btnBack=view.findViewById(R.id.btn_back);

        module=new Module();
        //chú ý cần gán giá trị của adminID cho module nữa khi admin dùng tính năng này


        setSpinnerAdmin();

        setSpinnerFeedback();


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

        btnFBStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(calendarFBStartDate,tvFBStartDate,formatterFeedbackDate,3);
                setDate(calendarFBStartDate,tvFBStartDate,formatterFeedbackDate,3);

            }
        });

        btnFBEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(calendarFBEndDate,tvFBEndDate,formatterFeedbackDate,3);
                setDate(calendarFBEndDate,tvFBEndDate,formatterFeedbackDate,4);

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
                        module.setModuleName(strName);
                        tvErrorName.setVisibility(View.GONE);
                    }
                }

                if(module.getStartTime()==null){
                    tvErrorStartDate.setText("Please choose start date or fill mm/dd/yyyy");
                    tvErrorStartDate.setVisibility(View.VISIBLE);
                    success=false;
                }
                else{
                    String strPresent=formatterDate.format(calendarPresent.getTime());
                    String strStartDate=formatterDate.format(module.getStartTime());

                    Date present= null;
                    Date startDate= null;
                    try {
                        present = formatterDate.parse(strPresent);
                        startDate = formatterDate.parse(strStartDate);

                        if(startDate.compareTo(present)<0){
                            tvErrorStartDate.setText("Please choose start date after now date");
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

                if(module.getEndTime()==null){
                    tvErrorEndDate.setText("Please choose end date or fill mm/dd/yyyy");
                    tvErrorEndDate.setVisibility(View.VISIBLE);
                    success=false;
                }
                else{
                    if(module.getStartTime()!=null)
                    {
                        String strStartDate=formatterDate.format(module.getStartTime());
                        String strEndDate=formatterDate.format(module.getEndTime());

                        Date startDate= null;
                        Date endDate=null;

                        try {

                            startDate = formatterDate.parse(strStartDate);
                            endDate = formatterDate.parse(strEndDate);

                            if(endDate.compareTo(startDate)<0){
                                tvErrorEndDate.setText("Please choose end date after start date");
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
                        String strEndDate=formatterDate.format(module.getEndTime());

                        Date present= null;
                        Date endDate= null;
                        try {
                            present = formatterDate.parse(strPresent);
                            endDate = formatterDate.parse(strEndDate);

                            if(endDate.compareTo(present)<0){
                                tvErrorEndDate.setText("Please choose end date after now date");
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

                if(module.getFeedbackStartTime()==null){
                    tvErrorFBStartDate.setText("Please choose Feedback start date or fill mm/dd/yyyy");
                    tvErrorFBStartDate.setVisibility(View.VISIBLE);
                    success=false;
                }
                else{
                    String strPresent=formatterFeedbackDate.format(calendarPresent.getTime());
                    String strFBStartDate=formatterFeedbackDate.format(module.getFeedbackStartTime());

                    Date present= null;
                    Date fbStartDate= null;
                    try {
                        present = formatterFeedbackDate.parse(strPresent);
                        fbStartDate = formatterFeedbackDate.parse(strFBStartDate);

                        if(fbStartDate.compareTo(present)<0){
                            tvErrorFBStartDate.setText("Please choose Feedback start date after now date");
                            tvErrorFBStartDate.setVisibility(View.VISIBLE);
                            success=false;
                        }
                        else {
                            tvErrorFBStartDate.setVisibility(View.GONE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        success=false;
                    }
                }

                if(module.getFeedbackEndTime()==null){
                    tvErrorFBEndDate.setText("Please choose Feedback end date or fill mm/dd/yyyy");
                    tvErrorFBEndDate.setVisibility(View.VISIBLE);
                    success=false;
                }
                else{
                    if(module.getFeedbackStartTime()!=null)
                    {
                        String strFBStartDate=formatterFeedbackDate.format(module.getFeedbackStartTime());
                        String strFBEndDate=formatterFeedbackDate.format(module.getFeedbackEndTime());

                        Date fbStartDate= null;
                        Date fbEndDate=null;

                        try {

                            fbStartDate = formatterFeedbackDate.parse(strFBStartDate);
                            fbEndDate = formatterFeedbackDate.parse(strFBEndDate);

                            if(fbEndDate.compareTo(fbStartDate)<0){
                                tvErrorFBEndDate.setText("Please choose Feedback end date after Feedback start date");
                                tvErrorFBEndDate.setVisibility(View.VISIBLE);
                                success=false;
                            }
                            else {
                                tvErrorFBEndDate.setVisibility(View.GONE);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            success=false;
                        }
                    }
                    else {
                        String strPresent=formatterFeedbackDate.format(calendarPresent.getTime());
                        String strFBEndDate=formatterFeedbackDate.format(module.getFeedbackEndTime());

                        Date present= null;
                        Date fbEndDate= null;
                        try {
                            present = formatterFeedbackDate.parse(strPresent);
                            fbEndDate = formatterFeedbackDate.parse(strFBEndDate);

                            if(fbEndDate.compareTo(present)<0){
                                tvErrorFBEndDate.setText("Please choose Feedback end date after now date");
                                tvErrorFBEndDate.setVisibility(View.VISIBLE);
                                success=false;
                            }
                            else {
                                tvErrorFBEndDate.setVisibility(View.GONE);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            success=false;

                        }
                    }

                }
                if(success==true){
                    ApiService.apiService.addModule(module).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.body()==true){
                                dialogSuccess();
                            }
                            else {
                                dialogFail();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Log.e("ErrorAddModuleFragment",t.getMessage());
                            dialogFail();
                        }
                    });
                }

            }
        });

        return view;
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
        tvTitleSuccess.setText("Add Success!");

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
    }

    private void setSpinnerFeedback() {
        ApiService.apiService.getFeedbacks().enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                List<Feedback> arrayList=(ArrayList<Feedback>) response.body();
                List<Object> feedbacks=new ArrayList<>();
                feedbacks.addAll(arrayList);



                SpinnerAdapter spFeedbackTitleAdapter=new SpinnerAdapter(getContext(),R.layout.item_sp_selected,feedbacks);
                spFeedbackTitle.setAdapter(spFeedbackTitleAdapter);

                if(arrayList.size()>0){
                    Feedback feedback=arrayList.get(0);
                    module.setFeedbackID(feedback.getFeedbackID());
                }

                spFeedbackTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spFeedbackTitleAdapter.getItem(position);
                        if(object instanceof Feedback){
                            Feedback feedback=(Feedback) object;
                            module.setFeedbackID(feedback.getFeedbackID());
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {

            }
        });
    }

    private void setSpinnerAdmin() {
        ApiService.apiService.getAdmins().enqueue(new Callback<List<Admin>>() {
            @Override
            public void onResponse(Call<List<Admin>> call, Response<List<Admin>> response) {
                List<Admin> arrayList=(ArrayList<Admin>) response.body();
                List<Object> admins=new ArrayList<>();
                admins.addAll(arrayList);

                SpinnerAdapter spAdminIDAdapter=new SpinnerAdapter(getContext(),R.layout.item_sp_selected,admins);
                spAdminID.setAdapter(spAdminIDAdapter);

                boolean existAdmin=false;
                for(int i=0; i<arrayList.size();i++){
                    Admin admin=arrayList.get(i);
                    if(admin.getUsername().trim().equals(user.getUsername())){
                        spAdminID.setSelection(spAdminIDAdapter.getPosition(arrayList.get(i)));
                        existAdmin=true;
                        break;
                    }
                }



                spAdminID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spAdminIDAdapter.getItem(position);
                        if(object instanceof Admin){
                            Admin admin=(Admin)object;
                            module.setAdminID(admin.getUsername());
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<Admin>> call, Throwable t) {

            }
        });
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
                        module.setStartTime(calendarStartDate.getTime());
                        break;
                    case 2:
                        module.setEndTime(calendarEndDate.getTime());
                        break;
                    case 3:
                        module.setFeedbackStartTime(calendarFBStartDate.getTime());
                        break;
                    case 4:
                        module.setFeedbackEndTime(calendarFBEndDate.getTime());
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

    private void setTime(Calendar calendar, TextView textView, SimpleDateFormat simpleDateFormat, int type) {


        TimePickerDialog.OnTimeSetListener listener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(calendar.MINUTE,minute);

                textView.setText(simpleDateFormat.format(calendar.getTime()));

                switch (type){
                    case 3:
                        module.setFeedbackStartTime(calendarFBStartDate.getTime());
                        break;
                    case 4:
                        module.setFeedbackEndTime(calendarFBEndDate.getTime());
                        break;
                }

            }
        };
        TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(),
                listener,
                calendar.get(calendar.HOUR_OF_DAY),
                calendar.get(calendar.MINUTE),
                true);
        timePickerDialog.show();
    }
}
