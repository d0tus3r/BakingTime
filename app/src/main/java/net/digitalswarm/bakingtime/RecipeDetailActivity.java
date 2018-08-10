package net.digitalswarm.bakingtime;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        //assign recipe from intent
        this.currentRecipe = getIntent().getParcelableExtra("Recipe");
        this.currentRecipeStepsList = currentRecipe.getRecipeSteps();
        this.currentRecipeStepsSize = currentRecipeStepsList.size();
        getSupportActionBar().setTitle(currentRecipe.getName());
        //use UP to return to recipe list
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            //start ingredients and step list fragments
            //attach within frame layout on detail_frame
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.detail_frame, RecipeMasterDetailFragment.newInstance(currentRecipe.getIngredients(), currentRecipe.getRecipeSteps()))
                    .commit();
        }
    }

    //store step fragment onSaveInstanceState
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (stepDetailFragment != null) {
            getSupportFragmentManager().putFragment(bundle, "fragment", stepDetailFragment);
        }
    }

    //Fragment transaction methods for step selection callbacks
    @Override
    public void onStepSelected(int position) {
        currentRecipeStep = currentRecipeStepsList.get(position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, Integer.parseInt(currentRecipeStep.getId()));
        transaction.replace(R.id.detail_frame, stepDetailFragment);
        transaction.commit();
    }
    //load previous step as long as it exists
    @Override
    public void onPrevStepSelected(int position) {
        if (0 <= (position - 1)) {
            currentRecipeStep = currentRecipeStepsList.get(position - 1);
            int currentRecipeStepId = Integer.parseInt(currentRecipeStep.getId());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, currentRecipeStepId);
            transaction.replace(R.id.detail_frame, stepDetailFragment);
            transaction.commit();
        }
    }
    //load next step as long as there is another step in list
    @Override
    public void onNextStepSelected(int position) {
        if (currentRecipeStepsSize > (position + 1)) {
            currentRecipeStep = currentRecipeStepsList.get(position + 1);
            int currentRecipeStepId = Integer.parseInt(currentRecipeStep.getId());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            stepDetailFragment = RecipeStepDetailFragment.newInstance(currentRecipeStep, currentRecipeStepId);
            transaction.replace(R.id.detail_frame, stepDetailFragment);
            transaction.commit();
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
