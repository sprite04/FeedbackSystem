package com.example.android_etpj.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Review implements Serializable {
    @SerializedName("Answers")
    private ArrayList<Answer> answers;

    @SerializedName("TraineeComment")
    private Trainee_Comment traineeComment;

    public Review() {
    }

    public Review(ArrayList<Answer> answers, Trainee_Comment traineeComment) {
        this.answers = answers;
        this.traineeComment = traineeComment;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Answer> answers) {
        this.answers = answers;
    }

    public Trainee_Comment getTraineeComment() {
        return traineeComment;
    }

    public void setTraineeComment(Trainee_Comment traineeComment) {
        this.traineeComment = traineeComment;
    }
}
