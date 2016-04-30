package com.example.milomir93.battleships;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;

public class JoinGameThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final BluetoothDevice bluetoothDevice;
    private String mSocketType;
    private GameSurface v;

    public JoinGameThread(BluetoothDevice device, GameSurface v) {
        bluetoothDevice = device;
        this.v = v;
        BluetoothSocket temp = null;
        try {
            temp = device.createRfcommSocketToServiceRecord(v.getUuid());
        }
        catch (IOException e) {}
        bluetoothSocket = temp;
    }

    public void run() {
        v.getBluetoothAdapter().cancelDiscovery();
        System.out.println("CLIENT: Starting connect...");
        try {
            bluetoothSocket.connect();
        }
        catch (IOException e) {
            try {
                bluetoothSocket.close();
            }
            catch (IOException e2) {}
            return;
        }
        System.out.println("Join game socket created");
        v.setBluetoothSocket(bluetoothSocket);
        v.setConnectedToServer(true);
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        }
        catch (IOException e) {}
    }
}
