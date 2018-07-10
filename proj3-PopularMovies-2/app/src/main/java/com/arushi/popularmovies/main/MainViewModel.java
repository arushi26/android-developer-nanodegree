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
    private int nextPage = 1;
    private List<Movie> movieList = null;
    private int currentSorting = Constants.SORT_POPULAR;
    private LiveData<List<Movie>> favouritesList = null;
    private LiveData<MoviesResponse> popularList = null,
                                     topRatedList = null;

    @Inject
    public MainViewModel(MovieRepository repository) {
        this.movieRepository = repository;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    @NonNull
    public List<Movie> getMovieList() {
        if(movieList==null) return new ArrayList<>();
        return movieList;
    }
    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public void setCurrentSorting(int currentSorting) {
        this.currentSorting = currentSorting;
    }

    public int getCurrentSorting() {
        return currentSorting;
    }

    public LiveData<List<Movie>> getFavourites(){
        if (favouritesList == null) {
            favouritesList = movieRepository.getFavourites();
        }
        return favouritesList;
    }

    private LiveData<MoviesResponse> getPopularMovies(){
        if(popularList == null) {
            popularList = movieRepository.getPopularMovies(nextPage);
        }
        return popularList;
    }

    private LiveData<MoviesResponse> getTopRatedMovies(){
        if (topRatedList == null) {
            topRatedList = movieRepository.getTopMovies(nextPage);
        }
        return topRatedList;
    }

    public LiveData<MoviesResponse> getMoviesFromAPI(final boolean isFirstRequest){
        if(isFirstRequest) {
            // To get the 1st page
            nextPage = 1;
        }

        switch (currentSorting){
            case Constants.SORT_POPULAR:
                return getPopularMovies();
            case Constants.SORT_TOP_RATED:
                return getTopRatedMovies();
            default:
                return getPopularMovies();
        }
    }

}
