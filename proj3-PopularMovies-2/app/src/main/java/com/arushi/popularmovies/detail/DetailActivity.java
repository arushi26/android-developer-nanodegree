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
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.Animatable2Compat;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.arushi.popularmovies.PMApplication;
import com.arushi.popularmovies.R;
import com.arushi.popularmovies.data.local.entity.FavouriteEntity;
import com.arushi.popularmovies.data.model.CreditsResponse;
import com.arushi.popularmovies.data.model.Genre;
import com.arushi.popularmovies.data.model.MovieDetail;
import com.arushi.popularmovies.data.model.MovieReviewResponse;
import com.arushi.popularmovies.data.model.VideoResponse;
import com.arushi.popularmovies.utils.Constants;
import com.arushi.popularmovies.utils.GlideApp;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by arushi on 03/06/18.
 */

public class DetailActivity extends AppCompatActivity
        implements View.OnClickListener {
    private final String TAG = DetailActivity.class.getSimpleName();

    private TextView mName, mYear, mDuration, mRating, mOriginalName, mSynopsis;
    private TextView mLblCast, mLblTrailers, mLblReviews, mTvGenres;
    private ToggleButton mBtnFavourite;
    private NestedScrollView mNestedScrollView;
    private ImageView mPoster, mBgPoster;
    private DetailViewModel mViewModel;
    private boolean mMarkedFavourite = false;
    private MovieDetail mMovieDetail = null;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private CreditsAdapter mCreditsAdapter;
    private RecyclerView mRvCast, mRvTrailers, mRvReviews;

    private ConstraintLayout mLayoutError, mLayoutProgress;
    ImageView mProgressBar;
    AnimatedVectorDrawableCompat mAnimatedLoader;
    Animatable2Compat.AnimationCallback mAnimationCallback;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initViews();

        setupViewModel();

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
    }

    private void initViews(){
        this.setTitle(getString(R.string.title_movie_details)); // default title

        mNestedScrollView = findViewById(R.id.scroll_view);
        mLayoutProgress = findViewById(R.id.layout_progress);
        mProgressBar = findViewById(R.id.iv_progress);
        showLoader();

        mLayoutError = findViewById(R.id.layout_error);
        Button btnRetry = findViewById(R.id.btn_retry);
        btnRetry.setOnClickListener(this);

        mName = findViewById(R.id.tv_title);
        mPoster = findViewById(R.id.iv_movie_poster);
        mBgPoster = findViewById(R.id.img_bg_poster);
        mYear = findViewById(R.id.tv_year);
        mDuration = findViewById(R.id.tv_duration);
        mRating = findViewById(R.id.tv_rating);
        mOriginalName = findViewById(R.id.tv_original_title);
        mSynopsis = findViewById(R.id.tv_synopsis);

        mTvGenres = findViewById(R.id.tv_genres);
        mBtnFavourite = findViewById(R.id.btn_favourite);
        mBtnFavourite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                updateFavouriteStatus(isChecked);
            }
        });

        /* Cast */
        mLblCast = findViewById(R.id.lbl_credits);
        mRvCast = findViewById(R.id.rv_credits);
        RecyclerView.LayoutManager castLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvCast.setLayoutManager(castLayoutManager);
        mRvCast.setHasFixedSize(true);
        mRvCast.setNestedScrollingEnabled(false);// Smooth scrolling when Recyclerview within Scrollview

        mCreditsAdapter = new CreditsAdapter(this);
        mRvCast.setAdapter(mCreditsAdapter);

        /* Trailers */
        mLblTrailers = findViewById(R.id.lbl_trailers);
        mRvTrailers = findViewById(R.id.rv_trailers);
        RecyclerView.LayoutManager trailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTrailers.setLayoutManager(trailerLayoutManager);
        mRvTrailers.setHasFixedSize(true);
        mRvTrailers.setNestedScrollingEnabled(false);// Smooth scrolling when Recyclerview within Scrollview

        mTrailerAdapter = new TrailerAdapter(this);
        mRvTrailers.setAdapter(mTrailerAdapter);

        /* Reviews */
        mLblReviews = findViewById(R.id.lbl_reviews);
        mRvReviews = findViewById(R.id.rv_reviews);
        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvReviews.setLayoutManager(reviewLayoutManager);
        mRvReviews.setHasFixedSize(true);
        mRvReviews.setNestedScrollingEnabled(false);// Smooth scrolling when Recyclerview within Scrollview

        mReviewAdapter = new ReviewAdapter(this);
        mRvReviews.setAdapter(mReviewAdapter);

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

        mViewModel.getCast().observe(this, new Observer<CreditsResponse>() {
            @Override
            public void onChanged(@Nullable CreditsResponse creditsResponse) {
                if(creditsResponse!=null) {
                    mCreditsAdapter.setCastList(creditsResponse.getCast());
                    if(mCreditsAdapter.getItemCount()>0) {
                        mLblCast.setVisibility(View.VISIBLE);
                        mRvCast.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight));
                    }
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

        mViewModel.getTrailers().observe(this, new Observer<VideoResponse>() {
            @Override
            public void onChanged(@Nullable VideoResponse movieTrailerResponse) {
                if(movieTrailerResponse!=null) {
                    mTrailerAdapter.setTrailerList(movieTrailerResponse.getResults());
                    if(mTrailerAdapter.getItemCount()>0) {
                        mLblTrailers.setVisibility(View.VISIBLE);
                        mRvTrailers.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight));
                    }
                }
            }
        });

        mViewModel.getReviews().observe(this, new Observer<MovieReviewResponse>() {
            @Override
            public void onChanged(@Nullable MovieReviewResponse movieReviewResponse) {
                if(movieReviewResponse!=null) {
                    mReviewAdapter.setReviewList(movieReviewResponse.getResults());
                    if(mReviewAdapter.getItemCount()>0) {
                        mLblReviews.setVisibility(View.VISIBLE);
                        mRvReviews.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight));
                    }
                }
            }
        });
    }

    private void removeObservers(){
        mViewModel.getMovieDetails().removeObservers(this);
        mViewModel.getCast().removeObservers(this);
        mViewModel.getFavouriteEntity().removeObservers(this);
        mViewModel.getTrailers().removeObservers(this);
        mViewModel.getReviews().removeObservers(this);
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

        GlideApp.with(this)
                .load(mMovieDetail.getBackdropPath())
                .dontAnimate()
                .centerCrop()
                .into(mBgPoster);

        mYear.setText(mMovieDetail.getYear());
        String runtime = String.valueOf(mMovieDetail.getRuntime()) + " " + getString(R.string.minutes);
        mDuration.setText(runtime);
        mOriginalName.setText(mMovieDetail.getOriginalTitle());
        mRating.setText(String.valueOf(mMovieDetail.getVoteAverage()));

        List<Genre> genres = mMovieDetail.getGenres();
        StringBuilder genresList = new StringBuilder("");

        for (int i=0; i < genres.size(); i++) {
            genresList.append(genres.get(i).getName());
            if(i != genres.size() - 1) genresList.append(" | ");
        }

        mTvGenres.setText(genresList);

        mSynopsis.setText(mMovieDetail.getOverview());
        mNestedScrollView.setVisibility(View.VISIBLE);

    }

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
            /* Infinite repeat animation for loader AVD */
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

        mNestedScrollView.setVisibility(View.GONE);
        mLayoutProgress.setVisibility(View.VISIBLE);
    }

    private void hideLoader(){
        mLayoutProgress.setVisibility(View.GONE);
        if(mAnimatedLoader != null) {
            mAnimatedLoader.unregisterAnimationCallback(mAnimationCallback);
        }
    }

    private void showError(){
        mNestedScrollView.setVisibility(View.GONE);
        hideLoader();
        mLayoutError.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_retry:
                showLoader();
                removeObservers();
                setupObservers();
                break;
        }
    }
}
