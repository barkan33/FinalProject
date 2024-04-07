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
    private StringBuilder currentNumber;
    private double operand1;
    private double operand2;
    private char operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        resultTextView = findViewById(R.id.resultTextView);
        currentNumber = new StringBuilder();


    }

    public void numberClick(View view) {
        Button button = (Button) view;
        currentNumber.append(button.getText().toString());
        resultTextView.setText(currentNumber.toString());
        if (resultTextView.getText().toString().length() > 12) {
            resultTextView.setTextSize(30);
        }
    }

    public void operatorClick(View view) {
        if (currentNumber.length() < 1) {
            return;
        }
        Button button = (Button) view;
        operator = button.getText().toString().charAt(0);
        operand1 = Double.parseDouble(currentNumber.toString());
        currentNumber.setLength(0);
    }

    public void equalsClick(View view) {
        if (currentNumber.length() < 1 || operator == ' ') {
            return;
        }
        operand2 = Double.parseDouble(currentNumber.toString());
        double result = performOperation(operand1, operand2, operator);
        resultTextView.setText(String.valueOf(result));
        currentNumber.setLength(0);
    }

    private double performOperation(double operand1, double operand2, char operator) {
        double result = 0;
        switch (operator) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                if (operand2 != 0) {
                    result = operand1 / operand2;
                } else {
                    resultTextView.setText("ERROR: Divide by 0");
                }
                break;
        }
        return result;
    }

    public void clearClick(View view) {
        currentNumber.setLength(0);
        resultTextView.setText("");
        operand1 = 0;
        operand2 = 0;
        operator = ' ';
    }


}