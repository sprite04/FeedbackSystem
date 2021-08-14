package com.example.android_etpj.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.adapter.AssignmentAdapter;
import com.example.android_etpj.adapter.EnrollmentAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeAssignment;
import com.example.android_etpj.interfaces.ExchangeEnrollment;
import com.example.android_etpj.models.Assignment;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Enrollment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollmentFragment extends Fragment implements ExchangeEnrollment {

    private RecyclerView rcvEnrollment;
    private EnrollmentAdapter enrollmentAdapter;
    private TextView tvTitle;
    private ImageButton btnAdd;
    private List<Enrollment> enrollmentList;
    private MainActivity mainActivity;
    private LinearLayout spinnerSearch;
    private TextView tvTitleSearch;
    private Spinner spClassName;

    private int classId;
    public EnrollmentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvEnrollment=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        btnAdd=view.findViewById(R.id.btn_add);
        spinnerSearch=view.findViewById(R.id.spinner_search);
        spinnerSearch.setVisibility(View.VISIBLE);
        tvTitleSearch=view.findViewById(R.id.tv_title_search);
        tvTitleSearch.setText("Class Name:  ");
        spClassName=view.findViewById(R.id.sp_search);

        enrollmentAdapter=new EnrollmentAdapter(this);
        loadData();

        Log.e("value",String.valueOf(enrollmentAdapter.getItemCount()));

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvEnrollment.setLayoutManager(linearLayoutManager);
        rcvEnrollment.setAdapter(enrollmentAdapter);
        setSpinnerClass();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addEnrollmentFragment();
            }
        });
        return view;
    }


    @Override
    public void loadData() {
        ApiService.apiService.getEnrollment().enqueue(new Callback<List<Enrollment>>() {
            @Override
            public void onResponse(Call<List<Enrollment>> call, Response<List<Enrollment>> response) {
                enrollmentList = (ArrayList<Enrollment>) response.body();
                tvTitle.setText("Enrollment List");
                enrollmentAdapter.setData(enrollmentList);
            }

            @Override
            public void onFailure(Call<List<Enrollment>> call, Throwable t) {

            }
        });
    }
    private void setSpinnerClass() {
        ApiService.apiService.getClasses().enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                Class all=new Class();
                all.setClassID(-1);
                all.setClassName("All");
                List<Class> arrayList=(ArrayList<Class>) response.body();
                List<Object> classes =new ArrayList<>();
                classes.add((Class)all);
                classes.addAll(arrayList);


                SpinnerAdapter spClassNameAdapter=new SpinnerAdapter(getContext(),R.layout.item_sp_selected,classes);
                spClassName.setAdapter(spClassNameAdapter);

                if(arrayList.size()>0){
                    Class aClass=arrayList.get(0);
                    classId=aClass.getClassID();
                    Log.e("classId",String.valueOf(classId));
                    searchEnrollment(classId);
                }

                spClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spClassNameAdapter.getItem(position);
                        if(object instanceof Class){
                            Class aClass=(Class) object;
                            classId=aClass.getClassID();
                            Log.e("classId",String.valueOf(classId));
                            if(classId==-1)
                                loadData();
                            else
                                searchEnrollment(classId);
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

    @Override
    public void editData(Enrollment enrollment) {
        mainActivity.editEnrollmentFragment(enrollment);
    }

    @Override
    public void viewData(Enrollment enrollment) {
        mainActivity.viewEnrollmentFragment(enrollment);
    }
    public void searchEnrollment(int classId){
        ApiService.apiService.searchEnrollment(classId).enqueue(new Callback<List<Enrollment>>() {
            @Override
            public void onResponse(Call<List<Enrollment>> call, Response<List<Enrollment>> response) {
                enrollmentList = (ArrayList<Enrollment>) response.body();
                Log.e("listsize",String.valueOf(enrollmentList.size()));
                enrollmentAdapter.setData(enrollmentList);
            }

            @Override
            public void onFailure(Call<List<Enrollment>> call, Throwable t) {

            }
        });
    }
}