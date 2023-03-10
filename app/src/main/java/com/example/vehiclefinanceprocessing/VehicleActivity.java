package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.vehiclefinanceprocessing.databinding.ActivityVehicleBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

public class VehicleActivity extends DrawerBaseActivity {

    ActivityVehicleBinding activityBinding;
    ActivityVehicleBinding gridBinding;
    ImageSlider imgSlider ;
    ImageView img;
    ImageView iview;


    AlertDialog dialog;
    boolean test = false;
    AlertDialog.Builder builder;
    Uri ImageUri;
    DatabaseReference db=FirebaseDatabase.getInstance().getReference("Cars");
    DatabaseReference dbApplication=FirebaseDatabase.getInstance().getReference("Applications");
    StorageReference storage= FirebaseStorage.getInstance().getReference();
    FloatingActionButton fb;
    String[] choises ={"Edit Item","View Item"};
    LoadingDialog loader;

    ArrayList<Cars> arr = new ArrayList<>();
    CarsListAdapter myadp;
    String Role;


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


        loader.StartloadingDialog();
        myadp  = new CarsListAdapter(VehicleActivity.this,arr);
        gridBinding.CarGridView.setAdapter(myadp);
        
        GetData();
        loader.dismissDialog();
        gridBinding.CarGridView.setOnItemClickListener((adapterView, view, i, l) -> {

            SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
            Role = (shared.getString("Role", ""));
            if(Role.equals("Admin")){
            builder =new AlertDialog.Builder(VehicleActivity.this);
            builder.setTitle(arr.get(i).getName() +" selected");
                builder.setSingleChoiceItems(choises, -1, (dialogInterface, j) -> {
                    switch (choises[j]){
                        case "View Item":
                            dialog.dismiss();
                            showSingleCarDialog(i);
                            break;
                        case "Edit Item":
                            dialog.dismiss();
                            showEditCarDialog(i);
                    }
                });
            builder.setNegativeButton("Cancel", (dialogInterface, i1) -> dialog.dismiss());
            dialog= builder.create();
            dialog.show();
            }else{
                showSingleCarDialog(i);
            }

        });
        fb = findViewById(R.id.fb);
        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
        Role = (shared.getString("Role", ""));
        if(!Role.equals("Admin") ){
            fb.setVisibility(View.GONE);
        }
        fb.setOnClickListener(view -> showAddCarDialog());

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
        img = dialog.findViewById(R.id.carImageView);
        Button btnSubmit =dialog.findViewById(R.id.btnSubmit);
        Button btnCancel =dialog.findViewById(R.id.btnCancel);
        img.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        });

        btnSubmit.setOnClickListener(view -> {
            String name = carName.getText().toString().trim();
            String price = carPrice.getText().toString().trim();
            String description = carDescription.getText().toString().trim();
            String milage = carMilage.getText().toString().trim();
            String cartype = carType.getText().toString().trim();
            boolean nameErr, priceErr, imageErr;
           if(name.equals("")){
               carName.setError("Car name is required");
               nameErr = false;
           }else{
               carName.setError(null);
               nameErr = true;
           }
           if (price.equals("")){
               carPrice.setError("Car price is required");
               priceErr = false;
           }else{
               carPrice.setError(null);
               priceErr = true;
           }
            imageErr = ImageUri != null;
           if((nameErr && priceErr && imageErr)){
               String UniqueId = UUID.randomUUID().toString();
               loader.StartloadingDialog();
               Boolean check = AddData(UniqueId,name,price,description,milage,cartype,ImageUri);
               if (check){
                   loader.dismissDialog();
                   dialog.dismiss();
                   Toast.makeText(VehicleActivity.this, "Data Added Succesfully", Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(VehicleActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
               }
           }


        });
        btnCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showEditCarDialog(int i) {
        final Dialog dialog= new Dialog(VehicleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.add_car_dialog);
        TextView tw = dialog.findViewById(R.id.twHeading);
        EditText carName = dialog.findViewById(R.id.txtCarName);
        EditText carPrice = dialog.findViewById(R.id.txtCarPrice);
        EditText carDescription = dialog.findViewById(R.id.txtCarDescription);
        EditText carMilage = dialog.findViewById(R.id.txtCarMilage);
        EditText carType = dialog.findViewById(R.id.txtCarType);
        img = dialog.findViewById(R.id.carImageView);
        Button btnSubmit =dialog.findViewById(R.id.btnSubmit);
        Button btnCancel =dialog.findViewById(R.id.btnCancel);
        tw.setText("Edit Car");
        carName.setText(arr.get(i).getName());
        carPrice.setText(arr.get(i).getPrice());
        carDescription.setText(arr.get(i).getDescription());
        carMilage.setText(arr.get(i).getMilage());
        carType.setText(arr.get(i).getFuelType());
        Picasso.get().load(arr.get(i).getImage()).into(img);
        img.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        });
            btnSubmit.setText("Update Car");
        btnSubmit.setOnClickListener(view -> {
           // Toast.makeText(VehicleActivity.this, "Toasttt", Toast.LENGTH_SHORT).show();
            String name = carName.getText().toString().trim();
            String price = carPrice.getText().toString().trim();
            String description = carDescription.getText().toString().trim();
            String milage = carMilage.getText().toString().trim();
            String cartype = carType.getText().toString().trim();
            boolean nameErr, priceErr, imageErr;
            if(name.equals("")){
                carName.setError("Car name is required");
                nameErr = false;
            }else{
                carName.setError(null);
                nameErr = true;
            }
            if (price.equals("")){
                carPrice.setError("Car price is required");
                priceErr = false;
            }else{
                carPrice.setError(null);
                priceErr = true;
            }
            imageErr = ImageUri != null;
            if((nameErr && priceErr && imageErr)){
                loader.StartloadingDialog();
                Boolean check = UpdateData(arr.get(i).getId(),name,price,description,milage,cartype,ImageUri);
                if (check){
                    loader.dismissDialog();
                    dialog.dismiss();
                    Toast.makeText(VehicleActivity.this, "Data Updated Succesfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VehicleActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }else  if((nameErr && priceErr)){
                loader.StartloadingDialog();
                Boolean check = UpdateDataWithoutImage(arr.get(i).getId(),name,price,description,milage,cartype,arr.get(i).getImage());
                if (check){
                    loader.dismissDialog();
                    dialog.dismiss();
                    Toast.makeText(VehicleActivity.this, "Data Updated Succesfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VehicleActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }



        });
        btnCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void showSingleCarDialog(int i) {
        final Dialog dialog= new Dialog(VehicleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
        String Role = (shared.getString("Role", ""));

        if (Role.equals("Dealer")){
            dialog.setContentView(R.layout.car_view_user);
        }else{

        dialog.setContentView(R.layout.car_view_layout);
        }
        TextView carName = dialog.findViewById(R.id.SingleCarViewName);
        TextView carPrice = dialog.findViewById(R.id.SingleCarPrice);
        TextView carDescription = dialog.findViewById(R.id.SingleCarDescription);
        TextView carMilage = dialog.findViewById(R.id.SingleCarViewMilage);
        TextView carType = dialog.findViewById(R.id.SingleCarViewType);
        iview = dialog.findViewById(R.id.SingleCarViewImage);
        Button btnCancel =dialog.findViewById(R.id.btnSingleCarCancel);
        Button btnDelete =dialog.findViewById(R.id.btnSingleCarDelete);

        if(Role.equals("User")){
            btnDelete.setText("Apply");
        }

        carName.setText(arr.get(i).getName());
        carPrice.setText(arr.get(i).getPrice());
        carDescription.setText(arr.get(i).getDescription());
        carMilage.setText(arr.get(i).getMilage());
        carType.setText(arr.get(i).getFuelType());
        Picasso.get().load(arr.get(i).getImage()).into(iview);


            if(Role.equals("Admin")){
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    Dialog d=new Dialog(VehicleActivity.this);
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        builder = new AlertDialog.Builder(VehicleActivity.this);
                        builder.setTitle("Are you sure want to delete??");
                        builder.setIcon(R.drawable.logo);
                        builder.setCancelable(true);
                        builder.setPositiveButton("Delete", (dialogInterface, i1) -> {
                            FirebaseStorage s = FirebaseStorage.getInstance();
                            StorageReference photoRef = s.getReferenceFromUrl(arr.get(i).getImage());
                            photoRef.delete().addOnSuccessListener(aVoid -> {
                                Query applesQuery = db.orderByChild("id").equalTo(arr.get(i).getId());
                                test = false;
                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                            GetData();
                                            test = true;
                                            Toast.makeText(VehicleActivity.this, "Data Deleted Succesfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(VehicleActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }).addOnFailureListener(exception -> {

                            });
                            if(!test){
                                Query applesQuery = db.orderByChild("id").equalTo(arr.get(i).getId());

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                            GetData();
                                            Toast.makeText(VehicleActivity.this, "Data Deleted Succesfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(VehicleActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                                });
                        builder.setNegativeButton("Cancel", (dialogInterface, i12) -> d.dismiss());
                        d = builder.create();
                        d.show();
                    }
                });
            }
            if (Role.equals("User")){
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        ShowApplyDialog(i);
                    }
                });
            }
        btnCancel.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void ShowApplyDialog(int i) {
        final Dialog dialog= new Dialog(VehicleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.applicationmodal);
        Button btnCancel = dialog.findViewById(R.id.btnCancelFinance);
        Button btnApply= dialog.findViewById(R.id.btnSaveFinance);
        EditText finance = dialog.findViewById(R.id.Finance);
        EditText cardNumber = dialog.findViewById(R.id.cardNumber);
        EditText cardHolder = dialog.findViewById(R.id.cardHolderName);
        EditText cardCVV = dialog.findViewById(R.id.cardCVV);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UniqueId = UUID.randomUUID().toString();
                SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
                String Id = (shared.getString("Id", ""));
                String Name = (shared.getString("Name", ""));
                if(Apply(UniqueId,Id,Name,"","",arr.get(i).getId(),arr.get(i).getName(),finance.getText().toString().trim(),
                        cardNumber.getText().toString().trim(),cardHolder.getText().toString().trim(),cardCVV.getText().toString().trim(),"Applied")){
                    dialog.dismiss();
                    Toast.makeText(VehicleActivity.this, "Applied Succesfully Please Wait Of Approval", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VehicleActivity.this, "Something went wrong please try later", Toast.LENGTH_SHORT).show();
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
    
    private boolean Apply(String uniqueId,String userId, String userName, String dealerId, String dealerName, String vehicleId, String vehicleName, String amountOfFinance, String cardNumber, String cardHolderName, String CVV, String status){
        try {
            Applications app = new Applications(uniqueId,userId,userName,dealerId,dealerName,vehicleId,vehicleName,amountOfFinance,cardNumber,cardHolderName,CVV,status);
            dbApplication.child(uniqueId).setValue(app);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private Boolean AddData(String uniqueId, String name, String price, String description, String milage, String cartype, Uri imageUri) {
        try {
            StorageReference fileref= storage.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));
            fileref.putFile(ImageUri).addOnSuccessListener(taskSnapshot -> fileref.getDownloadUrl().addOnSuccessListener(uri -> {
                Cars car = new Cars(uniqueId,name,uri.toString(),milage,cartype,description,"Active",price);
                db.child(uniqueId).setValue(car);
            }));
            return true;
        }catch (Exception ex){
return false;
        }
    }
    private Boolean UpdateData(String uniqueId, String name, String price, String description, String milage, String cartype, Uri imageUri) {
        try {
            StorageReference fileref= storage.child(System.currentTimeMillis()+"."+getFileExtention(imageUri));
            fileref.putFile(ImageUri).addOnSuccessListener(taskSnapshot -> fileref.getDownloadUrl().addOnSuccessListener(uri -> {
                Cars car = new Cars(uniqueId,name,uri.toString(),milage,cartype,description,"Active",price);
                db.child(uniqueId).setValue(car);
            }));
            return true;
        }catch (Exception ex){
return false;
        }
    }

    private Boolean UpdateDataWithoutImage(String uniqueId, String name, String price, String description, String milage, String cartype, String image) {
        try {
            Cars car = new Cars(uniqueId,name,image,milage,cartype,description,"Active",price);
            db.child(uniqueId).setValue(car);

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
 private void GetData(){

   db.addValueEventListener(new ValueEventListener() {

       @Override
       public void onDataChange(@NonNull DataSnapshot snapshot) {


               arr.clear();
           for (DataSnapshot ds : snapshot.getChildren()){
               Cars car = ds.getValue(Cars.class);
               arr.add(car);
               myadp.notifyDataSetChanged();

           }

       }

       @Override
       public void onCancelled(@NonNull DatabaseError error) {
           loader.dismissDialog();
           Toast.makeText(VehicleActivity.this, "Error", Toast.LENGTH_SHORT).show();
       }
   });
 }



    @Override
    public void onBackPressed() {
    }
}