package com.example.milomir93.battleships;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;

public class HostGameThread extends Thread{
    private final BluetoothServerSocket bluetoothServerSocket;
    private GameSurface v;

    public HostGameThread(GameSurface v) {
        this.v = v;
        BluetoothServerSocket temp = null;
        while(temp == null) {
            try {
                temp = v.getBluetoothAdapter().listenUsingRfcommWithServiceRecord("Battleships", v.getUuid());
            }
            catch (IOException e) {}
        }
        bluetoothServerSocket = temp;
    }

    public void run() {
        BluetoothSocket bluetoothSocket = null;
        while (true) {
            //System.out.println("SERVER: Waiting for client");
            try {
                bluetoothSocket = bluetoothServerSocket.accept();
            }
            catch (IOException e) {
                break;
            }
            if (bluetoothSocket != null) {
                //System.out.println("Host game socket created");
                v.setBluetoothSocket(bluetoothSocket);
                v.setClientConnected(true);
                try {
                    bluetoothServerSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void cancel() {
        try {
            bluetoothServerSocket.close();
        }
        catch (IOException e) {}
    }
}
