package com.example.android_etpj.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android_etpj.MainActivity;
import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeResult;
import com.example.android_etpj.models.Admin;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Module;
import com.example.android_etpj.models.Question;
import com.example.android_etpj.models.Statistic;
import com.example.android_etpj.models.TopicStatistic;
import com.example.android_etpj.models.Trainer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PercentStatisticFragment extends Fragment {

    private Spinner spClassSearch;
    private Spinner spModuleSearch;

    private Class clss;
    private Module module;

    private LinearLayout layoutMain;
    private MainActivity mainActivity;

    private Object user;
    private Button btnComment;

    private Button btnOverview;
    private ExchangeResult exchangeResult;

    public PercentStatisticFragment(Object user, ExchangeResult exchangeResult) {
        this.exchangeResult=exchangeResult;
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_percent_statistic, container, false);

        mainActivity=(MainActivity)getActivity();
        clss = new Class();
        module = new Module();

        btnComment=view.findViewById(R.id.btn_comment);
        btnOverview=view.findViewById(R.id.btn_overview);

        spClassSearch = view.findViewById(R.id.sp_search_1);
        spModuleSearch = view.findViewById(R.id.sp_search_2);

        //Phan quyen
        if (user instanceof Admin) {
            setAdminClassSpinnerSearch();
            setAdminModuleSpinnerSearch();
        } else if (user instanceof Trainer) {
            setTrainerClassSpinnerSearch();
            setTrainerModuleSpinnerSearch();
        } else {
            view =  inflater.inflate(R.layout.fragment_access_forbidden_2, container, false);
            return view;
        }

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.viewCommentResultFragment(user);
            }
        });

        btnOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeResult.tranferPage(0);
            }
        });

        layoutMain = view.findViewById(R.id.layout_percent_statistic);
        setPercentStatistic();

        return view;
    }

    private void setAdminClassSpinnerSearch() {
        ApiService.apiService.getClasses().enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                List<Class> arrayClassList=(ArrayList<Class>) response.body();
                List<Object> classes = new ArrayList<>();
                classes.addAll(arrayClassList);

                SpinnerAdapter spClassAdapter = new SpinnerAdapter(getContext(),R.layout.item_sp_selected,classes);
                try {
                    spClassSearch.setAdapter(spClassAdapter);
                } catch (Exception e) {
                    Log.e("a",e.getMessage());
                }


                if(arrayClassList.size()>=2) {
                    spClassSearch.setSelection(spClassAdapter.getPosition(arrayClassList.get(0)));
                }

                spClassSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spClassAdapter.getItem(position);
                        clss = (Class) object;
                        setPercentStatistic();
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

    private void setAdminModuleSpinnerSearch() {
        ApiService.apiService.getModules().enqueue(new Callback<List<Module>>() {
            @Override
            public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {
                List<Module> arrayModuleList=(ArrayList<Module>) response.body();
                List<Object> modules = new ArrayList<>();
                modules.addAll(arrayModuleList);

                SpinnerAdapter spModuleAdapter = new SpinnerAdapter(getContext(),R.layout.item_sp_selected,modules);
                try {
                    spModuleSearch.setAdapter(spModuleAdapter);
                } catch (Exception e) {
                    Log.e("a",e.getMessage());
                }


                if(arrayModuleList.size()>=2) {
                    spModuleSearch.setSelection(spModuleAdapter.getPosition(arrayModuleList.get(0)));
                }

                spModuleSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spModuleAdapter.getItem(position);
                        module = (Module)object;
                        setPercentStatistic();
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

    private void setTrainerClassSpinnerSearch() {
        ApiService.apiService.getClassesByTrainer(((Trainer)user).getUsername()).enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                List<Class> arrayClassList=(ArrayList<Class>) response.body();
                List<Object> classes = new ArrayList<>();
                classes.addAll(arrayClassList);

                SpinnerAdapter spClassAdapter = new SpinnerAdapter(getContext(),R.layout.item_sp_selected,classes);
                try {
                    spClassSearch.setAdapter(spClassAdapter);
                } catch (Exception e) {
                    Log.e("a",e.getMessage());
                }


                if(arrayClassList.size()>=2) {
                    spClassSearch.setSelection(spClassAdapter.getPosition(arrayClassList.get(0)));
                }

                spClassSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spClassAdapter.getItem(position);
                        clss = (Class) object;
                        setPercentStatistic();
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

    private void setTrainerModuleSpinnerSearch() {
        ApiService.apiService.getModuleByIdTrainer(((Trainer)user).getUsername()).enqueue(new Callback<List<Module>>() {
            @Override
            public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {
                List<Module> arrayModuleList=(ArrayList<Module>) response.body();
                List<Object> modules = new ArrayList<>();
                modules.addAll(arrayModuleList);

                SpinnerAdapter spModuleAdapter = new SpinnerAdapter(getContext(),R.layout.item_sp_selected,modules);
                try {
                    spModuleSearch.setAdapter(spModuleAdapter);
                } catch (Exception e) {
                    Log.e("a",e.getMessage());
                }


                if(arrayModuleList.size()>=2) {
                    spModuleSearch.setSelection(spModuleAdapter.getPosition(arrayModuleList.get(0)));
                }

                spModuleSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object object=spModuleAdapter.getItem(position);
                        module = (Module)object;
                        setPercentStatistic();
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

    private void setPercentStatistic () {
        layoutMain.removeAllViews();

        ApiService.apiService.getTopicStatisticByClassModule(clss.getClassID(),module.getModuleID()).enqueue(new Callback<List<TopicStatistic>>() {
            @Override
            public void onResponse(Call<List<TopicStatistic>> call, Response<List<TopicStatistic>> response) {
                List<TopicStatistic> topicStatisticList = (ArrayList<TopicStatistic>) response.body();
                for (int i = 0; i < topicStatisticList.size(); i++) {

                    View viewTopic;
                    viewTopic = getLayoutInflater().inflate(R.layout.item_topic_statistic,null,false);

                    TextView tvTopic = viewTopic.findViewById(R.id.tv_title_topic);
                    String topicString = "<b>" + (i+1) + "<b>. " + topicStatisticList.get(i).getTopic().getTopicName() +"<br>";
                    tvTopic.setText(Html.fromHtml(topicString,1));

                    List<Question> questionList = (ArrayList<Question>) topicStatisticList.get(i).getTopic().getQuestions();
                    List<Statistic> statisticList = (ArrayList<Statistic>) topicStatisticList.get(i).getStatistics();

                    layoutMain.addView(viewTopic);

                    for (int j = 0; j < questionList.size(); j++) {
                        Float sum = new Float(0.0);

                        for (int value : statisticList.get(j).getAmount()) {
                            sum = sum + value;
                        }

                        if (sum > 0) {

                            View viewQuestion;
                            viewQuestion = getLayoutInflater().inflate(R.layout.item_question_statistic, null, false);

                            TextView tvQuestion = viewQuestion.findViewById(R.id.tv_title_question);
                            String questionString = "- " + questionList.get(j).getQuestionContent();
                            tvQuestion.setText(Html.fromHtml(questionString, 1));

                            layoutMain.addView(viewQuestion);


                            List<Float> percentList = new ArrayList<>();

                            for (int k = 0; k < statisticList.get(j).getAmount().size(); k++) {
                                if (sum != 0) {
                                    percentList.add(statisticList.get(j).getAmount().get(k) * 100 / sum);
                                } else {
                                    percentList.add(new Float(0));
                                }

                            }

                            View viewAnswer;
                            viewAnswer = getLayoutInflater().inflate(R.layout.item_answer_statistic, null, false);

                            for (int x = 0; x < statisticList.get(j).getAmount().size(); x++) {
                                switch (x) {
                                    case 0:
                                        TextView tvAnswer0 = viewAnswer.findViewById(R.id.tv_answer0);
                                        tvAnswer0.setText(String.valueOf(percentList.get(0)) + "%");
                                        break;
                                    case 1:
                                        TextView tvAnswer1 = viewAnswer.findViewById(R.id.tv_answer1);
                                        tvAnswer1.setText(String.valueOf(percentList.get(1)) + "%");
                                        break;
                                    case 2:
                                        TextView tvAnswer2 = viewAnswer.findViewById(R.id.tv_answer2);
                                        tvAnswer2.setText(String.valueOf(percentList.get(2)) + "%");
                                        break;
                                    case 3:
                                        TextView tvAnswer3 = viewAnswer.findViewById(R.id.tv_answer3);
                                        tvAnswer3.setText(String.valueOf(percentList.get(3)) + "%");
                                        break;
                                    case 4:
                                        TextView tvAnswer4 = viewAnswer.findViewById(R.id.tv_answer4);
                                        tvAnswer4.setText(String.valueOf(percentList.get(4)) + "%");
                                        break;
                                }
                            }
                            layoutMain.addView(viewAnswer);
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<TopicStatistic>> call, Throwable t) {

            }
        });
    }
}