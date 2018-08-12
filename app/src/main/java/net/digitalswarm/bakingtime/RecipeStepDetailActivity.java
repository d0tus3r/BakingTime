package net.digitalswarm.bakingtime;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import net.digitalswarm.bakingtime.fragments.RecipeStepDetailFragment;
import net.digitalswarm.bakingtime.models.Recipe;
import net.digitalswarm.bakingtime.models.RecipeStep;

public class RecipeStepDetailActivity extends AppCompatActivity {

    //vars
    private Recipe mCurrentRecipe;
    private RecipeStep mCurrentRecipeStep;
    private RecipeStepDetailFragment mStepDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_detail_activity);
        //get intent from detail activity
        Intent intent = getIntent();
        mCurrentRecipe = intent.getParcelableExtra("Recipe");
        mCurrentRecipeStep = intent.getParcelableExtra("RecipeStep");
        if (savedInstanceState == null) {
            mStepDetailFragment = new RecipeStepDetailFragment();
            mStepDetailFragment.setCurrentRecipe(mCurrentRecipe);
            mStepDetailFragment.setCurrentStep(mCurrentRecipeStep);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.step_fragment_frame, mStepDetailFragment)
                    .commit();
        }
    }
}
