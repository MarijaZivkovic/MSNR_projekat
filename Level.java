package com.example.milomir93.battleships;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;

public class Level {
    public enum EnemyType {BOT, HUMAN};
    public enum WaitingForPlayer {PLAYER1, PLAYER2};
    private GameSurface v;
    private Bitmap background;
    private int x = 0;
    private int y = 0;
    private Bot bot;
    private Human human;
    private Board board1;
    private Board board2;
    private WaitingForPlayer waitingForPlayer;
    private boolean piecesPositioned;
    private boolean player1Positioned;
    private boolean player2Positioned;
    private GameButton rotationGameButton;
    private GameButton checkGameButton;
    private GameButton launchGameButton;
    private HashSet<GameButton> gameButtons;
    private Bitmap[] rotationGameButtonIcons;
    private Bitmap[] checkGameButtonIcons;
    public static final int RIGHT_ORIENTATION = 0;
    public static final int LEFT_ORIENTATION = 1;
    public static int orientation = RIGHT_ORIENTATION;
    private long time;
    private boolean waitingForAnimation = false;
    private boolean animationStarted = false;
    private EnemyType enemyType;
    private boolean isSecondPlayer = false;
    private Movie movie;
    private InputStream stream = null;
    private long moviestart;
    private boolean gameFinished = false;
    private WaitingForPlayer winner;
    private Bitmap victory;
    private Bitmap defeat;
    private GameButton returnToMenuButton;

    public Level(Bot p2, EnemyType et, GameSurface v) {
        bot = p2;
        enemyType = et;
        this.v = v;
    }

    public Level(Human p2, EnemyType et, GameSurface v) {
        human = p2;
        enemyType = et;
        this.v = v;
    }

    public void initialization(boolean b) {
        isSecondPlayer = b;
        piecesPositioned = false;
        player1Positioned = false;
        player2Positioned = false;
        waitingForPlayer = WaitingForPlayer.PLAYER1;
        board1 = new Board(v);
        board1.setBelongsToPlayer1(true);
        board2 = new Board(v);
        board2.setBelongsToPlayer1(false);
        switch (enemyType) {
            case BOT:
                bot.setOwnBoard(board2);
                bot.setEnemyBoard(board1);
                break;
            case HUMAN:
                human.setOwnBoard(board2);
                human.setEnemyBoard(board1);
                break;
        }
        rotationGameButtonIcons = new Bitmap[2];
        rotationGameButtonIcons[0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.arrow_r);
        rotationGameButtonIcons[1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.arrow_l);
        rotationGameButton = new GameButton(rotationGameButtonIcons[orientation], 50, 300, v, GameSurface.MethodName.ROTATE_SHIP);
        checkGameButtonIcons = new Bitmap[2];
        checkGameButtonIcons[0] = BitmapFactory.decodeResource(v.getResources(), R.drawable.check_grey);
        checkGameButtonIcons[1] = BitmapFactory.decodeResource(v.getResources(), R.drawable.check_green);
        checkGameButton = new GameButton(checkGameButtonIcons[0], 660, 300, v, GameSurface.MethodName.FINISH_SETTING_UP);
        gameButtons = new HashSet<GameButton>();
        gameButtons.add(rotationGameButton);
        gameButtons.add(checkGameButton);
        launchGameButton = new GameButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.launch_button), 660, 300, v, GameSurface.MethodName.LAUNCH);
        Bitmap f = BitmapFactory.decodeResource(v.getResources(), R.drawable.flames);
        for(int i = 0; i < 16; i++) {
            Square.getFlames()[i] = Bitmap.createBitmap(f, (i % 4) * f.getWidth() / 4, (i / 4) * f.getHeight() / 4, f.getWidth() / 4, f.getHeight() / 4);
        }
        Bitmap s = BitmapFactory.decodeResource(v.getResources(), R.drawable.splashes);
        for(int i = 0; i < 5; i++) {
            Square.getSplashes()[i] = Bitmap.createBitmap(s, 0, i * s.getHeight() / 5, s.getWidth(), s.getHeight() / 5);
        }
        for(int i = 5; i < 8; i++) {
            Square.getSplashes()[i] = Bitmap.createBitmap(s, 0, (8 - i) * s.getHeight() / 5, s.getWidth(), s.getHeight() / 5);
        }
        try {
            stream = v.getContext().getAssets().open("level_background.gif");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        movie = Movie.decodeStream(stream);
    }

    public void inputActionDown(int x, int y) {
        if (waitingForPlayer == WaitingForPlayer.PLAYER2 || animationStarted)
            return;
        for(GameButton g : gameButtons)
            if(y >= g.getY() && y <= g.getY() + g.getBackground().getHeight())
                if(x >= g.getX() && x <= g.getX() + g.getBackground().getWidth()) {
                    switch (g.getMethodName()) {
                        case ROTATE_SHIP:
                            changeOrientation();
                            return;
                        case FINISH_SETTING_UP:
                            if (board1.areAllShipsPlaced())
                                startBattle();
                            return;
                        case LAUNCH:
                            launchMissile();
                            return;
                        case RETURN_TO_MENU:
                            returnToMenu();
                            return;
                    }
                }
        if(piecesPositioned) {
            if(waitingForPlayer == WaitingForPlayer.PLAYER1)
                board2.inputActionDown(x, y);
        }
        else
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

    public void startBattle() {
        player1Positioned = true;
        board1.setPlayerPositioned(true);
        gameButtons.clear();
        gameButtons.add(launchGameButton);
        if(enemyType == EnemyType.HUMAN) {
            StringBuffer sb = new StringBuffer("pos ");
            for (int i = 0; i < Board.NUMBER_OF_SHIPS; i++) {
                sb.append(new String(board1.getShips()[i].getX() + " " + board1.getShips()[i].getY() + " " +
                        board1.getShips()[i].getSquareX() + " " + board1.getShips()[i].getSquareY() + " " + board1.getShips()[i].getOrientation() + " "));
            }
            System.out.println(sb);
            v.getCommunicationThread().write(sb.toString().getBytes());
        }
    }

    public void launchMissile() {
        if(board2.isSquareTargeted()) {
            Square s = board2.getSquares()[board2.getTargetSquareX()][board2.getTargetSquareY()];
            if (enemyType == EnemyType.HUMAN) {
                StringBuffer sb = new StringBuffer("att ");
                sb.append(new String(board2.getTargetSquareX() + " " + board2.getTargetSquareY()));
                v.getCommunicationThread().write(sb.toString().getBytes());
            }
            s.setAttacked(true);
            if (s.isOccupied())
                s.getShip().decreaseHealthPoints();
            board2.setSquareTargeted(false);
            waitingForAnimation = true;
        }
    }

    public void processMessage(String message) {
        Scanner scanner = new Scanner(message);
        String type = scanner.next();
        System.out.println(message);
        if (type.startsWith("pos")) {
            for (int i = 0; i < board2.NUMBER_OF_SHIPS; i++) {
                board2.getShips()[i].setX(scanner.nextInt());
                board2.getShips()[i].setY(scanner.nextInt());
                board2.getShips()[i].setSquareX(scanner.nextInt());
                board2.getShips()[i].setSquareY(scanner.nextInt());
                board2.getShips()[i].setOrientation(scanner.nextInt());
                board2.getShips()[i].setBackground(board2.getRotatedIcons()[i][board2.getShips()[i].getOrientation()]);
                for (int j = 0; j < board2.getShips()[i].getLength(); j++) {
                    if (board2.getShips()[i].getOrientation() == LEFT_ORIENTATION) {
                        board2.getSquares()[board2.getShips()[i].getSquareX()][board2.getShips()[i].getSquareY() + j].setOccupied(true);
                        board2.getSquares()[board2.getShips()[i].getSquareX()][board2.getShips()[i].getSquareY() + j].setShip(board2.getShips()[i]);
                        board2.getShips()[i].getCurrentSquares().add(board2.getSquares()[board2.getShips()[i].getSquareX()][board2.getShips()[i].getSquareY() + j]);
                    }
                    else {
                        board2.getSquares()[board2.getShips()[i].getSquareX() + j][board2.getShips()[i].getSquareY()].setOccupied(true);
                        board2.getSquares()[board2.getShips()[i].getSquareX() + j][board2.getShips()[i].getSquareY()].setShip(board2.getShips()[i]);
                        board2.getShips()[i].getCurrentSquares().add(board2.getSquares()[board2.getShips()[i].getSquareX() + j][board2.getShips()[i].getSquareY()]);
                    }
                }
                board2.getShips()[i].setPlaced(true);
            }
            player2Positioned = true;
            board2.setPlayerPositioned(true);
        }
        else if (type.startsWith("att")){
            int i1 = scanner.nextInt();
            int i2 = scanner.nextInt();
            Square s = board1.getSquares()[i1][i2];
            s.setAttacked(true);
            if(s.isOccupied())
                s.getShip().decreaseHealthPoints();
            waitingForAnimation = true;
        }
    }

    void returnToMenu() {
        System.out.println("Povratak u meni");
        v.setMainMenu(new MainMenu(v));
        v.setState(GameSurface.State.MENU_STATE);
        v.setBluetoothSocket(null);
        v.setHostGameThread(null);
        v.setClientConnected(false);
        v.setJoinGameThread(null);
        v.setClientConnected(false);
        v.setCommunicationThread(null);
        v.setReceiverRegistered(false);
    }

    public void update() {
        if(gameFinished)
            return;
        if(piecesPositioned) {
            switch (waitingForPlayer) {
                case PLAYER1:
                    board2.update();
                    if(board2.areAllShipsDestroyed()) {
                        gameFinished = true;
                        winner = WaitingForPlayer.PLAYER1;
                        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.end_game);
                        victory = BitmapFactory.decodeResource(v.getResources(), R.drawable.victory);
                        returnToMenuButton = new GameButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.return_to_menu_button), 0, 240, v, GameSurface.MethodName.RETURN_TO_MENU);
                        returnToMenuButton.setX((800 - returnToMenuButton.getBackground().getWidth()) / 2);
                        gameButtons.clear();
                        gameButtons.add(returnToMenuButton);
                        return;
                    }
                    if(waitingForAnimation) {
                        time = System.nanoTime();
                        waitingForAnimation = false;
                        animationStarted = true;
                        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.end_game);
                    }
                    if(animationStarted) {
                        if ((System.nanoTime() - time) / 1000000 < 500)
                            break;
                        animationStarted = false;
                        waitingForPlayer = (waitingForPlayer == WaitingForPlayer.PLAYER1 ? WaitingForPlayer.PLAYER2 : WaitingForPlayer.PLAYER1);
                    }
                    break;
                case PLAYER2:
                    board1.update();
                    if(board1.areAllShipsDestroyed()) {
                        gameFinished = true;
                        winner = WaitingForPlayer.PLAYER2;
                        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.end_game);
                        defeat = BitmapFactory.decodeResource(v.getResources(), R.drawable.defeat);
                        returnToMenuButton = new GameButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.return_to_menu_button), 0, 240, v, GameSurface.MethodName.RETURN_TO_MENU);
                        returnToMenuButton.setX((800 - returnToMenuButton.getBackground().getWidth()) / 2);
                        gameButtons.clear();
                        gameButtons.add(returnToMenuButton);
                        return;
                    }
                    switch (enemyType) {
                        case BOT:
                            if(bot.hasStartedThinking()) {
                                time = System.nanoTime();
                                bot.setStartedThinking(false);
                            }
                            if(!bot.hasFinishedThinking()) {
                                if((System.nanoTime() - time) / 1000000 < 500)
                                    break;
                                bot.setFinishedThinking(true);
                            }
                            if(!bot.hasAttacked()) {
                                bot.shoot();
                                time = System.nanoTime();
                            }
                            if((System.nanoTime() - time) / 1000000 < 500)
                                break;
                            bot.setStartedThinking(true);
                            bot.setFinishedThinking(false);
                            bot.setAttacked(false);
                            waitingForPlayer = (waitingForPlayer == WaitingForPlayer.PLAYER1 ? WaitingForPlayer.PLAYER2 : WaitingForPlayer.PLAYER1);
                            break;
                        case HUMAN:
                            if(waitingForAnimation) {
                                time = System.nanoTime();
                                waitingForAnimation = false;
                                animationStarted = true;
                            }
                            if(animationStarted) {
                                if ((System.nanoTime() - time) / 1000000 < 500)
                                    break;
                                animationStarted = false;
                                waitingForPlayer = (waitingForPlayer == WaitingForPlayer.PLAYER1 ? WaitingForPlayer.PLAYER2 : WaitingForPlayer.PLAYER1);
                            }
                            break;
                    }
                    break;
            }
        }
        else {
            if(player1Positioned) {
                if(isSecondPlayer)
                    waitingForPlayer = WaitingForPlayer.PLAYER2;
                if (player2Positioned)
                    piecesPositioned = true;
                else {
                    switch (enemyType) {
                        case BOT:
                            bot.positionShips();
                            player2Positioned = true;
                            board2.setPlayerPositioned(true);
                            break;
                        case HUMAN:
                            //
                            break;
                    }
                }
            }
            else {
                board1.update();
                rotationGameButton.setBackground(rotationGameButtonIcons[orientation]);
                checkGameButton.setBackground(board1.areAllShipsPlaced() ? checkGameButtonIcons[1] : checkGameButtonIcons[0]);
            }
        }
    }

    public void draw(Canvas canvas) {
        if(gameFinished) {
            canvas.drawBitmap(background, x, y, null);
            switch (winner) {
                case PLAYER1:
                    canvas.drawBitmap(victory, (800 - victory.getWidth()) / 2, 30, null);
                    break;
                case PLAYER2:
                    canvas.drawBitmap(defeat, (800 - defeat.getWidth()) / 2, 30, null);
                    break;
            }
            for (GameButton g : gameButtons)
                g.draw(canvas);
            return;
        }
        final long now = SystemClock.uptimeMillis();
        if (moviestart == 0) {
            moviestart = now;
        }
        final int relTime = (int)((now - moviestart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas, 0, 0);

        if(piecesPositioned) {
            switch (waitingForPlayer) {
                case PLAYER1:
                    //privremeno
                    launchGameButton.draw(canvas);
                    board2.draw(canvas);
                    break;
                case PLAYER2:
                    //privremeno
                    board1.draw(canvas);
                    break;
            }
        }
        else {
            if(player1Positioned) {
                if (player2Positioned) {
                    //
                }
                else {
                    //
                }
            }
            else {
                for (GameButton g : gameButtons)
                    g.draw(canvas);
                board1.draw(canvas);
            }
        }
    }

    public WaitingForPlayer getWaitingForPlayer() { return waitingForPlayer; }

    public void setWaitingForPlayer(WaitingForPlayer waitingForPlayer) { this.waitingForPlayer = waitingForPlayer; }
}
