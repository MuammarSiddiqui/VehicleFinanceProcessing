package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.TimeUnit;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout layout;
    AlertDialog dialog;
    AlertDialog.Builder builder;

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

        layout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.NavCars:

                startActivity(new Intent(this,VehicleActivity.class));
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                break;
            case R.id.NavUsers:

                startActivity(new Intent(this,UsersActivity.class));

                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                break;
            case R.id.NavApplications:

                startActivity(new Intent(this,ApplicationActivity.class));

                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

                break;
            case R.id.NavDealers:
                startActivity(new Intent(this,DealersActivity.class));
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                break;
            case R.id.NavLogout:
                builder = new AlertDialog.Builder(DrawerBaseActivity.this);
                builder.setTitle("Are you sure want to logout??");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(DrawerBaseActivity.this);
//                        SharedPreferences.Editor editor = pref.edit();
                        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("Id" ,"");
                        editor.putString("Role" ,"");
                        editor.putString("Email" ,"");
                        editor.putString("Name","");
                        editor.apply();
                        startActivity(new Intent(DrawerBaseActivity.this,LoginActivity.class));
                        overridePendingTransition(1,1);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
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