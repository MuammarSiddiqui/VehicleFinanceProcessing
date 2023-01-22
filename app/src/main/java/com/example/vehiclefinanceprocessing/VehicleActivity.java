package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.vehiclefinanceprocessing.databinding.ActivityVehicleBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VehicleActivity extends DrawerBaseActivity {

    ActivityVehicleBinding activityBinding;
    ActivityVehicleBinding gridBinding;
    ImageSlider imgSlider ;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    FloatingActionButton fb;
    String[] choises ={"Edit Item","View Item"};
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
        String[] carIds={"IdCivic","IdVigo","IdBMW","IdCivic","IdVigo","IdBMW"};
        String[] carPrice={"800000","1000000","1200000","800000","1000000","1200000"};
        int[] Carimages = {R.mipmap.slider1,R.mipmap.slider2,R.mipmap.slider3,R.mipmap.slider1,R.mipmap.slider2,R.mipmap.slider3};
        GridAdapter adapter = new GridAdapter(VehicleActivity.this,carnames,carPrice,Carimages,carIds);
        gridBinding.CarGridView.setAdapter(adapter);
        gridBinding.CarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                builder =new AlertDialog.Builder(VehicleActivity.this);
                builder.setTitle(carnames[i]+" selected");
                builder.setIcon(Carimages[i]);


                builder.setSingleChoiceItems(choises, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(VehicleActivity.this, choises[i]+"was clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog= builder.create();
                dialog.show();

            }
        });
        fb = findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddCarDialog();
                Toast.makeText(VehicleActivity.this, "Fb was clicked", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showAddCarDialog() {
        final Dialog dialog= new Dialog(VehicleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_car_dialog);
        EditText carName = dialog.findViewById(R.id.txtCarName);
        EditText carPrice = dialog.findViewById(R.id.txtCarPrice);
        EditText carDescription = dialog.findViewById(R.id.txtCarDescription);
        EditText carMilage = dialog.findViewById(R.id.txtCarMilage);
        EditText carType = dialog.findViewById(R.id.txtCarType);
        Button btnSubmit =dialog.findViewById(R.id.btnSubmit);
        Button btnCancel =dialog.findViewById(R.id.btnCancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VehicleActivity.this, "Submit button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}