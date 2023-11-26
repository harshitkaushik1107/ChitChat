package com.example.hkmessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class resgistration extends AppCompatActivity {
Button btn;
TextView loginBtn;
EditText rg_username, rg_email,rg_password,rg_repassword;

FirebaseAuth auth;
Uri imageUri;
String imageuri;
CircleImageView rg_profileImg;
FirebaseDatabase database;
FirebaseStorage storage;
String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";
ProgressDialog progressDialog;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistration);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Establsihing the account ");
        progressDialog.setCancelable(false);
        btn = findViewById(R.id.signUpButton);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        rg_username = findViewById(R.id.Username);
        rg_email = findViewById(R.id.signUpEmail);
        rg_password = findViewById(R.id.signUpPassword);
        rg_repassword = findViewById(R.id.signUpRePassword);
        rg_profileImg = findViewById(R.id.profilerg);
        loginBtn = findViewById((R.id.loginButton));



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(resgistration.this, login.class);
                startActivity(i);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namee = rg_username.getText().toString();
                String emaill = rg_email.getText().toString();
                String Password = rg_password.getText().toString();
                String cPassword = rg_repassword.getText().toString();
                String status = "Hey i am using this application";


                if(TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword) || TextUtils.isEmpty(status)){
                    progressDialog.dismiss();
                    Toast.makeText(resgistration.this, "Please enter valid information", Toast.LENGTH_SHORT).show();
                }
                else if(!emaill.matches(emailPattern)){
                    progressDialog.dismiss();
                    rg_email.setError("Type a valid email adddress");
                }
                else if(Password.length()<6 || Password.length()>15){
                    progressDialog.dismiss();
                    rg_password.setError("Password length must be 6-15");
                }
                else if(!Password.equals(cPassword)){
                    progressDialog.dismiss();
                    rg_repassword.setError("Password must be same");
                }
                else{
                    auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference  reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("upload").child(id);

                                if(imageUri!=null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        Users users = new Users(id,namee,emaill,Password,imageuri,status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    progressDialog.show();
                                                                    Intent i = new Intent(resgistration.this, MainActivity.class);
                                                                    startActivity(i);
                                                                    finish();
                                                                }
                                                                else {
                                                                    Toast.makeText(resgistration.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else{
                                    String status ="Hey i am using this app";
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/hkmessenger-594be.appspot.com/o/man.png?alt=media&token=d72ea9d9-a609-4354-9f47-e06a75ef0f3c";
                                    Users users = new Users(id,namee,emaill,Password,imageuri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.show();
                                                Intent i = new Intent(resgistration.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(resgistration.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else{
                                Toast.makeText(resgistration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"select picture"),10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageUri = data.getData();
                rg_profileImg.setImageURI(imageUri);
            }
        }
    }
}