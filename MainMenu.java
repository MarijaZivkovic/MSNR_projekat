package com.example.milomir93.battleships;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.HashSet;

public class MainMenu {
    private GameSurface v;
    private HashSet<GameButton> buttons;
    private GameButton lastChanged;
    private Bitmap background;
    private int x = 0;
    private int y = 0;
    private GameButton newGameButton;
    private GameButton joinGameButton;
    private GameButton exitGameButton;

    public MainMenu(GameSurface v) {
        this.v = v;
        lastChanged = new GameButton();
        buttons = new HashSet<GameButton>();
        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.menu_bg);
        newGameButton = new GameButton(90, 200, v, GameSurface.MethodName.CREATE_GAME);
        joinGameButton = new GameButton(90, 350, v, GameSurface.MethodName.JOIN_GAME);
        exitGameButton = new GameButton(90, 500, v, GameSurface.MethodName.EXIT_GAME);
        buttons.add(newGameButton);
        buttons.add(joinGameButton);
        buttons.add(exitGameButton);
    }

    public void inputActionDown(int x, int y) {
        for(GameButton g : buttons)
            if(y >= g.getY() && y <= g.getY() + g.getBackground().getHeight())
                if(x >= g.getX() && x <= g.getX() + g.getBackground().getWidth()) {
                    g.setIsWide(GameButton.WIDE);
                    lastChanged = g;
                    break;
                }
    }

    public void inputActionMove(int x, int y) {
        lastChanged.setIsWide(GameButton.NON_WIDE);
        for(GameButton g : buttons)
            if(y >= g.getY() && y <= g.getY() + g.getBackground().getHeight())
                if(x >= g.getX() && x <= g.getX() + g.getBackground().getWidth()) {
                    g.setIsWide(GameButton.WIDE);
                    lastChanged = g;
                    break;
                }
    }

    public void inputActionUp(int x, int y) {
        for(GameButton g : buttons)
            if(y >= g.getY() && y <= g.getY() + g.getBackground().getHeight())
                if(x >= g.getX() && x <= g.getX() + g.getBackground().getWidth()) {
                    g.setIsWide(GameButton.NON_WIDE);
                    lastChanged = g;
                    switch (g.getMethodName()) {
                        case CREATE_GAME:
                            createGame();
                            break;
                        case JOIN_GAME:
                            joinGame();
                            break;
                        case EXIT_GAME:
                            exitGame();
                            break;
                    }
                    break;
                }
    }

    public void update() {
        newGameButton.update();
        joinGameButton.update();
        exitGameButton.update();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
        newGameButton.draw(canvas);
        joinGameButton.draw(canvas);
        exitGameButton.draw(canvas);
    }

    void createGame() {
        System.out.println("Create");
        //privremeno
        Level l = new Level(new Human(), new Bot(), v);
        v.setLevel(l);
    }

    void joinGame() {
        System.out.println("Join");
    }

    void exitGame() {
        System.out.println("Exit");
    }
}
