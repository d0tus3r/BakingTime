package net.digitalswarm.bakingtime.fragments;

import android.content.Context;
import android.os.Bundle;
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
import net.digitalswarm.bakingtime.models.RecipeSteps;
import java.util.ArrayList;

public class RecipeMasterDetailFragment extends Fragment {
    private ArrayList<Ingredients> mIngredientsList;
    public static final String INGREDIENTS_KEY = "INGREDIENTS";
    private ArrayList<RecipeSteps> mRecipeStepsList;
    public static final String RECIPE_STEPS_KEY = "RECIPE_STEPS";

    //views
    private RecyclerView ingredientsRV;
    private RecyclerView recipeStepsRV;
    private IngredientsRVAdapter ingredientsRVAdapter;
    private RecipeStepsListRVAdapter recipeStepsRVAdapter;
    LinearLayoutManager ingredientsLayout;
    LinearLayoutManager recipeStepsLayout;
    OnStepsClickListener mCallback;

    public interface OnStepsClickListener {
        void onStepSelected(int position);
    }


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
        //assign recipe data
        mIngredientsList = getArguments().getParcelableArrayList(INGREDIENTS_KEY);
        mRecipeStepsList = getArguments().getParcelableArrayList(RECIPE_STEPS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate fragment layout
        View rootView = inflater.inflate(R.layout.recipe_master_detail_layout, container, false);
        //assign views
        Context context = getContext();
        //ingredients
        ingredientsRV = rootView.findViewById(R.id.ingredients_rv);
        ingredientsLayout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        ingredientsRV.setHasFixedSize(true);
        ingredientsRV.setLayoutManager(ingredientsLayout);
        ingredientsRVAdapter = new IngredientsRVAdapter(context, mIngredientsList);
        ingredientsRV.setAdapter(ingredientsRVAdapter);
        //recipe steps
        recipeStepsRV = rootView.findViewById(R.id.recipe_steps_rv);
        recipeStepsLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recipeStepsRV.setHasFixedSize(true);
        recipeStepsRV.setLayoutManager(recipeStepsLayout);
        recipeStepsRVAdapter = new RecipeStepsListRVAdapter(context, mRecipeStepsList, new RecipeStepsListRVAdapter.RecipeStepsListRVAdapterClickListener() {
            @Override
            public void onClick(int pos) {
                mCallback.onStepSelected(pos);
            }
        });
        recipeStepsRV.setAdapter(recipeStepsRVAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepsClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepsClickListener");
        }
    }
}


