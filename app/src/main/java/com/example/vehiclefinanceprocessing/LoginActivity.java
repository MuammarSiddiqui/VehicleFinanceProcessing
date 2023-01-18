package com.example.vehiclefinanceprocessing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button signupbutton ,login,loginsubmit;
    EditText email,password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signupbutton = findViewById(R.id.Signup);
        login = findViewById(R.id.login);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        loginsubmit = findViewById(R.id.loginsubmit);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i2);
                overridePendingTransition(R.anim.slide_in_up,  R.anim.slide_out_up);
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
                    emailerr = true;
                    email.setBackground( ContextCompat.getDrawable(LoginActivity.this, R.drawable.errorborder));
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    email.setError("Enter a valid Email");
                }
                if (PasswordText.length() < 8)
                {
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
                    Intent i2 = new Intent(LoginActivity.this, AdminHome.class);
                    startActivity(i2);
                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
            }
        });
    }
    public void logoclicked(View v){
        v.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.myanim));
    }

}