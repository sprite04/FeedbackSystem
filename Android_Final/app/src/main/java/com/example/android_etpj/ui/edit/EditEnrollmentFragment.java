package com.example.android_etpj.ui.edit;

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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditEnrollmentFragment extends Fragment {
    public static final String TAG = EditAssignmentFragment.class.getName();

    private TextView tvTrainerId;
    private TextView tvTrainerName;

    private Spinner spClassName;

    private Button btnSave;
    private Button btnBack;

    Enrollment enrollment;
    private int idClassOld;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_enrollment, container, false);


        Bundle bundle = getArguments();
        enrollment = (Enrollment) bundle.get("ENROLLMENT");
        idClassOld=enrollment.getClassId();


        tvTrainerId = view.findViewById(R.id.tv_trainer_id);
        tvTrainerId.setText(String.valueOf(enrollment.getTraineeId()));


        tvTrainerName = view.findViewById(R.id.tv_class_name);
        tvTrainerName.setText(String.valueOf(enrollment.getTrainee().getName()));


        spClassName = view.findViewById(R.id.class_name);

        btnSave = view.findViewById(R.id.btn_save);
        btnBack = view.findViewById(R.id.btn_back);

        setSpinnerClassName();

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
                ApiService.apiService.editEnrollment(idClassOld,enrollment.getClassId(),enrollment.getTraineeId()).enqueue(new Callback<Boolean>() {
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
                            tvTitleSuccess.setText("Update success!");

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
                        else{
                            Dialog dialog = new Dialog(view.getContext());
                            dialog.setContentView(R.layout.dialog_error);
                            dialog.setCancelable(false);

                            Window window = dialog.getWindow();
                            if (window == null)
                                return;
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView tvTitleSuccess=dialog.findViewById(R.id.tv_title);
                            tvTitleSuccess.setText("Update fail !");

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

    private void setSpinnerClassName() {
        ApiService.apiService.getClasses().enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                List<Class> arrayList = (ArrayList<Class>) response.body();
                List<Object> classes = new ArrayList<>();
                classes.addAll(arrayList);


                SpinnerAdapter spClassNameAdapter = new SpinnerAdapter(getContext(), R.layout.item_sp_selected, classes);
                spClassName.setAdapter(spClassNameAdapter);

                boolean existClass=false;
                for(int i=0; i<arrayList.size(); i++){
                    Class aClass=arrayList.get(i);
                    if(aClass.getClassID()==enrollment.getClassId()){
                        existClass=true;
                        spClassName.setSelection(spClassNameAdapter.getPosition(arrayList.get(i)));
                        break;
                    }
                }
                if(arrayList.size()>0 && existClass==false){
                    Class aClass=arrayList.get(0);
                    enrollment.setClassId(aClass.getClassID());
                }
                spClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object = spClassNameAdapter.getItem(position);
                        if (object instanceof Class) {
                            Class aClass = (Class) object;
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
}
