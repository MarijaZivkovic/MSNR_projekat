package com.example.milomir93.battleships;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class Game extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new GameSurface(this));
    }
}
