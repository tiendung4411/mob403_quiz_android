package com.example.mob403_quiz.Models;

public class RankingItem {
    private String name;
    private int score;
    private String imageUrl;

    public RankingItem(String name, int score, String imageUrl) {
        this.name = name;
        this.score = score;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    // Getters and setters
}