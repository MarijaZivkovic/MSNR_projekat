package com.example.milomir93.battleships;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.Random;

public class Bot{
    private Board ownBoard;
    private Board enemyBoard;
    private SurfaceView v;
    private boolean attacked = false;
    private boolean startedThinking = true;
    private boolean finishedThinking = false;

    public Bot(SurfaceView v) {
        this.v = v;
    }

    public void positionShips() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < Board.NUMBER_OF_SHIPS; i++) {
            ownBoard.getShips()[i].setOrientation(Level.LEFT_ORIENTATION);
            ownBoard.getShips()[i].setBackground(ownBoard.getRotatedIcons()[i][ownBoard.getShips()[i].getOrientation()]);
            for (int j = 0; j < ownBoard.getShips()[i].getLength(); j++) {
                ownBoard.getSquares()[x + i][y + j].setOccupied(true);
                ownBoard.getSquares()[x + i][y + j].setShip(ownBoard.getShips()[i]);
                ownBoard.getShips()[i].getCurrentSquares().add(ownBoard.getSquares()[x + i][y + j]);
            }
            ownBoard.getShips()[i].setX(ownBoard.getSquares()[x + i][y].getX());
            ownBoard.getShips()[i].setY(ownBoard.getSquares()[x + i][y].getY());
            ownBoard.getShips()[i].setPlaced(true);
        }
    }

    public void shoot() {
        int i;
        int j;
        while(true) {
            Random random = new Random();
            int r = random.nextInt(100);
            i = r / 10;
            j = r % 10;
            if (!enemyBoard.getSquares()[i][j].isAttacked())
                break;
        }
        enemyBoard.getSquares()[i][j].setAttacked(true);
        if(enemyBoard.getSquares()[i][j].isOccupied())
            enemyBoard.getSquares()[i][j].getShip().decreaseHealthPoints();
        attacked = true;
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        //
    }

    public void setOwnBoard(Board ownBoard) { this.ownBoard = ownBoard; }

    public void setEnemyBoard(Board enemyBoard) { this.enemyBoard = enemyBoard; }

    public boolean hasAttacked() { return attacked; }

    public void setAttacked(boolean attacked) { this.attacked = attacked; }

    public boolean hasStartedThinking() { return startedThinking; }

    public void setStartedThinking(boolean startedThinking) { this.startedThinking = startedThinking; }

    public boolean hasFinishedThinking() { return finishedThinking; }

    public void setFinishedThinking(boolean finishedThinking) { this.finishedThinking = finishedThinking; }
}
