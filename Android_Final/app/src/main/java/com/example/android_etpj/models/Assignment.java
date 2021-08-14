package com.example.android_etpj.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.android_etpj.models.Class;


import java.io.Serializable;

public class Assignment implements Serializable {
    @SerializedName("ClassID")
    private int classID;

    @SerializedName("ModuleID")
    private int moduleID;

    @SerializedName("TrainerID")
    private String trainerID;

    @SerializedName("RegistrationCode")
    private String registrationCode;

    @SerializedName("Class")
    private Class clss;

    @SerializedName("Module")
    private Module module;

    @SerializedName("Trainer")
    private Trainer trainer;

    public Assignment() {
    }

    public Assignment(int classID, int moduleID, String trainerID, String registrationCode) {
        this.classID = classID;
        this.moduleID = moduleID;
        this.trainerID = trainerID;
        this.registrationCode = registrationCode;
    }

    public Class getClss() {
        return clss;
    }

    public void setClss(Class clss) {
        this.clss = clss;
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

    public String getTrainerID() {
        return trainerID;
    }

    public void setTrainerID(String trainerID) {
        this.trainerID = trainerID;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }



    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "classID=" + classID +
                ", moduleID=" + moduleID +
                ", trainerID='" + trainerID + '\'' +
                ", registrationCode='" + registrationCode + '\'' +
                '}';
    }
}
