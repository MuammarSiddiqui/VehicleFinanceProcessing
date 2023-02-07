package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;


public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout layout;
    AlertDialog dialog;
    AlertDialog.Builder builder;

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(View view) {

        layout=(DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout Container =layout.findViewById(R.id.activityContainer);
        Container.addView(view);
        super.setContentView(layout);

        Toolbar toolbar = layout.findViewById(R.id.tool);
        setSupportActionBar(toolbar);

        NavigationView navview = layout.findViewById(R.id.nav_view);
        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);

        String Role = (shared.getString("Role", ""));
        if (Role.equals("User")){
            navview.getMenu().clear();
            navview.inflateMenu(R.menu.user_menu);
        } else if (Role.equals("Dealer")) {
            navview.getMenu().clear();
            navview.inflateMenu(R.menu.user_menu);
        }
        navview.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toogle =new ActionBarDrawerToggle(this,layout,toolbar,R.string.drawerOpen,R.string.drawerClosed);
        layout.addDrawerListener(toogle);
        toogle.syncState();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        layout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case  R.id.NavManageAccount:
                startActivity(new Intent(this,ManageMyAccount.class));
                break;
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
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("Id" ,"");
                    editor.putString("Role" ,"");
                    editor.putString("Email" ,"");
                    editor.putString("Name","");
                    editor.apply();
                    startActivity(new Intent(DrawerBaseActivity.this,LoginActivity.class));
                    overridePendingTransition(1,1);
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> dialog.dismiss());
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