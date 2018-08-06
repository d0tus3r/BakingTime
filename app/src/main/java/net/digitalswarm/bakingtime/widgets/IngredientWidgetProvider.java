package net.digitalswarm.bakingtime.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.RecipeListActivity;


public class IngredientWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //update each widget if multiple
        for (int appWidgetId : appWidgetIds) {
            updateIngredientWidget(context, appWidgetManager, appWidgetId);
        }
    }

    //set views  with shared pref data
    private static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_base);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = prefs.getString("RECIPE_NAME", "Add a recipe");
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
        //list view for ingredients, get intent from recipe list activity
        remoteViews.setRemoteAdapter(R.id.widget_recipe_ingredients, new Intent(context, IngredientWidgetViewsService.class));
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recipe_ingredients);
    }
}
