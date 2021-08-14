package com.example.android_etpj.ui;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.adapter.FeedbackTraineeAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeFeedbackTrainee;
import com.example.android_etpj.models.Assignment;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Trainee;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackTraineeFragment extends Fragment implements ExchangeFeedbackTrainee {

    private RecyclerView rcvFeedback;
    private FeedbackTraineeAdapter feedbackTraineeAdapter;
    private TextView tvTitle;
    private ImageButton btnAdd;
    private List<Assignment> assignmentList;
    private List<Class> classList;

    private MainActivity mainActivity;
    private Trainee trainee;
    //thêm
    int check;
    private List<Assignment> assignmentsList;
    private Assignment assignment;

    public FeedbackTraineeFragment(Trainee trainee, int check) {
        this.trainee=trainee;
        //thêm
        this.check=check;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_common,container,false);

        mainActivity=(MainActivity)getActivity();

        rcvFeedback=view.findViewById(R.id.rcv_common);
        tvTitle=view.findViewById(R.id.tv_title);
        tvTitle.setText("List Feedback");
        btnAdd=view.findViewById(R.id.btn_add);
        btnAdd.setVisibility(View.GONE);


        feedbackTraineeAdapter=new FeedbackTraineeAdapter(this,trainee);
        loadData();


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        if(check==1){
            showDialogJoin();
        }

        rcvFeedback.setLayoutManager(linearLayoutManager);
        rcvFeedback.setAdapter(feedbackTraineeAdapter);
        return view;
    }
    public void showDialogJoin(){
        Dialog dialogJoin = new Dialog(getActivity());
        dialogJoin.setContentView(R.layout.dialog_join);
        dialogJoin.setCancelable(false);

        Window window = dialogJoin.getWindow();
        if (window == null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtCode=dialogJoin.findViewById(R.id.edt_code);

        Button btnClose = dialogJoin.findViewById(R.id.btn_close);
        Button btnSubmit = dialogJoin.findViewById(R.id.btn_submit);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogJoin.cancel();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.apiService.getAssignmentByRegistrationCode(edtCode.getText().toString().trim()).enqueue(new Callback<List<Assignment>>() {
                    @Override
                    public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                        assignmentsList= (ArrayList<Assignment>) response.body();
                        if(assignmentsList.size()!=0){
                            Log.e("Size",String.valueOf(assignmentsList.size()));
                            assignment=assignmentsList.get(0);
                            Log.e("Code",String.valueOf(assignment.getRegistrationCode()));
                            if(assignment!=null){
                                ApiService.apiService.getClassesByTrainee(trainee.getUserId().toString().trim()).enqueue(new Callback<List<Class>>() {
                                    @Override
                                    public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                                        classList= (ArrayList<Class>) response.body();
                                        Log.e("SizeByTrainee",String.valueOf(classList.size()));
                                        int checkk=0;//nếu đi qua vòng lặp
                                        if(classList.size()!=0){
                                            for(int i = 0; i < classList.size(); i++){
                                                if(classList.get(i).getClassID()==assignment.getClassID()){
                                                    //hiện thông báo đã vào assignment này rồi
                                                    Dialog dialog = new Dialog(getActivity());
                                                    dialog.setContentView(R.layout.dialog_error);
                                                    dialog.setCancelable(false);
                                                    Window window = dialog.getWindow();
                                                    if (window == null)
                                                        return;
                                                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                    TextView tvTitleSuccess=dialog.findViewById(R.id.tv_title);
                                                    tvTitleSuccess.setText("You already join this module, please try another!!!");
                                                    Button btnOk=dialog.findViewById(R.id.btn_ok_error);
                                                    btnOk.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    dialog.show();
                                                    checkk=1;
                                                    break;
                                                }
                                            }
                                        }
                                        if(checkk==0){
                                            ApiService.apiService.addEnrollment(assignment.getClassID(),trainee.getUserId()).enqueue(new Callback<Boolean>() {
                                                @Override
                                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                                    if(response.body()==true){
                                                        Dialog dialogSuccess=new Dialog(getActivity());
                                                        dialogSuccess.setContentView(R.layout.dialog_notification);
                                                        dialogSuccess.setCancelable(false);

                                                        Window window=dialogSuccess.getWindow();
                                                        if(window==null)
                                                            return ;
                                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
                                                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                                        TextView tvTitleSuccess=dialogSuccess.findViewById(R.id.tv_title);
                                                        tvTitleSuccess.setText("Join Success!");

                                                        Button btnOk=dialogSuccess.findViewById(R.id.btn_ok);
                                                        btnOk.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialogSuccess.cancel();
                                                                dialogJoin.cancel();
                                                                if(getActivity().getSupportFragmentManager()!=null){
                                                                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                                                                    fragmentManager.popBackStack();
                                                                }
                                                            }
                                                        });
                                                        dialogSuccess.show();
                                                    }else{
                                                        //hiện thông báo đã vào assignment này rồi
                                                        Dialog dialog = new Dialog(getActivity());
                                                        dialog.setContentView(R.layout.dialog_error);
                                                        dialog.setCancelable(false);

                                                        Window window = dialog.getWindow();
                                                        if (window == null)
                                                            return;
                                                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                                                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                                        TextView tvTitleSuccess=dialog.findViewById(R.id.tv_title);
                                                        tvTitleSuccess.setText("Sai rồi đó");

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
                                    }

                                    @Override
                                    public void onFailure(Call<List<Class>> call, Throwable t) {

                                    }
                                });
                            }
                        }
                        else {
                            Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_error);
                            dialog.setCancelable(false);

                            Window window = dialog.getWindow();
                            if (window == null)
                                return;
                            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView tvTitleSuccess=dialog.findViewById(R.id.tv_title);
                            tvTitleSuccess.setText("Invalid Registation Code!!!");

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
                    public void onFailure(Call<List<Assignment>> call, Throwable t) {

                    }
                });
            }
        });
        dialogJoin.show();
    }


    @Override
    public void loadData() {
        ApiService.apiService.getAssignmentsByTrainee(trainee.getUserId()).enqueue(new Callback<List<Assignment>>() {
            @Override
            public void onResponse(Call<List<Assignment>> call, Response<List<Assignment>> response) {
                assignmentList=response.body();
                Log.e("thu",String.valueOf(assignmentList.size()));
                tvTitle.setText("List Feedback");
                feedbackTraineeAdapter.setData(assignmentList);
            }

            @Override
            public void onFailure(Call<List<Assignment>> call, Throwable t) {
                Log.e("loi",t.getMessage());
            }
        });
    }

    @Override
    public void reviewData(Assignment assignment, Trainee trainee) {

        mainActivity.addFeedbackTraineeReviewFragment(assignment,trainee);

    }
}