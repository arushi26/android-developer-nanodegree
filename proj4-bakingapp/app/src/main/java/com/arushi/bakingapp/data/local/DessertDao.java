package com.arushi.bakingapp.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.graphics.Movie;

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
