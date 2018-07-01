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

package com.arushi.popularmovies.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arushi.popularmovies.R;
import com.arushi.popularmovies.data.ApiRequestInterface;
import com.arushi.popularmovies.data.local.MainActivitySaveInstance;
import com.arushi.popularmovies.data.model.Movie;
import com.arushi.popularmovies.data.model.MoviesResponse;
import com.arushi.popularmovies.utils.Constants;
import com.arushi.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arushi on 30/05/18.
 */

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener{

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private ConstraintLayout mLayoutError, mLayoutProgress;
    private TextView mTvNoFav;
    private GridLayoutManager mLayoutManager;
    private Call<MoviesResponse> mCall;
    private MainViewModel mViewModel = null;

    private int mNextPage = 1;
    private int mCurrentSorting = Constants.SORT_POPULAR;
    private int mTotalPages=1;
    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;

    ImageView mProgressBar;
    AnimatedVectorDrawableCompat mAnimatedLoader;
    Animatable2Compat.AnimationCallback mAnimationCallback;


    // Start loading when visible threshold of page reached
    private final int mVisibleThreshold = 6;

    private static final String KEY_INSTANCE = "state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        List<Movie> movieList = new ArrayList<>();
        int position = 0;

        if(savedInstanceState!=null && savedInstanceState.containsKey(KEY_INSTANCE)) {
            // Activity recreated
            MainActivitySaveInstance instance = savedInstanceState.getParcelable(KEY_INSTANCE);
            if (instance != null) {
                this.setTitle(instance.getTitle());
                mCurrentSorting = instance.getSorting();

                movieList = instance.getMovieList();
                if (movieList.size() > 0) {
                    mNextPage = instance.getNextPage();
                    mTotalPages = instance.getTotalPages();
                    position = instance.getPosition();
                }
            }
        }

        if(movieList.size()>0){
            recreateMovieList(movieList,position);
        } else {
            getMovieList(true);
        }

    }

    private void initViews() {
        mLayoutProgress = findViewById(R.id.layout_progress);
        mProgressBar = findViewById(R.id.iv_progress);
        showLoader();

        mRecyclerView = findViewById(R.id.rv_movie_posters);
        mTvNoFav = findViewById(R.id.tv_no_fav);

        /* Grid layout column count will be according to orientation */
        int spanCount = getResources().getInteger(R.integer.span_count);

        mLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // Pagination
        RecyclerView.OnScrollListener onScrollListener =
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if(mCurrentSorting!=Constants.SORT_FAVOURITES) {
                            int totalItemCount = mLayoutManager.getItemCount();
                            int lastItemVisiblePosition = mLayoutManager.findLastVisibleItemPosition();

                            if (!mIsLoading && !mIsLastPage
                                    && totalItemCount <= (lastItemVisiblePosition + mVisibleThreshold)) {
                                getMovieList(false);
                            }
                        }
                    }
                };
        mRecyclerView.addOnScrollListener(onScrollListener);

        mLayoutError = findViewById(R.id.layout_error);
        Button btnRetry = findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(this);
    }

    private void getMovieList(final boolean isFirstRequest){
        if(mCurrentSorting==Constants.SORT_FAVOURITES){
            setupFavouritesObserver();
        } else {
            removeObservers();
            getMovieListFromAPI(isFirstRequest);
        }
    }

    private void setupFavouritesObserver(){
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getFavourites().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if(movies!=null && movies.size() > 0) {
                    Log.i("Movies", movies.toString());
                    mAdapter.clearMovieList();
                    mAdapter.addMovieList(movies);
                    mAdapter.removeLoadingMore();
                    showData();
                } else {
                    hideLoader();
                    mTvNoFav.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void removeObservers(){
        if(mViewModel!=null) {
            mViewModel.getFavourites().removeObservers(this);
        }
    }

    private void getMovieListFromAPI(final boolean isFirstRequest){
        /* Get list of Movies from API */
        mIsLoading = true;
        mAdapter.showLoadingMore(true); // show loading more card at the end of the list

        final ApiRequestInterface request = NetworkUtils.getRetrofitInstance().create(ApiRequestInterface.class);

        if(isFirstRequest) {
            // To get the 1st page
            mNextPage = 1;
        }

        switch (mCurrentSorting){
            case Constants.SORT_POPULAR:
                mCall = request.getPopularMovieList(Constants.API_KEY, mNextPage);
                break;
            case Constants.SORT_TOP_RATED:
                mCall = request.getTopRatedMovieList(Constants.API_KEY, mNextPage);
                break;
            default:
                mCall = request.getPopularMovieList(Constants.API_KEY, mNextPage);
        }

        Log.d(TAG,"URL called - " + mCall.request().url());

        mCall.enqueue(new Callback<MoviesResponse>() {

            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call,@NonNull Response<MoviesResponse> response) {
                if(response.isSuccessful()){
                    MoviesResponse movieResponse = response.body();

                    try{
                        if(movieResponse!=null) {
                            List<Movie> movieList = movieResponse.getMovieList();
                            mAdapter.addMovieList(movieList);
                            showData();
                            mNextPage = movieResponse.getPage() + 1;
                            mTotalPages = movieResponse.getTotalPages();
                            if (mNextPage > mTotalPages) {
                                mIsLastPage = true;
                                mAdapter.showLoadingMore(false);
                            }
                        } else {
                            onError(isFirstRequest);
                        }
                    } catch (Exception e){
                        Log.e(TAG,"Error getting movie list", e);
                        onError(isFirstRequest);
                    }
                } else {
                    ResponseBody errorBody = response.errorBody();
                    if(errorBody!=null){
                        Log.e(TAG,"Error getting movie list - " + errorBody.toString());
                    }

                    onError(isFirstRequest);
                }
                mIsLoading = false;
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call,@NonNull Throwable t) {
                if(!mCall.isCanceled()) {
                    Log.e(TAG, "Error getting movie list", t);
                }

                mIsLoading = false;
                onError(isFirstRequest);
            }
        });
    }

    private void recreateMovieList(List<Movie> movieList,
                                   int position) {
        /* Called after activity recreated */
        mAdapter.clearMovieList();
        mAdapter.addMovieList(movieList);
        showData();
        if (mNextPage > mTotalPages) {
            mIsLastPage = true;
            mAdapter.showLoadingMore(false);
        }
        mRecyclerView.scrollToPosition(position);
    }

    private void showLoader(){
        /* https://stackoverflow.com/questions/41767676/how-to-restart-android-animatedvectordrawables-animations */
        mAnimatedLoader = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_progress);
        mProgressBar.setImageDrawable(mAnimatedLoader);
        final Handler mainHandler = new Handler(Looper.getMainLooper());

        if(mAnimatedLoader != null) {
            mAnimationCallback = new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(final Drawable drawable) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAnimatedLoader.start();
                        }
                    });
                }
            };

            mAnimatedLoader.registerAnimationCallback(mAnimationCallback);

            mAnimatedLoader.start();
        }

        mLayoutProgress.setVisibility(View.VISIBLE);
    }

    private void hideLoader(){
        if(mAnimatedLoader != null) {
            mAnimatedLoader.unregisterAnimationCallback(mAnimationCallback);
        }
        mLayoutProgress.setVisibility(View.GONE);
    }

    private void showData(){
        hideLoader();
        mRecyclerView.setVisibility(View.VISIBLE);
        mLayoutError.setVisibility(View.GONE);
        mTvNoFav.setVisibility(View.GONE);
    }

    private void onError(boolean isFirstRequest){
        if(isFirstRequest) {
            hideLoader();
            mRecyclerView.setVisibility(View.GONE);
            mLayoutError.setVisibility(View.VISIBLE);
            mTvNoFav.setVisibility(View.GONE);
        } else {
            mAdapter.removeLoadingMore();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        switch (menuItemSelected) {
            case R.id.menu_popular:
                mCurrentSorting = Constants.SORT_POPULAR;
                sortChangeSelected();
                setTitle(R.string.menu_popular);
                return true;
            case R.id.menu_top_rated:
                mCurrentSorting = Constants.SORT_TOP_RATED;
                sortChangeSelected();
                setTitle(R.string.menu_top_rated);
                return true;
            case R.id.menu_favourites:
                mCurrentSorting = Constants.SORT_FAVOURITES;
                sortChangeSelected();
                setTitle(R.string.menu_favourites);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortChangeSelected(){
        showLoader();
        if(mCall!=null) { // Cancel any pending request
            mCall.cancel();
        }
        mIsLastPage = false;
        mAdapter.clearMovieList();
        getMovieList(true);
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_retry:
                showLoader();
                getMovieList(true);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /* For activity recreate -
         Save scrolled to position, loaded movie list,
         sorting, nextpage, total page count */
        super.onSaveInstanceState(outState);

        List<Movie> movieList = mAdapter.getMovieList();
        MainActivitySaveInstance instance = new MainActivitySaveInstance();
        instance.setTitle(this.getTitle().toString());
        instance.setSorting(mCurrentSorting);

        if(movieList.size() > 0) {
            instance.setMovieList(movieList);
            instance.setNextPage(mNextPage);
            instance.setPosition(mLayoutManager.findFirstVisibleItemPosition());
            instance.setTotalPages(mTotalPages);
        }

        outState.putParcelable(KEY_INSTANCE, instance);
    }


    @Override
    protected void onDestroy() {
        if(mCall!=null) { // Cancel any pending request
            mCall.cancel();
        }
        removeObservers();
        super.onDestroy();
    }
}
