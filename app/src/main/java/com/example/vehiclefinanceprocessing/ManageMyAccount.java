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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vehiclefinanceprocessing.databinding.ActivityManageMyAccountBinding;
import com.example.vehiclefinanceprocessing.databinding.ActivityUsersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManageMyAccount extends DrawerBaseActivity {
    Users user = new Users();
    DatabaseReference db;
    Dialog dialog;
    Uri ImageUri;
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
        LoadingDialog loader = new LoadingDialog(ManageMyAccount.this);
        loader.StartloadingDialog();
        Query query = db.orderByChild("id").equalTo(Id);
        TextView myName = findViewById(R.id.MyName);
        TextView Email = findViewById(R.id.myEmail);
        TextView Role = findViewById(R.id.MyRole);
        CircleImageView iview =findViewById(R.id.myImage);
        Button Edit = findViewById(R.id.btnEditProfile);
        Button ChangePwd = findViewById(R.id.btnChangePwd);
        Button MyApplication = findViewById(R.id.btnMyApplications);
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
                    if (!user.getImage().equals("")){
                        Picasso.get().load(user.getImage()).into(iview);
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
        MyApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ManageMyAccount.this, "My Applications was clicked", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ManageMyAccount.this, "Update button clicked", Toast.LENGTH_SHORT).show();
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