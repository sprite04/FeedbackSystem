package com.example.android_etpj.ui;

import android.os.Bundle;
import android.util.Log;
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

import com.example.android_etpj.interfaces.ExchangeModule;
import com.example.android_etpj.MainActivity;
import com.example.android_etpj.adapter.ModuleAdapter;
import com.example.android_etpj.R;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModuleFragment extends Fragment implements ExchangeModule {

    private RecyclerView rcvModule;
    private ModuleAdapter moduleAdapter;
    private TextView tvTitle;
    private ImageButton btnAdd;
    private List<Module> moduleList;
    private Object user;

    private MainActivity mainActivity;

    public ModuleFragment(Object user) {
        this.user=user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvModule=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        btnAdd=view.findViewById(R.id.btn_add);
        if(user instanceof Admin){
            try {
                Admin admin=(Admin) user;
                btnAdd.setVisibility(View.VISIBLE);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        mainActivity.addModuleFragment(admin);
                    }
                });
            }
            catch (Exception e){
                Log.e("ErrorModuleFragmentButtonAdd",e.getMessage());
            }

        }
        else {
            btnAdd.setVisibility(View.GONE);
        }



        moduleAdapter=new ModuleAdapter(this, user);
        loadData();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvModule.setLayoutManager(linearLayoutManager);
        rcvModule.setAdapter(moduleAdapter);



        return view;
    }



    @Override
    public void loadData() {
        if(user instanceof Admin) {
            ApiService.apiService.getModules().enqueue(new Callback<List<Module>>() {
                @Override
                public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {
                    moduleList = (ArrayList<Module>) response.body();
                    tvTitle.setText("Module List");

                    moduleAdapter.setData(moduleList);
                }

                @Override
                public void onFailure(Call<List<Module>> call, Throwable t) {

                }
            });
        }
        else if(user instanceof Trainer){
            try {
                Trainer trainer=(Trainer)user;
                ApiService.apiService.getModuleByIdTrainer(trainer.getUsername()).enqueue(new Callback<List<Module>>() {
                    @Override
                    public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {
                        moduleList = (ArrayList<Module>) response.body();
                        tvTitle.setText("Module List");

                        moduleAdapter.setData(moduleList);
                    }

                    @Override
                    public void onFailure(Call<List<Module>> call, Throwable t) {

                    }
                });
            }
            catch (Exception ex){
                Log.e("ModuleFragment",ex.getMessage());
            }
        }
        else if(user instanceof Trainee){
            try {
                Trainee trainee=(Trainee) user;
                ApiService.apiService.getModuleByIdTrainee(trainee.getUserId()).enqueue(new Callback<List<Module>>() {
                    @Override
                    public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {
                        moduleList = (ArrayList<Module>) response.body();
                        tvTitle.setText("Module List");

                        moduleAdapter.setData(moduleList);
                    }

                    @Override
                    public void onFailure(Call<List<Module>> call, Throwable t) {

                    }
                });
            }
            catch (Exception ex){
                Log.e("ModuleFragment",ex.getMessage());
            }

        }

    }

    @Override
    public void editData(Module module) {

        mainActivity.editModuleFragment(module);
    }

}
