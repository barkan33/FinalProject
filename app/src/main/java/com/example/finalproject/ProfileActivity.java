package com.example.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        User currentUser = myApplication.getCurrentUser();

        if (currentUser != null) {
            TextView usernameText = findViewById(R.id.profileUsername);
            TextView emailText = findViewById(R.id.profileEmail);
            TextView carModelText = findViewById(R.id.profileCarModel);
            ImageView profileImage = findViewById(R.id.profileImage);

            usernameText.setText("Username: " + currentUser.getUsername());
            emailText.setText("Email: " + currentUser.getEmail());
            carModelText.setText("Car Model: " + currentUser.getCarModel());

            if (currentUser.getBase64Image() != null) {
                byte[] decodedString = Base64.decode(currentUser.getBase64Image(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profileImage.setImageBitmap(decodedByte);
            }
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }
    }
}