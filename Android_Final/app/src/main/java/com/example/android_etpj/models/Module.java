package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Module implements Serializable {
    @SerializedName("ModuleID")
    private int moduleID;

    @SerializedName("ModuleName")
    private String moduleName;

    @SerializedName("AdminID")
    private String adminID;

    @SerializedName("IsDeleted")
    private boolean isDeleted;

    @SerializedName("StartTime")
    private Date startTime;

    @SerializedName("EndTime")
    private Date endTime;

    @SerializedName("FeedbackStartTime")
    private Date feedbackStartTime;

    @SerializedName("FeedbackEndTime")
    private Date feedbackEndTime;

    @SerializedName("FeedbackID")
    private int feedbackID;

    @SerializedName("Feedback")
    private Feedback feedback;

    @SerializedName("Admin")
    private Admin admin;

    public Module() {
    }

    public Module(String moduleName, String adminID, Date startTime,Date endTime, Date feedbackStartTime, Date feedbackEndTime, int feedbackID) {
        this.moduleName = moduleName;
        this.adminID = adminID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.feedbackStartTime = feedbackStartTime;
        this.feedbackEndTime = feedbackEndTime;
        this.feedbackID = feedbackID;
    }

    public int getModuleID() {
        return moduleID;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getFeedbackStartTime() {
        return feedbackStartTime;
    }

    public void setFeedbackStartTime(Date feedbackStartTime) {
        this.feedbackStartTime = feedbackStartTime;
    }

    public Date getFeedbackEndTime() {
        return feedbackEndTime;
    }

    public void setFeedbackEndTime(Date feedbackEndTime) {
        this.feedbackEndTime = feedbackEndTime;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return moduleName;
    }
}
