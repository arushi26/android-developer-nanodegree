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

package com.arushi.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

/**
 * Created by arushi on 30/05/18.
 */

public class Constants {
    public final static String BASE_URL = "http://api.themoviedb.org/3/";
    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public final static String IMAGE_SIZE = "w185";
    public final static int SORT_POPULAR = 0;
    public final static int SORT_TOP_RATED = 1;
    public final static int SORT_FAVOURITES = 2;

    /* Register in TMDB & request API Key --> https://www.themoviedb.org/faq/api */
    public final static String API_KEY = "9c632fb2238b819734456db8e26178b0";//"ENTER_YOUR_KEY_HERE";

    /* API Timeouts - (in Seconds) */
    public static final long API_CONNECT_TIMEOUT = TimeUnit.SECONDS.toSeconds(20); // 20 sec
    public static final long API_WRITE_TIMEOUT = TimeUnit.MINUTES.toSeconds(4); // 4 min
    public static final long API_READ_TIMEOUT = TimeUnit.SECONDS.toSeconds(30); // 30 sec

    /* Keys */
    public static final String KEY_ID = "ID";

    public static boolean isOnline(Context context) {
        /* Consult - https://stackoverflow.com/a/4009133/1583837 for code */
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm==null) {return false;}

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
