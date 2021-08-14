package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Enrollment implements Serializable {
    @SerializedName("ClassID")
    private int classId;

    @SerializedName("TraineeID")
    private String traineeId;

    @SerializedName("Class")
    private Class aClass;

    @SerializedName("Trainee")
    private Trainee trainee;

    public Enrollment() {
    }

    public Enrollment(int classId, String traineeId) {
        this.classId = classId;
        this.traineeId = traineeId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }
}
