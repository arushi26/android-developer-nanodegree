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
