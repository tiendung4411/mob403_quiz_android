package com.example.mob403_quiz.Models;
public class ProfileResponse {
    private int score;
    private int quizzes;
    private int rank;
    private int level;

    // Getters and setters for each field

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(int quizzes) {
        this.quizzes = quizzes;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
