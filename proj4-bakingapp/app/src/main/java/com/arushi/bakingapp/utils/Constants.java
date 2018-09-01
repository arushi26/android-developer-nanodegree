package com.arushi.bakingapp.utils;

import java.util.concurrent.TimeUnit;

public class Constants {
    public final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/";

    /* API Timeouts - (in Seconds) */
    public static final long API_CONNECT_TIMEOUT = TimeUnit.SECONDS.toSeconds(20); // 20 sec
    public static final long API_WRITE_TIMEOUT = TimeUnit.MINUTES.toSeconds(4); // 4 min
    public static final long API_READ_TIMEOUT = TimeUnit.SECONDS.toSeconds(30); // 30 sec

}
