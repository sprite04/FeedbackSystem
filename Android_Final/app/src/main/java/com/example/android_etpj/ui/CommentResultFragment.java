package com.example.android_etpj.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.adapter.CommentResultAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeCommentResult;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.CommentResult;
import com.example.android_etpj.models.Module;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentResultFragment extends Fragment implements ExchangeCommentResult {
    public static final String TAG= CommentResultFragment.class.getName();
    private RecyclerView rcvComment;
    private CommentResultAdapter commentResultAdapter;
    private TextView tvTitle;
    private ImageButton btnAdd;
    private List<CommentResult> commentResultList;
    private MainActivity mainActivity;
    private LinearLayout spinner;
    private LinearLayout select;

    private Button btnShowOverView;
    private Button btnShowDetail;

    private Spinner spModuleName;
    private Spinner spClassName;

    private CommentResult commentResult;

    private int idModule;
    private int idClass;

    private Object user;



    public CommentResultFragment(Object user) {
        this.user=user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_comment,container,false);

        mainActivity=(MainActivity)getActivity();
        btnShowOverView=view.findViewById(R.id.btn_show_overview);
        btnShowDetail=view.findViewById(R.id.btn_show_detail);

        rcvComment=view.findViewById(R.id.rcv_common);

        tvTitle=view.findViewById(R.id.tv_title);

        btnAdd=view.findViewById(R.id.btn_add);
        btnAdd.setVisibility(View.GONE);



        spClassName=view.findViewById(R.id.sp_class);
        spModuleName=view.findViewById(R.id.sp_module);
        spinner=view.findViewById(R.id.search);
        spinner.setVisibility(View.VISIBLE);

        select=view.findViewById(R.id.btn_select);
        select.setVisibility(View.VISIBLE);

        setSpinnerClass();
        setSpinnerModule();


        commentResultAdapter=new CommentResultAdapter(this,user);
        loadData();

        Log.e("value",String.valueOf(commentResultAdapter.getItemCount()));

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        rcvComment.setLayoutManager(linearLayoutManager);
        rcvComment.setAdapter(commentResultAdapter);

        btnShowOverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager()!=null){
                    mainActivity.type=0;
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });

        btnShowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager()!=null){
                    mainActivity.type=2;
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });

        return view;
    }
    public void loadData() {
        ApiService.apiService.getCommentResult(idClass,idModule).enqueue(new Callback<List<CommentResult>>() {
            @Override
            public void onResponse(Call<List<CommentResult>> call, Response<List<CommentResult>> response) {
                commentResultList = (ArrayList<CommentResult>) response.body();
                if(commentResultList==null)
                    Log.e("Size","null");
                tvTitle.setText("COMMENT RESULT");
                tvTitle.setTextSize(20);
                tvTitle.setTypeface(Typeface.SERIF);
                commentResultAdapter.setData(commentResultList);
            }

            @Override
            public void onFailure(Call<List<CommentResult>> call, Throwable t) {

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


                SpinnerAdapter spModuleNameAdapter=new SpinnerAdapter(getContext(),R.layout.item_sp_selected,modules);
                spModuleName.setAdapter(spModuleNameAdapter);

                if(arrayList.size()>0){
                    Module module=arrayList.get(0);
                    idModule=module.getModuleID();
                }

                spModuleName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spModuleNameAdapter.getItem(position);
                        if(object instanceof Module){
                            Module module=(Module) object;
                            idModule=module.getModuleID();
                            loadData();
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

    private void setSpinnerClass() {
        ApiService.apiService.getClasses().enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                List<Class> arrayList=(ArrayList<Class>) response.body();
                List<Object> classes =new ArrayList<>();
                classes.addAll(arrayList);


                SpinnerAdapter spClassNameAdapter=new SpinnerAdapter(getContext(),R.layout.item_sp_selected,classes);
                spClassName.setAdapter(spClassNameAdapter);

                if(arrayList.size()>0){
                    Class aClass=arrayList.get(0);
                    idClass=aClass.getClassID();
                }

                spClassName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spClassNameAdapter.getItem(position);
                        if(object instanceof Class){
                            Class aClass=(Class) object;
                            idClass=aClass.getClassID();
                            loadData();
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
