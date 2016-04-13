package com.example.milomir93.battleships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{
    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;
    private static int x1 = 0, y1 = 0, x2 = 140, y2 = 350;//change
    private boolean gameMode = false;//change
    private Bitmap menuBackground;//change
    private Bitmap gameBackground;//change
    private Bitmap newGameButton;//change
    private GameThread thread;
    private MainMenu mainMenu;

    public GameSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainMenu = new MainMenu();

        menuBackground = BitmapFactory.decodeResource(getResources(), R.drawable.g);//change
        newGameButton = BitmapFactory.decodeResource(getResources(), R.drawable.r);//change
        gameBackground = BitmapFactory.decodeResource(getResources(), R.drawable.b);//change

        thread = new GameThread(getHolder(), this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        try{
            thread.join();
            thread = null;
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {//change
            if(event.getX() >= 140 && event.getX() <= 340 && event.getY() >= 350 && event.getY() <= 450)
                gameMode = true;
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        //
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        System.out.println(canvas.getWidth());

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            if(!gameMode) {//change
                canvas.drawBitmap(menuBackground, x1, y1, null);
                canvas.drawBitmap(newGameButton, x2, y2, null);
            } else
                canvas.drawBitmap(gameBackground, x1, y1, null);

            canvas.restoreToCount(savedState);

        }
    }
}
