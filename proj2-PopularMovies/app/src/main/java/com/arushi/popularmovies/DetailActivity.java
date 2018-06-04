package com.arushi.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arushi.popularmovies.data.ApiRequestInterface;
import com.arushi.popularmovies.data.model.Movie;
import com.arushi.popularmovies.data.model.MovieDetail;
import com.arushi.popularmovies.data.model.MoviesResponse;
import com.arushi.popularmovies.utils.Constants;
import com.arushi.popularmovies.utils.GlideApp;
import com.arushi.popularmovies.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arushi on 03/06/18.
 */

public class DetailActivity extends AppCompatActivity {
    MovieDetail mMovieDetail;
    TextView mName, mYear, mDuration, mRating, mOriginalName, mSynopsis;
    ImageView mPoster;
    Call mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String movieId = null;

        Intent intentThatStartedActivity = getIntent();
        if(intentThatStartedActivity.hasExtra(Constants.KEY_ID)){
            movieId = intentThatStartedActivity.getStringExtra(Constants.KEY_ID);
            Log.d("Details: Movie ID - ", movieId);
        } else{
            noData();
        }

        if(movieId==null) noData();

        initViews();
        getDetails(movieId);
    }

    private void initViews(){
        mName = findViewById(R.id.tv_title);
        mPoster = findViewById(R.id.iv_movie_poster);
        mYear = findViewById(R.id.tv_year);
        mDuration = findViewById(R.id.tv_duration);
        mRating = findViewById(R.id.tv_rating);
        mOriginalName = findViewById(R.id.tv_original_title);
        mSynopsis = findViewById(R.id.tv_synopsis);
    }

    private void populateUi(){
        String title = mMovieDetail.getTitle();
        this.setTitle(title);

        mName.setText(title);
        GlideApp.with(this)
                .load(mMovieDetail.getPosterPath())
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
    }

    private void getDetails(String movieId){
        final ApiRequestInterface request = NetworkUtils.getRetrofitInstance().create(ApiRequestInterface.class);
        mCall = request.getMovieDetails(movieId, Constants.API_KEY);

        mCall.enqueue(new Callback<MovieDetail>(){

            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if(response.isSuccessful()) {
                    mMovieDetail = response.body();
                    populateUi();
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                if(!mCall.isCanceled()) {
                    Log.e("Details", "Error getting API response", t);
                }
                noData();
            }
        });
    }

    private void noData(){
        if(!mCall.isCanceled()) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_movie_data_not_available), Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    // TODO save instance state


    @Override
    protected void onDestroy() {
        if(mCall!=null) {
            mCall.cancel();
        }
        super.onDestroy();
    }
}
