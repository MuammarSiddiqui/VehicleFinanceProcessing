package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
public class ContentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_content);
        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);

        String Id = (shared.getString("Id", ""));
        String Role = (shared.getString("Role", ""));
        if (Id.equals("") && Role.equals("")){
            startActivity(new Intent(ContentActivity.this, LoginActivity.class));
            finish();
        }
    }
}