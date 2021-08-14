package com.example.android_etpj;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Module;
import com.example.android_etpj.sharedpreference.DataLocal;
import com.example.android_etpj.ui.AccessForbidden2Fragment;
import com.example.android_etpj.ui.AccessForbiddenFragment;
import com.example.android_etpj.ui.AssignmentFragment;
import com.example.android_etpj.ui.ClassFragment;
import com.example.android_etpj.ui.CommentResultFragment;
import com.example.android_etpj.ui.ContactFragment;
import com.example.android_etpj.ui.EnrollmentFragment;
import com.example.android_etpj.ui.FeedbackFragment;
import com.example.android_etpj.ui.FeedbackTraineeFragment;
import com.example.android_etpj.ui.FeedbackTraineeReviewFragment;
import com.example.android_etpj.ui.HomeFragment;
import com.example.android_etpj.ui.LogoutFragment;
import com.example.android_etpj.ui.ModuleFragment;
import com.example.android_etpj.ui.QuestionFragment;
import com.example.android_etpj.ui.ResultFragment;
import com.example.android_etpj.ui.add.*;
import com.example.android_etpj.ui.edit.*;
import com.example.android_etpj.ui.view.ViewClassFragment;
import com.example.android_etpj.ui.view.ViewEnrollmentDetailFragment;
import com.google.android.material.navigation.NavigationView;
import com.example.android_etpj.models.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.IntentCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Type currentFragment=Type.FRAGMENT_HOME;
    private static final int REQUEST_CODE = 12;
    private Role currentRole= Role.ADMIN;
    private Object user;
    private Trainer trainer;
    private Trainee trainee;
    private Admin admin;
    private String role;

    public int type=0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode==RESULT_OK){
            role = (String) data.getExtras().get("ROLE");
            currentRole = Role.valueOf(role);
            user = data.getExtras().get("USER");
            setNavigationView();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawer,toolbar,0,0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);

        checkLogin();

        currentRole = Role.valueOf(DataLocal.getUserRole());

        if (currentRole==Role.ADMIN){
            user = DataLocal.getAdmin();
        }
        else if (currentRole==Role.TRAINER){
            user = DataLocal.getTrainer();
        }
        else  {
            user = DataLocal.getTrainee();
        }
        setNavigationView();

    }

    private void setNavigationView(){

        currentFragment= Type.FRAGMENT_HOME;

        Menu nav_Menu = navigationView.getMenu();
        if(user instanceof Admin){
            nav_Menu.findItem(R.id.nav_join).setVisible(false);
            nav_Menu.findItem(R.id.nav_home).setVisible(true);
            nav_Menu.findItem(R.id.nav_class).setVisible(true);
            nav_Menu.findItem(R.id.nav_module).setVisible(true);
            nav_Menu.findItem(R.id.nav_contact).setVisible(true);
            nav_Menu.findItem(R.id.nav_log_out).setVisible(true);
            nav_Menu.findItem(R.id.nav_result).setVisible(true);
            nav_Menu.findItem(R.id.nav_assignment).setVisible(true);
            nav_Menu.findItem(R.id.nav_enrollment).setVisible(true);
            nav_Menu.findItem(R.id.nav_feedback).setVisible(true);
            nav_Menu.findItem(R.id.nav_question).setVisible(true);

        }
        else if(user instanceof Trainer){
            nav_Menu.findItem(R.id.nav_enrollment).setVisible(false);
            nav_Menu.findItem(R.id.nav_feedback).setVisible(false);
            nav_Menu.findItem(R.id.nav_question).setVisible(false);
            nav_Menu.findItem(R.id.nav_join).setVisible(false);
            nav_Menu.findItem(R.id.nav_home).setVisible(true);
            nav_Menu.findItem(R.id.nav_class).setVisible(true);
            nav_Menu.findItem(R.id.nav_module).setVisible(true);
            nav_Menu.findItem(R.id.nav_contact).setVisible(true);
            nav_Menu.findItem(R.id.nav_log_out).setVisible(true);
            nav_Menu.findItem(R.id.nav_result).setVisible(true);
            nav_Menu.findItem(R.id.nav_assignment).setVisible(true);
        }
        else if(user instanceof Trainee){
            nav_Menu.findItem(R.id.nav_assignment).setVisible(false);
            nav_Menu.findItem(R.id.nav_enrollment).setVisible(false);
            nav_Menu.findItem(R.id.nav_feedback).setVisible(false);
            nav_Menu.findItem(R.id.nav_result).setVisible(false);
            nav_Menu.findItem(R.id.nav_question).setVisible(false);
            nav_Menu.findItem(R.id.nav_home).setVisible(true);
            nav_Menu.findItem(R.id.nav_class).setVisible(true);
            nav_Menu.findItem(R.id.nav_module).setVisible(true);
            nav_Menu.findItem(R.id.nav_contact).setVisible(true);
            nav_Menu.findItem(R.id.nav_log_out).setVisible(true);
            nav_Menu.findItem(R.id.nav_join).setVisible(true);
        }
        else {
            nav_Menu.findItem(R.id.nav_home).setVisible(false);
            nav_Menu.findItem(R.id.nav_assignment).setVisible(false);
            nav_Menu.findItem(R.id.nav_class).setVisible(false);
            nav_Menu.findItem(R.id.nav_module).setVisible(false);
            nav_Menu.findItem(R.id.nav_enrollment).setVisible(false);
            nav_Menu.findItem(R.id.nav_feedback).setVisible(false);
            nav_Menu.findItem(R.id.nav_result).setVisible(false);
            nav_Menu.findItem(R.id.nav_question).setVisible(false);
            nav_Menu.findItem(R.id.nav_contact).setVisible(false);
            nav_Menu.findItem(R.id.nav_join).setVisible(false);
            nav_Menu.findItem(R.id.nav_log_out).setVisible(false);
        }

        if(user instanceof Admin || user instanceof Trainer){
            replaceFragment(new AssignmentFragment(user));
        }
        else if(user instanceof Trainee){
            Trainee trainee=(Trainee)user;
            replaceFragment(new FeedbackTraineeFragment(trainee,0));
        }
        else {
            replaceFragment(new AccessForbidden2Fragment());
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                switch (id){
                    case R.id.nav_home:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_HOME){
                            currentFragment=Type.FRAGMENT_HOME;
                            if(user instanceof Admin || user instanceof Trainer){
                                replaceFragment(new AssignmentFragment(user));
                            }
                            else if(user instanceof Trainee){
                                Trainee trainee=(Trainee)user;
                                replaceFragment(new FeedbackTraineeFragment(trainee,0));
                            }
                            else {
                                replaceFragment(new AccessForbiddenFragment());
                            }

                        }
                        break;
                    case R.id.nav_assignment:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_ASSIGNMENT){
                            currentFragment=Type.FRAGMENT_ASSIGNMENT;
                            if(user instanceof Admin || user instanceof Trainer){
                                replaceFragment(new AssignmentFragment(user));
                            }
                            else if(user instanceof Trainee){
                                replaceFragment(new AccessForbiddenFragment());
                            }
                            else {
                                replaceFragment(new AccessForbidden2Fragment());
                            }

                        }
                        break;
                    case R.id.nav_class:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_CLASS){
                            currentFragment=Type.FRAGMENT_CLASS;
                            if(user instanceof Admin || user instanceof Trainer || user instanceof Trainee){
                                replaceFragment(new ClassFragment(user));
                            }
                            else {
                                replaceFragment(new AccessForbidden2Fragment());
                            }
                        }
                        break;
                    case R.id.nav_module:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_MODULE){
                            currentFragment=Type.FRAGMENT_MODULE;
                            if(user instanceof Admin || user instanceof Trainer || user instanceof Trainee){
                                replaceFragment(new ModuleFragment(user));
                            }
                            else {
                                replaceFragment(new AccessForbidden2Fragment());
                            }
                        }
                        break;
                    case R.id.nav_enrollment:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_ENROLLMENT){
                            currentFragment=Type.FRAGMENT_ENROLLMENT;
                            if(user instanceof Admin){
                                replaceFragment(new EnrollmentFragment());
                            }
                            else if(user instanceof Trainer || user instanceof Trainee){
                                replaceFragment(new AccessForbiddenFragment());
                            }
                            else {
                                replaceFragment(new AccessForbidden2Fragment());
                            }
                        }
                        break;
                    case R.id.nav_feedback:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_FEEDBACK){
                            currentFragment=Type.FRAGMENT_FEEDBACK;
                            if(user instanceof Admin){
                                replaceFragment(new FeedbackFragment());
                            }
                            else if(user instanceof Trainer || user instanceof Trainee){
                                replaceFragment(new AccessForbiddenFragment());
                            }
                            else {
                                replaceFragment(new AccessForbidden2Fragment());
                            }
                        }
                        break;
                    case R.id.nav_result:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_RESULT){
                            currentFragment=Type.FRAGMENT_RESULT;
                            if(user instanceof Admin || user instanceof Trainer){
                                replaceFragment(new ResultFragment(user));
                            }
                            else if(user instanceof Trainee){
                                replaceFragment(new AccessForbiddenFragment());
                            }
                            else {
                                replaceFragment(new AccessForbidden2Fragment());
                            }

                        }
                        break;
                    case R.id.nav_question:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_QUESTION){
                            currentFragment=Type.FRAGMENT_QUESTION;
                            if(user instanceof Admin ){
                                replaceFragment(new QuestionFragment(user));
                            }
                            else if(user instanceof Trainee || user instanceof Trainer){
                                replaceFragment(new AccessForbiddenFragment());
                            }
                            else{
                                replaceFragment(new AccessForbidden2Fragment());
                            }
                        }
                        break;
                    case R.id.nav_contact:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_CONTACT){
                            currentFragment=Type.FRAGMENT_CONTACT;
                            if(user instanceof Admin || user instanceof Trainer || user instanceof Trainee){
                                replaceFragment(new ContactFragment());
                            }
                            else {
                                replaceFragment(new AccessForbidden2Fragment());
                            }
                        }
                        break;
                    case R.id.nav_join:
                        checkLogin();
                        if(currentFragment!=Type.FRAGMENT_JOIN){
                            currentFragment=Type.FRAGMENT_JOIN;
                            if(user instanceof Trainee){
                                try{
                                    Trainee trainee=(Trainee)user;
                                    replaceFragment(new FeedbackTraineeFragment(trainee,1));
                                }catch (Exception e){
                                    Log.e("Notive",e.getMessage());
                                }
                            }
                            else if(user instanceof Admin || user instanceof Trainer) {
                                replaceFragment(new AccessForbiddenFragment());
                            }
                            else {
                                replaceFragment(new AccessForbidden2Fragment());
                            }
                        }
                        break;

                    case R.id.nav_log_out:
                        replaceFragment(new LogoutFragment());

                        break;
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    public void logout() {
        MenuItem item=navigationView.getMenu().findItem(R.id.nav_home);
        item.setChecked(true);
        item.setCheckable(true);

        if(user instanceof Admin || user instanceof Trainer){
            replaceFragment(new AssignmentFragment(user));
        }
        else if(user instanceof Trainee){
            Trainee trainee=(Trainee)user;
            replaceFragment(new FeedbackTraineeFragment(trainee,0));
        }
        else {
            replaceFragment(new AccessForbidden2Fragment());
        }

        DataLocal.setIsLogin(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }

    public void noLogout()
    {
        MenuItem item;
        switch (currentFragment){
            case FRAGMENT_HOME:
                item=navigationView.getMenu().findItem(R.id.nav_home);
                item.setChecked(true);
                item.setCheckable(true);
                if(user instanceof Admin || user instanceof Trainer){
                    replaceFragment(new AssignmentFragment(user));
                }
                else if(user instanceof Trainee){
                    Trainee trainee=(Trainee)user;
                    replaceFragment(new FeedbackTraineeFragment(trainee,0));
                }
                else {
                    replaceFragment(new AccessForbiddenFragment());
                }
                break;
            case FRAGMENT_CLASS:
                item=navigationView.getMenu().findItem(R.id.nav_class);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Admin || user instanceof Trainer || user instanceof Trainee){
                    replaceFragment(new ClassFragment(user));
                }
                else {
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
            case FRAGMENT_MODULE:
                item=navigationView.getMenu().findItem(R.id.nav_module);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Admin || user instanceof Trainer || user instanceof Trainee){
                    replaceFragment(new ModuleFragment(user));
                }
                else {
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
            case FRAGMENT_CONTACT:
                item=navigationView.getMenu().findItem(R.id.nav_contact);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Admin || user instanceof Trainer || user instanceof Trainee){
                    replaceFragment(new ContactFragment());
                }
                else {
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
            case FRAGMENT_RESULT:
                item=navigationView.getMenu().findItem(R.id.nav_result);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Admin || user instanceof Trainer){
                    replaceFragment(new ResultFragment(user));
                }
                else if(user instanceof Trainee){
                    replaceFragment(new AccessForbiddenFragment());
                }
                else {
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
            case FRAGMENT_ASSIGNMENT:
                item=navigationView.getMenu().findItem(R.id.nav_assignment);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Admin || user instanceof Trainer){
                    replaceFragment(new AssignmentFragment(user));
                }
                else if(user instanceof Trainee){
                    replaceFragment(new AccessForbiddenFragment());
                }
                else {
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
            case FRAGMENT_FEEDBACK:
                item=navigationView.getMenu().findItem(R.id.nav_feedback);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Admin){
                    replaceFragment(new FeedbackFragment());
                }
                else if(user instanceof Trainer || user instanceof Trainee){
                    replaceFragment(new AccessForbiddenFragment());
                }
                else {
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
            case FRAGMENT_QUESTION:
                item=navigationView.getMenu().findItem(R.id.nav_question);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Admin ){
                    replaceFragment(new QuestionFragment(user));
                }
                else if(user instanceof Trainee || user instanceof Trainer){
                    replaceFragment(new AccessForbiddenFragment());
                }
                else{
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
            case FRAGMENT_ENROLLMENT:
                item=navigationView.getMenu().findItem(R.id.nav_enrollment);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Admin){
                    replaceFragment(new EnrollmentFragment());
                }
                else if(user instanceof Trainer || user instanceof Trainee){
                    replaceFragment(new AccessForbiddenFragment());
                }
                else {
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
            case FRAGMENT_JOIN:
                item=navigationView.getMenu().findItem(R.id.nav_join);
                item.setChecked(true);
                item.setCheckable(true);

                if(user instanceof Trainee){
                    try{
                        Trainee trainee=(Trainee)user;
                        replaceFragment(new FeedbackTraineeFragment(trainee,1));
                    }catch (Exception e){
                        Log.e("Notive",e.getMessage());
                    }
                }
                else if(user instanceof Admin || user instanceof Trainer) {
                    replaceFragment(new AccessForbiddenFragment());
                }
                else {
                    replaceFragment(new AccessForbidden2Fragment());
                }
                break;
        }
    }



    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame,fragment);
        fragmentTransaction.commit();
    }

    private void checkLogin() {
        if(DataLocal.getIsLogin()==false){
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivityForResult(intent,REQUEST_CODE);
        }

        if(DataLocal.getIsLogin()==true){
            Date date=DataLocal.getDateLogin();
            boolean rememberMe=DataLocal.getRememberMe();

            long distance= Calendar.getInstance().getTime().getTime()-date.getTime();

            if(rememberMe==false && distance>30*60*1000){ //milliseconds = 30 minutes
                DataLocal.setIsLogin(false);
                checkLogin();
            }
            else if(rememberMe==true && distance>24*60*60*1000){ // 24 hours
                DataLocal.setIsLogin(false);
                checkLogin();
            }

        }


    }

    public void backFeedbackFragment(){
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    public void backFeedbackFragmentRV(){

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    public void addModuleFragment(Admin admin){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        AddModuleFragment addModuleFragment=new AddModuleFragment(admin);

        fragmentTransaction.replace(R.id.content_frame,addModuleFragment);
        fragmentTransaction.addToBackStack(AddModuleFragment.TAG);
        fragmentTransaction.commit();
    }

    public void editModuleFragment(Module module) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        EditModuleFragment editModuleFragment=new EditModuleFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("MODULE",module);
        editModuleFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,editModuleFragment);
        fragmentTransaction.addToBackStack(EditModuleFragment.TAG);
        fragmentTransaction.commit();
    }

    public void addFeedbackFragment() {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        AddFeedbackFragment addFeedbackFragment=new AddFeedbackFragment();

        fragmentTransaction.replace(R.id.content_frame,addFeedbackFragment);
        fragmentTransaction.addToBackStack(AddFeedbackFragment.TAG);
        fragmentTransaction.commit();
    }

    public void reviewAddFeedbackFragment(Feedback feedback){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        ReviewAddFeedbackFragment reviewFeedbackFragment=new ReviewAddFeedbackFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("FEEDBACKREVIEW",feedback);
        reviewFeedbackFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,reviewFeedbackFragment);
        fragmentTransaction.addToBackStack(ReviewAddFeedbackFragment.TAG);
        fragmentTransaction.commit();
    }

    public void reviewEditFeedbackFragment(Feedback feedback, int type){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        ReviewEditFeedbackFragment reviewFeedbackFragment=new ReviewEditFeedbackFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("FEEDBACKREVIEW",feedback);
        bundle.putInt("TYPEREVIEW",type);
        reviewFeedbackFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,reviewFeedbackFragment);
        fragmentTransaction.addToBackStack(ReviewEditFeedbackFragment.TAG);
        fragmentTransaction.commit();
    }

    public void editFeedbackFragment(Feedback feedback,int type) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        EditFeedbackFragment editFeedbackFragment=new EditFeedbackFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("FEEDBACK_EDIT",feedback);
        bundle.putInt("TYPE",type);
        editFeedbackFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,editFeedbackFragment);
        fragmentTransaction.addToBackStack(EditFeedbackFragment.TAG);
        fragmentTransaction.commit();
    }

    public void reviewFeedbackFragment(Feedback feedback){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        ReviewFeedbackFragment reviewFeedbackFragment=new ReviewFeedbackFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("FEEDBACKREVIEW",feedback);
        reviewFeedbackFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,reviewFeedbackFragment);
        fragmentTransaction.addToBackStack(ReviewFeedbackFragment.TAG);
        fragmentTransaction.commit();
    }

    public void addFeedbackTraineeReviewFragment(Assignment assignment, Trainee trainee){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        FeedbackTraineeReviewFragment feedbackTraineeReviewFragment=new FeedbackTraineeReviewFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("ASSIGNMENT",assignment);
        bundle.putSerializable("TRAINEE",trainee);
        feedbackTraineeReviewFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,feedbackTraineeReviewFragment);
        fragmentTransaction.addToBackStack(FeedbackTraineeReviewFragment.TAG);
        fragmentTransaction.commit();
    }

    public void addClassFragment(){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        AddClassFragment addClassFragment=new AddClassFragment();

        fragmentTransaction.replace(R.id.content_frame,addClassFragment);
        fragmentTransaction.addToBackStack(AddClassFragment.TAG);
        fragmentTransaction.commit();
    }

    public void editClassFragment(Class clss) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        EditClassFragment editClassFragment=new EditClassFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("CLASS",clss);
        editClassFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,editClassFragment);
        fragmentTransaction.addToBackStack(EditModuleFragment.TAG);
        fragmentTransaction.commit();
    }

    public void viewClassFragment(Class clss) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        ViewClassFragment viewClassFragment=new ViewClassFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("VIEWCLASS",clss);
        viewClassFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,viewClassFragment);
        fragmentTransaction.addToBackStack(EditModuleFragment.TAG);
        fragmentTransaction.commit();
    }


    public void addQuestionFragment(){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        AddQuestionFragment addQuestionFragment=new AddQuestionFragment();

        fragmentTransaction.replace(R.id.content_frame,addQuestionFragment);
        fragmentTransaction.addToBackStack(AddQuestionFragment.TAG);
        fragmentTransaction.commit();
    }

    public void editQuestionFragment(Question question) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        EditQuestionFragment editQuestionFragment=new EditQuestionFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("QUESTION",question);
        editQuestionFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,editQuestionFragment);
        fragmentTransaction.addToBackStack(EditQuestionFragment.TAG);
        fragmentTransaction.commit();
    }

    public void addEnrollmentFragment(){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        AddEnrollmentFragment addEnrollmentFragment=new AddEnrollmentFragment();

        fragmentTransaction.replace(R.id.content_frame,addEnrollmentFragment);
        fragmentTransaction.addToBackStack(AddEnrollmentFragment.TAG);
        fragmentTransaction.commit();
    }

    public void editEnrollmentFragment(Enrollment enrollment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        EditEnrollmentFragment editEnrollmentFragment=new EditEnrollmentFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("ENROLLMENT",enrollment);
        editEnrollmentFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,editEnrollmentFragment);
        fragmentTransaction.addToBackStack(EditEnrollmentFragment.TAG);
        fragmentTransaction.commit();
    }

    public void viewEnrollmentFragment(Enrollment enrollment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        ViewEnrollmentDetailFragment viewEnrollmentFragment=new ViewEnrollmentDetailFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("ENROLLMENT",enrollment);
        viewEnrollmentFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,viewEnrollmentFragment);
        fragmentTransaction.addToBackStack(EditEnrollmentFragment.TAG);
        fragmentTransaction.commit();
    }

/*    public void viewCommentResultFragment(Object user) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        CommentResultFragment commentResultFragment=new CommentResultFragment(user);


        fragmentTransaction.replace(R.id.content_frame,commentResultFragment);
        fragmentTransaction.addToBackStack(CommentResultFragment.TAG);
        fragmentTransaction.commit();
    }*/

    public void addAssignmentFragment(){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        AddAssignmentFragment addAssignmentFragment=new AddAssignmentFragment();

        fragmentTransaction.replace(R.id.content_frame,addAssignmentFragment);
        fragmentTransaction.addToBackStack(AddAssignmentFragment.TAG);
        fragmentTransaction.commit();
    }
    public void editAssignmentFragment(Assignment assignment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        EditAssignmentFragment editAssignmentFragment=new EditAssignmentFragment();
        Bundle bundle=new Bundle();

        bundle.putSerializable("ASSIGNMENT",assignment);
        editAssignmentFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.content_frame,editAssignmentFragment);
        fragmentTransaction.addToBackStack(EditAssignmentFragment.TAG);
        fragmentTransaction.commit();
    }

    public void viewCommentResultFragment(Object user) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        CommentResultFragment commentResultFragment=new CommentResultFragment(user);


        fragmentTransaction.replace(R.id.content_frame,commentResultFragment);
        fragmentTransaction.addToBackStack(CommentResultFragment.TAG);
        fragmentTransaction.commit();
    }

}