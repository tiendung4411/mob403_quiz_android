package com.example.mob403_quiz.Models;

public class Answer {
    private int id;
    private int question_id;
    private String text;
    private int is_correct;

    public Answer() {
    }

    public Answer(int id, int question_id, String text, int is_correct) {
        this.id = id;
        this.question_id = question_id;
        this.text = text;
        this.is_correct = is_correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int isIs_correct() {
        return is_correct;
    }

    public void setIs_correct(int is_correct) {
        this.is_correct = is_correct;
    }
}
