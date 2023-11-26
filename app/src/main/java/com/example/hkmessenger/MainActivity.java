package com.example.hkmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;

    RecyclerView recyclerView;
    UserAdapter adpater;
    ImageView imageView;
    ImageView cambtn,setting,delbtn;

    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.mainUserRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageView = findViewById(R.id.logutimg);
        cambtn = findViewById(R.id.camerabtn);
        setting = findViewById(R.id.settingbtn);
        delbtn = findViewById(R.id.deletebtn);

        DatabaseReference reference = database.getReference().child("user");

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this,R.style.dialog);
                dialog.setContentView(R.layout.delete_dialog);
                Button yes ,no;
                yes = dialog.findViewById(R.id.yesbt);
                no = dialog.findViewById(R.id.nobt);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseAuth.getInstance().getCurrentUser().delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Intent i = new Intent(MainActivity.this, login.class);
                                                            startActivity(i);
                                                            finish();
                                                            Toast.makeText(MainActivity.this, "Account deleted successfully!!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                });
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this,R.style.dialog);
                dialog.setContentView(R.layout.dialog_layout);
                Button yes,no;
                yes = dialog.findViewById(R.id.yesbtn);
                no = dialog.findViewById(R.id.nobtn);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(MainActivity.this, login.class);
                        startActivity(i);
                        finish();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        usersArrayList = new ArrayList<>();
        adpater = new UserAdapter(MainActivity.this,usersArrayList);
        recyclerView.setAdapter(adpater);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adpater.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,setting.class);
                startActivity(i);
            }
        });
        cambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,10);
            }
        });
        /*
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                FirebaseAuth.getInstance().getCurrentUser().delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(MainActivity.this, "Account deleted successfully!!", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(MainActivity.this, login.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                                else {
                                                    Toast.makeText(MainActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
*/

        if(auth.getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this, login.class);
            startActivity(i);
        }

    }
}