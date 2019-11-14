package com.cargoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);

        final int loading_time = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent login=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(login);
                finish();

            }
        },loading_time);
    }
}
