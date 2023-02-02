package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vehiclefinanceprocessing.databinding.ActivityDealersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealersActivity extends DrawerBaseActivity {

    ActivityDealersBinding activityBinding;
    ActivityDealersBinding listBinding;

    DatabaseReference db= FirebaseDatabase.getInstance().getReference("User");
    ArrayList<Users> arr = new ArrayList<>();
    UsersListAdapter myadp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dealers);
        activityBinding = listBinding=ActivityDealersBinding.inflate(getLayoutInflater());

        setContentView(activityBinding.getRoot());
        AllocateTitle("Dealers");
        builder =new AlertDialog.Builder(DealersActivity.this);
        myadp = new UsersListAdapter(DealersActivity.this,arr);
        listBinding.dealerslist.setAdapter(myadp);
        GetData();
        listBinding.dealerslist.setOnItemClickListener((adapterView, view, i, l) ->
                ShowUserDialog(i));
    }

    private void GetData(){
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arr.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Users user = ds.getValue(Users.class);
                    assert user != null;
                    if (user.getRole().equals("Dealer") ){
                        arr.add(user);
                       myadp.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DealersActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private  void ShowUserDialog(int i){
        Dialog d = new Dialog(DealersActivity.this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCancelable(true);
        d.setContentView(R.layout.singleuserview);
        TextView Name = d.findViewById(R.id.SingleUserName);
        TextView Email = d.findViewById(R.id.SingleUserEmail);
        TextView Role = d.findViewById(R.id.SingleUserRole);
        TextView Status = d.findViewById(R.id.SingleUserStatus);
        Button btnCancel = d.findViewById(R.id.btnSingleUserCancel);
        Button btnActive = d.findViewById(R.id.btnSingleUserActive);
        ImageView iview = d.findViewById(R.id.SingleCarViewImage);

        Name.setText(arr.get(i).getName());
        Email.setText(arr.get(i).getEmailAddress());
        Role.setText(arr.get(i).getRole());
        Status.setText(arr.get(i).getStatus());
        if (!arr.get(i).getImage().equals("")) {
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
                builder= new AlertDialog.Builder(DealersActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure want to "+btnActive.getText()+" this dealer");
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
                            Toast.makeText(DealersActivity.this, "User "+btnActive.getText()+ " Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DealersActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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