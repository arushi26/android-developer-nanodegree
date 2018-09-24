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

package com.udacity.gradle.builditbigger.utils;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.pratibimb.jokedisplaylibrary.JokeActivity;
import com.udacity.gradle.builditbigger.backend.jokeApi.JokeApi;

import java.io.IOException;


public class EndpointsAsyncTask extends AsyncTask<Object, Void, String> {
    private static JokeApi jokeService = null;
    private Context context;
    @Nullable private GCEIdlingResource idlingResource;
    LoadListener mLoadListener;

    public interface LoadListener {
        void jokeLoaded();
    }

    /* @params: Context , GCEIdlingResource */
    @Override
    protected String doInBackground(Object... objects) {
        context = (Context) objects[0];

        try {
            mLoadListener = (LoadListener) context;
        } catch (ClassCastException e) {
        throw new ClassCastException(context.toString()
                + " must implement LoadListener");
        }

        idlingResource = (GCEIdlingResource) objects[1];
        // idlingresource is null in production
        if(idlingResource!=null){
            idlingResource.setIdleState(false);
        }

        if ( jokeService == null ){ // Only do this once
            JokeApi.Builder builder = new JokeApi.Builder( AndroidHttp.newCompatibleTransport(),
                            new AndroidJsonFactory(), null )
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            jokeService = builder.build();
        }


        try {
            return jokeService.tellJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Intent intent = new Intent(context, JokeActivity.class);
        intent.putExtra("joke", result);

        if(idlingResource!=null){
            idlingResource.setIdleState(true);
        }

        context.startActivity(intent);
        mLoadListener.jokeLoaded();
    }
}
