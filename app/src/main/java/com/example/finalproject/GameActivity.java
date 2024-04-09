package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends BaseActivity {
    private int randomNumber;
    private int level = 1;
    private int attempts = 0;
    private EditText guessInput;
    private TextView infoText;
    private TextView messageText;
    User currentUser;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        myApplication = (MyApplication) getApplicationContext();
        currentUser = myApplication.getCurrentUser();

        guessInput = findViewById(R.id.GuessInput);
        infoText = findViewById(R.id.InfoText);
        messageText = findViewById(R.id.MessageText);
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
            messageText.setText("");
            attempts++;
            if (guess == randomNumber) {
                level++;
                messageText.setText("Congrats! You advanced to level " + level);
                generateRandomNumber();
                infoText.setText("Guess a number between 1 and " + 10 * level);
                guessInput.setText("");
                int currentScore = currentUser.getScore();
                attempts = 0;

                if (level > currentScore) {
                    currentUser.setScore(level);
                    myApplication.saveCurrentUser(currentUser);
                    messageText.setText("New high score!");
                }
            } else {
                String message = guess > randomNumber ? "Try a lower number" : "Try a higher number";
                infoText.setText(message);
                if (attempts > logBase2(10 * level)) {
                    if (level > 1) {
                        level--;
                    }
                    attempts = 0;
                    messageText.setText("Your level was decreased to " + level + ". Keep trying!");
                }
            }
        } catch (NumberFormatException e) {
            messageText.setText("Please enter a number");
        }
    }

    private int logBase2(int i) {
        return (int) (Math.log(i) / Math.log(2));
    }
}