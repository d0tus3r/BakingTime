package net.digitalswarm.bakingtime.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.RecipeDetailActivity;
import net.digitalswarm.bakingtime.RecipeListActivity;


public class IngredientWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        }
    }

    //set views  with shared pref data
    private static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews remoteViews;



    }

    private static RemoteViews getViewForWidget(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_base);
        //setup intent to launch recipe list overview
        Intent intent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        //set onclick listener to linear layout to launch list overview
        remoteViews.setOnClickPendingIntent(R.id.widget_linear_layout_base, pendingIntent);
        //setup listview intent
        Intent intent1 = new Intent(context, IngredientWidgetViewsService.class);
        remoteViews.setRemoteAdapter(R.id.widget_recipe_ingredients, intent1);
        Intent appIntent = new Intent(context, RecipeListActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_recipe_ingredients, appPendingIntent);

        return remoteViews;
    }

    private static void updateAllAppWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateIngredientWidget(context, appWidgetManager, appWidgetId);
    }
}
