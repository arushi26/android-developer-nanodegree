/*
 * This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * I, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Besides the above notice, the MIT license applies and this license notice
 * must be included in all works derived from this project
 *
 * Copyright (c) 2018 Arushi Pant
 */

package com.pratibimb.jokedisplaylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pratibimb.jokedisplaylibrary.utils.GlideApp;


public class JokeActivity extends AppCompatActivity {
    public static final String KEY_JOKE = "joke";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        ImageView bgImage = findViewById(R.id.iv_background);
        GlideApp.with(this)
                .load(R.drawable.background)
                .dontAnimate()
                .thumbnail(0.1f)
                .centerCrop()
                .into(bgImage);

        Intent intentThatStartedActivity = getIntent();
        if(intentThatStartedActivity.hasExtra(KEY_JOKE)){
            String joke = intentThatStartedActivity.getStringExtra(KEY_JOKE);
            TextView tvJoke = findViewById(R.id.tv_joke);
            tvJoke.setText(joke);
        } else{
            Toast.makeText(this,
                    getString(R.string.joke_not_available),
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
