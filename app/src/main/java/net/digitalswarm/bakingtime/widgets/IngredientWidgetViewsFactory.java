package net.digitalswarm.bakingtime.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.digitalswarm.bakingtime.R;

public class IngredientWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private String mIngredients;
    private String mRecipeName;

    public IngredientWidgetViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
    }

    private void getIngredientsData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mIngredients = prefs.getString("INGREDIENTS", "Add a recipe");
        mRecipeName = prefs.getString("RECIPE_NAME", "Add a recipe");

    }

    @Override
    public void onCreate() {
        getIngredientsData();
    }

    @Override
    public void onDataSetChanged() {
        getIngredientsData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredients_base);
        remoteViews.setTextViewText(R.id.widget_recipe_name, mRecipeName);
        remoteViews.setTextViewText(R.id.widget_recipe_ingredients, mIngredients);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
