/*
 *
 *  This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *
 *  As part of Udacity Honor code, your submissions must be your own work, hence
 *  submitting this project as yours will cause you to break the Udacity Honor Code
 *  and the suspension of your account.
 *
 *  I, the author of the project, allow you to check the code as a reference, but if
 *  you submit it, it's your own responsibility if you get expelled.
 *
 *  Besides the above notice, the MIT license applies and this license notice
 *  must be included in all works derived from this project
 *
 *  Copyright (c) 2018 Arushi Pant
 *
 */

package com.arushi.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arushi.popularmovies.data.local.FavouriteDao;
import com.arushi.popularmovies.data.local.entity.FavouriteEntity;
import com.arushi.popularmovies.data.model.CreditsResponse;
import com.arushi.popularmovies.data.model.Movie;
import com.arushi.popularmovies.data.model.MovieDetail;
import com.arushi.popularmovies.data.model.MovieReviewResponse;
import com.arushi.popularmovies.data.model.MovieTrailerResponse;
import com.arushi.popularmovies.data.model.MoviesResponse;
import com.arushi.popularmovies.data.model.VideoResponse;
import com.arushi.popularmovies.utils.Constants;
import com.arushi.popularmovies.utils.NetworkUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton // informs Dagger that this class should be constructed once
public class MovieRepository {
    private final FavouriteDao favouriteDao;
    private final ApiRequestInterface apiRequestInterface;
    private static final String TAG = MovieRepository.class.getSimpleName();

    @Inject
    public MovieRepository(FavouriteDao favouriteDao, ApiRequestInterface apiRequestInterface){
        this.favouriteDao = favouriteDao;
        this.apiRequestInterface = apiRequestInterface;
    }

    public LiveData<FavouriteEntity> getFavouriteById(int movieId){
        return favouriteDao.getFavouriteById(movieId);
    }

    public LiveData<List<Movie>> getFavourites(){
        return favouriteDao.loadAllFavourites();
    }

    public void deleteFavourite(int movieId){
        favouriteDao.deleteFavourite(movieId);
    }

    public void addFavourite(FavouriteEntity favouriteEntity){
        if(favouriteEntity!=null){
            favouriteDao.insertFavourite(favouriteEntity);
        }
    }

    public LiveData<MovieDetail> getMovieDetails(String movieId){
        /* Get movie details from API */
        final MutableLiveData<MovieDetail> data = new MutableLiveData<>();

        Call<MovieDetail> call = apiRequestInterface.getMovieDetails(movieId, Constants.API_KEY);
        Log.d(TAG,"URL called - " + call.request().url());

        call.enqueue(new Callback<MovieDetail>(){

            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                if(response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call,@NonNull Throwable t) {
                Log.e(TAG, "Error getting API response", t);
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<MoviesResponse> getPopularMovies(int nextPage){
        /* Get list of popular movies */
        Call<MoviesResponse> call = apiRequestInterface.getPopularMovieList(Constants.API_KEY, nextPage);
        Log.d(TAG,"URL called - " + call.request().url());
        return handleMovieListResponse(call);
    }

    public LiveData<MoviesResponse> getTopMovies(int nextPage){
        /* Get list of highest rated movies */
        Call<MoviesResponse> call = apiRequestInterface.getTopRatedMovieList(Constants.API_KEY, nextPage);
        Log.d(TAG,"URL called - " + call.request().url());
        return handleMovieListResponse(call);
    }

    private LiveData<MoviesResponse> handleMovieListResponse(Call<MoviesResponse> call){
        final MutableLiveData<MoviesResponse> data = new MutableLiveData<>();

        call.enqueue(new Callback<MoviesResponse>(){

            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                data.setValue(null);
            }

        });

        return data;
    }

    public LiveData<VideoResponse> getMovieTrailers(String movieId){
        /* Get movie trailers from API */
        final MutableLiveData<VideoResponse> data = new MutableLiveData<>();

        Call<VideoResponse> call = apiRequestInterface.getMovieTrailers(movieId, Constants.API_KEY);
        Log.d(TAG,"URL called - " + call.request().url());

        call.enqueue(new Callback<VideoResponse>(){

            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                if(response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                Log.e(TAG, "Error getting API response", t);
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<MovieReviewResponse> getMovieReviews(String movieId){
        /* Get movie reviews from API */
        final MutableLiveData<MovieReviewResponse> data = new MutableLiveData<>();

        Call<MovieReviewResponse> call = apiRequestInterface.getMovieReviews(movieId, Constants.API_KEY);
        Log.d(TAG,"URL called - " + call.request().url());

        call.enqueue(new Callback<MovieReviewResponse>(){

            @Override
            public void onResponse(Call<MovieReviewResponse> call, Response<MovieReviewResponse> response) {
                if(response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieReviewResponse> call, Throwable t) {
                Log.e(TAG, "Error getting API response", t);
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<CreditsResponse> getCredits(String movieId){
        /* Get movie credits from API */
        final MutableLiveData<CreditsResponse> data = new MutableLiveData<>();

        Call<CreditsResponse> call = apiRequestInterface.getMovieCredits(movieId, Constants.API_KEY);
        Log.d(TAG,"URL called - " + call.request().url());

        call.enqueue(new Callback<CreditsResponse>(){

            @Override
            public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                if(response.isSuccessful()) {
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CreditsResponse> call, Throwable t) {
                Log.e(TAG, "Error getting API response", t);
                data.setValue(null);
            }
        });
        return data;
    }
}
