package com.example.milomir93.battleships;

import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class DeviceListItem {
    private Bitmap background;
    private int x;
    private int y;
    private BluetoothDevice bluetoothDevice;
    private Paint paint;

    public DeviceListItem(Bitmap b, int x, int y, BluetoothDevice d, Paint p) {
        background = b;
        this.x = x;
        this.y = y;
        bluetoothDevice = d;
        paint = p;
    }

    public void update() {
        //
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, x, y, null);
        canvas.drawText(bluetoothDevice.getName(), x + 20, y + 40, paint);
    }

    public Bitmap getBackground() { return background; }

    public void setBackground(Bitmap background) { this.background = background; }

    public int getX() { return x; }

    public int getY() { return y; }

    public BluetoothDevice getBluetoothDevice() { return bluetoothDevice; }
}
