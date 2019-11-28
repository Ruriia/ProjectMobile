package com.cargoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private RecyclerView rvItem;
    private List<Items> itemLst = new ArrayList();
    private FirebaseRecyclerAdapter<Items, ItemHolder> itemCheckoutAdapter;

    private String orderID;

    private TextView txtDestAddress, txtPickupAddress, txtPickupDate, txtDeliveryPrices, txtTotalPrices;

    private String refDestName, refDestAddress, refDestFullAddress, refPickupName, refPickupAddress, refPickupFullAddress, refPickupDate;
    private String refDeliveryPrices, refTotalPrices;

    private Button btnPayment, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Init view
        rvItem = findViewById(R.id.rvItem);
//        rvItem.setHasFixedSize(true);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        txtDestAddress = findViewById(R.id.txtDestAddress);
        txtPickupAddress = findViewById(R.id.txtPickupAddress);
        txtPickupDate = findViewById(R.id.txtPickupDate);
        txtDeliveryPrices = findViewById(R.id.txtDeliveryPrices);
        txtTotalPrices = findViewById(R.id.txtTotalPrices);

        Intent i = getIntent();
        orderID = i.getStringExtra("orderID");

//        Toast.makeText(getApplicationContext(), "Order ID: " + orderID, Toast.LENGTH_LONG).show();

        // Retrieve Item data
        retrieveItemData();

        // Set Item data
        setItemData();

        // ------------- Retrieve Delivery Data -------------
        Query qDeliveryCheckout = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderID);

        qDeliveryCheckout.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(CheckoutActivity.this, dataSnapshot.child("namaPenerima").getValue().toString(), Toast.LENGTH_LONG).show();
                refDestName = dataSnapshot.child("namaPenerima").getValue(String.class);
                refDestAddress = dataSnapshot.child("AlamatPenerima").child("Full").getValue(String.class);
                refDestFullAddress = refDestName + ", " + refDestAddress;

                refPickupName = dataSnapshot.child("namaPengirim").getValue(String.class);
                refPickupAddress = dataSnapshot.child("AlamatPengirim").child("Full").getValue(String.class);
                refPickupFullAddress = refPickupName + ", " + refPickupAddress;

                refPickupDate = dataSnapshot.child("deliveryDate").getValue(String.class);

                refDeliveryPrices = "Rp " + String.valueOf(dataSnapshot.child("deliveryPrice").getValue(Integer.class));
                refTotalPrices = "Rp " + String.valueOf(dataSnapshot.child("totalPrice").getValue(Integer.class));

                txtDestAddress.setText(refDestFullAddress);
                txtPickupAddress.setText(refPickupFullAddress);
                txtPickupDate.setText(refPickupDate);
                txtDeliveryPrices.setText(refDeliveryPrices);
                txtTotalPrices.setText(refTotalPrices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnPayment = findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CheckoutActivity.this, activity_order_complete.class);
//                i.putExtra("totalPrice", refTotalPrices);
                i.putExtra("orderID", orderID);
                startActivity(i);
                finish();
            }
        });
    }

    private void retrieveItemData() {
        itemLst.clear();

        DatabaseReference dbItem = FirebaseDatabase.getInstance().getReference().child("Items");

        dbItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemSnapShot: dataSnapshot.getChildren()){
                    Items items = itemSnapShot.getValue(Items.class);
                    itemLst.add(items);
                    Log.d("CheckDatabase", "Database ada");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("CheckDatabase", "" + databaseError.getMessage());
            }
        });
    }

    private void setItemData() {
        Query qCheckoutItem = FirebaseDatabase.getInstance().getReference().child("Items")
                .orderByChild("orderID")
                .equalTo(orderID);

        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(qCheckoutItem, Items.class)
                        .build();

        itemCheckoutAdapter = new FirebaseRecyclerAdapter<Items, ItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Items model) {
//                holder.setIsRecyclable(false);
                holder.setItemName(model.getItemName());
                holder.setQuantity(model.getQuantity());
                holder.setFragileStatus(model.isFragileStatus());
                holder.setItemPrice(model.getItemPrice());
            }

            @NonNull
            @Override
            public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_checkout_list, parent, false);

                return new ItemHolder(view);
            }
        };

        rvItem.setAdapter(itemCheckoutAdapter);

    }

    @Override
    protected void onStart() {
        if(itemCheckoutAdapter != null) {
            Log.d("KeberadaanData", "Datanya ada untuk untuk ID order: " + orderID);
            itemCheckoutAdapter.startListening();
        }else{
            Toast.makeText(getApplicationContext(), "Data kosong", Toast.LENGTH_LONG).show();
            Log.d("KeberadaanData", "No Data");
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(itemCheckoutAdapter != null)
            itemCheckoutAdapter.stopListening();
        super.onStop();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{
        View mView;
        public ItemHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setItemName(String itemName){
            TextView txtItemName = mView.findViewById(R.id.txtItemName);
            txtItemName.setText(itemName);
        }

        public void setQuantity(int quantity){
            TextView txtQuantity = mView.findViewById(R.id.txtQty);
            txtQuantity.setText(String.valueOf(quantity));
        }

        public void setFragileStatus(boolean isFragile){
            TextView txtFragile = mView.findViewById(R.id.txtFragile);

            if(isFragile){
                txtFragile.setText("Fragile");
            }else{
                txtFragile.setText("");
            }
        }

        public void setItemPrice(int itemPrice){
            TextView txtItemPrice = mView.findViewById(R.id.txtItemPrice);
            txtItemPrice.setText(String.valueOf(itemPrice));
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ActivityLoader.class));
        finish();
    }
}
