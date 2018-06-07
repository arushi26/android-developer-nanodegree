package com.arushi.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by arushi on 30/05/18.
 */

public class Constants {
    public final static String BASE_URL = "http://api.themoviedb.org/3/";
    public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public final static String IMAGE_SIZE = "w185";
    public final static int SORT_POPULAR = 0;
    public final static int SORT_TOP_RATED = 1;

    /* Register in TMDB & request API Key --> https://www.themoviedb.org/faq/api */
    public final static String API_KEY = "ENTER_YOUR_KEY_HERE";

    /* API Timeouts - (in Seconds) */
    public static final int API_CONNECT_TIMEOUT = 20; // 20 sec
    public static final int API_WRITE_TIMEOUT = 240; // 4 min
    public static final int API_READ_TIMEOUT = 30; // 30 sec

    /* Keys */
    public static final String KEY_ID = "ID";

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
