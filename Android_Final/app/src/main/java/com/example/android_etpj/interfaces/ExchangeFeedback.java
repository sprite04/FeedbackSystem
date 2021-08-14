package com.example.android_etpj.interfaces;

import com.example.android_etpj.models.Feedback;

public interface ExchangeFeedback {
    void loadData();

    void editData(Feedback feedback);

    void viewData(Feedback feedback);
}
