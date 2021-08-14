package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommentResult implements Serializable {

    @SerializedName("ClassID")
    private int classId;
    @SerializedName("ModuleID")
    private int moduleId;
    @SerializedName("TraineeID")
    private String traineeId;
    @SerializedName("Comment")
    private String comment;
    @SerializedName("Class")
    private Class aClass;
    @SerializedName("Module")
    private Module module;

    public CommentResult() {
    }

    public CommentResult(int classId, int moduleId, String traineeId, String comment) {
        this.classId = classId;
        this.moduleId = moduleId;
        this.traineeId = traineeId;
        this.comment = comment;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(String traineeId) {
        this.traineeId = traineeId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}
