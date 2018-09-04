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

package com.arushi.bakingapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.arushi.bakingapp.data.local.entity.DessertEntity;
import com.arushi.bakingapp.data.local.entity.IngredientEntity;
import com.arushi.bakingapp.data.local.entity.StepEntity;

import java.util.List;

@Dao
public interface DessertDao {
    @Query("SELECT * FROM desserts ORDER BY id DESC")
    LiveData<List<DessertEntity>> loadDessertList();

    @Query("SELECT * FROM desserts WHERE id = :id")
    LiveData<DessertEntity> getDessert(int id);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    long insertDessert(DessertEntity dessertEntity);

    @Query("DELETE FROM ingredients WHERE dessertId = :dessertId;")
    void deleteRecipeIngredients(int dessertId);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insertIngredients(List<IngredientEntity> ingredientEntities);

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    void insertRecipeSteps(List<StepEntity> stepEntities);

    @Query("SELECT * FROM ingredients WHERE dessertId = :dessertId")
    LiveData<List<IngredientEntity>> getIngredients(int dessertId);

    @Query("SELECT * FROM ingredients WHERE dessertId = :dessertId")
    List<IngredientEntity> getIngredientEntities(int dessertId);

    @Query("SELECT * FROM steps WHERE dessertId = :dessertId")
    LiveData<List<StepEntity>> getRecipeSteps(int dessertId);

    @Query("SELECT * FROM steps WHERE id = :id AND dessertId = :dessertId")
    LiveData<StepEntity> getRecipeStep(int id, int dessertId);
}
