package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.HashSet;

public class Ship {
    public enum ShipName {DESTROYER, LIGHT_CRUISER, HEAVY_CRUISER, BATTLECRUISER, BATTLESHIP, CARRIER};
    private int orientation;
    private Bitmap background;
    private int x;
    private int y;
    private int squareX;
    private int squareY;
    private int length;
    private int healthPoints;//!
    private ShipName shipName;
    private boolean placed;
    private HashSet<Square> currentSquares;
    private boolean destroyed = false;
    private Board board;
    private int index;

    public Ship(ShipName shipName, int orientation, Board b) {
        currentSquares = new HashSet<Square>();
        this.orientation = orientation;
        this.shipName = shipName;
        switch (shipName) {
            case DESTROYER:
                length = 2;
                index = 0;
                break;
            case LIGHT_CRUISER:
                length = 2;
                index = 1;
                break;
            case HEAVY_CRUISER:
                length = 3;
                index = 2;
                break;
            case BATTLECRUISER:
                length = 4;
                index = 3;
                break;
            case BATTLESHIP:
                length = 5;
                index = 4;
                break;
            case CARRIER:
                length = 5;
                index = 5;
                break;
        }
        healthPoints = length;
        board = b;
    }

    public void decreaseHealthPoints() {
        healthPoints--;
        if(healthPoints <= 0) {
            background = board.getRotatedIconsDestroyed()[index][orientation];
            destroyed = true;
            for(Square s : currentSquares)
                s.setShipDestroyed(true);
        }
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x - (orientation == Level.RIGHT_ORIENTATION ? (length - 1) * 40 : 0), y, null);
    }

    public void setBackground(Bitmap background) { this.background = background; }

    public int getLength() { return length; }

    public int getOrientation() { return orientation; }

    public void setOrientation(int orientation) { this.orientation = orientation; }

    public boolean isPlaced() { return placed; }

    public void setPlaced(boolean placed) { this.placed = placed; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public int getX() { return x; }

    public int getY() { return y; }

    public void setSquareX(int squareX) { this.squareX = squareX; }

    public void setSquareY(int squareY) { this.squareY = squareY; }

    public int getSquareX() { return squareX; }

    public int getSquareY() { return squareY; }

    public HashSet<Square> getCurrentSquares() { return currentSquares; }

    public boolean isDestroyed() { return destroyed; }
}
