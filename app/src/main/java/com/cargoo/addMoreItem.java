package com.cargoo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class addMoreItem extends AppCompatActivity {

    // Variables that gotten from Intent
    private int totalPrice; // To update totalPrice in coll. Orders
    private float totalWeight; // To update totalWeight in coll. Orders
    private float totalVolume; // To update totalVolume in coll. Orders
    private String orderID; // Get order ID from order form.

    private EditText edtNamaBarang, edtQuantity, edtWeight, edtWidth, edtLength, edtHeight;
    private Spinner spinUnit;
    private CheckBox cbFragile;

    private boolean isFragile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_item);
    }
}
