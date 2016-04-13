package com.example.milomir93.battleships;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private int FPS = 30;
    public boolean running = true;
    private SurfaceHolder surfaceHolder;
    private GameSurface gameSurface;
    public static Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder, GameSurface gameSurface) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameSurface = gameSurface;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long targetTime = 1000 / FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameSurface.update();
                    this.gameSurface.draw(canvas);
                }
            }
            catch (Exception e) {}
            finally{
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            }
            catch(Exception e){}
        }
    }
}
