package com.example.finalproject;


import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;


    private String email;
    private String carModel;
    private String base64Image;

    private int highestScoreSnake;
    private int highestScoreNumbers;

    public User(String username, String password, String email, String carModel, String base64Image) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.carModel = carModel;
        this.base64Image = base64Image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public int GetScoreSnake() {
        return highestScoreSnake;
    }

    public void SetScoreSnake(int score) {
        this.highestScoreSnake = score;
    }

    public int getScore() {
        return highestScoreNumbers;
    }

    public void setScore(int level) {
        this.highestScoreNumbers = level;
    }
}
