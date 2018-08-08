package net.digitalswarm.bakingtime.widgets;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.digitalswarm.bakingtime.models.Ingredients;

import java.util.ArrayList;

public class IngredientWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<Ingredients> mIngredientsList;
    private String mRecipeName;

    public IngredientWidgetViewsFactory(Context mContext) {
        this.mContext = mContext;
        mIngredientsList = new ArrayList<>();
    }

    private void getIngredientsData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mRecipeName = prefs.getString("RECIPE_NAME", "Add a recipe");
        //grab ingredientslist from shared pref
        Gson gson = new Gson();
        String jsonResponse = prefs.getString("INGREDIENTS_KEY", "Default String");
        mIngredientsList = gson.fromJson(jsonResponse, new TypeToken<ArrayList<Ingredients>>(){}.getType());

    }

    @Override
    public void onCreate() {

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
        return mIngredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
    //redo
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
