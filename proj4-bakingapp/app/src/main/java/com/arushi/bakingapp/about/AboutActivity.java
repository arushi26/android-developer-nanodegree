/*
 *
 *  *
 *  *  This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *  *
 *  *  As part of Udacity Honor code, your submissions must be your own work, hence
 *  *  submitting this project as yours will cause you to break the Udacity Honor Code
 *  *  and the suspension of your account.
 *  *
 *  *  I, the author of the project, allow you to check the code as a reference, but if
 *  *  you submit it, it's your own responsibility if you get expelled.
 *  *
 *  *  Besides the above notice, the MIT license applies and this license notice
 *  *  must be included in all works derived from this project
 *  *
 *  *  Copyright (c) 2018 Arushi Pant
 *  *
 *
 */

package com.arushi.bakingapp.about;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.arushi.bakingapp.R;

public class AboutActivity extends AppCompatActivity
    implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViews();
    }

    private void initViews(){
        findViewById(R.id.link_git).setOnClickListener(this);
        findViewById(R.id.link_monkik).setOnClickListener(this);
        findViewById(R.id.link_flaticon).setOnClickListener(this);
        findViewById(R.id.link_license).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedId = view.getId();
        Intent intent;
        Uri uri;

        switch (clickedId){
            case R.id.link_git:
                uri = Uri.parse(getString(R.string.link_git));
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.link_monkik:
                uri = Uri.parse(getString(R.string.link_monkik));
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.link_flaticon:
                uri = Uri.parse(getString(R.string.link_flaticon));
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.link_license:
                uri = Uri.parse(getString(R.string.link_license));
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }
}
