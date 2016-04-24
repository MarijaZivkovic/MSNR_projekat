package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Ship {
    public enum ShipName {DESTROYER, LIGHT_CRUISER, HEAVY_CRUISER, BATTLECRUISER, BATTLESHIP, CARRIER};
    public enum ShipOrientation {LEFT, RIGHT};
    private Bitmap background;
    private int x;
    private int y;
    private int length;


    public Ship(ShipName shipName, ShipOrientation shipOrientation, GameSurface v) {
        switch (shipName) {
            case DESTROYER:
                length = 2;
                break;
            case LIGHT_CRUISER:
                length = 2;
                break;
            case HEAVY_CRUISER:
                length = 3;
                break;
            case BATTLECRUISER:
                length = 4;
                break;
            case BATTLESHIP:
                length = 5;
                break;
            case CARRIER:
                length = 6;
                break;
        }
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        //
    }
}
