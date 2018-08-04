package net.digitalswarm.bakingtime.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.Ingredients;

import java.util.ArrayList;

public class IngredientsRVAdapter extends RecyclerView.Adapter<IngredientsRVAdapter.IngredientsViewHolder> {
    //vars
    private ArrayList<Ingredients> mIngredientsList;

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        final TextView IngredientsQuantityTV;
        final TextView IngredientsMeasureTV;
        final TextView IngredientsNameTV;

        IngredientsViewHolder(View view) {
            super(view);
            //assign views
            IngredientsQuantityTV = view.findViewById(R.id.ingredient_quantity_tv);
            IngredientsMeasureTV = view.findViewById(R.id.ingredient_measure_tv);
            IngredientsNameTV = view.findViewById(R.id.ingredient_name_tv);
        }
    }
    //adapter constructor
    public IngredientsRVAdapter(Context context, ArrayList<Ingredients> ingredientsList) {
        Context mContext = context;
        this.mIngredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View ingredientsItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_steps_ingredients_item, parent, false);
        return new IngredientsViewHolder(ingredientsItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientsViewHolder ingredientsHolder, int position){
        Ingredients currentIngredient = mIngredientsList.get(position);
        ingredientsHolder.IngredientsQuantityTV.setText(currentIngredient.getQuantity());
        ingredientsHolder.IngredientsMeasureTV.setText(currentIngredient.getMeasurement());
        ingredientsHolder.IngredientsNameTV.setText(currentIngredient.getIngredient());
    }

    @Override
    public int getItemCount(){
        return mIngredientsList.size();
    }

    public void setmRecipeList(ArrayList<Ingredients> ingredientsList){
        mIngredientsList = ingredientsList;
    }

}
