package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.vehiclefinanceprocessing.databinding.ActivityVehicleBinding;

import java.util.ArrayList;

public class VehicleActivity extends DrawerBaseActivity {

    ActivityVehicleBinding activityBinding;
    ActivityVehicleBinding gridBinding;
    ImageSlider imgSlider ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding =gridBinding=ActivityVehicleBinding.inflate(getLayoutInflater());

        setContentView(activityBinding.getRoot());
        AllocateTitle("Cars");
        imgSlider = findViewById(R.id.imgSlider);
        ArrayList<SlideModel>  images = new ArrayList<>();
        images.add(new SlideModel(R.mipmap.slider1,null));
        images.add(new SlideModel(R.mipmap.slider2,null));
        images.add(new SlideModel(R.mipmap.slider3,null));

        imgSlider.setImageList(images, ScaleTypes.CENTER_CROP);


        String[] carnames={"Civic","Vigo","BMW","Civic","Vigo","BMW"};
        String[] carPrice={"800000","1000000","1200000","800000","1000000","1200000"};
        int[] Carimages = {R.mipmap.slider1,R.mipmap.slider2,R.mipmap.slider3,R.mipmap.slider1,R.mipmap.slider2,R.mipmap.slider3};
        GridAdapter adapter = new GridAdapter(VehicleActivity.this,carnames,carPrice,Carimages);
        gridBinding.CarGridView.setAdapter(adapter);
        gridBinding.CarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(VehicleActivity.this, carnames[i]+"was clicked", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }
}