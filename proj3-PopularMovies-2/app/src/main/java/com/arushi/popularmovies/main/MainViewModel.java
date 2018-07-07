package com.arushi.popularmovies.main;

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

    private void initFavourites() {
        favouritesList = movieRepository.getFavourites();
    }
    public LiveData<List<Movie>> getFavourites(){
        if (favouritesList == null) {
            initFavourites();
        }
        return favouritesList;
    }

    private LiveData<MoviesResponse> getPopularMovies(){
        return movieRepository.getPopularMovies(nextPage);
    }

    private LiveData<MoviesResponse> getTopRatedMovies(){
        return movieRepository.getTopMovies(nextPage);
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
