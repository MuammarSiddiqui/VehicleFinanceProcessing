package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout layout;

    @Override
    public void setContentView(View view) {
        layout=(DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout Container =layout.findViewById(R.id.activityContainer);
        Container.addView(view);
        super.setContentView(layout);

        Toolbar toolbar = layout.findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        NavigationView navview = layout.findViewById(R.id.nav_view);
        navview.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toogle =new ActionBarDrawerToggle(this,layout,toolbar,R.string.drawerOpen,R.string.drawerClosed);
        layout.addDrawerListener(toogle);
        toogle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.NavCars:
                startActivity(new Intent(this,VehicleActivity.class));
                overridePendingTransition(0,0);
                break;
        }
        return false;
    }
    protected  void AllocateTitle(String title){
        if (getSupportActionBar()!= null){
            getSupportActionBar().setTitle(title);
        }
    }
}