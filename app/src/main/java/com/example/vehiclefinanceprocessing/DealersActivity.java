package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vehiclefinanceprocessing.databinding.ActivityDealersBinding;
import com.example.vehiclefinanceprocessing.databinding.ActivityUsersBinding;

public class DealersActivity extends DrawerBaseActivity {

    ActivityDealersBinding activityBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dealers);
        activityBinding = ActivityDealersBinding.inflate(getLayoutInflater());

        setContentView(activityBinding.getRoot());
        AllocateTitle("Dealers");
    }
}