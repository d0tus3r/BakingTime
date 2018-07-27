package net.digitalswarm.bakingtime;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.digitalswarm.bakingtime.fragments.RecipeMasterDetailFragment;
import net.digitalswarm.bakingtime.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    Recipe currentRecipe;
    //Todo: change title bar to recipe
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        //assign recipe from intent
        this.currentRecipe = getIntent().getParcelableExtra("recipe");
        //start ingredients and step list fragments
        //attach within frame layout on detail_frame
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.detail_frame, RecipeMasterDetailFragment.newInstance(currentRecipe.getIngredients(), currentRecipe.getRecipeSteps()))
                .commit();
    }
    //Todo: add onClick methods for recipe step clicks

}
