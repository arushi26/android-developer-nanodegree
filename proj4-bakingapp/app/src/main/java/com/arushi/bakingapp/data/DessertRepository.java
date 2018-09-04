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

package com.arushi.bakingapp.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arushi.bakingapp.data.local.DessertDao;
import com.arushi.bakingapp.data.local.entity.DessertEntity;
import com.arushi.bakingapp.data.local.entity.IngredientEntity;
import com.arushi.bakingapp.data.local.entity.StepEntity;
import com.arushi.bakingapp.data.remote.ApiRequestInterface;
import com.arushi.bakingapp.data.remote.model.Dessert;
import com.arushi.bakingapp.data.remote.model.Ingredient;
import com.arushi.bakingapp.data.remote.model.Step;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import timber.log.Timber;

@Singleton // informs Dagger that this class should be constructed once
public class DessertRepository {
    private final DessertDao dessertDao;
    private final ApiRequestInterface apiRequestInterface;

    @Inject
    public DessertRepository(DessertDao dessertDao,
                             ApiRequestInterface apiRequestInterface) {
        this.dessertDao = dessertDao;
        this.apiRequestInterface = apiRequestInterface;
    }

    /* Get Dessert from local DB
    * @param id Dessert Id
    */
    public LiveData<DessertEntity> getDessert(int id){
        return dessertDao.getDessert(id);
    }

    /* Get List of all Ingredients of a Dessert from local DB
     * as LiveData
     * @param id Dessert Id
     */
    public LiveData<List<IngredientEntity>> getIngredients(int id){
        return dessertDao.getIngredients(id);
    }

    /* Get List of all Ingredients of a Dessert from local DB
     * @param dessertId Dessert Id
     */
    public List<IngredientEntity> getIngredientEntities(int dessertId) {
        return dessertDao.getIngredientEntities(dessertId);
    }

    /* Get List of all Recipe Steps of a Dessert from local DB
     * as LiveData
     * @param id Dessert Id
     */
    public LiveData<List<StepEntity>> getRecipeSteps(int id){
        return dessertDao.getRecipeSteps(id);
    }

    /* Get details for a particular Recipe Step of a Dessert from local DB
     * as LiveData
     * @param id Step Id
     * @param dessertId Dessert Id
     */
    public LiveData<StepEntity> getRecipeStep(int id, int dessertId){
        return dessertDao.getRecipeStep(id, dessertId);
    }

    /* Get List of all Desserts & related data from API response,
     * save in local DB, and return dessert list as LiveData
     */
    public LiveData<Resource<List<DessertEntity>>> getDessertList() {
        return new NetworkBoundResource<List<DessertEntity>, List<Dessert>>() {

            @Override
            protected void saveCallResult(@NonNull List<Dessert> itemList) {
                if(!itemList.isEmpty()){
                    for(Dessert dessert: itemList){
                        long insertedId = dessertDao.insertDessert(transformDessert(dessert));

                        // If dessert Item successfully inserted
                        if( insertedId > 0 ){
                            int id = dessert.getId();
                            // insert/replace ingredients
                            dessertDao.deleteRecipeIngredients(id);
                            dessertDao.insertIngredients(transformIngredient(
                                                                dessert.getIngredients(),
                                                                id) );
                            // insert recipe steps
                            dessertDao.insertRecipeSteps(transformStep( dessert.getSteps(),
                                                                        id) );

                        }
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<DessertEntity> data) {
                return (data==null || data.isEmpty());
            }

            @NonNull
            @Override
            protected LiveData<List<DessertEntity>> loadFromDb() {
                Timber.i("DB called for Dessert list");
                return dessertDao.loadDessertList();
            }

            @NonNull
            @Override
            protected Call<List<Dessert>> createCall() {
                Timber.i("API called for Dessert list");
                return apiRequestInterface.getDessertData();
            }
        }.getAsLiveData();
    }


    /* Methods to transform API response models into Room Entities */

    private DessertEntity transformDessert(Dessert dessert) {
        DessertEntity dessertEntity = null;
        if (dessert != null) {
            dessertEntity = new DessertEntity(dessert.getId(),
                                        dessert.getImage(),
                                        dessert.getName(),
                                        dessert.getServings());
        }
        return dessertEntity;
    }

    private IngredientEntity transformIngredient(Ingredient ingredient, int dessertId) {
        IngredientEntity ingredientEntity = null;
        if (ingredient != null) {
            ingredientEntity = new IngredientEntity(ingredient.getQuantity(),
                                        ingredient.getMeasure(),
                                        ingredient.getIngredient(),
                                        dessertId);
        }
        return ingredientEntity;
    }

    private List<IngredientEntity> transformIngredient(List<Ingredient> ingredientList,
                                                       int dessertId) {
        final List<IngredientEntity> ingredientEntities = new ArrayList<>();
        for (Ingredient ingredient : ingredientList) {
            final IngredientEntity ingredientEntity = transformIngredient(ingredient, dessertId);
            if (ingredientEntity != null) {
                ingredientEntities.add(ingredientEntity);
            }
        }
        return ingredientEntities;
    }

    private StepEntity transformStep(Step step, int dessertId) {
        StepEntity stepEntity = null;
        if (step != null) {
            stepEntity = new StepEntity(step.getId(),
                    step.getVideoURL(),
                    step.getDescription(),
                    step.getShortDescription(),
                    step.getThumbnailURL(),
                    dessertId);
        }
        return stepEntity;
    }

    private List<StepEntity> transformStep(List<Step> stepList,
                                                       int dessertId) {
        final List<StepEntity> stepEntities = new ArrayList<>();
        for (Step step : stepList) {
            final StepEntity stepEntity = transformStep(step, dessertId);
            if (stepEntity != null) {
                stepEntities.add(stepEntity);
            }
        }
        return stepEntities;
    }

}
