package com.arushi.popularmovies.main;
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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.arushi.popularmovies.data.MovieRepository;
import com.arushi.popularmovies.data.model.Movie;
import com.arushi.popularmovies.data.model.MoviesResponse;
import com.arushi.popularmovies.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private final MovieRepository movieRepository;
    private LiveData<List<Movie>> favouritesList = null;

    @Inject
    public MainViewModel(MovieRepository repository) {
        this.movieRepository = repository;
    }

    public LiveData<List<Movie>> getFavourites(){
        if (favouritesList == null || favouritesList.getValue() == null) {
            favouritesList = movieRepository.getFavourites();
        }
        return favouritesList;
    }

    public LiveData<MoviesResponse> getPopularMovies(int nextPage){
        return movieRepository.getPopularMovies(nextPage);
    }

    public LiveData<MoviesResponse> getTopRatedMovies(int nextPage){
        return movieRepository.getTopMovies(nextPage);
    }

}
