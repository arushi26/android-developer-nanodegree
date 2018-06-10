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

package com.arushi.popularmovies.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by arushi on 30/05/18.
 */

public class MoviesResponse {

        @SerializedName("page")
        @Expose
        private int page;
        @SerializedName("total_results")
        @Expose
        private int totalResults;
        @SerializedName("total_pages")
        @Expose
        private int totalPages;
        @SerializedName("results")
        @Expose
        private List<Movie> movieList = null;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public List<Movie> getMovieList() {
            return movieList;
        }

        public void setMovieList(List<Movie> movieList) {
            this.movieList = movieList;
        }
}
