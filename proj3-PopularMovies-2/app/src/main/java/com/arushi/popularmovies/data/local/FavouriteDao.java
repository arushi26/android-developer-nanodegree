package com.arushi.popularmovies.data.local;

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
