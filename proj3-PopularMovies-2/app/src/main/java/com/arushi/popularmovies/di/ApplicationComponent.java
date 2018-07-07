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

import com.arushi.popularmovies.data.MovieRepository;
import com.arushi.popularmovies.detail.DetailActivity;
import com.arushi.popularmovies.main.MainActivity;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {  AppModule.class,
                        DataServiceModule.class,
                        NetworkServiceModule.class,
                        ViewModelModule.class})
public interface ApplicationComponent {

    MovieRepository getMovieRepository();

    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);
}
