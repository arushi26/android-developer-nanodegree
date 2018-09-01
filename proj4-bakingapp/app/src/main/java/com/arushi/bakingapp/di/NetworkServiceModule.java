package com.arushi.bakingapp.di;

import com.arushi.bakingapp.data.remote.ApiRequestInterface;
import com.arushi.bakingapp.utils.Constants;

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
