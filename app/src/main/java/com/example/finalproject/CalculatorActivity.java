package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorActivity extends BaseActivity implements View.OnClickListener {
    private TextView resultTextView;
    private StringBuilder currentNumber;
    private double operand1;
    private double operand2;
    private char operator;

    private int[] btnArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        resultTextView = findViewById(R.id.resultTextView);
        currentNumber = new StringBuilder();

        btnArr = new int[]{R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.buttonDot, R.id.buttonMinus, R.id.buttonPlus, R.id.buttonDiv, R.id.buttonMul, R.id.buttonModulo, R.id.buttonEq, R.id.buttonC, R.id.buttonSqrt};

        for (int i : btnArr) {
            findViewById(i).setOnClickListener(this);
        }


    }

    public void numberClick(View view) {
        Button button = (Button) view;
        if (button.getText().toString().equals(".")) {
            if (currentNumber.toString().contains(".")) {
                return;
            }
        }
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
        currentNumber.append(String.valueOf(result));
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
                    Toast.makeText(this, "ERROR: Divide by 0", Toast.LENGTH_SHORT).show();
                }
                break;
            case '%':
                if (operand2 != 0) {
                    result = operand1 % operand2;
                } else {
                    Toast.makeText(this, "ERROR: Modulo by 0", Toast.LENGTH_SHORT).show();
                }
                break;
            case '^':
                result = Math.pow(operand1, operand2);
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


    public void sqrtClick(View view) {

        if (currentNumber.length() < 1) {
            return;
        }
        operand2 = Double.parseDouble(currentNumber.toString());
        double result = Math.sqrt(operand2);
        resultTextView.setText(String.valueOf(result));
        currentNumber.setLength(0);

    }

    public void negateClick(View view) {
        if (currentNumber.length() == 0) {
            currentNumber.append("-");
        } else {
            operatorClick(view);
        }
        resultTextView.setText(currentNumber.toString());
    }

    @Override
    public void onClick(View v) {

        for (int i = 0; i <= 10; i++) {
            if (v.getId() == btnArr[i]) {
                numberClick(v);
                return;
            }
        }
        if (v.getId() == btnArr[11]) {
            negateClick(v);
            return;
        }
        for (int i = 12; i <= 15; i++) {
            if (v.getId() == btnArr[i]) {
                operatorClick(v);
                return;
            }
        }
        if (v.getId() == btnArr[16]) {
            equalsClick(v);
            return;
        }
        if (v.getId() == btnArr[17]) {
            clearClick(v);
            return;
        }
        if (v.getId() == btnArr[18]) {
            sqrtClick(v);
            return;
        }
        return;

    }
}