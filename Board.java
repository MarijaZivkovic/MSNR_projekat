package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Board {
    public static final int NUMBER_OF_SHIPS = 6;
    private Square[][] squares;
    private Bitmap background;
    private int x = 0;
    private int y = 0;
    private Bitmap[] icons;
    private boolean playerPositioned;
    private int[] iconXCoordinate = {24, 108, 192, 306, 450, 624};

    public Board(boolean playerPositioned, GameSurface v) {
        squares = new Square[10][10];
        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.grid);
        icons = new Bitmap[NUMBER_OF_SHIPS];
        this.playerPositioned = playerPositioned;
        icons[0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_dest);
        icons[1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_lcr);
        icons[2] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_hcr);
        icons[3] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_bcr);
        icons[4] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_bship);
        icons[5] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_carr);
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
        if(playerPositioned) {
            //
        }
        else {
            for(int i = 0; i < NUMBER_OF_SHIPS; i++) {
                canvas.drawBitmap(icons[i], iconXCoordinate[i], y + background.getHeight() + 10, null);
            }
        }
    }
}
