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

import com.arushi.popularmovies.data.ApiRequestInterface;
import com.arushi.popularmovies.utils.Constants;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkServiceModule {

    @Provides
    @Singleton
    ApiRequestInterface provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiRequestInterface.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient
                .Builder()
                .connectTimeout(Constants.API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.API_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.API_READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

}
