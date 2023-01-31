package com.example.vehiclefinanceprocessing;


import android.os.Bundle;

import com.example.vehiclefinanceprocessing.databinding.ActivityApplicationBinding;

public class ApplicationActivity extends DrawerBaseActivity {

    ActivityApplicationBinding activityBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_application);
        activityBinding = ActivityApplicationBinding.inflate(getLayoutInflater());

        setContentView(activityBinding.getRoot());
        AllocateTitle("Application");
    }
}