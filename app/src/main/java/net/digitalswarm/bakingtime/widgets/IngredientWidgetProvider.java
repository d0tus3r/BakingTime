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
        //redp
    }
}
