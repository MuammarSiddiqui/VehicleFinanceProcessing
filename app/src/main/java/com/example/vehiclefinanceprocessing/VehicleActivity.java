package com.example.vehiclefinanceprocessing;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.vehiclefinanceprocessing.databinding.ActivityVehicleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class VehicleActivity extends DrawerBaseActivity {

    ActivityVehicleBinding activityBinding;
    ActivityVehicleBinding gridBinding;
    ImageSlider imgSlider ;
    ImageButton img;
    AlertDialog dialog;
    Uri ImageUri;
    AlertDialog.Builder builder;
    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("Cars");
    StorageReference storage= FirebaseStorage.getInstance().getReference();
    FloatingActionButton fb;
    String[] choises ={"Edit Item","View Item"};
    LoadingDialog loader;
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
        loader = new LoadingDialog(VehicleActivity.this);

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
        img= dialog.findViewById(R.id.carImageButton);
        Button btnSubmit =dialog.findViewById(R.id.btnSubmit);
        Button btnCancel =dialog.findViewById(R.id.btnCancel);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            String name = carName.getText().toString().trim();
            String price = carPrice.getText().toString().trim();
            String description = carDescription.getText().toString().trim();
            String milage = carMilage.getText().toString().trim();
            String cartype = carType.getText().toString().trim();
            @Override
            public void onClick(View view) {
               if(name.length()==0){
                   carName.setError("Car name is required");
                   Toast.makeText(VehicleActivity.this, "Name is required", Toast.LENGTH_SHORT).show();
               }else{
                   carName.setError(null);
               }
               if (price.length() ==0){
                   carPrice.setError("Car price is required");
                   Toast.makeText(VehicleActivity.this, "price is required", Toast.LENGTH_SHORT).show();
               }else{
                   carPrice.setError(null);
               }if(ImageUri == null){
                    Toast.makeText(VehicleActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }
               if(name.length()>0 && carPrice.length()>0 && ImageUri != null){
                   String UniqueId = UUID.randomUUID().toString();
                   loader.StartloadingDialog();
                   Boolean check = AddData(UniqueId,name,price,description,milage,cartype,ImageUri);
                   if (check){
                       loader.dismissDialog();
                       Toast.makeText(VehicleActivity.this, "Data Added Succesfully", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(VehicleActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                   }
               }


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

    private Boolean AddData(String uniqueId, String name, String price, String description, String milage, String cartype, Uri imageUri) {
        try {
            StorageReference fileref= storage.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));
            fileref.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Cars car = new Cars(uniqueId,name,uri.toString(),milage,cartype,description,"Active",price);
                            db.child(uniqueId).setValue(car);
                        }
                    });
                }
                
            });
            return true;
        }catch (Exception ex){
return false;
        }
    }

    private String getFileExtention(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(imageUri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode == RESULT_OK && data!= null){
            ImageUri = data.getData();
            img.setImageURI(ImageUri);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}