package com.arushi.bakingapp.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.arushi.bakingapp.data.DessertRepository;
import com.arushi.bakingapp.data.Resource;
import com.arushi.bakingapp.data.local.entity.DessertEntity;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private DessertRepository mRepository;
    private LiveData<Resource<List<DessertEntity>>> mDessertsList = null;

    @Inject
    public MainViewModel(DessertRepository dessertRepository){
        this.mRepository = dessertRepository;
    }

    public LiveData<Resource<List<DessertEntity>>> getDessertsList(){
        if (mDessertsList == null) {
            mDessertsList = mRepository.getDessertList();
        }
        return mDessertsList;
    }

}
