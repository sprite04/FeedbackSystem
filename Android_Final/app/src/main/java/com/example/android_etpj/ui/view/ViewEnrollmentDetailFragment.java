package com.example.android_etpj.ui.view;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.android_etpj.R;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Enrollment;
import com.example.android_etpj.models.Trainee;
import com.example.android_etpj.ui.edit.EditAssignmentFragment;

import java.text.SimpleDateFormat;

public class ViewEnrollmentDetailFragment extends Fragment {
    public static final String TAG = EditAssignmentFragment.class.getName();

    private TextView tvContentTrainee;
    private TextView tvContentClass;

    private Button btnBack;

    Enrollment enrollment;
    Trainee trainee;
    Class aClass;

    private SimpleDateFormat formatterDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enrollment_view_detail, container, false);


        Bundle bundle = getArguments();
        enrollment = (Enrollment) bundle.get("ENROLLMENT");
        trainee=enrollment.getTrainee();
        aClass=enrollment.getaClass();

        formatterDate= new SimpleDateFormat("dd/MM/yyyy");
        String strEndTime=formatterDate.format(aClass.getEndTime());
        String strStartTime=formatterDate.format(aClass.getStartTime());

        String classContent = "";
        classContent = "<b>Class ID:</b> " + String.valueOf(aClass.getClassID()) + "<br>" +
                "<b>StartTime:</b> " + strStartTime + "<br>" +
                "<b>Class Name:</b> " + String.valueOf(aClass.getClassName()) + "<br>" +
                "<b>EndTime:</b> " + strEndTime + "<br>" +
                "<b>Capacity :</b> " + String.valueOf(aClass.getCapacity()) + "<br>";


        tvContentClass = view.findViewById(R.id.tv_content_class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvContentClass.setText(Html.fromHtml(classContent, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvContentClass.setText(Html.fromHtml(classContent));
        }

        String traineeContent = "";
        traineeContent = "<b>" + "Trainee ID: " + "</b> " + String.valueOf(trainee.getUserId()) + "<br>" +
                "<b>" + "Phone: " + "</b> " + String.valueOf(trainee.getPhone()) + "<br>" +
                "<b>" + "Trainee Name: " + "</b> " + String.valueOf(trainee.getName()) + "<br>" +
                "<b>" + "Address: " + "</b> " + String.valueOf(trainee.getAddress()) + "<br>" +
                "<b>" + "Email : " + "</b> " + String.valueOf(trainee.getEmail()) + "<br>";
        tvContentTrainee = view.findViewById(R.id.tv_content_trainee);
        tvContentTrainee.setText(Html.fromHtml(traineeContent, 1));


        btnBack = view.findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getSupportFragmentManager()!=null){
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });
        return view;


    }
}
