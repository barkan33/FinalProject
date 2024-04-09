package com.example.finalproject;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SnakeActivity extends BaseActivity {
    SnakeEngine snakeEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = getWindow();
        //מוריד את השורה התחתונה
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);

        MyApplication myApplication = (MyApplication) getApplicationContext();

        // קבל את מידות הפיקסלים של המסך
        Display display = getWindowManager().getDefaultDisplay();

        // אתחול התוצאה לאובייקט Point
        Point size = new Point();
        display.getSize(size);

        snakeEngine = new SnakeEngine(this, size, myApplication);

        // Make snakeEngine the view of the Activity
        setContentView(snakeEngine);


    }


    @Override
    protected void onResume() {
        super.onResume();
        snakeEngine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snakeEngine.pause();
    }
}