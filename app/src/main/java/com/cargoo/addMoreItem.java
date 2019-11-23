package com.cargoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class addMoreItem extends AppCompatActivity {

    // Variables that gotten from Intent
    private int totalPrice; // To update totalPrice in coll. Orders
    private float totalWeight; // To update totalWeight in coll. Orders
    private float totalVolume; // To update totalVolume in coll. Orders
    private String orderID; // Get order ID from order form.

    private Button btnUpload, btnAddItem, btnDone, btnCancel;
    private EditText edtNamaBarang, edtQuantity, edtWeight, edtWidth, edtLength, edtHeight;
    private Spinner spinUnit;
    private CheckBox cbFragile;

    private int weightPrice = 15000; // Untuk harga per kg
    private int volumePrice = 20000; // Untuk harga per m3
    private int itemPrice;
    private boolean isFragile = false;

    DatabaseReference dbOrder, dbItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_item);

        btnDone = findViewById(R.id.btnDone);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpload = findViewById(R.id.btnUploadimage);

        edtNamaBarang = findViewById(R.id.edtNamaBarang);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtWeight = findViewById(R.id.edtWeight);
        edtWidth = findViewById(R.id.edtWidth);
        edtLength = findViewById(R.id.edtLength);
        edtHeight = findViewById(R.id.edtHeight);

        spinUnit = findViewById(R.id.spinUnit);

        cbFragile = findViewById(R.id.cbFragile);

        final ProgressBar progressBar4 = findViewById(R.id.progressBar4);

        dbOrder = FirebaseDatabase.getInstance().getReference().child("Orders");
        dbItems = FirebaseDatabase.getInstance().getReference().child("Items");

        Intent i = getIntent();
        totalPrice = i.getIntExtra("totalPrice", 0);
        totalWeight = i.getIntExtra("totalWeight", 0);
        totalVolume = i.getIntExtra("totalVolume", 0);
        orderID = i.getStringExtra("orderID");

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ------------------ ITEM ATTRIBUTE --------------
                String itemID = dbItems.push().getKey();
                String itemName = edtNamaBarang.getText().toString();
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                String unit = spinUnit.getSelectedItem().toString();
                float width = (float) Math.ceil(Float.parseFloat(edtWidth.getText().toString()));
                float length = (float) Math.ceil(Float.parseFloat(edtLength.getText().toString()));
                float height = (float) Math.ceil(Float.parseFloat(edtHeight.getText().toString()));
                float weight = (float) Math.ceil(Float.parseFloat(edtWeight.getText().toString()));
                float volume = width * length * height;

                if (cbFragile.isChecked()) {
                    isFragile = true;
                }

                // -------------------- CALCULATION ------------------

                totalWeight += (int)weight;
                totalVolume += (int) volume;
                itemPrice = ((int) volume * volumePrice) + ((int) weight * weightPrice);

                if(isFragile){
                    itemPrice += 10000;
                }

                totalPrice += itemPrice;

                progressBar4.setVisibility(View.VISIBLE);

                // --------- INPUT "ITEM" TO DATABASE -----------
                Items dataItems = new Items(itemID, orderID, itemName, quantity, unit, width, length, height, weight, volume, itemPrice, isFragile);
                dbItems.child(itemID).setValue(dataItems);

                // -------- UPDATE "ORDER" COLLECTION ------------
                // Update total price, total weight, and total volume.
                dbOrder.child(orderID).child("totalWeight").setValue(totalWeight);
                dbOrder.child(orderID).child("totalVolume").setValue(totalVolume);
                dbOrder.child(orderID).child("totalPrice").setValue(totalPrice);

                // ---------------------- EVENT LISTENER --------------------------
                ValueEventListener writeListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar4.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "New item has added. Make a payment now. ", Toast.LENGTH_LONG).show();

                        // Go to payment activity here !
                        // ...
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                    }
                };

                dbOrder.addValueEventListener(writeListener);
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // ------------------ ITEM ATTRIBUTE --------------
                String itemID = dbItems.push().getKey();
                String itemName = edtNamaBarang.getText().toString();
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                String unit = spinUnit.getSelectedItem().toString();
                float width = (float) Math.ceil(Float.parseFloat(edtWidth.getText().toString()));
                float length = (float) Math.ceil(Float.parseFloat(edtLength.getText().toString()));
                float height = (float) Math.ceil(Float.parseFloat(edtHeight.getText().toString()));
                float weight = (float) Math.ceil(Float.parseFloat(edtWeight.getText().toString()));
                float volume = width * length * height;

                if (cbFragile.isChecked()) {
                    isFragile = true;
                }

                // -------------------- CALCULATION ------------------

                totalWeight += (int)weight;
                totalVolume += (int) volume;
                itemPrice = ((int) volume * volumePrice) + ((int) weight * weightPrice);

                if(isFragile){
                    itemPrice += 10000;
                }

                totalPrice += itemPrice;

                progressBar4.setVisibility(View.VISIBLE);

                // --------- INPUT "ITEM" TO DATABASE -----------
                Items dataItems = new Items(itemID, orderID, itemName, quantity, unit, width, length, height, weight, volume, itemPrice, isFragile);
                dbItems.child(itemID).setValue(dataItems);

                // -------- UPDATE "ORDER" COLLECTION ------------
                // Update total price, total weight, and total volume.
                dbOrder.child(orderID).child("totalWeight").setValue(totalWeight);
                dbOrder.child(orderID).child("totalVolume").setValue(totalVolume);
                dbOrder.child(orderID).child("totalPrice").setValue(totalPrice);

                // ---------------------- EVENT LISTENER --------------------------
                ValueEventListener writeListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar4.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "New item has added. Make a payment now. ", Toast.LENGTH_LONG).show();

                        // Go to add new item activity here !
                        Intent i = new Intent(addMoreItem.this, addMoreItem.class);
                        i.putExtra("totalPrice", totalPrice);
                        i.putExtra("totalWeight", totalWeight);
                        i.putExtra("totalVolume", totalVolume);
                        i.putExtra("orderID", orderID);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                    }
                };

                dbOrder.addValueEventListener(writeListener);

            }
        });


    }
}
