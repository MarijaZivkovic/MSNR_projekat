package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Ship {
    public enum ShipName {DESTROYER, LIGHT_CRUISER, HEAVY_CRUISER, BATTLECRUISER, BATTLESHIP, CARRIER};
    //public enum ShipOrientation {LEFT, RIGHT};
    private int orientation;
    private Bitmap background;
    private int x;
    private int y;
    private int length;
    private ShipName shipName;
    private boolean placed;

    public Ship(ShipName shipName, int orientation, GameSurface v) {
        this.orientation = orientation;
        this.shipName = shipName;
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
                length = 5;
                break;
        }
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
    }

    public void setBackground(Bitmap background) { this.background = background; }

    public int getLength() { return length; }

    public int getOrientation() { return orientation; }

    public void setOrientation(int orientation) { this.orientation = orientation; }

    public boolean isPlaced() { return placed; }

    public void setPlaced(boolean placed) { this.placed = placed; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }
}
