package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import kotlin.NotImplementedError;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.LoginBtn).setOnClickListener(this::loginUser);
        findViewById(R.id.registerBtn).setOnClickListener(this::registerUser);
        findViewById(R.id.forgot_password).setOnClickListener(this::forgotPassword);
    }

    private void forgotPassword(View view) {
        try {
            throw new NotImplementedError("Forgot password");
        } catch (NotImplementedError e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loginUser(View view) {
        EditText usernameInput = findViewById(R.id.loginUsername);
        EditText passwordInput = findViewById(R.id.loginPassword);

        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences(RegistrationActivity.PREFS_NAME, MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user_json", "");
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user != null && username.equals(user.getUsername()) && password.equals(user.getPassword())) {
            MyApplication myApplication = (MyApplication) getApplicationContext();
            myApplication.setCurrentUser(user);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            passwordInput.setError("Invalid username or password");
        }
    }

    public void registerUser(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}