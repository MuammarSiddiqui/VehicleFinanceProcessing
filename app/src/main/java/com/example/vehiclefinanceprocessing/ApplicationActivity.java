package com.example.vehiclefinanceprocessing;


import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.vehiclefinanceprocessing.databinding.ActivityApplicationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplicationActivity extends DrawerBaseActivity {

    ActivityApplicationBinding activityBinding, viewbinding;
    ArrayList<Applications> arr = new ArrayList<>();
    DatabaseReference db;
    ApplicationAdapter myadp ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_application);
        activityBinding =viewbinding= ActivityApplicationBinding.inflate(getLayoutInflater());
        db = FirebaseDatabase.getInstance().getReference("Applications");
        setContentView(activityBinding.getRoot());
        AllocateTitle("Applications");

        myadp  = new ApplicationAdapter(ApplicationActivity.this,arr);
        viewbinding.applicationlist.setAdapter(myadp);

        GetData();
    }

    private void GetData() {
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                arr.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Applications app = ds.getValue(Applications.class);
                    arr.add(app);
                    myadp.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ApplicationActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}