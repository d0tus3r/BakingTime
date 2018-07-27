package net.digitalswarm.bakingtime.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import net.digitalswarm.bakingtime.models.Ingredients;
import net.digitalswarm.bakingtime.models.RecipeSteps;

import java.util.ArrayList;

public class RecipeMasterDetailFragment extends Fragment {
    private ArrayList<Ingredients> mIngredientsList;
    public static final String INGREDIENTS_KEY = "INGREDIENTS";
    private ArrayList<RecipeSteps> mRecipeStepsList;
    public static final String RECIPE_STEPS_KEY = "RECIPE_STEPS";


    public RecipeMasterDetailFragment() {
        //empty constructor
    }
    //master detail fragment instance
    public static RecipeMasterDetailFragment newInstance(ArrayList<Ingredients> ingredientsList, ArrayList<RecipeSteps> recipeStepsList) {
        RecipeMasterDetailFragment recipeMasterDetailFragment = new RecipeMasterDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(INGREDIENTS_KEY, ingredientsList);
        bundle.putParcelableArrayList(RECIPE_STEPS_KEY, recipeStepsList);
        recipeMasterDetailFragment.setArguments(bundle);
        return recipeMasterDetailFragment;
    }
    //Fragment on create setup
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //assign recipe data if bundled args != null
        if (getArguments() != null) {
            mIngredientsList = getArguments().getParcelableArrayList(INGREDIENTS_KEY);
            mRecipeStepsList = getArguments().getParcelableArrayList(RECIPE_STEPS_KEY);
        }

    }
    //Fragment View Setup
    private void generateRecipeView(ArrayList<Ingredients> ingredientsList, ArrayList<RecipeSteps> stepsList) {
        //assign views and click listeners
    }



}
