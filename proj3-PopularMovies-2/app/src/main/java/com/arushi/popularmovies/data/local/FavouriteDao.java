package com.arushi.popularmovies.data.local;
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

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.arushi.popularmovies.data.local.entity.FavouriteEntity;
import com.arushi.popularmovies.data.model.Movie;

import java.util.List;

@Dao
public interface FavouriteDao {
    @Query("SELECT movie_id AS movieId, poster_path AS posterPath FROM favourites ORDER BY id DESC")
    LiveData<List<Movie>> loadAllFavourites();

    @Insert
    void insertFavourite(FavouriteEntity favouriteEntity);

    @Query("DELETE FROM favourites WHERE movie_id = :movieId")
    void deleteFavourite(int movieId);

    @Query("SELECT * FROM favourites WHERE movie_id = :movieId")
    LiveData<FavouriteEntity> getFavouriteById(int movieId);
}
