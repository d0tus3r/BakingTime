package net.digitalswarm.bakingtime.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import net.digitalswarm.bakingtime.R;


public class IngredientWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //update each widget if multiple
        for (int appWidgetId : appWidgetIds) {
            updateIngredientWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    //set views  with shared pref data
    private static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_base);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String ingredients = prefs.getString("INGREDIENTS", "Add a recipe");
        String recipeName = prefs.getString("RECIPE_NAME", "Add a recipe");
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
        remoteViews.setTextViewText(R.id.widget_recipe_ingredients, ingredients);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }




    //update broadcast with intent, called by activity to update widget
    public static void updateBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        context.sendBroadcast(intent);
    }


}
