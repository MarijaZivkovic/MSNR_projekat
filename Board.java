package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.HashSet;

public class Board {
    private GameSurface v;
    public static final int NUMBER_OF_SHIPS = 6;
    private Square[][] squares;
    private Bitmap background;
    private int x = 0;
    private int y = 0;
    private Bitmap[] icons;
    private Bitmap[][] rotatedIcons;
    private boolean playerPositioned;
    private int[] iconXCoordinate = {24, 108, 192, 306, 450, 624};
    private Bitmap selectedIcon;
    private int iconX;
    private int iconY;
    private static boolean iconSelected = false;
    private int iconNumber;
    private Ship[] ships;
    private Square currentSquare;
    private int currentSquareX;
    private int currentSquareY;
    private Ship currentShip;
    private HashSet<Square> currentSquares;
    private int currentPositionX;
    private int currentPositionY;
    private boolean inPosition = false;
    private boolean allowedToPlace;

    public Board(boolean playerPositioned, GameSurface v) {
        this.v = v;
        currentSquares = new HashSet<Square>();
        squares = new Square[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                squares[i][j] = new Square(360 - 40 * i + 40 * j, 20 * i + 20 * j);
                squares[i][j].setBackground(BitmapFactory.decodeResource(v.getResources(), R.drawable.tile));
            }
        ships = new Ship[NUMBER_OF_SHIPS];
        ships[0] = new Ship(Ship.ShipName.DESTROYER, Level.orientation, v);
        ships[1] = new Ship(Ship.ShipName.LIGHT_CRUISER, Level.orientation, v);
        ships[2] = new Ship(Ship.ShipName.HEAVY_CRUISER, Level.orientation, v);
        ships[3] = new Ship(Ship.ShipName.BATTLECRUISER, Level.orientation, v);
        ships[4] = new Ship(Ship.ShipName.BATTLESHIP, Level.orientation, v);
        ships[5] = new Ship(Ship.ShipName.CARRIER, Level.orientation, v);

        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.grid);
        icons = new Bitmap[NUMBER_OF_SHIPS];
        rotatedIcons = new Bitmap[NUMBER_OF_SHIPS][2];
        this.playerPositioned = playerPositioned;

        icons[0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_dest);
        icons[1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_lcr);
        icons[2] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_hcr);
        icons[3] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_bcr);
        icons[4] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_bship);
        icons[5] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_carr);

        rotatedIcons[0][0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.dest_r);
        rotatedIcons[0][1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.dest_l);
        rotatedIcons[1][0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_lcr);
        rotatedIcons[1][1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_lcr);
        rotatedIcons[2][0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_hcr);
        rotatedIcons[2][1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_hcr);
        rotatedIcons[3][0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_bcr);
        rotatedIcons[3][1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_bcr);
        rotatedIcons[4][0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_bship);
        rotatedIcons[4][1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_bship);
        rotatedIcons[5][0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_carr);
        rotatedIcons[5][1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.ico_carr);
    }

    public void inputActionDown(int x, int y) {
        currentPositionX = x;
        currentPositionY = y;
        currentSquares.clear();
        for(int i = 0; i < NUMBER_OF_SHIPS; i++)
            if(y >= this.y + background.getHeight() + 10 && y <= this.y + background.getHeight() + 10 + icons[i].getHeight())
                if(x >= iconXCoordinate[i] && x <= iconXCoordinate[i] + icons[i].getWidth()) {
                    selectedIcon = rotatedIcons[i][Level.orientation];
                    iconX = x;
                    iconY = y;
                    iconSelected = true;
                    iconNumber = i;
                }
    }

    public void inputActionMove(int x, int y) {
        allowedToPlace = false;
        currentPositionX = x;
        currentPositionY = y;
        if (iconSelected) {
            iconX = x;
            iconY = y;
            boolean squareFound = false;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (x >= squares[i][j].getX() && x <= squares[i][j].getX() + squares[i][j].getBackground().getWidth())
                        if (y >= squares[i][j].getY() && y <= squares[i][j].getY() + squares[i][j].getBackground().getHeight()) {
                            currentSquare = squares[i][j];
                            currentSquareX = i;
                            currentSquareY = j;
                            squareFound = true;
                            break;
                        }
                }
                if (squareFound)
                    break;
            }
            //if(!squareFound)
            //    return;

            currentSquares.clear();
            boolean positionAvailable = true;
            currentShip = ships[iconNumber];
            for (int i = 0; i < currentShip.getLength(); i++) {
                if (Level.orientation == Level.RIGHT_ORIENTATION) {
                    if (currentSquareX + i > 9) {
                        positionAvailable = false;
                        break;
                    }
                    currentSquares.add(squares[currentSquareX + i][currentSquareY]);
                    if (squares[currentSquareX + i][currentSquareY].isOccupied()) {
                        positionAvailable = false;
                        break;
                    }
                } else {
                    if (currentSquareY + i > 9) {
                        positionAvailable = false;
                        break;
                    }
                    currentSquares.add(squares[currentSquareX][currentSquareY + i]);
                    if (squares[currentSquareX][currentSquareY + i].isOccupied()) {
                        positionAvailable = false;
                        break;
                    }
                }
            }
            for (Square s : currentSquares)
                s.setBackground(positionAvailable ? BitmapFactory.decodeResource(v.getResources(), R.drawable.tile_green) : BitmapFactory.decodeResource(v.getResources(), R.drawable.tile_red));
            if (squareFound && positionAvailable)
                allowedToPlace = true;
        }
    }

    public void inputActionUp(int x, int y) {
        currentPositionX = x;
        currentPositionY = y;
        if (iconSelected)
            iconSelected = false;
        if (allowedToPlace) {
            for (Square s : currentSquares)
                s.setOccupied(true);
            ships[iconNumber].setPlaced(true);
            ships[iconNumber].setBackground(rotatedIcons[iconNumber][Level.orientation]);
            ships[iconNumber].setX(currentSquare.getX());
            ships[iconNumber].setY(currentSquare.getY());
            allowedToPlace = false;
        }
        currentSquares.clear();
    }

    public void update() {
        inPosition = false;
        boolean ok = false;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (currentPositionX >= squares[i][j].getX() && currentPositionX <= squares[i][j].getX() + squares[i][j].getBackground().getWidth())
                    if (currentPositionY >= squares[i][j].getY() && currentPositionY <= squares[i][j].getY() + squares[i][j].getBackground().getHeight()) {
                        inPosition = true;
                        ok = true;
                        break;
                    }
            }
            if (ok)
                break;
        }
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
            if (iconSelected && inPosition) {
                for (Square s : currentSquares)
                    s.draw(canvas);
            }
            for (int i = 0; i < NUMBER_OF_SHIPS; i++)
                if (ships[i].isPlaced())
                    ships[i].draw(canvas);
            if (iconSelected) {
                canvas.drawBitmap(selectedIcon, iconX, iconY, null);
            }
        }
    }
}
