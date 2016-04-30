package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class MenuButton extends GameButton{
    public static final int NON_WIDE = 0;
    public static final int WIDE = 1;
    private Bitmap[] background;
    private int isWide = NON_WIDE;

    public MenuButton() {}

    public MenuButton(Bitmap b, int x, int y, GameSurface v, GameSurface.MethodName m) {
        background = new Bitmap[2];
        background[0] = b;
        Matrix matrix = new Matrix();
        matrix.postScale(1.2f, 1);
        background[1] = Bitmap.createBitmap(background[0], 0, 0, background[0].getWidth(), background[0].getHeight(), matrix, false);
        this.x = x;
        this.y = y;
        methodName = m;
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background[isWide], x - (background[1].getWidth() - background[0].getWidth()) * isWide / 2, y, null);
    }

    public Bitmap getBackground() {
        return background[isWide];
    }

    public void setIsWide(int w) {
        isWide = w;
    }
}
