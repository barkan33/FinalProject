package com.example.finalproject;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Toast;


class SnakeEngine extends SurfaceView implements Runnable {
    // לולאת המשחק הראשי
    private Thread thread = null;
    // רפרנס לאקטיביטי
    private Context context;
    private int eat_food = -1;
    private int snake_crash = -1;

    // למעקב אחר התנועה
    public enum Heading {UP, RIGHT, DOWN, LEFT}

    // איתחול התנועה ימינה
    private Heading heading = Heading.RIGHT;

    //גודל המסך בפיקסלים
    private int screenX;
    private int screenY;

    private int snakeLength;

    // מיקום האוכל
    private int foodX;
    private int foodY;

    // גודל בלוק
    private int blockSize;

    // רוחב לוח המשחק בבלוקים
    private final int NUM_BLOCKS_WIDE = 30;
    //נתון מושב בשביל להתאים למסכים עם יחס צדדים שונה
    private int numBlocksHigh;

    // שליטה בהשהייה בין רענון תמונות
    private long nextFrameTime;
    private final long FPS = 10;
    private final long MILLIS_PER_SECOND = 1000;
    private int score;

    //מיקום הנחש
    private int[] snakeXs;
    private int[] snakeYs;
    //volatile - לא שמור את המשתנה בקאש, בשביל שכמה זרמים יוכלו לעבוד עם המשתנה הזה והערך שלו שמיד יהיה הכי מעודכן
    private volatile boolean isPlaying;
    private Canvas canvas;
    // Surface זה סוג של canvas
    private SurfaceHolder surfaceHolder;
    private Paint paint;

    private User currentUser;
    MyApplication myApplication;

    public SnakeEngine(Context context, Point size, MyApplication myApplication) {
        super(context);

        this.context = context;
        this.myApplication = myApplication;

        myApplication = (MyApplication) context.getApplicationContext();
        currentUser = myApplication.getCurrentUser();

        screenX = size.x;
        screenY = size.y;

        // חישוב גודל בלוק
        blockSize = screenX / NUM_BLOCKS_WIDE;
        // חישוב גובה הלוח בבלוקים
        numBlocksHigh = screenY / blockSize;

        // איתחור הצייר
        surfaceHolder = getHolder();
        paint = new Paint();

        // האורך המקסימלי של הנחש הוא 200 בלוקים, אם מישה ויגיע לזה, המשחק יקרוס כניראה
        snakeXs = new int[200];
        snakeYs = new int[200];

        // התחלה
        newGame();
    }


    @Override
    public void run() {

        while (isPlaying) {

            // רענון 10 פעמים בשניה
            if (updateRequired()) {
                update();
                draw();
            }

        }
    }

    public void pause() {
        isPlaying = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println("Func: pause()" + e.getMessage());
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void newGame() {
        // אורך התחלתי
        snakeLength = 1;
        //התחלה בעמצע
        snakeXs[0] = NUM_BLOCKS_WIDE / 2;
        snakeYs[0] = numBlocksHigh / 2;

        // הכנת אוכל
        spawnFood();

        // איתחול התוצאה
        score = 0;

        // איתחול המשתנה שעוקב אחר הזמן
        nextFrameTime = System.currentTimeMillis();
    }


    public void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(NUM_BLOCKS_WIDE - 1) + 1;
        foodY = random.nextInt(numBlocksHigh - 1) + 1;
    }


    private void eatFood() {
        snakeLength++;
        spawnFood();
        score = score + 1;
    }


    private void moveSnake() {
        for (int i = snakeLength; i > 0; i--) {
            snakeXs[i] = snakeXs[i - 1];
            snakeYs[i] = snakeYs[i - 1];
        }

        // הזזת הראש הכיוון הנבחר
        switch (heading) {
            case UP:
                snakeYs[0]--;
                break;

            case RIGHT:
                snakeXs[0]++;
                break;

            case DOWN:
                snakeYs[0]++;
                break;

            case LEFT:
                snakeXs[0]--;
                break;
        }
    }

    private boolean detectDeath() {
        boolean dead = false;

        // פגע בקצה המסך
        if (snakeXs[0] == -1) dead = true;
        if (snakeXs[0] >= NUM_BLOCKS_WIDE) dead = true;
        if (snakeYs[0] == -1) dead = true;
        if (snakeYs[0] == numBlocksHigh) dead = true;

        // אחל את עצמו
        for (int i = snakeLength - 1; i > 0; i--) {
            if ((i > 4) && (snakeXs[0] == snakeXs[i]) && (snakeYs[0] == snakeYs[i])) {
                dead = true;
            }
        }

        return dead;
    }

    public void update() {
        // בדיקה אם הנחש אכל
        if (snakeXs[0] == foodX && snakeYs[0] == foodY) {
            eatFood();
        }

        moveSnake();

        if (detectDeath()) {
            upDateScore();
            newGame();
        }
    }

    private void upDateScore() {
        int currentScore = currentUser.GetScoreSnake();
        if (score > currentScore) {
            currentUser.SetScoreSnake(score);
            myApplication.saveCurrentUser(currentUser);
            canvas.drawText("New high score!", 10, 70, paint);

        }
    }

    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            // צבע רקע
            int myColor = getResources().getColor(R.color.BGColor);
            canvas.drawColor(myColor);

            // צבע נחש
            paint.setColor(Color.argb(255, 255, 255, 255));

            // גדול הטקסט שמציג את התוצאה
            paint.setTextSize(90);
            canvas.drawText("Score:" + score, 10, 70, paint);

            // ציור הנחש קוביה אחר קוביה
            for (int i = 0; i < snakeLength; i++) {
                canvas.drawRect(snakeXs[i] * blockSize, (snakeYs[i] * blockSize),
                        (snakeXs[i] * blockSize) + blockSize,
                        (snakeYs[i] * blockSize) + blockSize, paint);
            }

            // צבע האוכל
            paint.setColor(Color.argb(255, 255, 0, 0));

            // לצייר את האוכל
            canvas.drawRect(foodX * blockSize, (foodY * blockSize),
                    (foodX * blockSize) + blockSize,
                    (foodY * blockSize) + blockSize,
                    paint);

            // שליחת הציור המעודכן למסך
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public boolean updateRequired() {

        // בדיקת אם צריך לעדכן את הפריים
        if (nextFrameTime <= System.currentTimeMillis()) {

            // הגדר מתי העדכון הבא יופעל
            nextFrameTime = System.currentTimeMillis() + MILLIS_PER_SECOND / FPS;

            // החזר נכון כדי לעדכן ולצייר
            return true;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                if (motionEvent.getX() >= screenX / 2) {
                    switch (heading) {
                        case UP:
                            heading = Heading.RIGHT;
                            break;
                        case RIGHT:
                            heading = Heading.DOWN;
                            break;
                        case DOWN:
                            heading = Heading.LEFT;
                            break;
                        case LEFT:
                            heading = Heading.UP;
                            break;
                    }
                } else {
                    switch (heading) {
                        case UP:
                            heading = Heading.LEFT;
                            break;
                        case LEFT:
                            heading = Heading.DOWN;
                            break;
                        case DOWN:
                            heading = Heading.RIGHT;
                            break;
                        case RIGHT:
                            heading = Heading.UP;
                            break;
                    }
                }
        }
        return true;
    }
}