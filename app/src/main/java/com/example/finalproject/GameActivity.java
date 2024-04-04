package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private int randomNumber;
    private int level = 1;
    private EditText guessInput;
    private TextView infoText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        guessInput = findViewById(R.id.GuessInput);
        infoText = findViewById(R.id.InfoText);
        generateRandomNumber();
        findViewById(R.id.GuessBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guessNumber(view);
                guessInput.setText("");
            }
        });
    }
    private void generateRandomNumber() {
        Random random = new Random();
        int maxNumber = 10 * level;
        randomNumber = random.nextInt(maxNumber) + 1;
    }
    public void guessNumber(View view) {


        try {
            int guess = Integer.parseInt(guessInput.getText().toString());
            if (guess == randomNumber) {
                level++;
                Toast.makeText(this, "Correct! You advanced to level " + level, Toast.LENGTH_SHORT).show();
                generateRandomNumber();
                infoText.setText("Guess a number between 1 and " + 10 * level);
                guessInput.setText("");
            } else {
                String message = guess > randomNumber ? "Try a lower number" : "Try a higher number";
                infoText.setText(message);
            }
        } catch (NumberFormatException e) {
            infoText.setText("Please enter a number");
        }
    }
}