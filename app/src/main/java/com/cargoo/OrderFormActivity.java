package com.cargoo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final int image_id = 1;
    Button upload, btnDone;
    TextView imagename;
    ImageView previewimage;

    private EditText edtNamaPengirim, edtTelpPengirim, edtEmailPengirim, edtAlamatPengirim, edtProvinsiPengirim, edtKotaPengirim, edtKecamatanPengirim, edtKodePosPengirim;
    private EditText edtNamaPenerima, edtTelpPenerima, edtEmailPenerima, edtAlamatPenerima, edtProvinsiPenerima, edtKotaPenerima, edtKecamatanPenerima, edtKodePosPenerima;
    private EditText edtTglPengiriman;

    private EditText edtNamaBarang, edtQuantity, edtWeight, edtWidth, edtLength, edtHeight;
    private Spinner spinUnit;
    private CheckBox cbFragile;

    private boolean isFragile = false;

    DatabaseReference dbOrder, dbItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        upload = findViewById(R.id.uploadimage);
        previewimage = findViewById(R.id.previewimage);

        edtNamaPengirim = findViewById(R.id.edtNamaPengirim);
        edtTelpPengirim = findViewById(R.id.edtTelpPengirim);
        edtEmailPengirim = findViewById(R.id.edtEmailPengirim);

        edtNamaPenerima = findViewById(R.id.edtNamaPenerima);
        edtTelpPenerima = findViewById(R.id.edtTelpPenerima);
        edtEmailPenerima= findViewById(R.id.edtEmailPenerima);

        edtAlamatPengirim = findViewById(R.id.edtAlamatPengirim);
        edtProvinsiPengirim = findViewById(R.id.edtProvinsiPengirim);
        edtKotaPengirim = findViewById(R.id.edtKotaPengirim);
        edtKecamatanPengirim = findViewById(R.id.edtKecamatanPengirim);
        edtKodePosPengirim = findViewById(R.id.edtKodePosPengirim);

        edtAlamatPenerima= findViewById(R.id.edtAlamatPenerima);
        edtProvinsiPenerima= findViewById(R.id.edtProvinsiPenerima);
        edtKotaPenerima= findViewById(R.id.edtKotaPenerima);
        edtKecamatanPenerima= findViewById(R.id.edtKecamatanPenerima);
        edtKodePosPenerima= findViewById(R.id.edtKodePosPenerima);

        edtNamaBarang = findViewById(R.id.edtNamaBarang);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtWeight = findViewById(R.id.edtWeight);
        edtWidth = findViewById(R.id.edtWidth);
        edtLength = findViewById(R.id.edtLength);
        edtHeight = findViewById(R.id.edtHeight);

        spinUnit = findViewById(R.id.spinUnit);

        cbFragile = findViewById(R.id.cbFragile);


        final ProgressBar progressBar3 = findViewById(R.id.progressBar3);

        dbOrder = FirebaseDatabase.getInstance().getReference().child("Orders");
        dbItems = FirebaseDatabase.getInstance().getReference().child("Items");

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galery, image_id);
            }
        });

        edtTglPengiriman = findViewById(R.id.edtTglPengiriman);
        edtTglPengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Pick the Delivery Date");
            }
        });

        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderID = dbOrder.push().getKey();
                String userID = "-Ltf8UMe010yUMBuXvY5"; // Retrieve from Users Collection

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String orderDate = dateFormat.format(date);

                String orderStatus = "Pending";

                String NamaPengirim = edtNamaPengirim.getText().toString();
                String TelpPengirim = edtTelpPengirim.getText().toString();
                String EmailPengirim = edtEmailPengirim.getText().toString();

                String NamaPenerima = edtNamaPenerima.getText().toString();
                String TelpPenerima = edtTelpPenerima.getText().toString();
                String EmailPenerima= edtEmailPenerima.getText().toString();

                String AlamatPengirim = edtAlamatPengirim.getText().toString();
                String ProvinsiPengirim = edtProvinsiPengirim.getText().toString();
                String KotaPengirim = edtKotaPengirim.getText().toString();
                String KecamatanPengirim = edtKecamatanPengirim.getText().toString();
                String KodePosPengirim = edtKodePosPengirim.getText().toString();

                String AlamatPenerima= edtAlamatPenerima.getText().toString();
                String ProvinsiPenerima= edtProvinsiPenerima.getText().toString();
                String KotaPenerima= edtKotaPenerima.getText().toString();
                String KecamatanPenerima= edtKecamatanPenerima.getText().toString();
                String KodePosPenerima= edtKodePosPenerima.getText().toString();

                String tglPengiriman = edtTglPengiriman.getText().toString();

                Map<String, Object> mapAlamatPengirim = new HashMap<String, Object>();
                mapAlamatPengirim.put("AddressLine", AlamatPengirim);
                mapAlamatPengirim.put("Provinsi", ProvinsiPengirim);
                mapAlamatPengirim.put("Kota", KotaPengirim);
                mapAlamatPengirim.put("Kecamatan", KecamatanPengirim);
                mapAlamatPengirim.put("KodePos", KodePosPengirim);

                Map<String, Object> mapAlamatPenerima = new HashMap<String, Object>();
                mapAlamatPenerima.put("AddressLine", AlamatPenerima);
                mapAlamatPenerima.put("Provinsi", ProvinsiPenerima);
                mapAlamatPenerima.put("Kota", KotaPenerima);
                mapAlamatPenerima.put("Kecamatan", KecamatanPenerima);
                mapAlamatPenerima.put("KodePos", KodePosPenerima);

                Order dataOrder = new Order(orderID, userID, orderDate, orderStatus, NamaPengirim, EmailPengirim, TelpPengirim, NamaPenerima, EmailPenerima, TelpPenerima, tglPengiriman);

                dbOrder.child(orderID).setValue(dataOrder);
                dbOrder.child(orderID).child("AlamatPengirim").setValue(mapAlamatPengirim);
                dbOrder.child(orderID).child("AlamatPenerima").setValue(mapAlamatPenerima);

                // --------- INPUT ITEM -----------
                String itemID = dbItems.push().getKey();
                String itemName = edtNamaBarang.getText().toString();
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                String unit = spinUnit.getSelectedItem().toString();
                float width = Float.parseFloat(edtWidth.getText().toString());
                float length = Float.parseFloat(edtLength.getText().toString());
                float height = Float.parseFloat(edtHeight.getText().toString());
                float weight = Float.parseFloat(edtWeight.getText().toString());
                float volume = width * length * height;

                if(cbFragile.isChecked()){
                    isFragile = true;
                }

                Items dataItems = new Items(itemID, orderID, itemName, quantity, unit, width, length, height, weight, volume, isFragile);
                try{
                    dbItems.child(itemID).setValue(dataItems);
                    progressBar3.setVisibility(View.VISIBLE);

                    // Sementara aja, nanti yang bener direct ke activity pembayaran.
                    startActivity(new Intent(OrderFormActivity.this, HomeActivity.class));
                    Toast.makeText(getApplicationContext(), "Order has been created. Please wait for the confirmation", Toast.LENGTH_LONG).show();
                }catch(Exception ex){
                    Toast.makeText(OrderFormActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == image_id && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            previewimage.setImageURI(selectedImage);
        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        /*
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String orderDate = dateFormat.format(date);
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDateString = dateFormat.format(calendar.getTime());

        edtTglPengiriman.setText(currentDateString);
    }
}
