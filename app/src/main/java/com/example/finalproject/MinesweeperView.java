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
    private int cellSize = gridW * gridH;
    private final int numMines = cellSize / 5; // כמות פצצות
    private int[][] mines; // מיקום הפצצות
    private boolean[][] revealed; //מערך של משבצות פתוחות
    private boolean hasExplodedMine = false; // אינדיקטור לפצצות שהתפוצצו
    private boolean firstClick = true;


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int smallerDimension = Math.min(w, h);
        cellSize = smallerDimension / gridW; // Ensure 1:1 aspect ratio
    }

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
        mines = new int[gridW][gridH];
        revealed = new boolean[gridW][gridH];
        firstClick = true;
        hasExplodedMine = false;

        for (int x = 0; x < gridW; x++) {
            for (int y = 0; y < gridH; y++) {
                mines[x][y] = 0;
                revealed[x][y] = false;
            }
        }
    }

    void placeMines() {
        Random random = new Random();
        for (int i = 0; i < numMines; i++) {

            int x = random.nextInt(gridW);
            int y = random.nextInt(gridH);
            if (mines[x][y] == 1) continue;
            mines[x][y] = 1;
        }

    }


    void clearMines() {
        for (int x = 0; x < gridW; x++) {
            for (int y = 0; y < gridH; y++) {
                mines[x][y] = 0;
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) (event.getX() / cellSize);
        int y = (int) (event.getY() / cellSize);
        try {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (firstClick) {
                    firstClick = false;
                    do {
                        clearMines();
                        placeMines();
                    } while (calcNear(x, y) != 0);
                }

                if (hasExplodedMine) { // אחרי פסילה, לחיצה שניה בשביל להתחיל מהתחלה
                    invalidate();
                    setup();
                    return true;
                }

                if (mines[x][y] != 0) {
                    System.out.println("Mine Exploded at: (" + x + ", " + y + ")");
                    hasExplodedMine = true;
                } else {
                    reveal(x, y);
                }

                invalidate();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // צבע רקע
        int myColor = getResources().getColor(R.color.BGColor);
        canvas.drawColor(myColor);

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
        numberPaint.setColor(Color.WHITE);
        numberPaint.setTextSize(cellSize / 2);
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


                    int near = calcNear(x, y);
                    if (near > 0) {
                        canvas.drawText(String.valueOf(near), (x + 0.5f) * cellSize, (y + 0.5f) * cellSize + cellSize / 4, numberPaint);
                    }

                }
                if (mines[x][y] != 0 && hasExplodedMine) {
                    System.out.println("Drawing Exploded Mine at: (" + x + ", " + y + ")");
                    canvas.drawRect(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1) * cellSize, minePaint);
                }
            }
        }
    }
}
