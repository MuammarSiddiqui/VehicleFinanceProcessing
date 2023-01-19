package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class VehicleActivity extends AppCompatActivity {

    ImageSlider imgSlider ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        imgSlider = findViewById(R.id.imgSlider);
        ArrayList<SlideModel>  images = new ArrayList<>();
        images.add(new SlideModel(R.mipmap.slider1,null));
        images.add(new SlideModel(R.mipmap.slider2,null));
        images.add(new SlideModel(R.mipmap.slider3,null));

        imgSlider.setImageList(images, ScaleTypes.CENTER_CROP);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}