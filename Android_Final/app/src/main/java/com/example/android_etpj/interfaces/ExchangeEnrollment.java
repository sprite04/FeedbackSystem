package com.example.android_etpj.interfaces;

import com.example.android_etpj.models.Enrollment;

public interface ExchangeEnrollment {
    void loadData();

    void editData(Enrollment enrollment);

    void viewData(Enrollment enrollment);
}
