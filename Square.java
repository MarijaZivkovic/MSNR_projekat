package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.Random;

public class Square {
    private Bitmap background;
    private Bitmap flame;
    private Bitmap splash;
    private static Bitmap[] flames = new Bitmap[16];
    private static Bitmap[] splashes = new Bitmap[8];
    private int x;
    private int y;
    private int flameX;
    private int flameY;
    private int splashX;
    private int splashY;
    private boolean occupied = false;
    private boolean attacked = false;
    private Ship ship;
    private int frameCounter;
    private boolean shipDestroyed = false;

    public Square(int x, int y, SurfaceView v) {
        this.x = x;
        this.y = y;
        Random r = new Random();
        frameCounter = r.nextInt(32);
    }

    public void update() {
        if(attacked) {
            if(occupied) {
                if (!shipDestroyed) {
                    flame = flames[frameCounter / 2];
                    flameX = x;
                    flameY = y - 30;
                    frameCounter = (frameCounter + 1) % 32;
                }
            }
            else {
                splash = splashes[7 - frameCounter / 6];
                splashX = x - 37;
                splashY = y;
                frameCounter = (frameCounter + 1) % 48;
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
        if(attacked) {
            if (occupied) {
                if (!shipDestroyed)
                    canvas.drawBitmap(flame, flameX, flameY, null);
            }
            else {
                canvas.drawBitmap(splash, splashX, splashY, null);
            }
        }
    }

    public boolean isOccupied() { return occupied; }

    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public boolean isAttacked() { return attacked; }

    public void setAttacked(boolean attacked) { this.attacked = attacked; }

    public int getX() { return x; }

    public int getY() { return y; }

    public Bitmap getBackground() { return background; }

    public void setBackground(Bitmap background) { this.background = background; }

    public Ship getShip() { return ship; }

    public void setShip(Ship ship) { this.ship = ship; }

    public static Bitmap[] getFlames() { return flames; }

    public static Bitmap[] getSplashes() { return splashes; }

    public void setShipDestroyed(boolean shipDestroyed) { this.shipDestroyed = shipDestroyed; }
}
