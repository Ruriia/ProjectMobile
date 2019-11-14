package com.cargoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityLoader extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        loadFragment(new HomeActivity());

        BottomNavigationView botnav = findViewById(R.id.bottom_navigation);
        ImageButton track = findViewById(R.id.btnTrack);
        track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trackpage = new Intent(getApplicationContext(), TrackLists.class);
                startActivity(trackpage);
            }
        });

        botnav.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(getIntent().getExtras());
            ft.replace(R.id.framefragment, fragment);
            ft.commit();
            return true;
        }
        return false;
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.action_home:
                fragment = new HomeActivity();
                break;
            case R.id.action_product:
                fragment = new ProductPage();
                break;
            case R.id.action_services:
                fragment = new ServiceActivity();
                break;
            case R.id.action_account:
                fragment = new AccountActivity();
                break;
        }
        return loadFragment(fragment);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
