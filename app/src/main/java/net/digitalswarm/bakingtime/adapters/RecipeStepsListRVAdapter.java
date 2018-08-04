package net.digitalswarm.bakingtime.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.RecipeSteps;

import java.util.ArrayList;

public class RecipeStepsListRVAdapter extends RecyclerView.Adapter<RecipeStepsListRVAdapter.RecipeStepsListViewHolder>  {
    private ArrayList<RecipeSteps> mRecipeStepsList;
    private final RecipeStepsListRVAdapterClickListener mClickListener;

    public interface RecipeStepsListRVAdapterClickListener {
        void onClick(int pos);
    }

    public class RecipeStepsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView recipeStepsTextView;
        final TextView recipeStepsIdTextView;

        RecipeStepsListViewHolder(View view) {
            super(view);
            recipeStepsIdTextView = view.findViewById(R.id.recipe_steps_id_tv);
            recipeStepsTextView = view.findViewById(R.id.recipe_steps_tv); //assign view
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onClick(getAdapterPosition());
        }
    }

    //adapter constructor
    public RecipeStepsListRVAdapter(Context context, ArrayList<RecipeSteps> recipeStepsList, RecipeStepsListRVAdapterClickListener clickListener){
        Context mContext = context;
        this.mRecipeStepsList = recipeStepsList;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeStepsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View recipeItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_steps_list_item, parent, false);
        return new RecipeStepsListViewHolder(recipeItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeStepsListViewHolder recipeHolder, int position){
        RecipeSteps currentRecipeStep = mRecipeStepsList.get(position);
        recipeHolder.recipeStepsIdTextView.setText(currentRecipeStep.getId());
        recipeHolder.recipeStepsTextView.setText(currentRecipeStep.getShortDescription());
    }

    @Override
    public int getItemCount(){
        return mRecipeStepsList.size();
    }

    public void setmRecipeList(ArrayList<RecipeSteps> recipeStepsList){
        mRecipeStepsList = recipeStepsList;
    }

}
