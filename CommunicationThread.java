package com.example.milomir93.battleships;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommunicationThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

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
                System.out.println("Primljeno: " + new String(buffer));
                //obraditi poruku
            }
            catch (IOException e) {
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
            System.out.println("Poslato: " + new String(bytes));
        }
        catch (IOException e) {}
    }

    public void cancel() {
        try {
            bluetoothSocket.close();
        }
        catch (IOException e) {}
    }
}
