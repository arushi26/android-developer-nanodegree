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

package com.arushi.bakingapp.utils;

import java.util.concurrent.TimeUnit;

public class Constants {
    public final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/";

    /* API Timeouts - (in Seconds) */
    public static final long API_CONNECT_TIMEOUT = TimeUnit.SECONDS.toSeconds(20); // 20 sec
    public static final long API_WRITE_TIMEOUT = TimeUnit.MINUTES.toSeconds(4); // 4 min
    public static final long API_READ_TIMEOUT = TimeUnit.SECONDS.toSeconds(30); // 30 sec

}
