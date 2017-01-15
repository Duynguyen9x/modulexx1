package com.add.toeic.model.practice;

import java.util.Arrays;

/**
 * Created by sev_user on 12/29/2016.
 */

public class QuestionResponse {

    public int answer;
    public int points;
    public int duration_in_seconds;
    public String url_audio;
    public int negative_points;
    public String question;
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

    public String getUrl_audio() {
        return url_audio;
    }

    public void setUrl_audio(String url_audio) {
        this.url_audio = url_audio;
    }

    public int getNegative_points() {
        return negative_points;
    }

    public void setNegative_points(int negative_points) {
        this.negative_points = negative_points;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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
        return "QuestionResponse{" +
                "answer=" + answer +
                ", points=" + points +
                ", duration_in_seconds=" + duration_in_seconds +
                ", url_audio='" + url_audio + '\'' +
                ", negative_points=" + negative_points +
                ", question=" + question +
                ", options=" + Arrays.toString(options) +
                ", question_type=" + question_type +
                '}';
    }
}
