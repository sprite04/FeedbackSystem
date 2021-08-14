package com.example.android_etpj.ui.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android_etpj.R;
import com.example.android_etpj.adapter.ViewClassAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Enrollment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewClassFragment extends Fragment {

    private RecyclerView rcvViewClass;

    private TextView tvClassID;
    private TextView tvClassName;

    private Button btnBack;

    private Class clss;
    private List<String> traineeIDList;

    private ViewClassAdapter viewClassAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_view_class, container, false);

        Bundle bundle=getArguments();
        clss= (Class) bundle.get("VIEWCLASS");

        rcvViewClass = view.findViewById(R.id.rcv_view_class);

        tvClassID = view.findViewById(R.id.tv_title_class_id);
        tvClassName = view.findViewById(R.id.tv_title_class_name);

        tvClassName.setText("Class Name: " + String.valueOf(clss.getClassName()));
        tvClassID.setText("Class Name: " + String.valueOf(clss.getClassID()));

        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        viewClassAdapter = new ViewClassAdapter();

        ApiService.apiService.getEnrollmentByIdClass(clss.getClassID()).enqueue(new Callback<List<Enrollment>>() {
            @Override
            public void onResponse(Call<List<Enrollment>> call, Response<List<Enrollment>> response) {
                List<Enrollment> enrollmentList = (ArrayList<Enrollment>) response.body();
                traineeIDList = new ArrayList<>();
                for (Enrollment enrollment: enrollmentList) {
                    traineeIDList.add(enrollment.getTraineeId());
                }
                viewClassAdapter.setData(traineeIDList);
            }

            @Override
            public void onFailure(Call<List<Enrollment>> call, Throwable t) {

            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvViewClass.setLayoutManager(linearLayoutManager);
        rcvViewClass.setAdapter(viewClassAdapter);

        return view;
    }
}