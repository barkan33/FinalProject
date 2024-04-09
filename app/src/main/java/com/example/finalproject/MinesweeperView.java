package com.example.finalproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MinesweeperView extends View {
    private final int gridW = 10, gridH = 10;
    private final int cellSize = gridW * gridH;
    int numMines = cellSize / 5; // כמות פצצות
    int[][] mines; // מיקום הפצצות
    boolean[][] flags;// מערך של משבצות מסומנות בגדל
    boolean[][] revealed; //מערך של משבצות פתוחות

    public MinesweeperView(Context context) {
        super(context);
        setup();
    }


    boolean outBounds(int x, int y) {
        return x < 0 || y < 0 || x >= gridW || y >= gridH;
    }

    int calcNear(int x, int y) {
        if (outBounds(x, y)) return 0;
        int i = 0;
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                if (outBounds(offsetX + x, offsetY + y)) continue;
                i += mines[offsetX + x][offsetY + y];
            }
        }
        return i;
    }

    void reveal(int x, int y) {
        if (outBounds(x, y)) return;
        if (revealed[x][y]) return;
        revealed[x][y] = true;
        if (calcNear(x, y) != 0) return;
        reveal(x + 1, y);
        reveal(x - 1, y);

        reveal(x, y + 1);
        reveal(x, y - 1);

        reveal(x + 1, y + 1);
        reveal(x + 1, y - 1);

        reveal(x - 1, y + 1);
        reveal(x - 1, y - 1);
    }

    void setup() {
        //initialize and clear the arrays
        mines = new int[gridW][gridH];
        flags = new boolean[gridW][gridH];
        revealed = new boolean[gridW][gridH];
        for (int x = 0; x < gridW; x++) {
            for (int y = 0; y < gridH; y++) {
                mines[x][y] = 0;
                flags[x][y] = false;
                revealed[x][y] = false;
            }
        }
    }

    //Place numMines mines on the grid
    void placeMines() {
        Random random = new Random();
        for (int i = 0; i < numMines; i++) {

            int x = random.nextInt(gridW);
            int y = random.nextInt(gridH);
            if (mines[x][y] == 1) continue;
            mines[x][y] = 1;
        }

    }


    //Clear the mines
    void clearMines() {
        for (int x = 0; x < gridW; x++) {
            for (int y = 0; y < gridH; y++) {
                mines[x][y] = 0;
            }
        }
    }

    boolean firstClick = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) (event.getX() / cellSize);
        int y = (int) (event.getY() / cellSize);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (firstClick) {
                firstClick = false;
                do {
                    clearMines();
                    placeMines();
                } while (calcNear(x, y) != 0);
            }

            if (mines[x][y] != 0) {
                System.out.println("BOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOM!");
            } else {
                reveal(x, y);
            }

            invalidate();
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. Initialize Paint objects
        Paint gridLinePaint = new Paint();
        gridLinePaint.setColor(Color.BLACK);
        gridLinePaint.setStrokeWidth(4);
        gridLinePaint.setAntiAlias(true);
        gridLinePaint.setStyle(Paint.Style.STROKE);
        
        Paint cellBackgroundPaint = new Paint();
        cellBackgroundPaint.setColor(Color.LTGRAY);

        Paint minePaint = new Paint();
        minePaint.setColor(Color.RED);

        Paint numberPaint = new Paint();
        numberPaint.setColor(Color.BLUE);
        numberPaint.setTextSize(cellSize / 2); // Example text size
        numberPaint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i <= gridW; i++) {
            canvas.drawLine(i * cellSize, 0, i * cellSize, gridH * cellSize, gridLinePaint);
        }
        for (int i = 0; i <= gridH; i++) {
            canvas.drawLine(0, i * cellSize, gridW * cellSize, i * cellSize, gridLinePaint);
        }

        for (int x = 0; x < gridW; x++) {
            for (int y = 0; y < gridH; y++) {
                if (revealed[x][y]) {
                    canvas.drawRect(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, cellBackgroundPaint);

                    if (mines[x][y] == 1) {
                        canvas.drawCircle((x + 0.5f) * cellSize, (y + 0.5f) * cellSize, cellSize / 3, minePaint);
                    } else {
                        int near = calcNear(x, y);
                        if (near > 0) {
                            canvas.drawText(String.valueOf(near), (x + 0.5f) * cellSize, (y + 0.5f) * cellSize + cellSize / 4, numberPaint);
                        }
                    }
                }
            }
        }
    }
}
