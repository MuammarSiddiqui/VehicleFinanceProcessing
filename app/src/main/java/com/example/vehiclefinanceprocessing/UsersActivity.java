package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.vehiclefinanceprocessing.databinding.ActivityUsersBinding;
import com.example.vehiclefinanceprocessing.databinding.ActivityVehicleBinding;

public class UsersActivity extends DrawerBaseActivity {

    ActivityUsersBinding activityBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityUsersBinding.inflate(getLayoutInflater());

        setContentView(activityBinding.getRoot());
        AllocateTitle("Users");

    }
}