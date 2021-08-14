package com.example.android_etpj.ui.edit;

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
import com.example.android_etpj.models.Assignment;
import com.example.android_etpj.models.Trainer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAssignmentFragment extends Fragment {
    public static final String TAG = EditAssignmentFragment.class.getName();

    private TextView tvClassId;
    private TextView tvClassName;
    private TextView tvModuleId;
    private TextView tvModuleName;

    private Spinner spTrainerId;

    private Button btnSave;
    private Button btnBack;

    Assignment assignment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_assignment, container, false);


        Bundle bundle = getArguments();
        assignment = (Assignment) bundle.get("ASSIGNMENT");


        tvClassId = view.findViewById(R.id.tv_class_id);
        tvClassId.setText(String.valueOf(assignment.getClassID()));


        tvClassName = view.findViewById(R.id.tv_class_name);
        tvClassName.setText(assignment.getClss().getClassName());


        tvModuleId = view.findViewById(R.id.tv_module_id);
        tvModuleId.setText(String.valueOf(assignment.getModuleID()));


        tvModuleName = view.findViewById(R.id.tv_module_name);
        tvModuleName.setText(assignment.getModule().getModuleName());
        Log.e("Assignment", String.valueOf(assignment.getClss().getClassName()));

        spTrainerId = view.findViewById(R.id.trainer_id);

        btnSave = view.findViewById(R.id.btn_save);
        btnBack = view.findViewById(R.id.btn_back);

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
                ApiService.apiService.editAssignment(assignment).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()==true){
                            Dialog dialogSuccess=new Dialog(view.getContext());
                            dialogSuccess.setContentView(R.layout.dialog_notification);
                            dialogSuccess.setCancelable(false);

                            Window window=dialogSuccess.getWindow();
                            if(window==null)
                                return ;
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView tvTitleSuccess=dialogSuccess.findViewById(R.id.tv_title);
                            tvTitleSuccess.setText("Success!");

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
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }
        });

        /*btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.apiService.editAssignment(assignment).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.body()==true){
                            if(getActivity().getSupportFragmentManager()!=null){
                                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                fragmentManager.popBackStack();
                            }
                            Dialog dialogSuccess=new Dialog(view.getContext());
                            dialogSuccess.setContentView(R.layout.dialog_notification);
                            dialogSuccess.setCancelable(false);

                            Window window=dialogSuccess.getWindow();
                            if(window==null)
                                return ;
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView tvTitleSuccess=dialogSuccess.findViewById(R.id.tv_title);
                            tvTitleSuccess.setText("Success!");

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
                        }*//*
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }
        });*/
        return view;


    }

    private void setSpinnerTrainer() {
        ApiService.apiService.getTrainers().enqueue(new Callback<List<Trainer>>() {
            @Override
            public void onResponse(Call<List<Trainer>> call, Response<List<Trainer>> response) {
                List<Trainer> arrayList = (ArrayList<Trainer>) response.body();
                List<Object> trainers = new ArrayList<>();
                trainers.addAll(arrayList);


                SpinnerAdapter spTrainerIDAdapter = new SpinnerAdapter(getContext(), R.layout.item_sp_selected, trainers);
                spTrainerId.setAdapter(spTrainerIDAdapter);

                boolean existTrainer=false;
                for(int i=0; i<arrayList.size(); i++){
                    Trainer trainer=arrayList.get(i);
                    if(trainer.getUsername().trim().equals(assignment.getTrainer().getUsername().trim())){
                        existTrainer=true;
                        spTrainerId.setSelection(spTrainerIDAdapter.getPosition(arrayList.get(i)));
                        break;
                    }
                }
                if(arrayList.size()>0 && existTrainer==false){
                    Trainer trainer=arrayList.get(0);
                    assignment.setTrainerID(trainer.getUsername());
                }
                spTrainerId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object = spTrainerIDAdapter.getItem(position);
                        if (object instanceof Trainer) {
                            Trainer trainer = (Trainer) object;
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
}
