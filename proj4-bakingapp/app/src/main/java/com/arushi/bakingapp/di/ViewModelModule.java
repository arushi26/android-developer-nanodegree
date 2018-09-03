package com.arushi.bakingapp.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.arushi.bakingapp.main.MainViewModel;
import com.arushi.bakingapp.recipe.RecipeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/* Reference - https://stackoverflow.com/a/44506312/1583837 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel.class)
    abstract ViewModel bindRecipeViewModel(RecipeViewModel recipeViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);
}
