package com.arushi.popularmovies.detail;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.arushi.popularmovies.data.local.AppDatabase;
import com.arushi.popularmovies.data.model.MovieDetail;
import com.arushi.popularmovies.detail.DetailViewModel;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final AppDatabase mDb;
    private final MovieDetail mMovieDetail;

    public DetailViewModelFactory(AppDatabase database, MovieDetail movieDetail) {
        mDb = database;
        mMovieDetail = movieDetail;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) modelClass.cast(new DetailViewModel(mDb, mMovieDetail));
    }
}

