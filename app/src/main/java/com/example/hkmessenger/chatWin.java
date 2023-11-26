package com.example.hkmessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatWin extends AppCompatActivity {
String reciverimg,reciverUid,reciverName,senderUid;

public static String senderImg;
public static String reciverImg;
String senderRoom,receiverRoom;
CircleImageView profile;
TextView recivername;
CardView sendBtn;
EditText textmsg;
ImageView imgbtn;
ProgressDialog dialog;
FirebaseAuth auth;
FirebaseDatabase database;
FirebaseStorage storage;
RecyclerView recyclerView;
ArrayList<msgModle> messagesArraylist;
Messageadapter messageadapter;



    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);
        reciverName = getIntent().getStringExtra("namee");
        reciverimg = getIntent().getStringExtra("reciverimg");
        reciverUid = getIntent().getStringExtra("uid");
        messagesArraylist = new ArrayList<>();
        profile = findViewById(R.id.profileimgg);
        recivername = findViewById(R.id.recivername);
        sendBtn = findViewById(R.id.sendbtn);
        textmsg = findViewById(R.id.textmsg);
        imgbtn = findViewById(R.id.imagebtn);
        recyclerView = findViewById(R.id.msgAdapter);
        storage = FirebaseStorage.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageadapter = new Messageadapter(chatWin.this,messagesArraylist);
        recyclerView.setAdapter(messageadapter);


        Picasso.get().load(reciverimg).into(profile);
        recivername.setText(""+reciverName);
        senderUid = auth.getUid();
        senderRoom =senderUid+reciverUid;
        receiverRoom =reciverUid+senderUid;

        DatabaseReference reference =database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArraylist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    msgModle mssg = dataSnapshot.getValue(msgModle.class);
                    messagesArraylist.add(mssg);
                }
                messageadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("profilePic").getValue().toString();
                reciverImg=reciverimg;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,25);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = textmsg.getText().toString();
//                if(msg.isEmpty()){
//                    Toast.makeText(chatWin.this, "Enter message first", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                textmsg.setText("");
                Date date = new Date();
                msgModle mssg = new msgModle(msg,senderUid,date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(mssg).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(mssg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 25){
            if(data != null){
                if(data.getData() != null){
                    Uri selectedImage = data.getData();
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");
                    dialog.show();
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();
                                        String msg = textmsg.getText().toString();
//                                        if(msg.isEmpty()){
//                                            Toast.makeText(chatWin.this, "Enter message first", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
                                        textmsg.setText("");
                                        Date date = new Date();
                                        msgModle mssg = new msgModle(msg,senderUid,date.getTime());
                                        mssg.setMessage("photo");
                                        mssg.setImageUri(filePath);
                                        database = FirebaseDatabase.getInstance();
                                        database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(mssg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                database.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(mssg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                            }
                                        });
                                        Toast.makeText(chatWin.this, filePath, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }
}