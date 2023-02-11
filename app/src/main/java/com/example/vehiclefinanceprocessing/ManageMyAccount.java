package com.example.vehiclefinanceprocessing;


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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vehiclefinanceprocessing.databinding.ActivityManageMyAccountBinding;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageMyAccount extends DrawerBaseActivity {
    Users user = new Users();
    Users u;
    LoadingDialog loader;
    DatabaseReference db;
    StorageReference storage= FirebaseStorage.getInstance().getReference();
    Dialog dialog;
    Uri ImageUri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    CircleImageView iview;

    ActivityManageMyAccountBinding activityBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding= ActivityManageMyAccountBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        AllocateTitle("Manage My Profile");
        db = FirebaseDatabase.getInstance().getReference("User");
        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
        String Id = (shared.getString("Id", ""));
         loader = new LoadingDialog(ManageMyAccount.this);
        loader.StartloadingDialog();

        Button Edit = findViewById(R.id.btnEditProfile);
        Button ChangePwd = findViewById(R.id.btnChangePwd);
        Button MyApplication = findViewById(R.id.btnMyApplications);
       showData(Id);
        
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowEditDialog();
            }
        });
        ChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManageMyAccount.this, "Change Password", Toast.LENGTH_SHORT).show();
            }
        });
        String Role = (shared.getString("Role", ""));
        if (Role.equals("Admin") ){
            MyApplication.setVisibility(View.GONE);
        }
        MyApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManageMyAccount.this, "My Applications was clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showData(String Id) {
        Query query = db.orderByChild("id").equalTo(Id);
        TextView myName = findViewById(R.id.MyName);
        TextView Email = findViewById(R.id.myEmail);
        TextView Role = findViewById(R.id.MyRole);
        CircleImageView iview =findViewById(R.id.myImage);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    loader.dismissDialog();

                    for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                        user = userSnapshot.getValue(Users.class);
                    }
                    assert user != null;
                    myName.setText(user.getName());
                    Role.setText(user.getRole());
                    Email.setText(user.getEmailAddress());
                    try {
                        if (!user.getImage().equals("")){
                            Picasso.get().load(user.getImage()).into(iview);
                        }
                    }catch (Exception e){

                    }
                } else {
                    loader.dismissDialog();
                    Toast.makeText(ManageMyAccount.this, "Some thing went wrong please check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageMyAccount.this, "Some thing went wrong please check your internet connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private  void ShowEditDialog(){
         dialog= new Dialog(ManageMyAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.edituser_dialog);
        iview = dialog.findViewById(R.id.myImageView);
        EditText name = dialog.findViewById(R.id.txtmyName);
        EditText email = dialog.findViewById(R.id.txtmyEmail);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm(email.getText().toString().trim(),name.getText().toString().trim(),dialog)){
                    SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
                    String Id = (shared.getString("Id", ""));
                   if ( UpdateData(Id,email.getText().toString().trim(),name.getText().toString().trim(),ImageUri)){
                       dialog.dismiss();
                       TextView myName = findViewById(R.id.MyName);
                       TextView Email = findViewById(R.id.myEmail);
                       CircleImageView iview =findViewById(R.id.myImage);
                       if(ImageUri!= null){
                           iview.setImageURI(ImageUri);
                       }
                       myName.setText(name.getText());
                       Email.setText(email.getText());

                       Toast.makeText(ManageMyAccount.this, "Data Updated Succesfully", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(ManageMyAccount.this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                   }
                }

            }
        });
        Button Cancel = dialog.findViewById(R.id.btnCancel);
        Cancel.setOnClickListener(view -> dialog.dismiss());
        name.setText(user.getName());
        email.setText(user.getEmailAddress());
        if (!user.getImage().equals("")){
            Picasso.get().load(user.getImage()).into(iview);
        }
        iview.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
        });
        dialog.show();
    }

    private boolean UpdateData(String Id,String Email,String Name,Uri imageUri) {
       try {
           db = FirebaseDatabase.getInstance().getReference("User");
           Query query = db.orderByChild("id").equalTo(Id);
           query.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()){
                       for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                           u = userSnapshot.getValue(Users.class);
                       }
                       if (u != null){
                           u.setEmailAddress(Email);
                           u.setName(Name);
                           if (imageUri != null){
                               String ext = getFileExtention(imageUri);
                               if (ext.equals(null) || ext.equals("")){
                                   ext = "jpg";
                               }
                               StorageReference fileref= storage.child(System.currentTimeMillis()+"."+ext);
                               fileref.putFile(ImageUri).addOnSuccessListener(taskSnapshot -> fileref.getDownloadUrl().addOnSuccessListener(uri -> {
                                   u.setImage(uri.toString());
                                   db.child(Id).setValue(u);
                               }));
                           }else{
                               db.child(Id).setValue(u);
                           }

                       }else {
                           Toast.makeText(ManageMyAccount.this, "Null", Toast.LENGTH_SHORT).show();
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });

          return  true;
       }catch (Exception e){
            return  false;
       }
    }
    public  Boolean validateForm(String text, String N, Dialog dialog){
        TextInputLayout emaillayout,namelayout;
        emaillayout = dialog.findViewById(R.id.useremaillayout);
        namelayout = dialog.findViewById(R.id.userlayoutname);
        int a = 0;
        if (text.matches(emailPattern))
        {
            emaillayout.setError(null);
        }
        else
        {
            emaillayout.setError("Invalid Email Address");
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            a+=1;
        }


        if (N.length() < 3){
            a+=1;
            namelayout.setError("Enter a valid Name");
        }else{
            namelayout.setError(null);
        }

        if (a>0){
            return  false;
        }else{
            return  true;
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
            iview.setImageURI(ImageUri);
        }
    }
}