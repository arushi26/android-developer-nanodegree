package com.arushi.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.arushi.popularmovies.data.ApiRequestInterface;
import com.arushi.popularmovies.data.model.Movie;
import com.arushi.popularmovies.data.model.MoviesResponse;
import com.arushi.popularmovies.utils.Constants;
import com.arushi.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener{
    final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MovieListAdapter mAdapter;
    private LinearLayout mLayoutError;
    private Button mBtnRetry;
    private int currentSorting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        currentSorting = Constants.SORT_POPULAR;
        getMovieList();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutError = findViewById(R.id.layout_error);
        mBtnRetry = findViewById(R.id.btn_retry);
        mBtnRetry.setOnClickListener(this);
    }

    private void getMovieList(){
        final ApiRequestInterface request = NetworkUtils.getRetrofitInstance().create(ApiRequestInterface.class);
        Call mCall;

        switch (currentSorting){
            case Constants.SORT_POPULAR:
                mCall = request.getPopularMovieList(Constants.API_KEY);
                break;
            case Constants.SORT_TOP_RATED:
                mCall = request.getTopRatedMovieList(Constants.API_KEY);
                break;
            default:
                mCall = request.getPopularMovieList(Constants.API_KEY);
        }

        Log.d(TAG,"URL called - " + mCall.request().url());

        mCall.enqueue(new Callback<MoviesResponse>() {

            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if(response.isSuccessful()){
                    MoviesResponse movieResponse = response.body();

                    try{
                        List<Movie> movieList = movieResponse.getMovieList();
                        mAdapter.setMovieList(movieList);
                        showData();
                    } catch (Exception e){
                        Log.e(TAG,"Error getting movie list", e);
                        showError();
                    }
                } else {
                    String error = response.errorBody().toString();
                    Log.e(TAG,"Error getting movie list - " + error);
                    showError();
                }

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG,"Error getting movie list", t);
                showError();
            }
        });
    }

    // TODO Show loader method
    // TODO Load more on Scroll

    private void showData(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mLayoutError.setVisibility(View.GONE);
    }

    private void showError(){
        mRecyclerView.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
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
                currentSorting = Constants.SORT_POPULAR;
                getMovieList();
                setTitle(R.string.menu_popular);
                break;
            case R.id.menu_top_rated:
                currentSorting = Constants.SORT_TOP_RATED;
                getMovieList();
                setTitle(R.string.menu_top_rated);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_retry:
                getMovieList();
                break;
        }
    }
}
