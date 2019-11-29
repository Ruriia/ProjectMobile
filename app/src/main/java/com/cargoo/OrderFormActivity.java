package com.cargoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OrderFormActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final int image_id = 1;
    Button upload, btnDone;
    ImageView previewimage;
    Uri selectedImage;

    private EditText edtNamaPengirim, edtTelpPengirim, edtEmailPengirim, edtAlamatPengirim, edtProvinsiPengirim, edtKotaPengirim, edtKecamatanPengirim, edtKodePosPengirim;
    private EditText edtNamaPenerima, edtTelpPenerima, edtEmailPenerima, edtAlamatPenerima, edtProvinsiPenerima, edtKotaPenerima, edtKecamatanPenerima, edtKodePosPenerima;
    private EditText edtTglPengiriman;

    private EditText edtNamaBarang, edtQuantity, edtWeight, edtWidth, edtLength, edtHeight;
    private Spinner spinUnit;
    private CheckBox cbFragile;

    private boolean isFragile = false;

    private DatabaseReference dbOrder, dbItems, dbUsers;

    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser = fbAuth.getInstance().getCurrentUser();
    private String fbUserId = fbUser.getUid();

    private int weightPrice = 2000; // Untuk harga per kg
    private int volumePrice = 3000; // Untuk harga per m3
    private int deliveryCost = 5000; // Untuk harga per km
    private int itemPrice = 0;

    private String orderID;
    private int totalPrice = 0;
    private int totalWeight = 0;
    private int totalVolume = 0;


    // Initiate Form Auto-Fill
    private String refNamaPengirim, refTelpPengirim, refEmailPengirim, refAlamatPengirim, refProvinsiPengirim, refKotaPengirim, refKecamatanPengirim, refKodePosPengirim;


    private String uploadImage(){
        if(selectedImage != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference mStorageRef = firebaseStorage.getReference();;
            String path = "orderimage/" + UUID.randomUUID().toString() + "." + getExtension(selectedImage);
            StorageReference ref = mStorageRef.child(path);
            ref.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            return path;
        }else {
            return null;
        }
    }

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
        edtEmailPenerima = findViewById(R.id.edtEmailPenerima);

        edtAlamatPengirim = findViewById(R.id.edtAlamatPengirim);
        edtProvinsiPengirim = findViewById(R.id.edtProvinsiPengirim);
        edtKotaPengirim = findViewById(R.id.edtKotaPengirim);
        edtKecamatanPengirim = findViewById(R.id.edtKecamatanPengirim);
        edtKodePosPengirim = findViewById(R.id.edtKodePosPengirim);

        edtAlamatPenerima = findViewById(R.id.edtAlamatPenerima);
        edtProvinsiPenerima = findViewById(R.id.edtProvinsiPenerima);
        edtKotaPenerima = findViewById(R.id.edtKotaPenerima);
        edtKecamatanPenerima = findViewById(R.id.edtKecamatanPenerima);
        edtKodePosPenerima = findViewById(R.id.edtKodePosPenerima);

        edtNamaBarang = findViewById(R.id.edtNamaBarang);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtWeight = findViewById(R.id.edtWeight);
        edtWidth = findViewById(R.id.edtWidth);
        edtLength = findViewById(R.id.edtLength);
        edtHeight = findViewById(R.id.edtHeight);

        spinUnit = findViewById(R.id.spinUnit);

        cbFragile = findViewById(R.id.cbFragile);

        final ProgressBar progressBar3 = findViewById(R.id.progressBar3);

        fbAuth = FirebaseAuth.getInstance();

        dbOrder = FirebaseDatabase.getInstance().getReference().child("Orders");
        dbItems = FirebaseDatabase.getInstance().getReference().child("Items");

        // READ USER'S INFO
        dbUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        Query q1 = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("userID")
                .equalTo(fbUserId);

        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                refNamaPengirim = dataSnapshot.child(fbUserId).child("name").getValue(String.class);
                refTelpPengirim = dataSnapshot.child(fbUserId).child("phone").getValue(String.class);
                refEmailPengirim = dataSnapshot.child(fbUserId).child("email").getValue(String.class);
                refAlamatPengirim = dataSnapshot.child(fbUserId).child("address").child("address").getValue(String.class);
                refProvinsiPengirim = dataSnapshot.child(fbUserId).child("address").child("province").getValue(String.class);
                refKotaPengirim = dataSnapshot.child(fbUserId).child("address").child("city").getValue(String.class);
                refKecamatanPengirim = dataSnapshot.child(fbUserId).child("address").child("district").getValue(String.class);
                refKodePosPengirim = String.valueOf(dataSnapshot.child(fbUserId).child("address").child("zipcode").getValue(Integer.class));

                edtNamaPengirim.setText(refNamaPengirim);
                edtEmailPengirim.setText(refEmailPengirim);
                edtTelpPengirim.setText(refTelpPengirim);
                edtAlamatPengirim.setText(refAlamatPengirim);
                edtProvinsiPengirim.setText(refProvinsiPengirim);
                edtKotaPengirim.setText(refKotaPengirim);
                edtKecamatanPengirim.setText(refKecamatanPengirim);
                edtKodePosPengirim.setText(refKodePosPengirim);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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
                OrderFormActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                orderID = dbOrder.push().getKey();
                String userID = fbAuth.getCurrentUser().getUid(); // Retrieve from FB Authentication

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String orderDate = dateFormat.format(date);

                String orderStatus = "Pending";

                String NamaPengirim = edtNamaPengirim.getText().toString();
                String TelpPengirim = edtTelpPengirim.getText().toString();
                String EmailPengirim = edtEmailPengirim.getText().toString();

                String NamaPenerima = edtNamaPenerima.getText().toString();
                String TelpPenerima = edtTelpPenerima.getText().toString();
                String EmailPenerima = edtEmailPenerima.getText().toString();

                String AlamatPengirim = edtAlamatPengirim.getText().toString();
                String ProvinsiPengirim = edtProvinsiPengirim.getText().toString();
                String KotaPengirim = edtKotaPengirim.getText().toString();
                String KecamatanPengirim = edtKecamatanPengirim.getText().toString();
                String KodePosPengirim = edtKodePosPengirim.getText().toString();
                String fullAddrPengirim = AlamatPengirim + ", " + KecamatanPengirim + ", " + KotaPengirim + ", " + ProvinsiPengirim + " " + KodePosPengirim;

                String AlamatPenerima = edtAlamatPenerima.getText().toString();
                String ProvinsiPenerima = edtProvinsiPenerima.getText().toString();
                String KotaPenerima = edtKotaPenerima.getText().toString();
                String KecamatanPenerima = edtKecamatanPenerima.getText().toString();
                String KodePosPenerima = edtKodePosPenerima.getText().toString();
                String fullAddrPenerima = AlamatPenerima + ", " + KecamatanPenerima + ", " + KotaPenerima + ", " + ProvinsiPenerima + " " + KodePosPenerima;

                String tglPengiriman = edtTglPengiriman.getText().toString();

                // ------------------ ITEM ATTRIBUTE --------------
                String itemID = dbItems.push().getKey();
                String itemName = edtNamaBarang.getText().toString();
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                String unit = spinUnit.getSelectedItem().toString();
                float width = (float) Math.ceil(Float.parseFloat(edtWidth.getText().toString())) * quantity;
                float length = (float) Math.ceil(Float.parseFloat(edtLength.getText().toString())) * quantity;
                float height = (float) Math.ceil(Float.parseFloat(edtHeight.getText().toString())) * quantity;
                float weight = (float) Math.ceil(Float.parseFloat(edtWeight.getText().toString())) * quantity;

                float volume = width * length * height;

                if (cbFragile.isChecked()) {
                    isFragile = true;
                }

                // -------------------- CALCULATION ------------------
                itemPrice = ((int) volume * volumePrice) + ((int) weight * weightPrice);

                if(isFragile){
                    itemPrice += 10000;
                }

                LatLng latLngPickup;
                latLngPickup = getGeocode(OrderFormActivity.this, fullAddrPengirim);
                double latPickup = latLngPickup.latitude;
                double lngPickup = latLngPickup.longitude;

                LatLng latLngDest;
                latLngDest = getGeocode(OrderFormActivity.this, fullAddrPenerima);
                double latDest = latLngDest.latitude;
                double lngDest = latLngDest.longitude;

                Location locPickup = new Location("Pickup");
                locPickup.setLatitude(latPickup);
                locPickup.setLongitude(lngPickup);

                Location locDest = new Location("Destination");
                locDest.setLatitude(latDest);
                locDest.setLongitude(lngDest);

                float distance = locPickup.distanceTo(locDest)/1000;

                int deliveryPrice = (int) distance * deliveryCost;

                totalPrice = deliveryPrice + itemPrice;

                totalWeight = (int)weight;
                totalVolume = (int) volume;

                String filepath = uploadImage();


                // --------- INPUT ITEM TO DATABASE -----------
                Items dataItems = new Items(itemID, orderID, itemName, quantity, unit, width, length, height, weight, volume, itemPrice, isFragile, filepath);

                dbItems.child(itemID).setValue(dataItems);
                progressBar3.setVisibility(View.VISIBLE);

                // --------- INPUT ORDER TO DATABASE ----------
                Map<String, Object> mapAlamatPengirim = new HashMap<String, Object>();
                mapAlamatPengirim.put("AddressLine", AlamatPengirim);
                mapAlamatPengirim.put("Provinsi", ProvinsiPengirim);
                mapAlamatPengirim.put("Kota", KotaPengirim);
                mapAlamatPengirim.put("Kecamatan", KecamatanPengirim);
                mapAlamatPengirim.put("KodePos", KodePosPengirim);
                mapAlamatPengirim.put("Full", fullAddrPengirim);

                Map<String, Object> mapAlamatPenerima = new HashMap<String, Object>();
                mapAlamatPenerima.put("AddressLine", AlamatPenerima);
                mapAlamatPenerima.put("Provinsi", ProvinsiPenerima);
                mapAlamatPenerima.put("Kota", KotaPenerima);
                mapAlamatPenerima.put("Kecamatan", KecamatanPenerima);
                mapAlamatPenerima.put("KodePos", KodePosPenerima);
                mapAlamatPenerima.put("Full", fullAddrPenerima);

                Order dataOrder = new Order(orderID, userID, orderDate, orderStatus, NamaPengirim, EmailPengirim, TelpPengirim, NamaPenerima
                        , EmailPenerima, TelpPenerima, tglPengiriman, distance, deliveryPrice, totalWeight, totalVolume, itemPrice, totalPrice);

                dbOrder.child(orderID).setValue(dataOrder);
                dbOrder.child(orderID).child("AlamatPengirim").setValue(mapAlamatPengirim);
                dbOrder.child(orderID).child("AlamatPenerima").setValue(mapAlamatPenerima);

                // ---------------------- EVENT LISTENER --------------------------
                ValueEventListener writeListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar3.setVisibility(View.GONE);
                        // Go to checkout activity here !
                        Intent i = new Intent(OrderFormActivity.this, CheckoutActivity.class);
                        // Add extra orderID & others here
                        i.putExtra("orderID", orderID);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                    }
                };

                dbOrder.addValueEventListener(writeListener);

                totalPrice = 0;
                totalWeight = 0;
                totalVolume = 0;
            }
        });

        Button btnAddItem = findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderFormActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                orderID = dbOrder.push().getKey();
                String userID = fbAuth.getCurrentUser().getUid(); // Retrieve from FB Authentication

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String orderDate = dateFormat.format(date);

                String orderStatus = "Pending";

                String NamaPengirim = edtNamaPengirim.getText().toString();
                String TelpPengirim = edtTelpPengirim.getText().toString();
                String EmailPengirim = edtEmailPengirim.getText().toString();

                String NamaPenerima = edtNamaPenerima.getText().toString();
                String TelpPenerima = edtTelpPenerima.getText().toString();
                String EmailPenerima = edtEmailPenerima.getText().toString();

                String AlamatPengirim = edtAlamatPengirim.getText().toString();
                String ProvinsiPengirim = edtProvinsiPengirim.getText().toString();
                String KotaPengirim = edtKotaPengirim.getText().toString();
                String KecamatanPengirim = edtKecamatanPengirim.getText().toString();
                String KodePosPengirim = edtKodePosPengirim.getText().toString();
                String fullAddrPengirim = AlamatPengirim + ", " + KecamatanPengirim + ", " + KotaPengirim + ", " + ProvinsiPengirim + " " + KodePosPengirim;

                String AlamatPenerima = edtAlamatPenerima.getText().toString();
                String ProvinsiPenerima = edtProvinsiPenerima.getText().toString();
                String KotaPenerima = edtKotaPenerima.getText().toString();
                String KecamatanPenerima = edtKecamatanPenerima.getText().toString();
                String KodePosPenerima = edtKodePosPenerima.getText().toString();
                String fullAddrPenerima = AlamatPenerima + ", " + KecamatanPenerima + ", " + KotaPenerima + ", " + ProvinsiPenerima + " " + KodePosPenerima;

                String tglPengiriman = edtTglPengiriman.getText().toString();

                // ------------------ ITEM ATTRIBUTE --------------
                String itemID = dbItems.push().getKey();
                String itemName = edtNamaBarang.getText().toString();
                int quantity = Integer.parseInt(edtQuantity.getText().toString());
                String unit = spinUnit.getSelectedItem().toString();
                float width = (float) Math.ceil(Float.parseFloat(edtWidth.getText().toString())) * quantity;
                float length = (float) Math.ceil(Float.parseFloat(edtLength.getText().toString())) * quantity;
                float height = (float) Math.ceil(Float.parseFloat(edtHeight.getText().toString())) * quantity;
                float weight = (float) Math.ceil(Float.parseFloat(edtWeight.getText().toString())) * quantity;
                float volume = width * length * height;

                if (cbFragile.isChecked()) {
                    isFragile = true;
                }

                // -------------------- CALCULATION ------------------
                itemPrice = ((int) volume * volumePrice) + ((int) weight * weightPrice);

                if(isFragile){
                    itemPrice += 10000;
                }

                LatLng latLngPickup;
                latLngPickup = getGeocode(OrderFormActivity.this, fullAddrPengirim);
                double latPickup = latLngPickup.latitude;
                double lngPickup = latLngPickup.longitude;

                LatLng latLngDest;
                latLngDest = getGeocode(OrderFormActivity.this, fullAddrPenerima);
                double latDest = latLngDest.latitude;
                double lngDest = latLngDest.longitude;

                Location locPickup = new Location("Pickup");
                locPickup.setLatitude(latPickup);
                locPickup.setLongitude(lngPickup);

                Location locDest = new Location("Destination");
                locDest.setLatitude(latDest);
                locDest.setLongitude(lngDest);

                float distance = locPickup.distanceTo(locDest)/1000;

                int deliveryPrice = (int) distance * deliveryCost;

                totalPrice = deliveryPrice + itemPrice;

                totalWeight = (int)weight;
                totalVolume = (int) volume;

                progressBar3.setVisibility(View.VISIBLE);

                String filepath = uploadImage();

                // --------- INPUT ITEM TO DATABASE -----------
                Items dataItems = new Items(itemID, orderID, itemName, quantity, unit, width, length, height, weight, volume, itemPrice, isFragile, filepath );

                dbItems.child(itemID).setValue(dataItems);

                // --------- INPUT ORDER TO DATABASE ----------
                Map<String, Object> mapAlamatPengirim = new HashMap<String, Object>();
                mapAlamatPengirim.put("AddressLine", AlamatPengirim);
                mapAlamatPengirim.put("Provinsi", ProvinsiPengirim);
                mapAlamatPengirim.put("Kota", KotaPengirim);
                mapAlamatPengirim.put("Kecamatan", KecamatanPengirim);
                mapAlamatPengirim.put("KodePos", KodePosPengirim);
                mapAlamatPengirim.put("Full", fullAddrPengirim);

                Map<String, Object> mapAlamatPenerima = new HashMap<String, Object>();
                mapAlamatPenerima.put("AddressLine", AlamatPenerima);
                mapAlamatPenerima.put("Provinsi", ProvinsiPenerima);
                mapAlamatPenerima.put("Kota", KotaPenerima);
                mapAlamatPenerima.put("Kecamatan", KecamatanPenerima);
                mapAlamatPenerima.put("KodePos", KodePosPenerima);
                mapAlamatPenerima.put("Full", fullAddrPenerima);

                Order dataOrder = new Order(orderID, userID, orderDate, orderStatus, NamaPengirim, EmailPengirim, TelpPengirim, NamaPenerima
                        , EmailPenerima, TelpPenerima, tglPengiriman, distance, deliveryPrice, totalWeight, totalVolume, itemPrice, totalPrice);

                dbOrder.child(orderID).setValue(dataOrder);
                dbOrder.child(orderID).child("AlamatPengirim").setValue(mapAlamatPengirim);
                dbOrder.child(orderID).child("AlamatPenerima").setValue(mapAlamatPenerima);



                // ---------------------- WRITE EVENT LISTENER --------------------------
                ValueEventListener writeListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar3.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(), "Order has been created. First item has added. Now, add new item", Toast.LENGTH_LONG).show();

                        // Go to add new item activity here !
                        Intent i = new Intent(OrderFormActivity.this, addMoreItem.class);
                        i.putExtra("totalPrice", totalPrice);
                        i.putExtra("totalWeight", totalWeight);
                        i.putExtra("totalVolume", totalVolume);
                        i.putExtra("orderID", orderID);
                        i.putExtra("itemPrice", itemPrice);
                        startActivity(i);
                        finish();
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

    public LatLng getGeocode(Context context, String inputtedAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng resLatLng = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(inputtedAddress, 5);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            resLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return resLatLng;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_id && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            previewimage.setImageURI(selectedImage);
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mtm = MimeTypeMap.getSingleton();
        return mtm.getExtensionFromMimeType(cr.getType(uri));
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
