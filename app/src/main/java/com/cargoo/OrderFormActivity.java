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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class OrderFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final int image_id = 1;
    Button upload, btnDone;
    TextView imagename;
    ImageView previewimage;

    private EditText edtNamaPengirim, edtTelpPengirim, edtEmailPengirim, edtAlamatPengirim, edtProvinsiPengirim, edtKotaPengirim, edtKecamatanPengirim, edtKodePosPengirim;
    private EditText edtNamaPenerima, edtTelpPenerima, edtEmailPenerima, edtAlamatPenerima, edtProvinsiPenerima, edtKotaPenerima, edtKecamatanPenerima, edtKodePosPenerima;
    private EditText edtTglPengiriman;
    private String userID;

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
        edtAlamatPengirim = findViewById(R.id.edtAlamatPengirim);
        edtProvinsiPengirim = findViewById(R.id.edtProvinsiPengirim);
        edtKotaPengirim = findViewById(R.id.edtKotaPengirim);
        edtKecamatanPengirim = findViewById(R.id.edtKecamatanPengirim);
        edtKodePosPengirim = findViewById(R.id.edtKodePosPengirim);

        dbOrder = FirebaseDatabase.getInstance().getReference().child("Orders");

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

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderID = dbOrder.push().getKey();
                userID = "-Ltf8UMe010yUMBuXvY5";

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
                String orderDate = dateFormat.format(date);

                String orderStatus = "Pending";
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

        String currentDateString = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());

        edtTglPengiriman.setText(currentDateString);
    }
}
