package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.HashSet;

public class Level {
    public enum WaitingForPlayer {PLAYER1, PLAYER2};
    private GameSurface v;
    private Human player1;
    private Bot player2;
    private Board board1;
    private Board board2;
    private Bitmap background;
    private int x = 0;
    private int y = 0;
    private WaitingForPlayer waitingForPlayer;
    private boolean piecesPositioned;
    private boolean player1Positioned;
    private boolean player2Positioned;
    private GameButton rotationGameButton;
    private HashSet<GameButton> gameButtons;
    private Bitmap[] rotationGameButtonIcons;
    public static final int RIGHT_ORIENTATION = 0;
    public static final int LEFT_ORIENTATION = 1;
    public static int orientation = RIGHT_ORIENTATION;

    public Level(Human p1, Bot p2, GameSurface v) {
        piecesPositioned = false;
        player1Positioned = false;
        player2Positioned = false;
        waitingForPlayer = WaitingForPlayer.PLAYER1;
        this.v = v;
        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.level_bg);
        player1 = p1;
        player2 = p2;
        board1 = new Board(player1Positioned, v);
        board2 = new Board(player2Positioned, v);
        rotationGameButtonIcons = new Bitmap[2];
        rotationGameButtonIcons[0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.arrow_r);
        rotationGameButtonIcons[1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.arrow_l);
        rotationGameButton = new GameButton(rotationGameButtonIcons[orientation], 50, 300, v, GameSurface.MethodName.ROTATE_SHIP);
        gameButtons = new HashSet<GameButton>();
        gameButtons.add(rotationGameButton);
    }

    public void inputActionDown(int x, int y) {
        for(GameButton g : gameButtons)
            if(y >= g.getY() && y <= g.getY() + g.getBackground().getHeight())
                if(x >= g.getX() && x <= g.getX() + g.getBackground().getWidth()) {
                    switch (g.getMethodName()) {
                        case ROTATE_SHIP:
                            changeOrientation();
                            return;
                    }
                }
        board1.inputActionDown(x, y);
    }

    public void inputActionMove(int x, int y) {
        board1.inputActionMove(x, y);
    }

    public void inputActionUp(int x, int y) {
        board1.inputActionUp(x, y);
    }

    public void changeOrientation() {
        orientation = 1 - orientation;
    }

    public void update() {
        if(piecesPositioned) {
            switch (waitingForPlayer) {
                case PLAYER1:
                    //
                    break;
                case PLAYER2:
                    //
                    break;
            }
        }
        else {
            if(player1Positioned) {
                if (player2Positioned)
                    piecesPositioned = true;
                else
                    player2.positionShips();
            }
            else {
                board1.update();
                rotationGameButton.setBackground(rotationGameButtonIcons[orientation]);
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);

        if(piecesPositioned) {
            switch (waitingForPlayer) {
                case PLAYER1:
                    //
                    break;
                case PLAYER2:
                    //
                    break;
            }
        }
        else {
            if(player1Positioned) {
                if (player2Positioned)
                    piecesPositioned = true;
                else
                    player2.positionShips();
            }
            else {
                rotationGameButton.draw(canvas);
                board1.draw(canvas);
            }
        }
    }

    public WaitingForPlayer getWaitingForPlayer() { return waitingForPlayer; }

    public void setWaitingForPlayer(WaitingForPlayer waitingForPlayer) { this.waitingForPlayer = waitingForPlayer; }
}
