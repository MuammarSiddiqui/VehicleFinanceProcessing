package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.jar.Attributes;

public class SignupActivity extends AppCompatActivity {

    Button loginbtn,signup,SignUpSubmit;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText Name,Email,Password,CPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        loginbtn = findViewById(R.id.login);
        signup = findViewById(R.id.Signup);
        Email = findViewById(R.id.Email);
        Name = findViewById(R.id.Name);
        SignUpSubmit = findViewById(R.id.SignUpSubmit);
        Password = findViewById(R.id.Password);
        CPassword = findViewById(R.id.ConfirmPassword);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i2);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
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
                String text = Email.getText().toString().trim();
                String N = Name.getText().toString().trim();
                String PasswordText = Password.getText().toString().trim();
                String ConfirmPasswordText = CPassword.getText().toString().trim();

                if (validateForm(text,PasswordText,ConfirmPasswordText,N)){
                    Intent i2 = new Intent(SignupActivity.this, AdminHome.class);
                    startActivity(i2);
                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }

            }
        });
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
            Toast.makeText(SignupActivity.this,"Enter a valid Password",Toast.LENGTH_LONG).show();
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
            Toast.makeText(SignupActivity.this,"Confirm Password Doesn't Match",Toast.LENGTH_LONG).show();
        }else{
            CPassword.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.inputbordershape));
            CPassword.setError(null);
        }
        if (N.length() < 3){
            a+=1;
            Name.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.errorborder));
            Name.setError("Confirm Password Doesn't Match");
            Toast.makeText(SignupActivity.this,"Confirm Password Doesn't Match",Toast.LENGTH_LONG).show();
        }else{
            Name.setBackground( ContextCompat.getDrawable(SignupActivity.this, R.drawable.inputbordershape));
            Name.setError(null);
        }
        if (a>0){
            return  false;
        }else{
            return  true;
        }
    }
}