package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trainee_Comment implements Serializable {
    @SerializedName("ClassID")
    private int classID;

    @SerializedName("ModuleID")
    private int moduleID;

    @SerializedName("TraineeID")
    private String traineeID;

    @SerializedName("Comment")
    private String comment;

    public Trainee_Comment() {
    }

    public Trainee_Comment(int classID, int moduleID, String traineeID, String comment) {
        this.classID = classID;
        this.moduleID = moduleID;
        this.traineeID = traineeID;
        this.comment = comment;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public void setModuleID(int moduleID) {
        this.moduleID = moduleID;
    }

    public String getTraineeID() {
        return traineeID;
    }

    public void setTraineeID(String traineeID) {
        this.traineeID = traineeID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
