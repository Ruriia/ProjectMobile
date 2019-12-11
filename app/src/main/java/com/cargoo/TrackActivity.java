package com.cargoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TrackActivity extends AppCompatActivity {

    private RecyclerView rvPickingList;
    private List<Items> itemLst = new ArrayList();
    private FirebaseRecyclerAdapter<Items, ItemHolder> pickingListAdapter;

    private String orderID;
    public String driverID;

    // Elemen yang narik data TRANSACTION
    private TextView txtTransactionID, txtDeliveryStatus;

    // Elemen yang narik data DRIVER
    private TextView txtDriverName, txtActionCall;

    // Elemen yang narik data ORDER
    private TextView txtPickupDate, txtPickupAddress, txtDestAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        (findViewById(R.id.btnBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Elemen yang narik data TRANSACTION
        txtTransactionID = findViewById(R.id.txtTransactionID);
        txtDeliveryStatus = findViewById(R.id.txtDeliveryStatus);

        // Elemen yang narik data DRIVER
        txtDriverName = findViewById(R.id.txtDriverName);
        txtActionCall = findViewById(R.id.txtActionCall);

        // Elemen yang narik data ORDER
        txtPickupDate = findViewById(R.id.txtPickupDate);
        txtPickupAddress = findViewById(R.id.txtPickupAddress);
        txtDestAddress = findViewById(R.id.txtDestAddress);

        // Getting orderID
        Intent i = getIntent();
        orderID = i.getStringExtra("orderID");

        // ------------------------------------ RETRIEVE ORDER DATA --------------------------------------
        DatabaseReference dbOrder = FirebaseDatabase.getInstance().getReference().child("Orders").child(orderID);
        dbOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // ------------------------------------ SET ORDER DATA ----------------------------------------------
                String refDestName = dataSnapshot.child("namaPenerima").getValue(String.class);
                String refDestAddress = dataSnapshot.child("AlamatPenerima").child("Full").getValue(String.class);
                String refDestFullAddress = refDestName + ", " + refDestAddress;
                txtDestAddress.setText(refDestFullAddress);

                String refPickupName = dataSnapshot.child("namaPengirim").getValue(String.class);
                String refPickupAddress = dataSnapshot.child("AlamatPengirim").child("Full").getValue(String.class);
                String refPickupFullAddress = refPickupName + ", " + refPickupAddress;
                txtPickupAddress.setText(refPickupFullAddress);

                String refPickupDate = dataSnapshot.child("deliveryDate").getValue(String.class);
                txtPickupDate.setText(refPickupDate);
                // --------------------------------- END OF SET ORDER DATA -----------------------------------------

                Log.d("KeberadaanData", "Data Ada");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("KeberadaanData", "Data Gak Ada");
            }
        });
        // ------------------------------------ END OF RETRIEVE ORDER DATA --------------------------------------

        // ----------------------------------- RETRIEVE TRANSACTION DATA -----------------------------------------
        Query qTransaction = FirebaseDatabase.getInstance().getReference().child("Transactions")
                .orderByChild("TransactionID")
                .equalTo(orderID);

        qTransaction.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // ------------------------------------ SET TRANSACTION DATA -----------------------------------
                String refTransactionID = dataSnapshot.child(orderID).child("TransactionID").getValue(String.class);
                txtTransactionID.setText(refTransactionID);

                String refTransactionStatus = dataSnapshot.child(orderID).child("TransactionStatus").getValue(String.class);
                txtDeliveryStatus.setText(refTransactionStatus);

                driverID = dataSnapshot.child(orderID).child("DriverID").getValue(String.class);

                getDriverInfo(driverID);
            }

            private void getDriverInfo(final String driverID) {
                Query qDriver = FirebaseDatabase.getInstance().getReference().child("Drivers")
                        .orderByChild("DriverID")
                        .equalTo(driverID);

                qDriver.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String refDriverName = dataSnapshot.child(driverID).child("DriverName").getValue(String.class);
                        txtDriverName.setText(refDriverName);

                        final String refDriverPhone = dataSnapshot.child(driverID).child("DriverPhone").getValue(String.class);

                        txtActionCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Uri call = Uri.parse("tel: " + refDriverPhone);
                                Intent i = new Intent(Intent.ACTION_DIAL, call);
                                startActivity(i);
                                // Toast.makeText(TrackActivity.this, "Driver Phone: " + refDriverPhone, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // ----------------------------------- END OF RETRIEVE TRANSACTION DATA -----------------------------------------

        // -------------------------------- RETRIEVE ITEM DATA (Call function) -----------------------------------------

        // Init view
        rvPickingList = findViewById(R.id.rvPickingList);
        rvPickingList.setLayoutManager(new LinearLayoutManager(this));
        retrieveItemData();
        setItemData();
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

        pickingListAdapter = new FirebaseRecyclerAdapter<Items, ItemHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Items model) {
//                holder.setIsRecyclable(false);
                holder.setItemName(model.getItemName());
                holder.setQuantity(model.getQuantity());
                holder.setUnit(model.getUnit());
                holder.setVolume(model.getVolume());
                holder.setWeight(model.getWeight());
            }

            @NonNull
            @Override
            public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.picking_list, parent, false);

                return new ItemHolder(view);
            }
        };

        rvPickingList.setAdapter(pickingListAdapter);
    }

    @Override
    protected void onStart() {
        if(pickingListAdapter != null) {
            Log.d("KeberadaanData", "Datanya ada untuk untuk ID order: " + orderID);
            pickingListAdapter.startListening();
        }else{
            Toast.makeText(getApplicationContext(), "Data kosong", Toast.LENGTH_LONG).show();
            Log.d("KeberadaanData", "No Data");
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(pickingListAdapter != null)
            pickingListAdapter.stopListening();
        super.onStop();
    }

    private static class ItemHolder extends RecyclerView.ViewHolder{
        View mView;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setItemName(String itemName){
            TextView txtItemName = mView.findViewById(R.id.txtItemName);
            txtItemName.setText(itemName);
        }

        public void setQuantity(int quantity) {
            TextView txtQuantity = mView.findViewById(R.id.txtQty);
            txtQuantity.setText(String.valueOf(quantity));
        }

        public void setVolume(float volume) {
            TextView txtVolume = mView.findViewById(R.id.txtVolume);
            txtVolume.setText(String.valueOf(volume));
        }

        public void setWeight(float weight) {
            TextView txtWeight = mView.findViewById(R.id.txtWeight);
            txtWeight.setText(String.valueOf(weight));
        }

        public void setUnit(String unit) {
            TextView txtUnit = mView.findViewById(R.id.txtUnit);
            txtUnit.setText(String.valueOf(unit));
        }
    }
}
