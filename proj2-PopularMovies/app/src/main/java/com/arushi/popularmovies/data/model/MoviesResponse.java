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
