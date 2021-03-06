package com.example.milomir93.battleships;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommunicationThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private String message;
    private Level level;

    public CommunicationThread(BluetoothSocket socket) {
        bluetoothSocket = socket;
        InputStream tempIn = null;
        OutputStream tempOut = null;
        try {
            tempIn = socket.getInputStream();
            tempOut = socket.getOutputStream();
        }
        catch (IOException e) {}
        inputStream = tempIn;
        outputStream = tempOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        while (true) {
            try {
                bytes = inputStream.read(buffer);
                message = new String(buffer, 0, bytes);
                //System.out.println("Primljeno: " + message);
                level.processMessage(message);
            }
            catch (IOException e) {
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
            System.out.println("Poslato: " + new String(bytes).trim());
        }
        catch (IOException e) {}
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        }
        catch (IOException e) {}
    }

    public void setLevel(Level level) { this.level = level; }
}
