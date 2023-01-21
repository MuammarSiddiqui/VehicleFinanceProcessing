package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
        String Id = (shared.getString("Id", ""));
        String Role = (shared.getString("Role", ""));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Role != "" && Id !=  ""){

                startActivity(new Intent(MainActivity.this,VehicleActivity.class));
                finish();
                }else{
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            }
        },4000);


    }
//    @Override
//    public void onBackPressed() {
//        return;
//    }
}