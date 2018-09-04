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

package com.arushi.bakingapp.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.arushi.bakingapp.BApplication;
import com.arushi.bakingapp.R;
import com.arushi.bakingapp.about.AboutActivity;
import com.arushi.bakingapp.data.Resource;
import com.arushi.bakingapp.data.Status;
import com.arushi.bakingapp.data.local.entity.DessertEntity;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private DessertsAdapter mAdapter;
    private MainViewModel mViewModel = null;
    private int mScrollPosition = RecyclerView.NO_POSITION;

    private static final String KEY_SCROLL_POSITION = "position";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.FinalAppTheme); // Transition from launcher theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViewModel();
        initViews();

        if(savedInstanceState!=null && savedInstanceState.containsKey(KEY_SCROLL_POSITION)) {
            // Activity recreated
            mScrollPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION);
        }

        /* Set up observer for Dessert List data */
        setupDessertListObserver();
    }

    private void setupViewModel(){
        ((BApplication) getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.rv_desserts);

        // Grid columns depend on device orientation
        mLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.recipe_list_span_count));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new DessertsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setupDessertListObserver(){
        if(mViewModel.getDessertsList().hasObservers()) return;

        mViewModel.getDessertsList().observe(this,
                        new Observer<Resource<List<DessertEntity>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<DessertEntity>> listResource) {
                if ( listResource!= null
                        && listResource.status == Status.SUCCESS ){
                    List<DessertEntity> desserts = listResource.data;
                    if( desserts!= null && desserts.size()>0 ){
                        mAdapter.setDessertList(desserts);
                        mRecyclerView.scrollToPosition(mScrollPosition);
                    }
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // For activity recreate - Save scroll position
        outState.putInt(KEY_SCROLL_POSITION,
                mLayoutManager.findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        Intent intent;

        switch (menuItemSelected) {
            case R.id.menu_about:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}