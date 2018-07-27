package net.digitalswarm.bakingtime.utilities;

import net.digitalswarm.bakingtime.models.Recipe;

import java.util.ArrayList;
import retrofit2.http.GET;
import retrofit2.Call;

//interface to fetch recipe json
public interface RecipeService {
        @GET("baking.json")
        Call<ArrayList<Recipe>> getRecipes();
}

