package com.example.android_etpj.ui;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android_etpj.R;
import com.example.android_etpj.SpinnerAdapter;
import com.example.android_etpj.api.ApiService;
import com.example.android_etpj.interfaces.ExchangeResult;
import com.example.android_etpj.models.Admin;
import com.example.android_etpj.models.Answer;
import com.example.android_etpj.models.Class;
import com.example.android_etpj.models.Module;
import com.example.android_etpj.models.Trainer;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassStatisticFragment extends Fragment {

    private Spinner spClassSearch;
    private Spinner spModuleSearch;

    private TextView tvClassSpinner;
    private TextView tvModuleSpinner;
    private TextView tvStatisticTitle;
    private TextView tvGraphTitle;

    private Class clss;
    private Module module;

    private PieChart pcClassStatistic;

    private List<Answer> answerList;
    private List<Float> countList;
    private List<String> titleList;

    private Object user;
    private ExchangeResult exchangeResult;

    private Button btnDetail;



    public ClassStatisticFragment(Object user, ExchangeResult exchangeResult) {
        this.exchangeResult=exchangeResult;
        this.user = user;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_class_statistic, container, false);

        btnDetail=view.findViewById(R.id.btn_detail);

        clss = new Class();
        module = new Module();

        tvClassSpinner = view.findViewById(R.id.tv_title_search_1);
        tvModuleSpinner = view.findViewById(R.id.tv_title_search_2);
        tvStatisticTitle = view.findViewById(R.id.tv_title_statistic);
        tvGraphTitle = view.findViewById(R.id.tv_title_graph);

        tvClassSpinner.setText("Class:");
        tvModuleSpinner.setText("Module:");

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


        pcClassStatistic = view.findViewById(R.id.pie_chart_class);
        pcClassStatistic.setDrawHoleEnabled(false);
        setClassPieChart();

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeResult.tranferPage(2);
            }
        });

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
                        String tmp = "<font color = #000000>Feedback Statistics of Class </font> <font color = #F4D484>" + clss.getClassName() +"</font>";
                        tvStatisticTitle.setText(Html.fromHtml(tmp,1));
                        setClassPieChart();
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
                        String tmp = "<font color = #000000>Feedback Statistics of Module </font> <font color = #F4D484>" + module.getModuleName() +"</font>";
                        tvGraphTitle.setText(Html.fromHtml(tmp,1));
                        setClassPieChart();
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
                        String tmp = "<font color = #000000>Feedback Statistics of Class </font> <font color = #F4D484>" + clss.getClassName() +"</font>";
                        tvStatisticTitle.setText(Html.fromHtml(tmp,1));
                        setClassPieChart();
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
                        String tmp = "<font color = #000000>Feedback Statistics of Module </font> <font color = #F4D484>" + module.getModuleName() +"</font>";
                        tvGraphTitle.setText(Html.fromHtml(tmp,1));
                        setClassPieChart();
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

    private void setClassPieChart() {
        titleList = new ArrayList<>();
        answerList = new ArrayList<>();
        countList = new ArrayList<>();

        titleList.clear();
        answerList.clear();
        countList.clear();

        titleList.add(new String("Strongly Disagree"));
        titleList.add(new String("Disagree"));
        titleList.add(new String("Neural"));
        titleList.add(new String("Agree"));
        titleList.add(new String("Strong Agree"));

        for (int i = 0; i < 5 ; i++){
            countList.add(new Float(0));
        }

        ApiService.apiService.getAnswersByClassModule(clss.getClassID(),module.getModuleID()).enqueue(new Callback<List<Answer>>() {
            @Override
            public void onResponse(Call<List<Answer>> call, Response<List<Answer>> response) {
                answerList = (ArrayList<Answer>) response.body();

                for (Answer answer:answerList) {
                    switch (answer.getValue()) {
                        case 0:
                            countList.set(0,countList.get(0)+1);
                            break;
                        case 1:
                            countList.set(1,countList.get(1)+1);
                            break;
                        case 2:
                            countList.set(2,countList.get(2)+1);
                            break;
                        case 3:
                            countList.set(3,countList.get(3)+1);
                            break;
                        case 4:
                            countList.set(4,countList.get(4)+1);
                            break;
                    }
                }

                ArrayList<PieEntry> pieEntries=new ArrayList<>();
                for(int i=0; i<countList.size(); i++){
                    if (countList.get(i) != 0) {
                        pieEntries.add(new PieEntry(((countList.get(i)*100 / answerList.size())), titleList.get(i) + " (%)"));
                    }
                }

                pcClassStatistic.clear();
                PieDataSet pieDataSet=new PieDataSet(pieEntries,"Class Statistic");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.WHITE);
                pieDataSet.setValueTextSize(25f);

                PieData pieData=new PieData(pieDataSet);
                pcClassStatistic.setData(pieData);
                pcClassStatistic.getDescription().setEnabled(false);
                pcClassStatistic.animate();
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {

            }
        });


    }
}