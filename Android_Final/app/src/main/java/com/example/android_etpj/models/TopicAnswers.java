package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TopicAnswers implements Serializable {
    @SerializedName("Topic")
    private Topic topic;

    @SerializedName("Answers")
    public List<Answer> answers;

    public TopicAnswers() {
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "TopicAnswers{" +
                "topic=" + topic +
                '}';
    }
}
