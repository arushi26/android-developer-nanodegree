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
