package com.example.android_etpj.ui.add;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Enrollment;
import com.example.android_etpj.models.Trainee;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEnrollmentFragment extends Fragment {
    public static final String TAG = AddEnrollmentFragment.class.getName();
    private Spinner spTraineeId;
    private Spinner spClassId;
    private Enrollment enrollment;

    private Activity activity;

    private Button btnSave;
    private Button btnBack;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_enrollment, container, false);
        activity = new Activity();
        activity = (Activity) view.getContext();

        spTraineeId = view.findViewById(R.id.trainee_id);
        spClassId = view.findViewById(R.id.class_id);

        btnSave = view.findViewById(R.id.btn_save);
        btnBack = view.findViewById(R.id.btn_back);

        enrollment = new Enrollment();

        setSpinnerTrainee();
        setSpinnerClass();

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
                Log.e("classid",String.valueOf(enrollment.getClassId()));
                Log.e("trainee",String.valueOf(enrollment.getTraineeId()));
                ApiService.apiService.addEnrollment(enrollment.getClassId(),enrollment.getTraineeId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()==true){
                            Dialog dialogSuccess=new Dialog(activity);
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
                            Dialog dialog = new Dialog(activity);
                            dialog.setContentView(R.layout.dialog_error);
                            dialog.setCancelable(false);

                            Window window = dialog.getWindow();
                            if (window == null)
                                return;
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView tvTitleSuccess=dialog.findViewById(R.id.tv_title);
                            tvTitleSuccess.setText("Enrollment already exist!!");

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
        });
        return view;

    }

    private void setSpinnerClass() {
        ApiService.apiService.getClasses().enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                List<Class> arrayList=(ArrayList<Class>) response.body();
                List<Object> classes =new ArrayList<>();
                classes.addAll(arrayList);
                SpinnerAdapter spClassIdAdapter=new SpinnerAdapter(getContext(), R.layout.item_sp_selected,classes);
                spClassId.setAdapter(spClassIdAdapter);

                if(arrayList.size()>0){
                    Class aClass=arrayList.get(0);
                    enrollment.setClassId(aClass.getClassID());
                }

                spClassId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spClassIdAdapter.getItem(position);
                        if(object instanceof Class){
                            Class aClass=(Class) object;
                            enrollment.setClassId(aClass.getClassID());
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {

            }

        });
    }

    private void setSpinnerTrainee() {
        ApiService.apiService.getTrainee().enqueue(new Callback<List<Trainee>>() {
            @Override
            public void onResponse(Call<List<Trainee>> call, Response<List<Trainee>> response) {
                List<Trainee> arrayList=(ArrayList<Trainee>) response.body();
                List<Object> trainees =new ArrayList<>();
                trainees.addAll(arrayList);


                SpinnerAdapter spTraineeIDAdapter=new SpinnerAdapter(getContext(), R.layout.item_sp_selected,trainees);
                spTraineeId.setAdapter(spTraineeIDAdapter);

                if(arrayList.size()>0){
                    Trainee trainee=arrayList.get(0);
                    enrollment.setTraineeId(trainee.getUserId());
                }

                spTraineeId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spTraineeIDAdapter.getItem(position);
                        if(object instanceof Trainee){
                            Trainee trainee=(Trainee) object;
                            enrollment.setTraineeId(trainee.getUserId());
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Trainee>> call, Throwable t) {

            }
        });
    }
}
