package net.digitalswarm.bakingtime;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.digitalswarm.bakingtime.fragments.RecipeMasterDetailFragment;
import net.digitalswarm.bakingtime.models.Recipe;
import net.digitalswarm.bakingtime.models.RecipeSteps;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeMasterDetailFragment.OnStepsClickListener {

    Recipe currentRecipe;
    RecipeSteps currentRecipeStep;
    ArrayList<RecipeSteps> currentRecipeStepsList;
    //Todo: change title bar to recipe name
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        //assign recipe from intent
        this.currentRecipe = getIntent().getParcelableExtra("Recipe");
        this.currentRecipeStepsList = currentRecipe.getRecipeSteps();
        //start ingredients and step list fragments
        //attach within frame layout on detail_frame
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.detail_frame, RecipeMasterDetailFragment.newInstance(currentRecipe.getIngredients(), currentRecipe.getRecipeSteps()))
                .commit();
    }

    @Override
    public void onStepSelected(int position) {
        currentRecipeStep = currentRecipeStepsList.get(position);
        Toast.makeText(this, "Recipe step selected is: " + currentRecipeStep.getDescription(), Toast.LENGTH_SHORT).show();
    }
    //Todo: add onClick methods for recipe step clicks

}
