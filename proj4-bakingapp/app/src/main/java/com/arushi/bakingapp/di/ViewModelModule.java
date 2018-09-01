package com.arushi.bakingapp.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.arushi.bakingapp.main.MainViewModel;
import com.arushi.bakingapp.recipe.RecipeViewModel;
import com.arushi.bakingapp.step.StepViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/* https://stackoverflow.com/questions/44270577/android-lifecycle-library-viewmodel-using-dagger-2 */
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
    @IntoMap
    @ViewModelKey(StepViewModel.class)
    abstract ViewModel bindStepViewModel(StepViewModel stepViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);
}
