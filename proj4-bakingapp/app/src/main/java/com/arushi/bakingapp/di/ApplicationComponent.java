package com.arushi.bakingapp.di;

import com.arushi.bakingapp.main.MainActivity;
import com.arushi.bakingapp.recipe.RecipeActivity;
import com.arushi.bakingapp.recipe.RecipeFragment;
import com.arushi.bakingapp.step.StepActivity;
import com.arushi.bakingapp.step.StepDetailFragment;
import com.arushi.bakingapp.widget.IngredientWidgetService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {  AppModule.class,
                        DataServiceModule.class,
                        NetworkServiceModule.class,
                        ViewModelModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
    void inject(RecipeFragment recipeFragment);
    void inject(StepDetailFragment stepDetailFragment);
    void inject(IngredientWidgetService widgetService);
}
