package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Square {
    private Bitmap background;
    private int x;
    private int y;
    private boolean occupied = false;

    public Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
    }

    public boolean isOccupied() { return occupied; }

    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public int getX() { return x; }

    public int getY() { return y; }

    public Bitmap getBackground() { return background; }

    public void setBackground(Bitmap background) { this.background = background; }
}
