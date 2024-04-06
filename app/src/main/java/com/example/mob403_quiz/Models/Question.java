package com.example.mob403_quiz.Models;

import java.util.List;

public class Question {
    private int id;
    private int category_id;
    private String text;
    private String image;

    private List<Answer> answers;
    public Question() {
    }

    public Question(int id, int category_id, String text, String image) {
        this.id = id;
        this.category_id = category_id;
        this.text = text;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //get the answers of the question based on the question id
    public List<Answer> getAnswers() {
        return answers;
    }

}
