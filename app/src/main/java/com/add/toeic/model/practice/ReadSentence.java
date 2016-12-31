package com.add.toeic.model.practice;

import java.util.Arrays;

/**
 * Created by sev_user on 12/30/2016.
 */

public class ReadSentence {

    public int answer;
    public int points;
    public int duration_in_seconds;
    public String question;
    public int negative_points;
    public String[] options;
    public int question_type;

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDuration_in_seconds() {
        return duration_in_seconds;
    }

    public void setDuration_in_seconds(int duration_in_seconds) {
        this.duration_in_seconds = duration_in_seconds;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getNegative_points() {
        return negative_points;
    }

    public void setNegative_points(int negative_points) {
        this.negative_points = negative_points;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    @Override
    public String toString() {
        return "ReadSentence{" +
                "answer=" + answer +
                ", points=" + points +
                ", duration_in_seconds=" + duration_in_seconds +
                ", question='" + question + '\'' +
                ", negative_points=" + negative_points +
                ", options=" + Arrays.toString(options) +
                ", question_type=" + question_type +
                '}';
    }
}
