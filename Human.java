package com.example.milomir93.battleships;

import android.graphics.Canvas;
import android.view.SurfaceView;

public class Human{
    private Board ownBoard;
    private Board enemyBoard;
    private SurfaceView v;

    public Human(SurfaceView v) {
        this.v = v;
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        //
    }

    public void setOwnBoard(Board ownBoard) { this.ownBoard = ownBoard; }

    public void setEnemyBoard(Board enemyBoard) { this.enemyBoard = enemyBoard; }
}
