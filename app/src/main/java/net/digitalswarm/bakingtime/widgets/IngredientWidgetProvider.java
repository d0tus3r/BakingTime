package net.digitalswarm.bakingtime.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.text.TextUtils;
import android.widget.RemoteViews;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.Ingredients;
import net.digitalswarm.bakingtime.models.Recipe;

import java.util.ArrayList;

//inspired by https://code.tutsplus.com/tutorials/code-a-widget-for-android-input-and-display--cms-30396
public class IngredientWidgetProvider extends AppWidgetProvider {

    private static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager,
                                               int appWidgetId, Recipe recipe) {
        //create instance of Remote Views
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredients_widget);
        //parse recipe data
        String recipeName = recipe.getName();
        ArrayList<Ingredients> ingredientsArrayList = recipe.getIngredients();
        String convertedIngredients;
        //convert ingredients to a string for easy widget display
        convertedIngredients = convertIngredientsList(ingredientsArrayList);
        //assign views




    }

    //convert ingredients to a string for easy widget display
    private static String convertIngredientsList(ArrayList<Ingredients> ingredientsList) {
        String ingredientsString;
        ArrayList<String> convertedIngredientsStringList = new ArrayList<>();
        for (Ingredients ingredient : ingredientsList) {
            convertedIngredientsStringList.add(ingredient.toString());
        }
        //join String list as a single string with new line
        ingredientsString = TextUtils.join("\n", convertedIngredientsStringList);
        return ingredientsString;
    }




}
