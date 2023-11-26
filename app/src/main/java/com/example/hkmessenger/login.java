package com.example.hkmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
Button btn;
EditText email,password;

TextView signUpbtn;
FirebaseAuth auth;
String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
android.app.ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);

        setContentView(R.layout.activity_login);
        btn = findViewById(R.id.logButton);
        email = findViewById(R.id.logEmail);
        auth = FirebaseAuth.getInstance();
        password = findViewById(R.id.logPassword);
        signUpbtn = findViewById(R.id.signUpButton);

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this,resgistration.class);
                startActivity(i);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Please enter your email id", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Password)) {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    email.setError("Give Proper Email Address");
                } else if (password.length() < 6 || password.length() > 15) {
                    progressDialog.dismiss();
                    password.setError("Password length should be 6-15");
                } else {
                auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            progressDialog.show();
                            try {
                                Intent i = new Intent(login.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(login.this, "Account is not existed", Toast.LENGTH_SHORT).show();
//                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(login.this, "invalid credential", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            }
        });
    }
}