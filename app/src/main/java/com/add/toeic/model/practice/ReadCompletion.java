package com.add.toeic.model.practice;

import java.util.Arrays;

/**
 * Created by sev_user on 12/30/2016.
 */

public class ReadCompletion {

    public int[] answer;
    public int points;
    public int duration_in_seconds;
    public String question;
    public int negative_points;
    public String[] option_1;
    public String[] option_2;
    public String[] option_3;
    public int question_type;

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
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

    public String getQuestions() {
        return question;
    }

    public void setQuestions(String questions) {
        this.question = questions;
    }

    public int getNegative_points() {
        return negative_points;
    }

    public void setNegative_points(int negative_points) {
        this.negative_points = negative_points;
    }

    public String[] getOption_1() {
        return option_1;
    }

    public void setOption_1(String[] option_1) {
        this.option_1 = option_1;
    }

    public String[] getOption_2() {
        return option_2;
    }

    public void setOption_2(String[] option_2) {
        this.option_2 = option_2;
    }

    public String[] getOption_3() {
        return option_3;
    }

    public void setOption_3(String[] option_3) {
        this.option_3 = option_3;
    }

    public int getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(int question_type) {
        this.question_type = question_type;
    }

    @Override
    public String toString() {
        return "ReadCompletion{" +
                "answer=" + Arrays.toString(answer) +
                ", points=" + points +
                ", duration_in_seconds=" + duration_in_seconds +
                ", question='" + question + '\'' +
                ", negative_points=" + negative_points +
                ", option_1=" + Arrays.toString(option_1) +
                ", option_2=" + Arrays.toString(option_2) +
                ", option_3=" + Arrays.toString(option_3) +
                ", question_type=" + question_type +
                '}';
    }
}
