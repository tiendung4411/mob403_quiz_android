package com.example.mob403_quiz.Models;

public class UsersResult {
    private int id;
    private int user_id;
    private int category_id;
    private int score;
    private String completed_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(String completed_at) {
        this.completed_at = completed_at;
    }

    public UsersResult(int id, int user_id, int category_id, int score, String completed_at) {
        this.id = id;
        this.user_id = user_id;
        this.category_id = category_id;
        this.score = score;
        this.completed_at = completed_at;
    }

    public UsersResult() {
    }
}
