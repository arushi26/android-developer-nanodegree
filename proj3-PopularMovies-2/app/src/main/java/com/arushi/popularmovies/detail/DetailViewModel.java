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


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.arushi.popularmovies.data.MovieRepository;
import com.arushi.popularmovies.data.local.AppDatabase;
import com.arushi.popularmovies.data.local.FavouriteDao;
import com.arushi.popularmovies.data.local.entity.FavouriteEntity;
import com.arushi.popularmovies.data.model.MovieDetail;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {
//    private LiveData<FavouriteEntity> favouriteEntity;
//    private MovieDetail movieDetail = null;
    private int movieId = -1;
    private LiveData<MovieDetail> movieDetails = null;
    private final MovieRepository movieRepository;

    /*public DetailViewModel(AppDatabase database, MovieDetail movieDetail) {
        this.movieDetail = movieDetail;
        favouriteEntity = database.favouriteDao().getFavouriteById(this.movieDetail.getId());
    }*/

    @Inject
    public DetailViewModel(MovieRepository repository){
        movieRepository = repository;
    }

//    public void setMovieDetail(MovieDetail movieDetail){
//        this.movieDetail = movieDetail;
//    }
//
//    public MovieDetail getMovieDetail() {
//        return movieDetail;
//    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }

    public LiveData<MovieDetail> getMovieDetails() {
        /*if(movieDetail==null) {
            movieDetail = new MovieDetail();
        }*/
        if(movieDetails==null)
        {
            movieDetails = movieRepository.getMovieDetails(String.valueOf(movieId));
        }

        return movieDetails;
    }

    public LiveData<FavouriteEntity> getFavouriteEntity() {
        return movieRepository.getFavouriteById(this.movieId);
    }


    public void addFavourite(FavouriteEntity favouriteEntity){
        AddAsFavourite addAsFavourite = new AddAsFavourite();
        addAsFavourite.execute(favouriteEntity);
    }

    private class AddAsFavourite extends AsyncTask<FavouriteEntity, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteEntity... favouriteEntities) {
            movieRepository.addFavourite(favouriteEntities[0]);
            return null;
        }
    }

    public void deleteFavourite(){
        if(movieId >= 0){
            DeleteFromFavourite deleteFromFavourite = new DeleteFromFavourite();
            deleteFromFavourite.execute(this.movieId);
        }
    }

    private class DeleteFromFavourite extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            movieRepository.deleteFavourite(integers[0]);
            return null;
        }
    }
}
