package com.cargoo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ActivityLoader.class));
        finish();
    }
}
