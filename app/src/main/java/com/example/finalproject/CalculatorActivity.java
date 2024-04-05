package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorActivity extends AppCompatActivity {
    private TextView resultTextView;
    private String currentNumber = "";
    private String operator = "";
    private double firstNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        resultTextView = findViewById(R.id.resultTextView);
        
    }

    public void numberClick(View view) {
        Button button = (Button) view;
        currentNumber += button.getText().toString();
        resultTextView.setText(currentNumber);
    }

    public void operatorClick(View view) {
        Button button = (Button) view;
        operator = button.getText().toString();
        firstNumber = Double.parseDouble(currentNumber);
        currentNumber = "";
        //resultTextView.setText(operator);
    }

    public void equalsClick(View view) {
        double secondNumber = Double.parseDouble(currentNumber);
        double result = 0;
        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                result = firstNumber / secondNumber;
                break;
        }
        resultTextView.setText(String.valueOf(result));
        currentNumber = String.valueOf(result);
        operator = "";
    }

    public void clearClick(View view) {
        currentNumber = "";
        operator = "";
        firstNumber = 0;
        resultTextView.setText("0");
    }
}