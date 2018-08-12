package net.digitalswarm.bakingtime.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.adapters.IngredientsRVAdapter;
import net.digitalswarm.bakingtime.adapters.RecipeStepsListRVAdapter;
import net.digitalswarm.bakingtime.models.Ingredients;
import net.digitalswarm.bakingtime.models.Recipe;
import net.digitalswarm.bakingtime.models.RecipeStep;
import java.util.ArrayList;

public class RecipeMasterDetailFragment extends Fragment implements RecipeStepsListRVAdapter.RecipeStepsListRVAdapterClickListener {
    private Recipe mCurrentRecipe;
    private ArrayList<Ingredients> mIngredientsList;
    private ArrayList<RecipeStep> mRecipeStepList;
    private LinearLayoutManager ingredientsLayout;
    private LinearLayoutManager recipeStepsLayout;
    private OnRecipeClickListener mCallback;

    @Override
    public void onClick(int pos) {
        mCallback.onStepSelected(pos);
    }


    public interface OnRecipeClickListener {
        // implement on step selected
        void onStepSelected(int pos);
        Recipe OnRecipeSelected();
    }

    public RecipeMasterDetailFragment() {
        //empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment layout
        View rootView = inflater.inflate(R.layout.recipe_master_detail_layout, container, false);
        //assign views
        Context context = getContext();
        mCurrentRecipe = mCallback.OnRecipeSelected();
        mIngredientsList = mCurrentRecipe.getIngredients();
        mRecipeStepList = mCurrentRecipe.getRecipeSteps();
        //ingredients
        RecyclerView ingredientsRV = rootView.findViewById(R.id.ingredients_rv);
        ingredientsLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        ingredientsRV.setHasFixedSize(true);
        ingredientsRV.setLayoutManager(ingredientsLayout);
        IngredientsRVAdapter ingredientsRVAdapter = new IngredientsRVAdapter(context, mIngredientsList);
        ingredientsRV.setAdapter(ingredientsRVAdapter);
        //recipe steps
        RecyclerView recipeStepsRV = rootView.findViewById(R.id.recipe_steps_rv);
        recipeStepsLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipeStepsRV.setHasFixedSize(true);
        recipeStepsRV.setLayoutManager(recipeStepsLayout);
        RecipeStepsListRVAdapter recipeStepsRVAdapter = new RecipeStepsListRVAdapter(context, mRecipeStepList, this);
        recipeStepsRV.setAdapter(recipeStepsRVAdapter);

        return rootView;
    }
    //assign callback to click listener for step selection
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepsClickListener");
        }
    }
}


