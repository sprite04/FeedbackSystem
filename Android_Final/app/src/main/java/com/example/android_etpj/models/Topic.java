package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {
    @SerializedName("TopicID")
    private int topicID;

    @SerializedName("TopicName")
    private String topicName;

    @SerializedName("Questions")
    private List<Question> questions;

    public Topic() {
    }

    public Topic(int topicID, String topicName) {
        this.topicID = topicID;
        this.topicName = topicName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        return topicName;
    }
}
