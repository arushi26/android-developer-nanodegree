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
