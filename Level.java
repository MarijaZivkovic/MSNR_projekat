package com.example.milomir93.battleships;

import android.graphics.Canvas;

public class Level {
    private Player player1;
    private Player player2;
    private Board board1;
    private Board board2;

    public Level(Player p1, Player p2) {
        player1 = p1;
        player2 = p2;
        board1 = new Board();
        board2 = new Board();
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        //
    }
}
