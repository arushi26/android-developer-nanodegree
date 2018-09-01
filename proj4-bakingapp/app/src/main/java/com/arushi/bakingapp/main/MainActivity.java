package com.arushi.bakingapp.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Movie;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arushi.bakingapp.BApplication;
import com.arushi.bakingapp.R;
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

    // TODO Menu - Git link, Credits
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();

        initViews();

        if(savedInstanceState!=null && savedInstanceState.containsKey(KEY_SCROLL_POSITION)) {
            // Activity recreated
            mScrollPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION);
        }

        setupDessertListObserver();
    }

    private void setupViewModel(){
        ((BApplication) getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }


    private void initViews() {
        mRecyclerView = findViewById(R.id.rv_desserts);

        mLayoutManager = new GridLayoutManager(this,
                                                        getResources().getInteger(R.integer.recipe_list_span_count));
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
                mLayoutManager.findFirstCompletelyVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }
}