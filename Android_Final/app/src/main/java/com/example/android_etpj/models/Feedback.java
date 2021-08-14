package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Feedback implements Serializable {
    @SerializedName("FeedbackID")
    private int feedbackID;

    @SerializedName("Title")
    private String title;

    @SerializedName("AdminID")
    private String adminID;

    @SerializedName("IsDeleted")
    private boolean isDeleted;

    @SerializedName("TypeFeedbackID")
    private int typeFeedbackID;

    @SerializedName("Admin")
    private Admin admin;

    @SerializedName("TypeFeedback")
    private TypeFeedback typeFeedback;

    @SerializedName("Questions")
    private List<Question> questions;


    public Feedback(String title, String adminID, int typeFeedbackID) {
        this.title = title;
        this.adminID = adminID;
        this.typeFeedbackID = typeFeedbackID;
    }

    public Feedback() {
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public TypeFeedback getTypeFeedback() {
        return typeFeedback;
    }

    public void setTypeFeedback(TypeFeedback typeFeedback) {
        this.typeFeedback = typeFeedback;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public int getTypeFeedbackID() {
        return typeFeedbackID;
    }

    public void setTypeFeedbackID(int typeFeedbackID) {
        this.typeFeedbackID = typeFeedbackID;
    }

    @Override
    public String toString() {
        return title ;
    }
}
