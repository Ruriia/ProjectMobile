package com.cargoo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class HomeActivity extends Fragment {

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
        return v;
    }

}
