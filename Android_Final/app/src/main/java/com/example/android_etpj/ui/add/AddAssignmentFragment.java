package com.example.android_etpj.ui.add;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.Spinner;
import com.example.android_etpj.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.R;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Assignment;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Module;
import com.example.android_etpj.models.Trainer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAssignmentFragment extends Fragment {
    public static final String TAG = AddAssignmentFragment.class.getName();

    private Spinner spModuleName;
    private Spinner spClassName;
    private Spinner spTrainerId;
    private Assignment assignment;

    private Activity activity;

    private Button btnSave;
    private Button btnBack;

    private String registrationCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_assignment,container,false);
        activity = new Activity();
        activity = (Activity) view.getContext();

        spModuleName=view.findViewById(R.id.module_name);
        spClassName=view.findViewById(R.id.class_name);
        spTrainerId=view.findViewById(R.id.trainer_id);

        btnSave=view.findViewById(R.id.btn_save);
        btnBack=view.findViewById(R.id.btn_back);

        assignment= new Assignment();

        setSpinnerModule();
        setSpinnerClass();
        setSpinnerTrainer();



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
                // lấy ngày giờ hiện tại
                Date now = new Date();
                // chuyển Date sang Timestamp
                Timestamp timestamp = new Timestamp(now.getTime());
                assignment.setRegistrationCode("CL"+String.valueOf(assignment.getClassID()).trim()+"M"+String.valueOf(assignment.getModuleID()).trim()+"T"+timestamp.getTime());
                ApiService.apiService.addAssignment(assignment).enqueue(new Callback<Boolean>() {
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
                            tvTitleSuccess.setText("Assignment already exist!!");

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

    private void setSpinnerTrainer() {
        ApiService.apiService.getTrainers().enqueue(new Callback<List<Trainer>>() {
            @Override
            public void onResponse(Call<List<Trainer>> call, Response<List<Trainer>> response) {
                List<Trainer> arrayList=(ArrayList<Trainer>) response.body();
                List<Object> trainers =new ArrayList<>();
                trainers.addAll(arrayList);


                SpinnerAdapter spTrainerIDAdapter=new SpinnerAdapter(getContext(), R.layout.item_sp_selected,trainers);
                spTrainerId.setAdapter(spTrainerIDAdapter);

                if(arrayList.size()>0){
                    Trainer trainer=arrayList.get(0);
                    assignment.setTrainerID(trainer.getUsername());
                }

                spTrainerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spTrainerIDAdapter.getItem(position);
                        if(object instanceof Trainer){
                            Trainer trainer=(Trainer) object;
                            assignment.setTrainerID(trainer.getUsername());
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Trainer>> call, Throwable t) {

            }

        });
    }

    private void setSpinnerClass() {
        ApiService.apiService.getClasses().enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                List<Class> arrayList=(ArrayList<Class>) response.body();
                List<Object> classes =new ArrayList<>();
                classes.addAll(arrayList);


                SpinnerAdapter spClassNameAdapter=new SpinnerAdapter(getContext(), R.layout.item_sp_selected,classes);
                spClassName.setAdapter(spClassNameAdapter);

                if(arrayList.size()>0){
                    Class aClass=arrayList.get(0);
                    assignment.setClassID(aClass.getClassID());
                }

                spClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spClassNameAdapter.getItem(position);
                        if(object instanceof Class){
                            Class aClass=(Class) object;
                            assignment.setClassID(aClass.getClassID());
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

    private void setSpinnerModule() {
        ApiService.apiService.getModules().enqueue(new Callback<List<Module>>() {
            @Override
            public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {
                List<Module> arrayList=(ArrayList<Module>) response.body();
                List<Object> modules =new ArrayList<>();
                modules.addAll(arrayList);


                SpinnerAdapter spModuleNameAdapter=new SpinnerAdapter(getContext(), R.layout.item_sp_selected,modules);
                spModuleName.setAdapter(spModuleNameAdapter);

                if(arrayList.size()>0){
                    Module module=arrayList.get(0);
                    assignment.setModuleID(module.getModuleID());
                }

                spModuleName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spModuleNameAdapter.getItem(position);
                        if(object instanceof Module){
                            Module module=(Module) object;
                            assignment.setModuleID(module.getModuleID());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Module>> call, Throwable t) {

            }
        });
    }
}
