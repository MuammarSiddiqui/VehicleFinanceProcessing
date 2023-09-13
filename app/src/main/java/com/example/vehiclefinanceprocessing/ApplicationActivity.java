package com.example.vehiclefinanceprocessing;


import android.app.AlertDialog;
import android.content.SharedPreferences;
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
    DatabaseReference dbApplication=FirebaseDatabase.getInstance().getReference("Applications");
    ApplicationAdapter myadp ;
    AlertDialog dialog;
    String[] choises ={"Approve","Decline"};
    AlertDialog.Builder builder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_application);
        activityBinding =viewbinding= ActivityApplicationBinding.inflate(getLayoutInflater());
        db = FirebaseDatabase.getInstance().getReference("Applications");
        setContentView(activityBinding.getRoot());
        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
        String  Role = (shared.getString("Role", ""));
        if (Role.equals("User")){
            AllocateTitle("My Applications");
        }
       else {
            AllocateTitle("Applications");
        }

        myadp  = new ApplicationAdapter(ApplicationActivity.this,arr);
        viewbinding.applicationlist.setAdapter(myadp);

        GetData();

x
            viewbinding.applicationlist.setOnItemClickListener((adapterView, view, i, l) -> {
                if (Role.equals("Admin")){
                    builder = new AlertDialog.Builder(ApplicationActivity.this);
                    builder.setTitle("PKR "+arr.get(i).getAmountOfFinance() );
                    builder.setSingleChoiceItems(choises, -1, (dialogInterface, j) -> {

                        String  Id = (shared.getString("Id", ""));
                        String  Name = (shared.getString("Name", ""));
                        switch (choises[j]){
                            case "Approve":
                                dialog.dismiss();
                                Applications app = arr.get(i);
                                app.setDealerId(Id);
                                app.setDealerName(Name);
                                app.setStatus("Approved");
                                Apply(app);
                                Toast.makeText(this, "Approved Successfully", Toast.LENGTH_SHORT).show();

                                GetData();
                                break;
                            case "Decline":
                                dialog.dismiss();
                                Applications a = arr.get(i);
                                a.setDealerId(Id);
                                a.setDealerName(Name);
                                a.setStatus("Rejected");
                                Apply(a);
                                Toast.makeText(this, "Rejected Successfully", Toast.LENGTH_SHORT).show();
                                GetData();

                                break;

                        }
                    });
                    builder.setNegativeButton("Cancel", (dialogInterface, i1) -> dialog.dismiss());
                    dialog= builder.create();
                    dialog.show();
                }else if (Role.equals("Dealer") && arr.get(i).getStatus().equals("Applied")){
                    builder = new AlertDialog.Builder(ApplicationActivity.this);
                    builder.setTitle("PKR "+arr.get(i).getAmountOfFinance() );
                    builder.setSingleChoiceItems(choises, -1, (dialogInterface, j) -> {

                        String  Id = (shared.getString("Id", ""));
                        String  Name = (shared.getString("Name", ""));
                        switch (choises[j]){
                            case "Approve":
                                dialog.dismiss();
                                Applications app = arr.get(i);
                                app.setDealerId(Id);
                                app.setDealerName(Name);
                                app.setStatus("Approved");
                                Apply(app);
                                Toast.makeText(this, "Approved Successfully", Toast.LENGTH_SHORT).show();

                                GetData();
                                break;
                            case "Decline":
                                dialog.dismiss();
                                Applications a = arr.get(i);
                                a.setDealerId(Id);
                                a.setDealerName(Name);
                                a.setStatus("Rejected");
                                Apply(a);
                                Toast.makeText(this, "Rejected Successfully", Toast.LENGTH_SHORT).show();
                                GetData();

                                break;

                        }
                    });
                    builder.setNegativeButton("Cancel", (dialogInterface, i1) -> dialog.dismiss());
                    dialog= builder.create();
                    dialog.show();
                }
            });

    }

    private void GetData() {
        SharedPreferences shared = getSharedPreferences("Users", MODE_PRIVATE);
        String Role = (shared.getString("Role", ""));
        String Id = (shared.getString("Id", ""));
        db.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                arr.clear();
                if(!Role.equals("Users")) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Applications app = ds.getValue(Applications.class);
                        arr.add(app);
                        myadp.notifyDataSetChanged();

                    }
                }
                else{
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Applications app = ds.getValue(Applications.class);
                        if(app.getUserId().equals(Id)){
                            arr.add(app);
                        }
                        myadp.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ApplicationActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean Apply(Applications app){
        try {
            dbApplication.child(app.getId()).setValue(app);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}