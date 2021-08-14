package com.example.android_etpj.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android_etpj.LoginActivity;
import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.models.Admin;
import com.example.android_etpj.models.Trainee;
import com.example.android_etpj.models.Trainer;
import com.example.android_etpj.sharedpreference.DataLocal;

public class LogoutFragment extends Fragment {

    MainActivity mainActivity;
    public LogoutFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);

        mainActivity=(MainActivity)getActivity();

        setDialogConfirm();
        return view;
    }

    private void setDialogConfirm() {
        Dialog dialog=new Dialog(mainActivity);
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        Window window=dialog.getWindow();
        if(window==null)
            return ;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitle=dialog.findViewById(R.id.tv_title);
        tvTitle.setText("Are you sure?");

        TextView tvContent=dialog.findViewById(R.id.tv_content);
        tvContent.setText("Do you want to Log Out?");

        Button btnYes=dialog.findViewById(R.id.btn_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                mainActivity.logout();




            }
        });

        Button btnCancel=dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                mainActivity.noLogout();
            }
        });

        dialog.show();
    }
}
