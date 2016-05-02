package com.example.milomir93.battleships;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Set;
import java.util.UUID;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback{
    public enum State {MENU_STATE, LEVEL_STATE};
    public enum MethodName {CREATE_GAME, JOIN_GAME, START_SINGLE_PLAYER_GAME, START_MULTIPLAYER_GAME, EXIT_GAME, ROTATE_SHIP, FINISH_SETTING_UP, LAUNCH, SHOW_DEVICES, RETURN_TO_MENU}
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    private GameThread gameThread;
    private State state;
    private MainMenu mainMenu;
    private Level level;
    private BluetoothAdapter bluetoothAdapter = null;
    private UUID uuid = UUID.fromString("0b6bed26-0286-11e6-b512-3e1d05defe78");
    private BluetoothSocket bluetoothSocket;
    private HostGameThread hostGameThread;
    private boolean clientConnected = false;
    private JoinGameThread joinGameThread;
    private boolean connectedToServer = false;
    private CommunicationThread communicationThread;
    private boolean receiverRegistered = false;

    public GameSurface(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        state = State.MENU_STATE;
        mainMenu = new MainMenu(this);
        gameThread = new GameThread(getHolder(), this);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while(retry) {
            try {
                gameThread.running = false;
                gameThread.join();
                retry = false;
                gameThread = null;
            }
            catch (InterruptedException e) {}
        }
        if(receiverRegistered)
            getContext().unregisterReceiver(((Game)getContext()).getBroadcastReceiver());
        disableBluetooth();
    }

    void enableBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //bluetooth nije podržan
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ((Activity)getContext()).startActivityForResult(enableBtIntent, ((Game)getContext()).REQUEST_ENABLE_BT);
        }
        //treba proveriti da li je bluetooth uključen
    }

    void disableBluetooth() {
        if (((Game)getContext()).isBluetoothEnabled()) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()){
                bluetoothAdapter.disable();
            }
        }
    }

    void enableDiscoverability() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            //bluetooth nije podržan
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            ((Activity)getContext()).startActivityForResult(discoverableIntent, ((Game) getContext()).REQUEST_DISCOVERABLE_BT);
        }
    }

    void listPairedBluetoothDevices() {
        Game.bluetoothDevices/*Set<BluetoothDevice> pairedDevices*/ = bluetoothAdapter.getBondedDevices();
        /*if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Game.bluetoothDevices.add(device);
            }
        }*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (state) {
                case MENU_STATE:
                    mainMenu.inputActionDown((int) event.getX(), (int) event.getY());
                    return true;
                case LEVEL_STATE:
                    level.inputActionDown((int) event.getX(), (int) event.getY());
                    return true;
            }
        }

        else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            switch (state) {
                case MENU_STATE:
                    mainMenu.inputActionMove((int) event.getX(), (int) event.getY());
                    return true;
                case LEVEL_STATE:
                    level.inputActionMove((int) event.getX(), (int) event.getY());
                    return true;
            }
        }

        else if(event.getAction() == MotionEvent.ACTION_UP) {
            switch (state) {
                case MENU_STATE:
                    mainMenu.inputActionUp((int) event.getX(), (int) event.getY());
                    return true;
                case LEVEL_STATE:
                    level.inputActionUp((int) event.getX(), (int) event.getY());
                    return true;
            }
        }

        return super.onTouchEvent(event);
    }

    public void update() {
        switch (state) {
            case MENU_STATE:
                mainMenu.update();
                break;
            case LEVEL_STATE:
                level.update();
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if(canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            switch (state) {
                case MENU_STATE:
                    mainMenu.draw(canvas);
                    break;
                case LEVEL_STATE:
                    level.draw(canvas);
                    break;
            }

            canvas.restoreToCount(savedState);
        }
    }

    void setLevel(Level l) { level = l; }

    public void setState(State state) { this.state = state; }

    public BluetoothAdapter getBluetoothAdapter() { return bluetoothAdapter; }

    public UUID getUuid() { return uuid; }

    public BluetoothSocket getBluetoothSocket() { return bluetoothSocket; }

    public void setBluetoothSocket(BluetoothSocket bluetoothSocket) { this.bluetoothSocket = bluetoothSocket; }

    public HostGameThread getHostGameThread() { return hostGameThread; }

    public void setHostGameThread(HostGameThread hostGameThread) { this.hostGameThread = hostGameThread; }

    public boolean isClientConnected() { return clientConnected; }

    public void setClientConnected(boolean clientConnected) { this.clientConnected = clientConnected; }

    public JoinGameThread getJoinGameThread() { return joinGameThread; }

    public void setJoinGameThread(JoinGameThread joinGameThread) { this.joinGameThread = joinGameThread; }

    public boolean isConnectedToServer() { return connectedToServer; }

    public void setConnectedToServer(boolean connectedToServer) { this.connectedToServer = connectedToServer; }

    public CommunicationThread getCommunicationThread() { return communicationThread; }

    public void setCommunicationThread(CommunicationThread communicationThread) { this.communicationThread = communicationThread; }

    public void setReceiverRegistered(boolean receiverRegistered) { this.receiverRegistered = receiverRegistered; }

    public void setMainMenu(MainMenu mainMenu) { this.mainMenu = mainMenu; }
}
