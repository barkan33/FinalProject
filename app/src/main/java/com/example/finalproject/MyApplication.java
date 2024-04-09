package com.example.finalproject;

import android.app.Application;

public class MyApplication extends Application {
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void saveCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}