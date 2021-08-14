package com.example.android_etpj.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.adapter.ClassAdapter;
import com.example.android_etpj.interfaces.ExchangeClass;
import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Admin;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Trainee;
import com.example.android_etpj.models.Trainer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassFragment extends Fragment implements ExchangeClass {

    private Object user;
    private MainActivity mainActivity;
    private RecyclerView rcvClass;
    private ClassAdapter classAdapter;
    private TextView tvTitle;
    private ImageButton btnAdd;
    private List<Class> classList;

    public ClassFragment(Object user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (user instanceof Admin) {
            return setAdminView(inflater, container, savedInstanceState);
        }

        if (user instanceof Trainer) {
            return setTrainerView(inflater, container, savedInstanceState);
        }

        if (user instanceof Trainee) {
            return setTraineeView(inflater, container, savedInstanceState);
        }

        View view=inflater.inflate(R.layout.fragment_access_forbidden_2,container,false);
        return view;
    }

    @Override
    public void loadData() {
        if (user instanceof Admin) {
            ApiService.apiService.getClasses().enqueue(new Callback<List<Class>>() {
                @Override
                public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                    classList = (ArrayList<Class>) response.body();
                    tvTitle.setText("Class List");

                    classAdapter.setData(classList);
                }

                @Override
                public void onFailure(Call<List<Class>> call, Throwable t) {

                }
            });
        }

        if (user instanceof Trainer) {
            ApiService.apiService.getClassesByTrainer(((Trainer) user).getUsername()).enqueue(new Callback<List<Class>>() {
                @Override
                public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                    classList = (ArrayList<Class>) response.body();
                    tvTitle.setText("Class List");

                    classAdapter.setData(classList);
                }

                @Override
                public void onFailure(Call<List<Class>> call, Throwable t) {

                }
            });
        }

        if (user instanceof Trainee) {
            ApiService.apiService.getClassesByTrainee(((Trainee) user).getUserId()).enqueue(new Callback<List<Class>>() {
                @Override
                public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                    classList = (ArrayList<Class>) response.body();
                    tvTitle.setText("Class List");

                    classAdapter.setData(classList);
                }

                @Override
                public void onFailure(Call<List<Class>> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void editData(Class clss) {

        mainActivity.editClassFragment(clss);
    }

    private View setAdminView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvClass=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        btnAdd=view.findViewById(R.id.btn_add);
        //btnAdd.setVisibility(View.GONE);

        classAdapter=new ClassAdapter(this, user, mainActivity);
        loadData();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvClass.setLayoutManager(linearLayoutManager);
        rcvClass.setAdapter(classAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addClassFragment();
            }
        });

        return view;
    }

    private View setTrainerView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvClass=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        btnAdd=view.findViewById(R.id.btn_add);
        btnAdd.setVisibility(View.GONE);

        classAdapter=new ClassAdapter(this, user, mainActivity);
        loadData();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvClass.setLayoutManager(linearLayoutManager);
        rcvClass.setAdapter(classAdapter);

        return view;
    }

    private View setTraineeView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvClass=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        btnAdd=view.findViewById(R.id.btn_add);
        btnAdd.setVisibility(View.GONE);

        classAdapter=new ClassAdapter(this, user, mainActivity);
        loadData();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvClass.setLayoutManager(linearLayoutManager);
        rcvClass.setAdapter(classAdapter);

        return view;
    }
}
