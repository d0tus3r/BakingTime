package net.digitalswarm.bakingtime.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.RecipeListActivity;


public class IngredientWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //update each widgetid
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_base);
            //intent to launch activity
            Intent intent = new Intent(context, RecipeListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_linear_layout_base, pendingIntent);
            //set remote adapter and update
            Intent intent2 = new Intent(context, IngredientWidgetViewsService.class);
            remoteViews.setRemoteAdapter(R.id.widget_recipe_ingredients, intent2);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //get intent action and update list view if it's an action_widget_update
        String intentAction = intent.getAction();
        if (intentAction.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, IngredientWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.widget_recipe_ingredients);
        }
    }

    public static void updateBroadcast(Context context) {
        //send intent when calling static method
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, IngredientWidgetProvider.class));
        context.sendBroadcast(intent);
    }


}
