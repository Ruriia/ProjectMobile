package com.cargoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private List<Order> historyLst = new ArrayList();
    private FirebaseRecyclerAdapter<Order, OrderHolder> historyAdapter;

    private FirebaseAuth fbAuth;
    private FirebaseUser fbUser = fbAuth.getInstance().getCurrentUser();
    private String fbUserId = fbUser.getUid();

    private ProgressBar pbHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Init view
        rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
//        rvHistory.setHasFixedSize(true);

        pbHistory = findViewById(R.id.pbHistory);

        // Retrieve data
        retrieveData();

        // Set data
        setData();
    }

    private void retrieveData() {

        historyLst.clear();

        DatabaseReference dbOrder = FirebaseDatabase.getInstance().getReference().child("Orders");

        dbOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemSnapShot: dataSnapshot.getChildren()){
                    Order order = itemSnapShot.getValue(Order.class);
                    historyLst.add(order);
                    Log.d("CheckDatabase", "Database ada");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("CheckDatabase", "" + databaseError.getMessage());
            }
        });
    }

    private void setData() {
        Query qHistory = FirebaseDatabase.getInstance().getReference().child("Orders")
                .orderByChild("userID")
                .equalTo(fbUserId);

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(qHistory, Order.class)
                        .build();

        historyAdapter = new FirebaseRecyclerAdapter<Order, OrderHolder>(options) {

//            @Override
//            public int getItemViewType(int position) {
//                if(historyLst.get(position).getOrderStatus() == "Pending")
//                    return 1;
//                else if(historyLst.get(position).getOrderStatus() == "Confirmed")
//                    return 2;
//                else
//                    return 0;
//            }

            @Override
            protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull Order model) {
                holder.setOrderID(model.getOrderID());
                holder.setOrderDate(model.getOrderDate());
                holder.setOrderStatus(model.getOrderStatus(), model.getOrderID());
                //holder.setButton(model.getOrderStatus());
                pbHistory.setVisibility(View.GONE);
            }

            @NonNull
            @Override
            public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.history_list, parent, false);

                return new OrderHolder(view);
            }
        };

        rvHistory.setAdapter(historyAdapter);
    }

    @Override
    protected void onStart() {
        if(historyAdapter != null) {
            Log.d("KeberadaanData", "Datanya ada untuk user ID: " + fbUserId);
            historyAdapter.startListening();
        }else{
            Toast.makeText(getApplicationContext(), "Data kosong", Toast.LENGTH_LONG).show();
            Log.d("KeberadaanData", "No Data");
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(historyAdapter != null)
            historyAdapter.stopListening();
        super.onStop();
    }

    public static class OrderHolder extends RecyclerView.ViewHolder{

//        public TextView txtOrderID, txtOrderDate, txtOrderStatus;
//        public Button btnPay, btnTrack;
//
//        public OrderHolder(@NonNull View itemView, String orderStatusNya) {
//            super(itemView);
//
//            if(orderStatusNya == "Pending"){
//                txtOrderID = itemView.findViewById(R.id.txtOrderID);
//                txtOrderDate = itemView.findViewById(R.id.txtOrderTime);
//                txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
//                btnPay = itemView.findViewById(R.id.btnPay);
//            }
//            else if(orderStatusNya == "Confirmed"){
//                txtOrderID = itemView.findViewById(R.id.txtOrderID);
//                txtOrderDate = itemView.findViewById(R.id.txtOrderTime);
//                txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
//                btnTrack = itemView.findViewById(R.id.btnTrack);
//            }
//        }
        private Context mContext;

        View mView;
        public OrderHolder(View itemView){
            super(itemView);
            mView = itemView;
            mContext = itemView.getContext();
        }

        public void setOrderID(String orderID){
            TextView txtOrderID = mView.findViewById(R.id.txtOrderID);
            txtOrderID.setText(orderID);
        }

        public void setOrderDate(String orderDate){
            TextView txtOrderDate = mView.findViewById(R.id.txtOrderTime);
            txtOrderDate.setText(orderDate);
        }

        public void setOrderStatus(String orderStatus, final String orderID){
            TextView txtOrderStatus = mView.findViewById(R.id.txtOrderStatus);
            txtOrderStatus.setText(orderStatus);

            LinearLayout layoutForBtnPay = mView.findViewById(R.id.layoutForBtnPay);
            if(orderStatus.equals("Confirmed")){
                layoutForBtnPay.setVisibility(View.GONE);

                Button btnTrack = mView.findViewById(R.id.btnTrack);
                btnTrack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, TrackActivity.class);
                        // put orderID extra here!

                        mContext.startActivity(i);

                    }
                });
            }
            else{
                Button btnPay = mView.findViewById(R.id.btnPay);
                btnPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, activity_order_complete.class);
                        i.putExtra("orderID", orderID);
                        mContext.startActivity(i);
                    }
                });
            }
        }

        public void setButton(String orderStatus){
        }
    }
}
