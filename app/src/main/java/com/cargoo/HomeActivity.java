package com.cargoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends Fragment {

    Context context;
    private TextView txtCompanyName;
    private String refCompanyName;

    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private FirebaseUser fbUser = fbAuth.getInstance().getCurrentUser();
    private String fbUserId = fbUser.getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        DatabaseReference dbUser;

        CardView cvDelivery;
        Button btnDelivery;

        View v =  inflater.inflate(R.layout.activity_home, container, false);

        Button btnCheckAllProducts;
        Button btnCheckAllServices;

        btnCheckAllProducts = v.findViewById(R.id.btnCheckAllProducts);
        btnCheckAllProducts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.framefragment, new ProductPage());
                fr.commit();
            }
        });

        btnCheckAllServices = v.findViewById(R.id.btnCheckAllServices);
        btnCheckAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.framefragment, new ServiceActivity());
                fr.commit();
            }
        });

        txtCompanyName = v.findViewById(R.id.txtCompanyName);

        Query q1 = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("userID")
                .equalTo(fbUserId);

        q1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refCompanyName = dataSnapshot.child(fbUserId).child("name").getValue(String.class);
                txtCompanyName.setText(refCompanyName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        cvDelivery = v.findViewById(R.id.cvDelivery);
        cvDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OrderFormActivity.class));
            }
        });

        btnDelivery = v.findViewById(R.id.btnDelivery);
        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OrderFormActivity.class));
            }
        });
        return v;
    }

}
