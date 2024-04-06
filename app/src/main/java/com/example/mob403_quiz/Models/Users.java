package com.example.mob403_quiz.Models;

public class Users {
    private int id;
    private String username;
    private String name;
    private String email;
    private String password;
    private int age;
    private String role;

    private String score;
    private String profilePic;

    private String level;

    public Users(int id, String username, String name, String email, String password, int age, String role, String score, String profilePic, String level) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.role = role;
        this.score = score;
        this.profilePic = profilePic;
        this.level = level;
    }

    public Users(int id, String username, String name, String email, String password, int age, String role) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.role = role;
    }

    public Users() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
