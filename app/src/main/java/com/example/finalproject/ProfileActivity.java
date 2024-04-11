package com.example.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends MainActivity {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.navigation_0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;

        } else if (menuItem.getItemId() == R.id.navigation_1) {
            Intent intent = new Intent(this, SnakeActivity.class);
            startActivity(intent);
            return true;

        } else if (menuItem.getItemId() == R.id.navigation_2) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            return true;

        } else if (menuItem.getItemId() == R.id.navigation_3) {
            Intent intent = new Intent(this, CalculatorActivity.class);
            startActivity(intent);
            return true;

        } else if (menuItem.getItemId() == R.id.navigation_4) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (menuItem.getItemId() == R.id.navigation_5) {
            Intent intent = new Intent(this, MinesweeperActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        User currentUser = myApplication.getCurrentUser();

        if (currentUser != null) {
            TextView usernameText = findViewById(R.id.profileUsername);
            TextView emailText = findViewById(R.id.profileEmail);
            TextView carModelText = findViewById(R.id.profileCarModel);
            ImageView profileImage = findViewById(R.id.profileImage);
            TextView snakeScoreText = findViewById(R.id.profileSnakeScore);
            TextView guessScoreText = findViewById(R.id.profileGuessScore);

            usernameText.setText(currentUser.getUsername());
            emailText.setText(currentUser.getEmail());
            carModelText.setText(currentUser.getCarModel());
            snakeScoreText.setText("Snake Score: " + currentUser.GetScoreSnake());
            guessScoreText.setText("Guess Score: " + currentUser.getScore());

            if (currentUser.getBase64Image() != null) {
                byte[] decodedString = Base64.decode(currentUser.getBase64Image(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profileImage.setImageBitmap(decodedByte);
            }
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
        initializeProfile();
        findViewById(R.id.fab).setOnClickListener(this::someFunction);
    }


    private void someFunction(View view) {
        Toast.makeText(this, "Some function", Toast.LENGTH_SHORT).show();
    }
}
