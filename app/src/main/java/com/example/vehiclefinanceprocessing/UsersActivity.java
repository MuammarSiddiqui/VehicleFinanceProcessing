package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.widget.Toast;
import com.example.vehiclefinanceprocessing.databinding.ActivityUsersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersActivity extends DrawerBaseActivity {

    ActivityUsersBinding activityBinding;
    ActivityUsersBinding listBinding;
    DatabaseReference db= FirebaseDatabase.getInstance().getReference("User");
//    StorageReference storage= FirebaseStorage.getInstance().getReference();
    ArrayList<Users> arr = new ArrayList<>();
    UsersListAdapter myadp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = listBinding = ActivityUsersBinding.inflate(getLayoutInflater());
        myadp = new UsersListAdapter(UsersActivity.this,arr);
        listBinding.usersList.setAdapter(myadp);
        GetData();
        listBinding.usersList.setOnItemClickListener((adapterView, view, i, l) -> Toast.makeText(UsersActivity.this, arr.get(i).getName()+" was clicked", Toast.LENGTH_SHORT).show());
        setContentView(activityBinding.getRoot());
        AllocateTitle("Users");

    }

    private void GetData(){
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arr.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Users user = ds.getValue(Users.class);
                    assert user != null;
                    if (user.getRole().equals("User") && user.getStatus().equals("Active")){
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
}