package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.jar.Attributes;

public class SignupActivity extends AppCompatActivity {

    DatabaseReference db ;
    RadioGroup radioGroup;
    RadioButton radioButton;
    int SelectedRadio;
    Button loginbtn,signup,SignUpSubmit;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText Name,Email,Password,CPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        loginbtn = findViewById(R.id.login);
        signup = findViewById(R.id.Signup);
        Email = findViewById(R.id.Email);
        Name = findViewById(R.id.Name);
        SignUpSubmit = findViewById(R.id.SignUpSubmit);
        Password = findViewById(R.id.Password);
        CPassword = findViewById(R.id.ConfirmPassword);
        LoadingDialog loader = new LoadingDialog(SignupActivity.this);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i2);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(SignupActivity.this, R.anim.myanim));
            }
        });
        SignUpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SelectedRadio = radioGroup.getCheckedRadioButtonId();
                String text = Email.getText().toString().trim();
                String N = Name.getText().toString().trim();

                radioButton= findViewById(SelectedRadio);
                String PasswordText = Password.getText().toString().trim();
                String ConfirmPasswordText = CPassword.getText().toString().trim();
                    db = FirebaseDatabase.getInstance().getReference("User");
                    String UniqueId = UUID.randomUUID().toString();
                if (validateForm(text,PasswordText,ConfirmPasswordText,N)){
                    loader.StartloadingDialog();
                    db = FirebaseDatabase.getInstance().getReference("User");
                    Query query = db.orderByChild("emailAddress").equalTo(text);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                loader.dismissDialog();
                                Toast.makeText(SignupActivity.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Boolean adduser=  AddUserData(UniqueId,text,PasswordText,ConfirmPasswordText,N,radioButton.getText().toString().trim());
                                if (adduser){
                                    loader.dismissDialog();
                                    Intent i2 = new Intent(SignupActivity.this, VehicleActivity.class);
                                    startActivity(i2);
                                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                                    finish();
                                }else {
                                    loader.dismissDialog();
                                    Toast.makeText(SignupActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            loader.dismissDialog();
                            throw databaseError.toException(); // don't ignore errors
                        }
                    });



                }


            }
        });
    }

    private boolean AddUserData(String Id,String text,
                                String passwordText, String confirmPasswordText, String n,String role) {
           try {
               Users user = new Users(Id,n,text,passwordText,role,"Active");
               db.child(Id).setValue(user);
               return  true;
           }catch (Exception e){
               return  false;
           }
    }

    public void logoclicked(View v){
        v.startAnimation(AnimationUtils.loadAnimation(SignupActivity.this, R.anim.myanim));
    }
    public  Boolean validateForm(String text,String PasswordText,String ConfirmPasswordText,String N){

        int a = 0;
        if (text.matches(emailPattern))
        {
            Email.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.inputbordershape));
            Email.setError(null);
        }
        else
        {
            Email.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.errorborder));
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            Email.setError("Enter a valid Email");
            a+=1;
        }
        if (PasswordText.length() < 8)
        {
            a+=1;
            Password.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.errorborder));
            Password.setError("Minimun 8 characters Requires");
            Toast.makeText(SignupActivity.this,"Enter a valid Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Password.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.inputbordershape));
            Password.setError(null);
        }
        if (!PasswordText.equals(ConfirmPasswordText)){
            a+=1;
            CPassword.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.errorborder));
            if (CPassword.length() ==0){
                CPassword.setError("Confirm Password is required");
            }else{
                CPassword.setError("Confirm Password Doesn't Match");
            }
            Toast.makeText(SignupActivity.this,"Confirm Password Doesn't Match",Toast.LENGTH_SHORT).show();
        }else{
            CPassword.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.inputbordershape));
            CPassword.setError(null);
        }
        if (N.length() < 3){
            a+=1;
            Name.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.errorborder));
            Name.setError("Confirm Password Doesn't Match");
            Toast.makeText(SignupActivity.this,"Confirm Password Doesn't Match",Toast.LENGTH_SHORT).show();
        }else{
            Name.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.inputbordershape));
            Name.setError(null);
        }
        if (radioButton == null){

            Toast.makeText(this, "Please Select Role", Toast.LENGTH_LONG).show();
            return  false;
        }
        if (a>0){
            return  false;
        }else{
            return  true;
        }
    }
    @Override
    public void onBackPressed() {
        return;
    }


}