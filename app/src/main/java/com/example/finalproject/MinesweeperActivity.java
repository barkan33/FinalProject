package com.example.finalproject;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MinesweeperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minesweeper);

        LinearLayout gameLayout = findViewById(R.id.gameLayout);
        MinesweeperView minesweeperView = new MinesweeperView(this);
        gameLayout.addView(minesweeperView);
    }
}