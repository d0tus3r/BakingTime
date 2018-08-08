package net.digitalswarm.bakingtime.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.Ingredients;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class IngredientWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<Ingredients> mIngredientsList;
    //private String mRecipeName;

    public IngredientWidgetViewsFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        getIngredientsData();
    }

    private void getIngredientsData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        //mRecipeName = prefs.getString("RECIPE_NAME", "Add a recipe");
        //grab ingredientslist from shared pref
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Ingredients>>() {}.getType();
        String jsonResponse = prefs.getString("INGREDIENTS_KEY", "Default String");
        mIngredientsList = gson.fromJson(jsonResponse, type);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredients_item);
        //update listview with each ingredient
        remoteViews.setTextViewText(R.id.widget_ingredient_item_tv, mIngredientsList.get(position).toString());
        //Log.i("Ingredient", mIngredientsList.get(position).toString());
        return remoteViews;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
