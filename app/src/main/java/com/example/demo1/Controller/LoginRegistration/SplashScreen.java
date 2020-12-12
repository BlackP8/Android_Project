package com.example.demo1.Controller.LoginRegistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.demo1.R;

public class SplashScreen extends Activity {
    private final int SLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        },SLASH_DISPLAY_LENGTH);
        }

        @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    }
