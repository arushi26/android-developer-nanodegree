package com.arushi.bakingapp.step;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.arushi.bakingapp.data.DessertRepository;
import com.arushi.bakingapp.data.local.entity.DessertEntity;
import com.arushi.bakingapp.data.local.entity.IngredientEntity;
import com.arushi.bakingapp.data.local.entity.StepEntity;

import java.util.List;

import javax.inject.Inject;

public class StepViewModel extends ViewModel {
    DessertRepository mRepository;
    private LiveData<StepEntity> mStep = null;

    @Inject
    public StepViewModel(DessertRepository dessertRepository) {
        this.mRepository = dessertRepository;
    }

    public LiveData<StepEntity> getStep(int id, int dessertId){
        if (mStep == null) {
            mStep = mRepository.getRecipeStep(id, dessertId);
        }
        return mStep;
    }

}
