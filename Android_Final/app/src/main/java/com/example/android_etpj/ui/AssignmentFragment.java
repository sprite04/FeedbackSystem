package com.example.android_etpj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.android_etpj.MainActivity;

import com.example.android_etpj.R;
import com.example.android_etpj.adapter.AssignmentAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeAssignment;
import com.example.android_etpj.models.Assignment;
import com.example.android_etpj.models.Trainee;
import com.example.android_etpj.models.Trainer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignmentFragment extends Fragment implements ExchangeAssignment {

    private RecyclerView rcvAssignment;
    private AssignmentAdapter assignmentAdapter;
    private TextView tvTitle;
    private ImageButton btnAdd;
    private List<Assignment> assignmentsList;
    private MainActivity mainActivity;
    private LinearLayout editSearch;
    private EditText edtSearch;
    private Button btnSearch;

    private Object user;
    public AssignmentFragment(Object user) {
        this.user=user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvAssignment=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        btnAdd=view.findViewById(R.id.btn_add);
        if(user instanceof Trainer || user instanceof Trainee) {
            btnAdd.setVisibility(View.GONE);
        }
        //btnAdd.setVisibility(View.GONE);
        editSearch=view.findViewById(R.id.edit_search);
        editSearch.setVisibility(View.VISIBLE);
        edtSearch=view.findViewById(R.id.edt_search);
        btnSearch=view.findViewById(R.id.btn_search);



        assignmentAdapter=new AssignmentAdapter(this,user);
        loadData();

        Log.e("value",String.valueOf(assignmentAdapter.getItemCount()));

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvAssignment.setLayoutManager(linearLayoutManager);
        rcvAssignment.setAdapter(assignmentAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addAssignmentFragment();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.apiService.searchAssignments(edtSearch.getText().toString()).enqueue(new Callback<List<Assignment>>() {
                    @Override
                    public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                        assignmentsList = (ArrayList<Assignment>) response.body();
                        assignmentAdapter.setData(assignmentsList);
                    }

                    @Override
                    public void onFailure(Call<List<Assignment>> call, Throwable t) {

                    }
                });
            }
        });


        return view;
    }
    @Override
    public void loadData() {
        ApiService.apiService.getAssignments().enqueue(new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                assignmentsList = (ArrayList<Assignment>) response.body();
                tvTitle.setText("Assignment");

                assignmentAdapter.setData(assignmentsList);
            }

            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {

            }
        });
    }

    @Override
    public void editData(Assignment assignment) {
        mainActivity.editAssignmentFragment(assignment);
    }
}
