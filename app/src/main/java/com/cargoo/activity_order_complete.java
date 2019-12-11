package com.cargoo;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.internal.Util;

public class activity_order_complete extends AppCompatActivity {

    private TextView txtTotalPrices, txtWA;

    private String refTotalPrice;
    private String refOrderID;
    private ImageView imgCopy;

    private Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        Button transaction = findViewById(R.id.btnTransactionDone);
        transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
//        refTotalPrice = i.getStringExtra("totalPrice");
        refOrderID = i.getStringExtra("orderID");

        Query qTotalPrice = FirebaseDatabase.getInstance().getReference().child("Orders")
                .orderByChild("orderID")
                .equalTo(refOrderID);

        txtTotalPrices = findViewById(R.id.txtTotalPriceDone);

        qTotalPrice.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refTotalPrice = "Rp " + String.valueOf(dataSnapshot.child(refOrderID).child("totalPrice").getValue(Integer.class));
                txtTotalPrices.setText(refTotalPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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

        btnDone = findViewById(R.id.btnTransactionDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity_order_complete.this, HistoryActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
//        finish();
//    }
}
