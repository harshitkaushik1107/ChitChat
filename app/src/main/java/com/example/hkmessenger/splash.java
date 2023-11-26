package com.example.hkmessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class splash extends AppCompatActivity {
    ImageView img;
    TextView name;
    Animation topAnime,lowAnime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        img = findViewById(R.id.logoImg);
        name = findViewById(R.id.logoNameImg);
//        Objects.requireNonNull(getSupportActionBar()).hide();
        topAnime = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        lowAnime = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);


        img.setAnimation(topAnime);
        name.setAnimation(lowAnime);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, login.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }
}