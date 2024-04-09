package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;
import java.util.Random;

import kotlin.NotImplementedError;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView logo = findViewById(R.id.loginLogo);
        logo.setImageResource(randLogo());


        findViewById(R.id.LoginBtn).setOnClickListener(this::loginUser);
        findViewById(R.id.registerBtn).setOnClickListener(this::registerUser);
        findViewById(R.id.forgot_password).setOnClickListener(this::forgotPassword);
    }

    private int randLogo() {
        Random random = new Random();
        switch (random.nextInt(4)) {
            case 0:
                return R.drawable.logo_f1;
            case 1:
                return R.drawable.logo_car;
            case 2:
                return R.drawable.logo_pistons;
            default:
                return R.drawable.logo_turbo;
        }
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

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        User user = getUserFromPrefByName(username);


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

    private User getUserFromPrefByName(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String userDictJson = sharedPreferences.getString("user_dict_json", "{}");
        Gson gson = new Gson();
        Map<String, User> userDict = gson.fromJson(userDictJson, new TypeToken<Map<String, User>>() {
        }.getType());
        if (userDict == null) {
            return null;
        }
        for (User user : userDict.values()) {
            System.out.println("{" + user.getUsername() + ": " + user.getPassword() + ", " + user.getEmail() + " }");
        }
        return userDict.get(username);
    }

    public void registerUser(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }
}