package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        initializeProfile();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId() == R.id.navigation_1) {
            Intent intent = new Intent(MainActivity.this, SnakeActivity.class);
            startActivity(intent);
            return true;

        } else if (menuItem.getItemId() == R.id.navigation_2) {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
            return true;

        } else if (menuItem.getItemId() == R.id.navigation_3) {
            Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(intent);
            return true;

        } else if (menuItem.getItemId() == R.id.navigation_4) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void initializeProfile() {
        MyApplication myApplication = (MyApplication) getApplicationContext();
        User currentUser = myApplication.getCurrentUser();

        if (currentUser != null) {
            TextView usernameTextView = navigationView.getHeaderView(0).findViewById(R.id.sideProfileUsername);
            TextView emailTextView = navigationView.getHeaderView(0).findViewById(R.id.sideProfileEmail);
            ImageView profileImageView = navigationView.getHeaderView(0).findViewById(R.id.sideProfileImage);

            usernameTextView.setText(currentUser.getUsername());
            emailTextView.setText(currentUser.getEmail());

            if (currentUser.getBase64Image() != null) {
                byte[] decodedString = Base64.decode(currentUser.getBase64Image(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                profileImageView.setImageBitmap(decodedByte);
            }
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }


    }
}
