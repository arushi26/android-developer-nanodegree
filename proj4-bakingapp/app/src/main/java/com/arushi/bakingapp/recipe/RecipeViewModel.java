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

package com.arushi.bakingapp.recipe;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.arushi.bakingapp.data.DessertRepository;
import com.arushi.bakingapp.data.local.entity.DessertEntity;
import com.arushi.bakingapp.data.local.entity.IngredientEntity;
import com.arushi.bakingapp.data.local.entity.StepEntity;

import java.util.List;

import javax.inject.Inject;

public class RecipeViewModel extends ViewModel {
    private DessertRepository mRepository;
    private LiveData<DessertEntity> mDessert = null;
    private LiveData<List<IngredientEntity>> mIngredients = null;
    private LiveData<List<StepEntity>> mRecipeSteps = null;

    @Inject
    public RecipeViewModel(DessertRepository dessertRepository) {
        this.mRepository = dessertRepository;
    }

    public LiveData<DessertEntity> getDessert(int id){
        if (mDessert == null) {
            mDessert = mRepository.getDessert(id);
        }
        return mDessert;
    }

    public LiveData<List<IngredientEntity>> getIngredients(int id){
        if (mIngredients == null) {
            mIngredients = mRepository.getIngredients(id);
        }
        return mIngredients;
    }

    public LiveData<List<StepEntity>> getRecipeSteps(int id){
        if (mRecipeSteps == null) {
            mRecipeSteps = mRepository.getRecipeSteps(id);
        }
        return mRecipeSteps;
    }
}
