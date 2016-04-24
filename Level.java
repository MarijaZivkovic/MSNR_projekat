package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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
            //Čeka se da se igrač1 pozicionira
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
        board1.draw(canvas);//privremeno
    }

    public WaitingForPlayer getWaitingForPlayer() { return waitingForPlayer; }

    public void setWaitingForPlayer(WaitingForPlayer waitingForPlayer) { this.waitingForPlayer = waitingForPlayer; }
}
