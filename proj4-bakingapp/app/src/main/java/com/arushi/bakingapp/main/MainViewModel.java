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
