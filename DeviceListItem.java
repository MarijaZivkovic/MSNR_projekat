package com.example.milomir93.battleships;

import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class DeviceListItem {
    private Bitmap background;
    private int x;
    private int y;
    private BluetoothDevice bluetoothDevice;

    public DeviceListItem(Bitmap b, int x, int y, BluetoothDevice d, GameSurface v) {
        background = b;
        this.x = x;
        this.y = y;
        bluetoothDevice = d;
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
    }

    public Bitmap getBackground() { return background; }

    public void setBackground(Bitmap background) { this.background = background; }

    public int getX() { return x; }

    public int getY() { return y; }

    public BluetoothDevice getBluetoothDevice() { return bluetoothDevice; }
}
