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

package com.arushi.popularmovies.data;

import com.arushi.popularmovies.data.model.CreditsResponse;
import com.arushi.popularmovies.data.model.MovieDetail;
import com.arushi.popularmovies.data.model.MovieReviewResponse;
import com.arushi.popularmovies.data.model.MoviesResponse;
import com.arushi.popularmovies.data.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by arushi on 30/05/18.
 */

public interface ApiRequestInterface {

    @Headers({"Content-Type: application/json",
            "User-Agent: Popular-Movies"})
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovieList(@Query("api_key") String apiKey,
                                             @Query("page") int pageNum);

    @Headers({"Content-Type: application/json",
            "User-Agent: Popular-Movies"})
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovieList(@Query("api_key") String apiKey,
                                              @Query("page") int pageNum);

    @Headers({"Content-Type: application/json",
            "User-Agent: Popular-Movies"})
    @GET("movie/{movieId}")
    Call<MovieDetail> getMovieDetails(@Path("movieId") String movieId,
                                      @Query("api_key") String apiKey);

    @Headers({"Content-Type: application/json",
            "User-Agent: Popular-Movies"})
    @GET("movie/{movieId}/videos")
    Call<VideoResponse> getMovieTrailers(@Path("movieId") String movieId,
                                         @Query("api_key") String apiKey);

    @Headers({"Content-Type: application/json",
            "User-Agent: Popular-Movies"})
    @GET("movie/{movieId}/reviews")
    Call<MovieReviewResponse> getMovieReviews(@Path("movieId") String movieId,
                                               @Query("api_key") String apiKey);

    @Headers({"Content-Type: application/json",
            "User-Agent: Popular-Movies"})
    @GET("movie/{movieId}/credits")
    Call<CreditsResponse> getMovieCredits(@Path("movieId") String movieId,
                                          @Query("api_key") String apiKey);
}
