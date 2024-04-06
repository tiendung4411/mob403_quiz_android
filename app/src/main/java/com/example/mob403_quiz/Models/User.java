package com.example.mob403_quiz.Models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("user_id")
    private String userId;

    private String name;

    @SerializedName("profile_pic") // Make sure this matches the key in your JSON response
    private String profilePic;

    @SerializedName("total_score")
    private int totalScore;

    public User(String userId, String name, String profilePic, int totalScore) {
        this.userId = userId;
        this.name = name;
        this.profilePic = profilePic;
        this.totalScore = totalScore;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
