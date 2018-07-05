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

package com.arushi.popularmovies.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.arushi.popularmovies.data.ApiRequestInterface;
import com.arushi.popularmovies.data.MovieRepository;
import com.arushi.popularmovies.data.local.AppDatabase;
import com.arushi.popularmovies.data.local.FavouriteDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NetworkServiceModule.class, AppModule.class})
public class DataServiceModule {

    @Provides
    @Singleton
    MovieRepository provideMovieRepository(FavouriteDao favouriteDao, ApiRequestInterface apiRequestInterface){
        return new MovieRepository(favouriteDao, apiRequestInterface);
    }

    @Provides
    @Singleton
    FavouriteDao provideFavouriteDao(AppDatabase database){
        return database.favouriteDao();
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(Application application){
        return Room.databaseBuilder(
                application,
                AppDatabase.class,
                "popularmovies.db"
                ).build();
    }

}
