package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button signupbutton ,login,loginsubmit;
    EditText email,password;
    DatabaseReference db;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);
        signupbutton = findViewById(R.id.Signup);
        login = findViewById(R.id.login);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        loginsubmit = findViewById(R.id.loginsubmit);
        LoadingDialog loader = new LoadingDialog(LoginActivity.this);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(LoginActivity.this, SignupActivity.class);
                overridePendingTransition(R.anim.slide_in_up,  R.anim.slide_out_up);
                startActivity(i2);
                finish();

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.myanim));
            }
        });

        loginsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.StartloadingDialog();
                String text = email.getText().toString().trim();
                String PasswordText = password.getText().toString().trim();
                Boolean emailerr = false;
                Boolean passerr = false;
                if (text.matches(emailPattern))
                {
                    emailerr = false;
                    email.setBackground( ContextCompat.getDrawable(LoginActivity.this, R.drawable.inputbordershape));
                    email.setError(null);
                }
                else
                {
                    loader.dismissDialog();
                    emailerr = true;
                    email.setBackground( ContextCompat.getDrawable(LoginActivity.this, R.drawable.errorborder));
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    email.setError("Enter a valid Email");
                }
                if (PasswordText.length() < 8)
                {
                    loader.dismissDialog();
                    passerr = true;
                    password.setBackground( ContextCompat.getDrawable(LoginActivity.this, R.drawable.errorborder));
                    password.setError("Minimun 8 characters Requires");
                    Toast.makeText(LoginActivity.this,"Enter a valid Password",Toast.LENGTH_LONG).show();
                }
                else
                {
                    passerr = false;
                    password.setBackground( ContextCompat.getDrawable(LoginActivity.this, R.drawable.inputbordershape));
                    password.setError(null);
                }

                if (emailerr == false && passerr == false){

                    db = FirebaseDatabase.getInstance().getReference("User");
                    Query query = db.orderByChild("emailAddress").equalTo(text);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                loader.dismissDialog();
                                Users user = new Users();
                                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                    user = userSnapshot.getValue(Users.class);
                                }
                                if (user.getPassword().equals(password.getText().toString().trim())){
//                                   SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                   SharedPreferences pref= getSharedPreferences("Users", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("Id" ,user.getId());
                                    editor.putString("Role" ,user.getRole());
                                    editor.putString("Email" ,user.getEmailAddress());
                                    editor.putString("Name",user.getRole());
                                    editor.apply();
                                   Intent i2 = new Intent(LoginActivity.this, VehicleActivity.class);
                                    startActivity(i2);
                                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                                    finish();
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
                        public void onCancelled(DatabaseError databaseError) {
                            throw databaseError.toException(); // don't ignore errors
                        }
                    });



                }
            }
        });
    }
    public void logoclicked(View v){
        v.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.myanim));
    }

//    @Override
//    public void onBackPressed() {
//        return;
//    }
}