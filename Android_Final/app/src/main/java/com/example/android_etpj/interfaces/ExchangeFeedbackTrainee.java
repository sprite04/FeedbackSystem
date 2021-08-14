package com.example.android_etpj.interfaces;

import com.example.android_etpj.models.Assignment;
import com.example.android_etpj.models.Trainee;

public interface ExchangeFeedbackTrainee {
    void loadData();

    void reviewData(Assignment assignment, Trainee trainee);
}

