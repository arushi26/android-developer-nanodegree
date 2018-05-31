package com.arushi.popularmovies.data;

import com.arushi.popularmovies.data.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by arushi on 30/05/18.
 */

public interface ApiRequestInterface {

    @Headers({"Content-Type: application/json",
            "User-Agent: Popular-Movies"})
    @GET("3/movie/popular")
    Call<MoviesResponse> getPopularMovieList(@Query("api_key") String apiKey);

    @Headers({"Content-Type: application/json",
            "User-Agent: Popular-Movies"})
    @GET("3/movie/top_rated")
    Call<MoviesResponse> getTopRatedMovieList(@Query("api_key") String apiKey);

}
