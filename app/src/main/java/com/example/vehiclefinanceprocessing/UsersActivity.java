package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.vehiclefinanceprocessing.databinding.ActivityUsersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersActivity extends DrawerBaseActivity {

    ActivityUsersBinding activityBinding;
    ActivityUsersBinding listBinding;
    DatabaseReference db= FirebaseDatabase.getInstance().getReference("User");
//    StorageReference storage= FirebaseStorage.getInstance().getReference();
Uri ImageUri;
    StorageReference storage= FirebaseStorage.getInstance().getReference();
    ArrayList<Users> arr = new ArrayList<>();
    UsersListAdapter myadp;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = listBinding = ActivityUsersBinding.inflate(getLayoutInflater());
        myadp = new UsersListAdapter(UsersActivity.this,arr);
        listBinding.usersList.setAdapter(myadp);
        GetData();
        listBinding.usersList.setOnItemClickListener((adapterView, view, i, l) -> {
                 ShowUserDialog(i);
                });
        setContentView(activityBinding.getRoot());
        AllocateTitle("Users");

        builder =new AlertDialog.Builder(UsersActivity.this);

    }
private  void ShowUserDialog(int i){
    Dialog d = new Dialog(UsersActivity.this);
    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
    d.setCancelable(true);
    d.setContentView(R.layout.singleuserview);
    TextView Name = d.findViewById(R.id.SingleUserName);
    TextView Email = d.findViewById(R.id.SingleUserEmail);
    TextView Role = d.findViewById(R.id.SingleUserRole);
    TextView Status = d.findViewById(R.id.SingleUserStatus);
    Button btnCancel = d.findViewById(R.id.btnSingleUserCancel);
    Button btnActive = d.findViewById(R.id.btnSingleUserActive);
    ImageView iview = d.findViewById(R.id.SingleUserImage);

    Name.setText(arr.get(i).getName());
    Email.setText(arr.get(i).getEmailAddress());
    Role.setText(arr.get(i).getRole());
    Status.setText(arr.get(i).getStatus());
    if (!arr.get(i).getImage().equals("") && iview != null) {
        Picasso.get().load(arr.get(i).getImage()).into(iview);
    }
    if (arr.get(i).getStatus().toLowerCase().equals("active")){
        btnActive.setText("Deactivate");
    }else{
        btnActive.setText("Activate");
    }
    btnCancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            d.dismiss();
        }
    });
    btnActive.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            d.dismiss();
            builder= new AlertDialog.Builder(UsersActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Are you sure want to "+btnActive.getText()+" this user");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int j) {
                    boolean check;
                    if (arr.get(i).getStatus().toLowerCase().equals("active")){
                        check =  ActiveInActive(arr.get(i),"InActive");
                    }else{
                        check =  ActiveInActive(arr.get(i),"Active");
                    }
                  GetData();
                  if (check){
                      Toast.makeText(UsersActivity.this, "User "+btnActive.getText()+ " Successfully", Toast.LENGTH_SHORT).show();
                  }else{
                      Toast.makeText(UsersActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                  }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });
           dialog= builder.create();
           dialog.show();

        }
    });

    builder.create();
    d.show();

}
    private void GetData(){
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arr.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Users user = ds.getValue(Users.class);
                    assert user != null;
                    if (user.getRole().equals("User") ){
                        arr.add(user);
                        myadp.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UsersActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean ActiveInActive(Users user,String status){
        try {
            Users u = new Users(user.getId(),user.getName(),user.getEmailAddress(),user.getPassword(),user.getRole(),status,user.getImage());
            db.child(user.getId()).setValue(u);
            
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    

}