package net.digitalswarm.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.digitalswarm.bakingtime.adapters.RecipeListRVAdapter;
import net.digitalswarm.bakingtime.models.Recipe;
import net.digitalswarm.bakingtime.utilities.RecipeService;
import net.digitalswarm.bakingtime.utilities.RetrofitInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeListActivity extends AppCompatActivity implements RecipeListRVAdapter.RecipeListRVAdapterClickListener {
    private ArrayList<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list_activity);
        //create retrofit instance interface
        RecipeService service = RetrofitInstance.getRetrofitInstance().create(RecipeService.class);
        //Call service method to get data
        Call<ArrayList<Recipe>> call = service.getRecipes();
        //queue call request
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                recipeList = response.body();
                generateRecipeList(recipeList);
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                //todo: toast or log
            }
        });


    }

    private void generateRecipeList(ArrayList<Recipe> recipeList) {
        RecyclerView recipeRV = findViewById(R.id.recipe_list_rv);
        GridLayoutManager gridLayout = new GridLayoutManager(this, 1);
        recipeRV.setHasFixedSize(true);
        recipeRV.setLayoutManager(gridLayout);
        RecipeListRVAdapter recipeListRVAdapter = new RecipeListRVAdapter(this, recipeList, this);
        recipeRV.setAdapter(recipeListRVAdapter);
    }

    @Override
    public void onClick(int position){
        //assign context and activity class for scope
        Context context = this;
        Class destinationClass = RecipeDetailActivity.class;
        //create a new intent to launch detail activity, using current moviePosterList position
        Intent detailActivityIntent = new Intent(context, destinationClass);
        detailActivityIntent.putExtra("Recipe", recipeList.get(position));
        startActivity(detailActivityIntent);
    }
}
