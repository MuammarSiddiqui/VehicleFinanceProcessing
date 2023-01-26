package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.vehiclefinanceprocessing.databinding.ActivityDealersBinding;
import com.example.vehiclefinanceprocessing.databinding.ActivityUsersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DealersActivity extends DrawerBaseActivity {

    ActivityDealersBinding activityBinding;
    ActivityDealersBinding listBinding;

    DatabaseReference db= FirebaseDatabase.getInstance().getReference("User");
    StorageReference storage= FirebaseStorage.getInstance().getReference();
    ArrayList<Users> arr = new ArrayList<>();
    UsersListAdapter myadp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dealers);
        activityBinding = listBinding=ActivityDealersBinding.inflate(getLayoutInflater());

        setContentView(activityBinding.getRoot());
        AllocateTitle("Dealers");

        myadp = new UsersListAdapter(DealersActivity.this,arr);
        listBinding.dealerslist.setAdapter(myadp);
        GetData();
        listBinding.dealerslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(DealersActivity.this, arr.get(i).getName()+" was clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void GetData(){
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arr.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Users user = ds.getValue(Users.class);
                    if (user.getRole().equals("Dealer") && user.getStatus().equals("Active")){
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
}