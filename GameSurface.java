package com.example.milomir93.battleships;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{
    public enum State {MENU_STATE, LEVEL_STATE};
    public enum MethodName {CREATE_GAME, JOIN_GAME, START_GAME, EXIT_GAME}
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    private GameThread thread;
    private State state;
    private MainMenu mainMenu;
    private Level level;

    public GameSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        state = State.MENU_STATE;
        mainMenu = new MainMenu(this);
        thread = new GameThread(getHolder(), this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

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
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (state) {
                case MENU_STATE:
                    mainMenu.inputActionDown((int) event.getX(), (int) event.getY());
                case LEVEL_STATE:
                    //
                    return true;
            }
        }

        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            switch (state) {
                case MENU_STATE:
                    mainMenu.inputActionMove((int) event.getX(), (int) event.getY());
                    return true;
                case LEVEL_STATE:
                    //
                    return true;
            }
        }

        else if(event.getAction() == MotionEvent.ACTION_UP) {
            switch (state) {
                case MENU_STATE:
                    mainMenu.inputActionUp((int) event.getX(), (int) event.getY());
                    return true;
                case LEVEL_STATE:
                    //
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        switch (state) {
            case MENU_STATE:
                mainMenu.update();
                break;
            case LEVEL_STATE:
                level.update();
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if(canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            switch (state) {
                case MENU_STATE:
                    mainMenu.draw(canvas);
                    break;
                case LEVEL_STATE:
                    level.draw(canvas);
                    break;
            }

            canvas.restoreToCount(savedState);
        }
    }

    void setLevel(Level l) { level = l; }

    public void setState(State state) { this.state = state; }
}
