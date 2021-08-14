package com.example.android_etpj;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Admin;
import com.example.android_etpj.models.Trainee;
import com.example.android_etpj.models.Trainer;
import com.example.android_etpj.sharedpreference.DataLocal;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView txtvLogin;
    private Button btnExit;
    private EditText edtUsername;
    private EditText edtPassword;
    private CheckBox chkRememberMe;
    private TextView txtErrorUsername;
    private Spinner spRole;
    private ImageView imgvRoleMenu;
    private Role currentRole;
    private TextView txtErrorPassword;
    public LoginActivity() {
    }

    private Object user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        getSupportActionBar().hide();
        //view1 = findViewById(R.id.layout_login);
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        txtvLogin = findViewById(R.id.btn_login);
        currentRole = Role.ADMIN;
        chkRememberMe = findViewById(R.id.remember_me);
        txtErrorUsername = findViewById(R.id.username_error);
        txtErrorPassword = findViewById(R.id.password_error);
        spRole = findViewById(R.id.sp_role);
        txtErrorUsername.setVisibility(View.INVISIBLE);
        txtErrorPassword.setVisibility(View.INVISIBLE);
        user = new Object();

        setBtnLogin();

        setRoleSpinner();
        String hashed = md5("123456");
        if (hashed!=""){
            Log.e("hash",hashed);
        }

        if(DataLocal.getRememberMe()==true){
            edtUsername.setText(DataLocal.getUserLogin());
            edtPassword.setText(DataLocal.getUserPassword());
            chkRememberMe.setChecked(true);
            String role = DataLocal.getUserRole();
            currentRole = Role.valueOf(role);
            if (currentRole==Role.ADMIN){
                spRole.setSelection(0);
            }
            else if (currentRole==Role.TRAINER){
                spRole.setSelection(1);
            }
            else {
                spRole.setSelection(2);
            }


        }


        //setImgvRoleMenu();
        //setSignInButton();
        //setExitButton();
        //setFloatingActionButton();

    }
    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void setRoleSpinner(){
        ArrayList<String> roles= new ArrayList<String>();
        roles.add(0,Role.ADMIN.name());
        //roles.add(Role.ADMIN.name());
        roles.add(1,Role.TRAINER.name());
        roles.add(2,Role.TRAINEE.name());

        SpinnerRoleAdapter spinnerAdapter=new SpinnerRoleAdapter(this,R.layout.item_sp_login_selected,roles);
        spinnerAdapter.setDropDownViewResource(R.layout.item_sp_role);
        spRole.setAdapter(spinnerAdapter);
    }

    private void setLoginErrorDialog(){
        Button btnYes ;

        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dialog_login_warning);
        btnYes = dialog.findViewById(R.id.btn_yes);

        dialog.setCancelable(false);

        btnYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void roleLogin(String role, String username, String password){
        String pwHash = md5(password);
        if (role == Role.ADMIN.name()){
            Callback<Admin> call_admin = new Callback<Admin>() {
                @Override
                public void onResponse(Call<Admin> call, Response<Admin> response) {
                    user = (Object)  response.body();
                    if (user != null){
                        //sessionManager.saveAuthToken(loginResponse.authToken)
                        DataLocal.setIsLogin(true);
                        DataLocal.setDateLogin(Calendar.getInstance().getTime());
                        DataLocal.setUser(user);
                        DataLocal.setUserPassword(password);
                        DataLocal.setUserLogin(username);
                        DataLocal.setUserRole(Role.ADMIN.name());
                        DataLocal.setIsRememberMe(chkRememberMe.isChecked());

                        //edtPassword.setText(  ((Admin) user).getEmail());

                        Bundle bundle=new Bundle();
                        bundle.putSerializable("USER",((Serializable) user));
                        bundle.putString("ROLE", role);
                        Intent intent=getIntent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                    else{

                        setLoginErrorDialog();

                    }


                }

                @Override
                public void onFailure(Call<Admin> call, Throwable t) {

                    edtPassword.setText("fail");
                }
            };
            ApiService.apiService.loginAdmin(username, pwHash).enqueue(call_admin);
        }
        else if (role == Role.TRAINER.name()){
            Callback<Trainer> callTrainer = new Callback<Trainer>() {
                @Override
                public void onResponse(Call<Trainer> call, Response<Trainer> response) {
                    user = (Object)  response.body();


                    if (user != null){
                        DataLocal.setIsLogin(true);
                        DataLocal.setDateLogin(Calendar.getInstance().getTime());
                        DataLocal.setUser(user);
                        DataLocal.setUserPassword(password);
                        DataLocal.setUserLogin(username);
                        DataLocal.setUserRole(Role.TRAINER.name());
                        DataLocal.setIsRememberMe(chkRememberMe.isChecked());

                        //edtPassword.setText(  ((Trainer) user).getEmail());

                        Bundle bundle=new Bundle();
                        bundle.putSerializable("USER",((Serializable) user));
                        bundle.putString("ROLE", role);
                        Intent intent=getIntent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();

                    }
                    else{

                        setLoginErrorDialog();

                    }
                }

                @Override
                public void onFailure(Call<Trainer> call, Throwable t) {
                    edtPassword.setText("fail");
                }
            };
            ApiService.apiService.loginTrainer(username, pwHash).enqueue(callTrainer);
        }
        else{
            Callback<Trainee> callTrainee = new Callback<Trainee>() {
                @Override
                public void onResponse(Call<Trainee> call, Response<Trainee> response) {
                    user = (Object)  response.body();
                    //DataLocal.getInstance();
                    if (user != null){
                        DataLocal.setIsLogin(true);
                        DataLocal.setDateLogin(Calendar.getInstance().getTime());
                        DataLocal.setUser(user);
                        DataLocal.setUserPassword(password);
                        DataLocal.setUserLogin(username);
                        DataLocal.setUserRole(Role.TRAINEE.name());
                        DataLocal.setIsRememberMe(chkRememberMe.isChecked());

                        //edtPassword.setText(  ((Trainee) user).getEmail());

                        Bundle bundle=new Bundle();
                        bundle.putSerializable("USER",((Serializable) user));
                        bundle.putString("ROLE", role);
                        Intent intent=getIntent();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK,intent);
                        finish();

                    }
                    else{

                        setLoginErrorDialog();

                    }

                }

                @Override
                public void onFailure(Call<Trainee> call, Throwable t) {
                    edtPassword.setText("fail");
                }
            };
            ApiService.apiService.loginTrainee(username, pwHash).enqueue(callTrainee);
        }



    }

    private void setBtnLogin(){
        txtvLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = edtUsername.getText().toString();
                        String password = edtPassword.getText().toString();

                        if (username.isEmpty() == true) {
                            txtErrorUsername.setText("Username must have at least 1 character!");
                            txtErrorUsername.setVisibility(View.VISIBLE);
                            if (password.isEmpty() == true){
                                txtErrorPassword.setText("Password must have at least 1 character!");
                                txtErrorPassword.setVisibility(View.VISIBLE);
                            }
                            return;
                        }
                        else if (password.isEmpty() == true){
                            txtErrorPassword.setText("Password must have at least 1 character!");
                            txtErrorPassword.setVisibility(View.VISIBLE);
                            return;
                        }


                        if (username.contains(" ") ) {
                            txtErrorUsername.setText("Username must have at blank space!");

                            txtErrorUsername.setVisibility(View.VISIBLE);
                            if( password.isEmpty() == true){
                                txtErrorPassword.setText("Password must have at least 1 character!");
                                txtErrorPassword.setVisibility(View.VISIBLE);
                            }
                            return;
                        }
                        else if (password.isEmpty() == true){
                            txtErrorPassword.setText("Password must have at least 1 character!");
                            txtErrorPassword.setVisibility(View.VISIBLE);
                            return;
                        }


                        txtErrorUsername.setVisibility(View.INVISIBLE);
                        txtErrorPassword.setVisibility(View.INVISIBLE);

                        //password = md5(password);
                        roleLogin(spRole.getSelectedItem().toString(),username,password);

                    }
                }
        );
    }
}
