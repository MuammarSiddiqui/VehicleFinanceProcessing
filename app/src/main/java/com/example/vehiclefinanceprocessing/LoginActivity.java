package com.example.vehiclefinanceprocessing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button signupbutton ,login,loginsubmit;
    TextInputLayout emaillayout,pwdlayout;

    EditText email,password;
    DatabaseReference db;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);
        emaillayout = findViewById(R.id.emailayout);
        pwdlayout = findViewById(R.id.PasswordLayout);
        signupbutton = findViewById(R.id.Signup);
        login = findViewById(R.id.login);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        loginsubmit = findViewById(R.id.loginsubmit);
        LoadingDialog loader = new LoadingDialog(LoginActivity.this);
        signupbutton.setOnClickListener(view -> {
            Intent i2 = new Intent(LoginActivity.this, SignupActivity.class);
            overridePendingTransition(R.anim.slide_in_up,  R.anim.slide_out_up);
            startActivity(i2);
            finish();

        });
        login.setOnClickListener(view -> view.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.myanim)));

        loginsubmit.setOnClickListener(view -> {
            loader.StartloadingDialog();
            String text = email.getText().toString().trim();
            String PasswordText = password.getText().toString().trim();
            boolean emailerr;
            boolean passerr;
            if (text.matches(emailPattern))
            {
                emailerr = false;
                emaillayout.setError(null);
                email.setError(null);
            }
            else
            {
                loader.dismissDialog();
                emailerr = true;

                emaillayout.setError("Invalid Email Address");

                Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                email.setError("Enter a valid Email");
            }
            if (PasswordText.length() < 8)
            {
                loader.dismissDialog();
                passerr = true;
                pwdlayout.setError("Atleast 8 letters required");

                password.setError("Minimun 8 characters Required");

            }
            else
            {
                passerr = false;
                pwdlayout.setError(null);

                password.setError(null);
            }

            if (!emailerr && !passerr){

                db = FirebaseDatabase.getInstance().getReference("User");
                Query query = db.orderByChild("emailAddress").equalTo(text);
                query.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            loader.dismissDialog();
                            Users user = new Users();
                            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                user = userSnapshot.getValue(Users.class);
                            }
                            assert user != null;
                            if (user.getPassword().equals(password.getText().toString().trim()) ){
                                if (user.getStatus().equals("Active")){
//                                   SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                               SharedPreferences pref= getSharedPreferences("Users", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("Id" ,user.getId());
                                editor.putString("Role" ,user.getRole());
                                editor.putString("Email" ,user.getEmailAddress());
                                editor.putString("Name",user.getName());
                                editor.apply();
                               Intent i2 = new Intent(LoginActivity.this, VehicleActivity.class);
                                startActivity(i2);
                                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                                finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                loader.dismissDialog();
                                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            loader.dismissDialog();
                            Toast.makeText(LoginActivity.this, "User Not Found", Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });



            }
        });
    }
    public void logoclicked(View v){
        v.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.myanim));
    }
    @Override
    public void onBackPressed() {
    }
}