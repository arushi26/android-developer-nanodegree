package com.arushi.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.arushi.bakingapp.R;
import com.arushi.bakingapp.recipe.RecipeActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
    private static final int DESSERT_ID = 4;
    private static final String DESSERT_NAME = "Cheesecake";
    private static final int DESSERT_IMG = R.drawable.cakes;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(),
                                                R.layout.ingredients_widget);

        // Bundle with data to be passed into intents
        Bundle bundle = new Bundle();
        bundle.putInt(RecipeActivity.KEY_RECIPE_ID, DESSERT_ID);
        bundle.putString(RecipeActivity.KEY_RECIPE_NAME, DESSERT_NAME);
        bundle.putInt(RecipeActivity.KEY_RECIPE_DEFAULT_IMG, DESSERT_IMG);

        views.setTextViewText(R.id.appwidget_title, DESSERT_NAME);

        // Set IngredientWidgetService to act as Adapter for the List view
        Intent serviceIntent = new Intent(context, IngredientWidgetService.class);
        serviceIntent.putExtra(RecipeActivity.KEY_RECIPE_DATA, bundle);
        views.setRemoteAdapter(R.id.appwidget_list_view, serviceIntent);

        // Set RecipeActivity intent to launch on click of ListView item
        Intent itemIntent = new Intent(context, RecipeActivity.class);
        PendingIntent itemPendingIntent = PendingIntent.getActivity(context, 0,
                                                                    itemIntent,
                                                                    PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.appwidget_list_view, itemPendingIntent);

        // Pending intent to launch Recipe Activity
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(RecipeActivity.KEY_RECIPE_DATA, bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Set Pending intent for click on widget views
        views.setOnClickPendingIntent(R.id.appwidget, pendingIntent);

        // Handle empty ingredients list
        views.setEmptyView(R.id.appwidget_list_view, R.id.appwidget_empty_view);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_list_view);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // No operation
    }

    @Override
    public void onDisabled(Context context) {
        // No operation
    }
}

