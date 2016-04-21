package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Level {
    private Human player1;
    private Bot player2;
    private Board board1;
    private Board board2;
    private Bitmap background;
    private int x = 0;
    private int y = 0;

    public Level(Human p1, Bot p2, GameSurface v) {
        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.level_bg);
        player1 = p1;
        player2 = p2;
        board1 = new Board();
        board2 = new Board();
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
    }
}
