package net.digitalswarm.bakingtime;

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
import net.digitalswarm.bakingtime.models.RecipeSteps;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeMasterDetailFragment.OnStepsClickListener,
        RecipeStepDetailFragment.OnPrevStepsClickListener, RecipeStepDetailFragment.OnNextStepsClickListener{

    private Recipe currentRecipe;
    private RecipeSteps currentRecipeStep;
    private int currentRecipeStepsSize;
    private ArrayList<RecipeSteps> currentRecipeStepsList;
    private RecipeStepDetailFragment stepDetailFragment;
    private RecipeMasterDetailFragment recipeMasterDetailFragment;

    //boolean for 2 fragments when landscape mode
    private boolean twoFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //assign recipe from intent
        this.currentRecipe = getIntent().getParcelableExtra("Recipe");
        this.currentRecipeStepsList = currentRecipe.getRecipeSteps();
        this.currentRecipeStepsSize = currentRecipeStepsList.size();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            twoFragments = true;
            setContentView(R.layout.recipe_detail_activity_landscape);
            if (savedInstanceState == null) {
                //start by displaying first step
                currentRecipeStep = currentRecipeStepsList.get(0);
                //start ingredients and step list fragments
                //attach within frame layout on detail_frame
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                recipeMasterDetailFragment = RecipeMasterDetailFragment.newInstance(currentRecipe.getIngredients(), currentRecipe.getRecipeSteps());
                stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, Integer.parseInt(currentRecipeStep.getId()));
                transaction.add(R.id.detail_land_left_frame, recipeMasterDetailFragment, "recipeMasterDetailFragment");
                transaction.add(R.id.detail_land_right_frame, stepDetailFragment, "stepDetailFragment");
                transaction.commit();
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                recipeMasterDetailFragment = RecipeMasterDetailFragment.newInstance(currentRecipe.getIngredients(), currentRecipe.getRecipeSteps());
                stepDetailFragment = (RecipeStepDetailFragment) fragmentManager.findFragmentByTag("stepDetailFragment");
                transaction.add(R.id.detail_land_left_frame, recipeMasterDetailFragment, "recipeMasterDetailFragment");
                if (stepDetailFragment == null) {
                    currentRecipeStep = currentRecipeStepsList.get(0);
                    stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, Integer.parseInt(currentRecipeStep.getId()));
                }
                transaction.add(R.id.detail_land_right_frame, stepDetailFragment, "stepDetailFragment");
                transaction.commit();
            }

        } else {
            twoFragments = false;
            setContentView(R.layout.recipe_detail_activity);
                //start ingredients and step list fragments
                //attach within frame layout on detail_frame
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.detail_frame, RecipeMasterDetailFragment.newInstance(currentRecipe.getIngredients(), currentRecipe.getRecipeSteps()))
                        .commit();
        }
        //use UP to return to recipe list
        getSupportActionBar().setTitle(currentRecipe.getName());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //store step fragment onSaveInstanceState
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (stepDetailFragment != null) {
            getSupportFragmentManager().putFragment(bundle, "fragment", stepDetailFragment);
            bundle.putInt("currentStepId", Integer.parseInt(currentRecipeStep.getId()));
        }
    }

    //Fragment transaction methods for step selection callbacks
    @Override
    public void onStepSelected(int position) {
        currentRecipeStep = currentRecipeStepsList.get(position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (twoFragments) {
            stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, Integer.parseInt(currentRecipeStep.getId()));
            transaction.replace(R.id.detail_land_right_frame, stepDetailFragment, "stepDetailFragment");
            transaction.commit();
        } else {
            stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, Integer.parseInt(currentRecipeStep.getId()));
            transaction.replace(R.id.detail_frame, stepDetailFragment);
            transaction.commit();
        }
    }
    //load previous step as long as it exists
    @Override
    public void onPrevStepSelected(int position) {
        if (0 <= (position - 1)) {
            currentRecipeStep = currentRecipeStepsList.get(position - 1);
            int currentRecipeStepId = Integer.parseInt(currentRecipeStep.getId());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (twoFragments) {
                stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, currentRecipeStepId);
                transaction.replace(R.id.detail_land_right_frame, stepDetailFragment, "stepDetailFragment");
                transaction.commit();
            } else {
                stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, currentRecipeStepId);
                transaction.replace(R.id.detail_frame, stepDetailFragment);
                transaction.commit();
            }
        }
    }
    //load next step as long as there is another step in list
    @Override
    public void onNextStepSelected(int position) {
        if (currentRecipeStepsSize > (position + 1)) {
            currentRecipeStep = currentRecipeStepsList.get(position + 1);
            int currentRecipeStepId = Integer.parseInt(currentRecipeStep.getId());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (twoFragments) {
                stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, currentRecipeStepId);
                transaction.replace(R.id.detail_land_right_frame, stepDetailFragment, "stepDetailFragment");
                transaction.commit();
            } else {
                stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, currentRecipeStepId);
                transaction.replace(R.id.detail_frame, stepDetailFragment);
                transaction.commit();
            }
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
}
