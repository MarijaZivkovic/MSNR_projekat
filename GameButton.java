package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameButton {
    private Bitmap background;
    protected int x;
    protected int y;
    protected GameSurface.MethodName methodName;

    public GameButton() {}

    public GameButton(Bitmap b, int x, int y, GameSurface v, GameSurface.MethodName m) {
        background = b;
        this.x = x;
        this.y = y;
        methodName = m;
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
    }

    public Bitmap getBackground() { return background; }

    public void setBackground(Bitmap background) { this.background = background; }

    public void setX(int x) { this.x = x; }

    public int getX() { return x; }

    public int getY() { return y; }

    public GameSurface.MethodName getMethodName() {
        return methodName;
    }
}
