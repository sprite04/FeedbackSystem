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

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.adapter.FeedbackAdapter;
import com.example.android_etpj.adapter.ModuleAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeFeedback;
import com.example.android_etpj.models.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackFragment extends Fragment implements ExchangeFeedback {

    private RecyclerView rcvFeedback;
    private FeedbackAdapter feedbackAdapter;
    private TextView tvTitle;
    private ImageButton btnAdd;
    private List<Feedback> feedbackList;

    private MainActivity mainActivity;

    public FeedbackFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvFeedback=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        btnAdd=view.findViewById(R.id.btn_add);
        //btnAdd.setVisibility(View.GONE);



        feedbackAdapter=new FeedbackAdapter(this);
        loadData();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvFeedback.setLayoutManager(linearLayoutManager);
        rcvFeedback.setAdapter(feedbackAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.addFeedbackFragment();
            }
        });

        return view;
    }

    @Override
    public void loadData() {
        ApiService.apiService.getFeedbacks().enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {

                feedbackList=(ArrayList<Feedback>)response.body();
                tvTitle.setText("List Feedback");

                feedbackAdapter.setData(feedbackList);
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {

            }
        });

    }

    @Override
    public void editData(Feedback feedback) {
        mainActivity.editFeedbackFragment(feedback,1);
    }

    @Override
    public void viewData(Feedback feedback) {
        mainActivity.reviewFeedbackFragment(feedback);
    }
}
