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

package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.pratibimb.jokedisplaylibrary.utils.GlideApp;
import com.udacity.gradle.builditbigger.utils.AdUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener {
    private MainFragmentListener mFragmentListener;

    public MainActivityFragment() {
    }

    public interface MainFragmentListener {
        void tellJoke();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        ImageView bgImage = root.findViewById(R.id.iv_background);
        GlideApp.with(this)
                .load(R.drawable.background)
                .dontAnimate()
                .thumbnail(0.1f)
                .centerCrop()
                .into(bgImage);

        Button btnTell = root.findViewById(R.id.btn_joke);
        btnTell.setOnClickListener(this);

        // Ad displayed in free version
        AdUtils.showBanner(root);

        return root;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btn_joke:
                mFragmentListener.tellJoke();
                break;

        }
    }


    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mFragmentListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement MainFragmentListener");
        }
    }

}
