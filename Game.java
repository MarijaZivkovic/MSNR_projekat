package com.example.milomir93.battleships;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import java.util.Set;

public class Game extends Activity {
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int REQUEST_DISCOVERABLE_BT = 2;
    private boolean bluetoothEnabled = false;
    public static Set<BluetoothDevice> bluetoothDevices;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                bluetoothDevices.add(device);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new GameSurface(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK)
                bluetoothEnabled = true;
        }
        else if(requestCode == REQUEST_DISCOVERABLE_BT) {
            if (resultCode > 0)
                bluetoothEnabled = true;
        }
    }

//    public HashSet<BluetoothDevice> getBluetoothDevices() { return bluetoothDevices; }

    public boolean isBluetoothEnabled() { return bluetoothEnabled; }

    public BroadcastReceiver getBroadcastReceiver() { return broadcastReceiver; }
}
