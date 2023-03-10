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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class SignupActivity extends AppCompatActivity {

    DatabaseReference db ;
    RadioGroup radioGroup;
    RadioButton radioButton;
    TextInputLayout emaillayout,pwdlayout,cpwdlayout,namelayout;

    int SelectedRadio;
    Button loginbtn,signup,SignUpSubmit;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText Name,Email,Password,CPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_signup);
        radioGroup = findViewById(R.id.radioGroup);
        loginbtn = findViewById(R.id.login);
        signup = findViewById(R.id.Signup);
        Email = findViewById(R.id.Email);
        Name = findViewById(R.id.Name);
        emaillayout = findViewById(R.id.EmailLayout);
        pwdlayout = findViewById(R.id.PwdLayout);
        cpwdlayout = findViewById(R.id.ConfirmPasswordLayout);
        namelayout = findViewById(R.id.namelayout);
        SignUpSubmit = findViewById(R.id.SignUpSubmit);
        Password = findViewById(R.id.Password);
        CPassword = findViewById(R.id.ConfirmPassword);
        LoadingDialog loader = new LoadingDialog(SignupActivity.this);
        loginbtn.setOnClickListener(view -> {
            Intent i2 = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(i2);
            overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            finish();
        });
        signup.setOnClickListener(view -> view.startAnimation(AnimationUtils.loadAnimation(SignupActivity.this, R.anim.myanim)));
        SignUpSubmit.setOnClickListener(view -> {


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
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            loader.dismissDialog();
                            Toast.makeText(SignupActivity.this, "Email Already Exists", Toast.LENGTH_LONG).show();
                        } else {
                            boolean adduser=  AddUserData(UniqueId,text,PasswordText,ConfirmPasswordText,N,radioButton.getText().toString().trim());
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

        });
    }

    private boolean AddUserData(String Id,String text,
                                String passwordText, String confirmPasswordText, String n,String role) {
           try {
               Users user = new Users(Id,n,text,passwordText,role,"Active","");
               db.child(Id).setValue(user);
//               SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
               SharedPreferences pref= getSharedPreferences("Users", MODE_PRIVATE);
               SharedPreferences.Editor editor = pref.edit();
               editor.putString("Id" ,user.getId());
               editor.putString("Role" ,user.getRole());
               editor.putString("Email" ,user.getEmailAddress());
               editor.putString("Name",user.getRole());
               editor.apply();
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
            emaillayout.setError(null);
            Email.setError(null);
        }
        else
        {
            emaillayout.setError("Invalid Email Address");
            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            Email.setError("Enter a valid Email");
            a+=1;
        }
        if (PasswordText.length() < 8)
        {
            a+=1;
            pwdlayout.setError("Minimum 8 characters required");
            Password.setError("Minimun 8 characters Requires");
          //  Toast.makeText(SignupActivity.this,"Enter a valid Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
           pwdlayout.setError(null);
            Password.setError(null);
        }
        if (!PasswordText.equals(ConfirmPasswordText)){
            a+=1;

            if (CPassword.length() ==0){
                cpwdlayout.setError("Confirm Password is required");
                CPassword.setError("Confirm Password is required");
            }else{
                cpwdlayout.setError("Confirm password doesn't match");
                CPassword.setError("Confirm Password Doesn't Match");
            }
        }else{
            cpwdlayout.setError(null);
            CPassword.setError(null);
        }
        if (N.length() < 3){
            a+=1;
            namelayout.setError("Enter a valid Name");
            Name.setError("Minimum 3 chracters are requires");
           // Toast.makeText(SignupActivity.this,"Confirm Password Doesn't Match",Toast.LENGTH_SHORT).show();
        }else{
            namelayout.setError(null);
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