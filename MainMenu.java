package com.example.milomir93.battleships;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.HashSet;

public class MainMenu {
    private enum SubState {NORMAL, WAITING_FOR_BLUETOOTH_HOST, WAITING_FOR_BLUETOOTH_JOIN, WAITING_FOR_CLIENT, CLIENT_CONNECTED, CHOOSING_SERVER, WAITING_FOR_SERVER, CONNECTED_TO_SERVER};
    private GameSurface v;
    private HashSet<MenuButton> buttons;
    private MenuButton lastChanged;
    private Bitmap background;
    private Bitmap title;
    private int x = 0;
    private int y = 0;
    private int titleX;
    private int titleY;
    private MenuButton newMenuButton;
    private MenuButton singlePlayerGameMenuButton;
    private MenuButton multiPlayerGameMenuButton;
    private MenuButton joinMenuButton;
    private MenuButton exitMenuButton;
    private MenuButton showBluetoothDevicesMenuButton;
    private SubState subState;
    private HashSet<DeviceListItem> deviceListItems;
    private Paint paint;

    public MainMenu(GameSurface v) {
        subState = SubState.NORMAL;
        this.v = v;
        v.setLevel(null);
        lastChanged = new MenuButton();
        buttons = new HashSet<MenuButton>();
        background = BitmapFactory.decodeResource(v.getResources(), R.drawable.menu_bg);
        title = BitmapFactory.decodeResource(v.getResources(), R.drawable.menu_title);
        titleX = (800 - title.getWidth()) / 2;
        titleY = 30;
        newMenuButton = new MenuButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.new_game_button), 180, v, GameSurface.MethodName.CREATE_GAME);
        joinMenuButton = new MenuButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.join_game_button), 276, v, GameSurface.MethodName.JOIN_GAME);
        exitMenuButton = new MenuButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.exit_button), 372, v, GameSurface.MethodName.EXIT_GAME);
        buttons.add(newMenuButton);
        buttons.add(joinMenuButton);
        buttons.add(exitMenuButton);
        deviceListItems = new HashSet<DeviceListItem>();
    }

    public void inputActionDown(int x, int y) {
        for (MenuButton g : buttons)
            if (y >= g.getY() && y <= g.getY() + g.getBackground().getHeight())
                if (x >= g.getX() && x <= g.getX() + g.getBackground().getWidth()) {
                    g.setIsWide(MenuButton.WIDE);
                    lastChanged = g;
                    break;
                }
        if(subState == SubState.CHOOSING_SERVER){
            for (DeviceListItem d : deviceListItems)//poboljšati ovo da razmatra i ostale događaje
            if (y >= d.getY() && y <= d.getY() + d.getBackground().getHeight())
                if (x >= d.getX() && x <= d.getX() + d.getBackground().getWidth()) {
                    System.out.println(d.getBluetoothDevice().getName());
                    joinMultiPlayerGame(d.getBluetoothDevice());
                    break;
                }
        }
    }

    public void inputActionMove(int x, int y) {
        lastChanged.setIsWide(MenuButton.NON_WIDE);
        for(MenuButton g : buttons)
            if(y >= g.getY() && y <= g.getY() + g.getBackground().getHeight())
                if(x >= g.getX() && x <= g.getX() + g.getBackground().getWidth()) {
                    g.setIsWide(MenuButton.WIDE);
                    lastChanged = g;
                    break;
                }
    }

    public void inputActionUp(int x, int y) {
        for(MenuButton g : buttons)
            if(y >= g.getY() && y <= g.getY() + g.getBackground().getHeight())
                if(x >= g.getX() && x <= g.getX() + g.getBackground().getWidth()) {
                    g.setIsWide(MenuButton.NON_WIDE);
                    lastChanged = g;
                    switch (g.getMethodName()) {
                        case CREATE_GAME:
                            createGame();
                            break;
                        case START_SINGLE_PLAYER_GAME:
                            startSinglePlayerGame();
                            break;
                        case START_MULTIPLAYER_GAME:
                            startMultiPlayerGame();
                            break;
                        case JOIN_GAME:
                            joinGame();
                            break;
                        case SHOW_DEVICES:
                            showDevices();
                            break;
                        case EXIT_GAME:
                            exitGame();
                            break;
                    }
                    break;
                }
    }

    void createGame() {
        System.out.println("Create");
        singlePlayerGameMenuButton = new MenuButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.singleplayer_button), 180, v, GameSurface.MethodName.START_SINGLE_PLAYER_GAME);
        multiPlayerGameMenuButton = new MenuButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.multiplayer_button), 276, v, GameSurface.MethodName.START_MULTIPLAYER_GAME);
        buttons.clear();
        buttons.add(singlePlayerGameMenuButton);
        buttons.add(multiPlayerGameMenuButton);
    }

    void startSinglePlayerGame() {
        Level l = new Level(new Bot(v), Level.EnemyType.BOT, v);
        l.initialization(false);
        v.setLevel(l);
        v.setState(GameSurface.State.LEVEL_STATE);
    }

    void startMultiPlayerGame() {
        buttons.clear();
        v.enableDiscoverability();
        subState = SubState.WAITING_FOR_BLUETOOTH_HOST;
    }

    void joinGame() {
        System.out.println("Join");
        v.enableBluetooth();
        subState = SubState.WAITING_FOR_BLUETOOTH_JOIN;
    }

    void showDevices() {
        int counter = 0;
        deviceListItems.clear();
        for(BluetoothDevice b : Game.bluetoothDevices) {
            System.out.println(b.getName() + " " + b.getAddress());
            counter++;
            deviceListItems.add(new DeviceListItem(BitmapFactory.decodeResource(v.getResources(), R.drawable.device_list_item), 20 + 410 * (counter / 6), 150 + 65 * (counter % 6), b, paint));
        }
    }

    void joinMultiPlayerGame(BluetoothDevice b) {
        v.setJoinGameThread(new JoinGameThread(b, v));
        v.getJoinGameThread().start();
        subState = SubState.WAITING_FOR_SERVER;
    }

    void exitGame() {
        System.out.println("Exit");
        ((Activity)v.getContext()).finish();
    }

    public void update() {//!
        for(MenuButton b : buttons)
            b.update();
        if(v.isClientConnected()) {
            subState = SubState.CLIENT_CONNECTED;
            v.setHostGameThread(null);
        }
        else if(v.isConnectedToServer()) {
            subState = SubState.CONNECTED_TO_SERVER;
            v.setJoinGameThread(null);
        }
        switch (subState) {
            case CLIENT_CONNECTED:
                v.setCommunicationThread(new CommunicationThread(v.getBluetoothSocket()));
                v.getCommunicationThread().start();
                Level l1 = new Level(new Human(v), Level.EnemyType.HUMAN, v);
                l1.initialization(false);
                v.getCommunicationThread().setLevel(l1);
                v.setLevel(l1);
                v.setState(GameSurface.State.LEVEL_STATE);
                break;
            case CONNECTED_TO_SERVER:
                v.setCommunicationThread(new CommunicationThread(v.getBluetoothSocket()));
                v.getCommunicationThread().start();
                Level l2 = new Level(new Human(v), Level.EnemyType.HUMAN, v);
                l2.initialization(true);
                v.getCommunicationThread().setLevel(l2);
                v.setLevel(l2);
                v.setState(GameSurface.State.LEVEL_STATE);
                break;
            case WAITING_FOR_BLUETOOTH_HOST:
                if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    v.setHostGameThread(new HostGameThread(v));
                    v.getHostGameThread().start();//dodati mogućnost da se odustane
                    subState = SubState.WAITING_FOR_CLIENT;
                }
                break;
            case WAITING_FOR_BLUETOOTH_JOIN:
                if(BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    //System.out.println("Bluetooth enabled Join");
                    v.listPairedBluetoothDevices();
                    /*IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    ((Game) v.getContext()).registerReceiver(((Game) v.getContext()).getBroadcastReceiver(), filter);
                    v.setReceiverRegistered(true);
                    v.getBluetoothAdapter().startDiscovery();*/
                    paint = new Paint();
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(30);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    showBluetoothDevicesMenuButton = new MenuButton(BitmapFactory.decodeResource(v.getResources(), R.drawable.show_paired_devices_button), 180, v, GameSurface.MethodName.SHOW_DEVICES);
                    buttons.clear();
                    buttons.add(showBluetoothDevicesMenuButton);
                    subState = SubState.CHOOSING_SERVER;
                }
                break;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
        canvas.drawBitmap(title, titleX, titleY, null);
        for(MenuButton b : buttons)
            b.draw(canvas);
        switch (subState) {
            case WAITING_FOR_CLIENT:
                //System.out.println("Waiting for client");
                break;
            case CLIENT_CONNECTED:
                //System.out.println("Client connected");
                break;
            case CHOOSING_SERVER:
                for(DeviceListItem d : deviceListItems)
                    d.draw(canvas);
                break;
            case WAITING_FOR_SERVER:
                //System.out.println("Waiting for server");
                break;
            case CONNECTED_TO_SERVER:
                //System.out.println("Connected to server");
                break;
        }
    }
}
