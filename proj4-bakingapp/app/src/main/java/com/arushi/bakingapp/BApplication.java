/*
 *
 *  *
 *  *  This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *  *
 *  *  As part of Udacity Honor code, your submissions must be your own work, hence
 *  *  submitting this project as yours will cause you to break the Udacity Honor Code
 *  *  and the suspension of your account.
 *  *
 *  *  I, the author of the project, allow you to check the code as a reference, but if
 *  *  you submit it, it's your own responsibility if you get expelled.
 *  *
 *  *  Besides the above notice, the MIT license applies and this license notice
 *  *  must be included in all works derived from this project
 *  *
 *  *  Copyright (c) 2018 Arushi Pant
 *  *
 *
 */

package com.arushi.bakingapp;

import android.app.Application;

import com.arushi.bakingapp.di.AppModule;
import com.arushi.bakingapp.di.ApplicationComponent;
import com.arushi.bakingapp.di.DaggerApplicationComponent;
import com.arushi.bakingapp.utils.CustomDebugTree;
import com.arushi.bakingapp.utils.NoLogsTree;

import javax.inject.Singleton;

import timber.log.Timber;

/* Custom application definition. */
public class BApplication extends Application{
    protected ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponent();

        if (BuildConfig.DEBUG) {
            Timber.plant(new CustomDebugTree());
        } else {
            // No logging for Release APK
            Timber.plant(new NoLogsTree());
        }
    }

    private void initializeComponent(){
        mApplicationComponent = DaggerApplicationComponent
                                .builder()
                                .appModule(new AppModule(this))
                                .build();
    }

    @Singleton
    public ApplicationComponent getAppComponent() {
        return mApplicationComponent;
    }

}
