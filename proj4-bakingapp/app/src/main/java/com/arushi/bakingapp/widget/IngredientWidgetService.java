package com.arushi.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.arushi.bakingapp.BApplication;
import com.arushi.bakingapp.R;
import com.arushi.bakingapp.data.DessertRepository;
import com.arushi.bakingapp.data.local.entity.IngredientEntity;
import com.arushi.bakingapp.recipe.RecipeActivity;

import java.util.List;

import javax.inject.Inject;

public class IngredientWidgetService extends RemoteViewsService {
    @Inject
    DessertRepository repository;

    @Override
    public void onCreate() {
        ((BApplication) this.getApplicationContext()).getAppComponent()
                                        .inject(this);
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(),
                                            intent,
                                            repository);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<IngredientEntity> mIngredientList;
    private static int DESSERT_ID;
    private Bundle mBundle;
    private DessertRepository mRepository;

    public ListRemoteViewsFactory(Context applicationContext,
                                  Intent intent,
                                  DessertRepository repository) {
        mContext = applicationContext;
        mRepository = repository;

        mBundle = intent.getBundleExtra(RecipeActivity.KEY_RECIPE_DATA);
        DESSERT_ID = mBundle.getInt(RecipeActivity.KEY_RECIPE_ID);
    }

    @Override
    public void onCreate() {
    }

    // Called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        mIngredientList = mRepository.getIngredientEntities(DESSERT_ID);
    }

    @Override
    public void onDestroy() {
        if(mIngredientList!=null) {
            mIngredientList.clear();
        }
    }

    @Override
    public int getCount() {
        if (mIngredientList == null) return 0;
        return mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(mIngredientList==null || mIngredientList.size()==0) return null;

        IngredientEntity ingredient = mIngredientList.get(position);

        RemoteViews itemView = new RemoteViews(mContext.getPackageName(),
                                                R.layout.item_ingredients_widget);
        String description = ingredient.getIngredient()
                + "  (" + String.valueOf(ingredient.getQuantity())
                + " " + ingredient.getMeasure()
                + ")";
        itemView.setTextViewText(R.id.tv_ingredient, description);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeActivity.KEY_RECIPE_DATA, mBundle);
        itemView.setOnClickFillInIntent(R.id.layout_ingredient, fillInIntent);

        return itemView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mIngredientList.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

