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

package com.arushi.popularmovies.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.arushi.popularmovies.PMApplication;
import com.arushi.popularmovies.R;
import com.arushi.popularmovies.data.ApiRequestInterface;
import com.arushi.popularmovies.data.local.AppDatabase;
import com.arushi.popularmovies.data.local.entity.FavouriteEntity;
import com.arushi.popularmovies.data.model.MovieDetail;
import com.arushi.popularmovies.utils.Constants;
import com.arushi.popularmovies.utils.GlideApp;
import com.arushi.popularmovies.utils.NetworkUtils;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arushi on 03/06/18.
 */

public class DetailActivity extends AppCompatActivity
        implements View.OnClickListener {
    private final String TAG = DetailActivity.class.getSimpleName();

//    private MovieDetail mMovieDetail = null;
    private TextView mName, mYear, mDuration, mRating, mOriginalName, mSynopsis;
    private ToggleButton mBtnFavourite;
    private ScrollView mScrollView;
    private ImageView mPoster;
    private Call<MovieDetail> mCall;
//    private AppDatabase mDb;
    private DetailViewModel mViewModel;
    private boolean mMarkedFavourite = false;
    private MovieDetail mMovieDetail;

    private ConstraintLayout mLayoutError, mLayoutProgress;
    ImageView mProgressBar;
    AnimatedVectorDrawableCompat mAnimatedLoader;
    Animatable2Compat.AnimationCallback mAnimationCallback;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private static final String KEY_DETAIL = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViews();

        /*if(savedInstanceState!=null && savedInstanceState.containsKey(KEY_DETAIL)){
            // Activity recreated
            mMovieDetail = savedInstanceState.getParcelable(KEY_DETAIL);

            if(mMovieDetail!=null){
                title = mMovieDetail.getTitle(); // To check if any detail other than ID is available
                movieId = String.valueOf(mMovieDetail.getId());
            }
        } else {
            // First time creation
            Intent intentThatStartedActivity = getIntent();
            if(intentThatStartedActivity.hasExtra(Constants.KEY_ID)){
                movieId = intentThatStartedActivity.getStringExtra(Constants.KEY_ID);
                mMovieDetail = new MovieDetail();
                mMovieDetail.setId(Integer.parseInt(movieId));
                Log.d(TAG, "Movie ID - " + movieId);
            }
        }*/

//        mDb = AppDatabase.getInstance(this.getApplicationContext());

        setupViewModel();
        /*MovieDetail movieDetail = mViewModel.getMovieDetail();

        String title = movieDetail.getTitle(); // To check if any detail available
        String movieId = null;

        if(title!=null){
            movieId = String.valueOf(movieDetail.getId());
        } else {
            // First time creation
            Intent intentThatStartedActivity = getIntent();
            if(intentThatStartedActivity.hasExtra(Constants.KEY_ID)){
                movieId = intentThatStartedActivity.getStringExtra(Constants.KEY_ID);
                movieDetail.setId(Integer.parseInt(movieId));
                mViewModel.setMovieDetail(movieDetail);
                Log.d(TAG, "Movie ID - " + movieId);
            }
        }*/

        int movieId = mViewModel.getMovieId();

        if(movieId < 0) {
            // First time creation. ID not set in ViewModel
            Intent intentThatStartedActivity = getIntent();
            if(intentThatStartedActivity.hasExtra(Constants.KEY_ID)){
                String id = intentThatStartedActivity.getStringExtra(Constants.KEY_ID);
                movieId = Integer.parseInt(id);
                mViewModel.setMovieId(movieId);
            }
        }

        Log.d(TAG, "Movie ID - " + movieId);

        setupObservers();

/*
        if(!TextUtils.isEmpty(title) && !title.equals("null")) {
            // Details available
            populateUi();
        } else if (!TextUtils.isEmpty(movieId)){
            // ID available
            getDetails(movieId);
        } else {
            showError();
        }
*/

    }

    private void initViews(){
        this.setTitle(getString(R.string.title_movie_details)); // default title

        mScrollView = findViewById(R.id.scroll_view);
        mLayoutProgress = findViewById(R.id.layout_progress);
        mProgressBar = findViewById(R.id.iv_progress);
        showLoader();

        mLayoutError = findViewById(R.id.layout_error);
        Button btnRetry = findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(this);

        mName = findViewById(R.id.tv_title);
        mPoster = findViewById(R.id.iv_movie_poster);
        mYear = findViewById(R.id.tv_year);
        mDuration = findViewById(R.id.tv_duration);
        mRating = findViewById(R.id.tv_rating);
        mOriginalName = findViewById(R.id.tv_original_title);
        mSynopsis = findViewById(R.id.tv_synopsis);

        mBtnFavourite = findViewById(R.id.btn_favourite);
        mBtnFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                updateFavouriteStatus(isChecked);
            }
        });
    }

    private void setupViewModel(){
        // Required by Dagger2 for field injection
        ((PMApplication) getApplication()).getAppComponent().inject(this);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
    }

    private void setupObservers(){
        mViewModel.getMovieDetails().observe(this, new Observer<MovieDetail>() {
            @Override
            public void onChanged(@Nullable MovieDetail movieDetail) {
                if(movieDetail==null) {
                    showError();
                } else {
                    mMovieDetail = movieDetail;
                    populateUi();
                }

            }
        });

        mViewModel.getFavouriteEntity().observe(this, new Observer<FavouriteEntity>() {
            @Override
            public void onChanged(@Nullable FavouriteEntity favouriteEntity) {
                mMarkedFavourite = favouriteEntity!=null;
                mBtnFavourite.setChecked(mMarkedFavourite);
                showFavouriteStatus();
            }
        });
    }

    private void removeObservers(){
        mViewModel.getMovieDetails().removeObservers(this);
        mViewModel.getFavouriteEntity().removeObservers(this);
    }

    private void populateUi(){
        hideLoader();
        mLayoutError.setVisibility(View.GONE);

        String title = mMovieDetail.getTitle();
        this.setTitle(title);

        mName.setText(title);
        GlideApp.with(this)
                .load(mMovieDetail.getPosterPath())
                .dontAnimate()
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .centerCrop()
                .into(mPoster);

        mYear.setText(mMovieDetail.getYear());
        String runtime = String.valueOf(mMovieDetail.getRuntime()) + " " + getString(R.string.minutes);
        mDuration.setText(runtime);
        mOriginalName.setText(mMovieDetail.getOriginalTitle());
        mRating.setText(String.valueOf(mMovieDetail.getVoteAverage()));
        mSynopsis.setText(mMovieDetail.getOverview());
        mScrollView.setVisibility(View.VISIBLE);

    }

   /* private void getDetails(String movieId){
        *//* Get movie details from API *//*
        final ApiRequestInterface request = NetworkUtils.getRetrofitInstance().create(ApiRequestInterface.class);
        mCall = request.getMovieDetails(movieId, Constants.API_KEY);
        Log.d(TAG,"URL called - " + mCall.request().url());

        mCall.enqueue(new Callback<MovieDetail>(){

            @Override
            public void onResponse(@NonNull Call<MovieDetail> call,@NonNull Response<MovieDetail> response) {
                if(response.isSuccessful()) {
                    MovieDetail movieDetail = response.body();
                    mViewModel.setMovieDetail(movieDetail);
                    populateUi();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call,@NonNull Throwable t) {
                if(!mCall.isCanceled()) {
                    Log.e(TAG, "Error getting API response", t);
                    showError();
                }
            }
        });
    }*/

    private void showFavouriteStatus() {
        if (mBtnFavourite.isChecked()) {
            mBtnFavourite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_ripple_filled));
        } else {
            mBtnFavourite.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_ripple_outlined));
        }
        mBtnFavourite.setVisibility(View.VISIBLE);
    }

    private void updateFavouriteStatus(boolean isFavourite){
        if(mMovieDetail==null) return;

        if(isFavourite && !mMarkedFavourite){
//            MovieDetail movieDetail = mViewModel.getMovieDetail();
            FavouriteEntity favourite = new FavouriteEntity(mMovieDetail.getId(),
                    mMovieDetail.getTitle(),
                    mMovieDetail.getPosterOrigPath());
            mViewModel.addFavourite(favourite);
        } else if(!isFavourite){
            mViewModel.deleteFavourite();
        }

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

        mScrollView.setVisibility(View.GONE);
        mLayoutProgress.setVisibility(View.VISIBLE);
    }

    private void hideLoader(){
        mLayoutProgress.setVisibility(View.GONE);
        if(mAnimatedLoader != null) {
            mAnimatedLoader.unregisterAnimationCallback(mAnimationCallback);
        }
    }

    private void showError(){
        mScrollView.setVisibility(View.GONE);
        hideLoader();
        mLayoutError.setVisibility(View.VISIBLE);
    }

   /* @Override
    protected void onSaveInstanceState(Bundle outState) {
        *//* For activity recreate -
         Save movie details *//*
        super.onSaveInstanceState(outState);
        if(mMovieDetail!=null){
            outState.putParcelable(KEY_DETAIL, mMovieDetail);
        }
    }
*/
    @Override
    protected void onDestroy() {
//        if(mCall!=null) { // Cancel any pending request
//            mCall.cancel();
//        }
//
        removeObservers();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_retry:
                showLoader();
                removeObservers();
                setupObservers();

                /*try {
                    int movieId = mViewModel.getMovieId();
                    getDetails(String.valueOf(movieId));
                } catch (Exception e) {
                    showError();
                }*/
                break;
        }
    }
}
