package net.digitalswarm.bakingtime;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import net.digitalswarm.bakingtime.fragments.RecipeMasterDetailFragment;
import net.digitalswarm.bakingtime.fragments.RecipeStepDetailFragment;
import net.digitalswarm.bakingtime.models.Recipe;
import net.digitalswarm.bakingtime.models.RecipeStep;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeMasterDetailFragment.OnRecipeClickListener {

    private Recipe mCurrentRecipe;
    private RecipeStep mCurrentStep;
    private ArrayList<RecipeStep> mCurrentRecipeStepList;
    private boolean twoFragments;
    private RecipeStepDetailFragment mStepDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        //assign recipe from intent
        Intent intent = getIntent();
        mCurrentRecipe = intent.getParcelableExtra("Recipe");
        //assign recipe steps list
        mCurrentRecipeStepList = mCurrentRecipe.getRecipeSteps();
        //use UP to return to recipe list
        getSupportActionBar().setTitle(mCurrentRecipe.getName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //if using device (tablet) bigger than 600dp width
        if (findViewById(R.id.tablet_layout) == null) {
            twoFragments = false;
        } else {
            twoFragments = true;
            //check for saved instance state
            if (savedInstanceState == null) {
                //launch step detail as second fragment
                mStepDetailFragment = new RecipeStepDetailFragment();
                mStepDetailFragment.setCurrentRecipe(mCurrentRecipe);
                //use first recipe step as initial step displayed
                mCurrentStep = mCurrentRecipeStepList.get(0);
                mStepDetailFragment.setCurrentStep(mCurrentStep);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.step_fragment_frame, mStepDetailFragment)
                        .commit();
            }
        }
    }

    //Fragment transaction methods for step selection callbacks
    @Override
    public void onStepSelected(int position) {
        if (!twoFragments) {
            //launch step detail activity for non tablet devices
            Class destinationClass = RecipeStepDetailActivity.class;
            Intent detailActivityIntent = new Intent(this, destinationClass);
            detailActivityIntent.putExtra("Recipe", mCurrentRecipe);
            mCurrentStep = mCurrentRecipe.getRecipeSteps().get(position);
            detailActivityIntent.putExtra("RecipeStep", mCurrentStep);
            startActivity(detailActivityIntent);
        } else {
            //start fragment
            mStepDetailFragment = new RecipeStepDetailFragment();
            mStepDetailFragment.setCurrentRecipe(mCurrentRecipe);
            //use first recipe step as initial step displayed
            mCurrentStep = mCurrentRecipeStepList.get(position);
            mStepDetailFragment.setCurrentStep(mCurrentStep);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.step_fragment_frame, mStepDetailFragment)
                    .commit();
        }
    }

    //Up / home action bar setup
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Recipe OnRecipeSelected() {
        if (mCurrentRecipe == null) {
            Intent intent = getIntent();
            mCurrentRecipe = intent.getParcelableExtra("Recipe");
        }
        return mCurrentRecipe;
    }
}
