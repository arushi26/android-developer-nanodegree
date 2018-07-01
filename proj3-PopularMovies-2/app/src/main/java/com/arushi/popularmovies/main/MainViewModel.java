package com.arushi.popularmovies.main;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.arushi.popularmovies.data.local.AppDatabase;
import com.arushi.popularmovies.data.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> favourites;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favourites = database.favouriteDao().loadAllFavourites();
    }

    public LiveData<List<Movie>> getFavourites(){
        return favourites;
    }
}
