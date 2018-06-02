package com.arushi.popularmovies;

import android.support.constraint.ConstraintLayout;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.arushi.popularmovies.data.ApiRequestInterface;
import com.arushi.popularmovies.data.model.Movie;
import com.arushi.popularmovies.data.model.MoviesResponse;
import com.arushi.popularmovies.utils.Constants;
import com.arushi.popularmovies.utils.GlideApp;
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
    private ConstraintLayout mLayoutError, mLayoutProgress;
    private Button mBtnRetry;
    private ImageView mProgressBar;
    private int mNextPage = 1;
    private int mCurrentSorting;
    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;
    // Start loading when visible threshold reached
    private int mVisibleThreshold = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        mCurrentSorting = Constants.SORT_POPULAR;
        getMovieList(true);
    }

    private void initViews() {
        mLayoutProgress = findViewById(R.id.layout_progress);
        mProgressBar = findViewById(R.id.iv_progress);
        GlideApp.with(this)
                .load(R.drawable.blocks_loading_io)
                .centerInside()
                .into(mProgressBar);
        showLoader();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mRecyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        // Pagination
        RecyclerView.OnScrollListener onScrollListener =
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int totalItemCount = layoutManager.getItemCount();
                        int lastItemVisiblePosition = layoutManager.findLastVisibleItemPosition();

                        if(!mIsLoading && !mIsLastPage
                                && totalItemCount <= (lastItemVisiblePosition + mVisibleThreshold)){
                            getMovieList(false);
                        }
                    }
                };
        mRecyclerView.addOnScrollListener(onScrollListener);

        mLayoutError = findViewById(R.id.layout_error);
        mBtnRetry = findViewById(R.id.btn_retry);
        mBtnRetry.setOnClickListener(this);
    }

    private void getMovieList(final boolean isFirstRequest){
        mIsLoading = true;
        final ApiRequestInterface request = NetworkUtils.getRetrofitInstance().create(ApiRequestInterface.class);
        Call mCall;
        int nextPage;

        if(isFirstRequest) {
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
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if(response.isSuccessful()){
                    MoviesResponse movieResponse = response.body();

                    try{
                        List<Movie> movieList = movieResponse.getMovieList();
                        mAdapter.addMovieList(movieList);
                        showData();
                        mNextPage = movieResponse.getPage() + 1;
                        if (mNextPage > movieResponse.getTotalPages()) {
                            mIsLastPage = true;
                        }
                    } catch (Exception e){
                        Log.e(TAG,"Error getting movie list", e);
                        if(isFirstRequest) showError();
                    }
                } else {
                    String error = response.errorBody().toString();
                    Log.e(TAG,"Error getting movie list - " + error);
                    if(isFirstRequest) showError();
                }
                mIsLoading = false;
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG,"Error getting movie list", t);
                if(isFirstRequest) showError();
                mIsLoading = false;
            }
        });
    }

    // TODO Load more on Scroll

    private void showLoader(){
        mLayoutProgress.setVisibility(View.VISIBLE);
    }

    private void showData(){
        mLayoutProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLayoutError.setVisibility(View.GONE);
    }

    private void showError(){
        mLayoutProgress.setVisibility(View.GONE);
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
                mCurrentSorting = Constants.SORT_POPULAR;
                sortChangeSelected();
                setTitle(R.string.menu_popular);
                return true;
            case R.id.menu_top_rated:
                mCurrentSorting = Constants.SORT_TOP_RATED;
                sortChangeSelected();
                setTitle(R.string.menu_top_rated);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortChangeSelected(){
        showLoader();
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

}
