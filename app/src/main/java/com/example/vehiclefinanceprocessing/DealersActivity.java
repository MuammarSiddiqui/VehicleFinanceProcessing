package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.widget.Toast;

import com.example.vehiclefinanceprocessing.databinding.ActivityDealersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        myadp = new UsersListAdapter(DealersActivity.this,arr);
        listBinding.dealerslist.setAdapter(myadp);
        GetData();
        listBinding.dealerslist.setOnItemClickListener((adapterView, view, i, l) -> Toast.makeText(DealersActivity.this, arr.get(i).getName()+" was clicked", Toast.LENGTH_SHORT).show());
    }

    private void GetData(){
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arr.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Users user = ds.getValue(Users.class);
                    assert user != null;
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