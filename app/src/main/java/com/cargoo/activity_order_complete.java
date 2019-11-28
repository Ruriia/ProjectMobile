package com.cargoo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.internal.Util;

public class activity_order_complete extends AppCompatActivity {

    private TextView txtTotalPrices, txtWA;

    private String refTotalPrice;
    private ImageView imgCopy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        Intent i = getIntent();
        refTotalPrice = i.getStringExtra("totalPrice");

        txtTotalPrices = findViewById(R.id.txtTotalPriceDone);
        txtTotalPrices.setText(refTotalPrice);

        imgCopy = findViewById(R.id.imageCopy);
        imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String norek = "5211070987";

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("norek", norek);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(activity_order_complete.this, "The bank account number has copied", Toast.LENGTH_SHORT).show();
            }
        });

        txtWA = findViewById(R.id.textWA);
        txtWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "081546164842";

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("phone", phone);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(activity_order_complete.this, "The phone number has copied", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
