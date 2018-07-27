package net.digitalswarm.bakingtime.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.RecipeSteps;

import android.view.View.OnClickListener;
import java.util.ArrayList;

public class RecipeStepsListRVAdapter extends RecyclerView.Adapter<RecipeStepsListRVAdapter.RecipeStepsListViewHolder> {
    private ArrayList<RecipeSteps> mRecipeStepsList;
    private final Context mContext;
    private final RecipeStepsListRVAdapterClickListener mClickListener;

    public interface RecipeStepsListRVAdapterClickListener {
        void onClick(int pos);
    }

    public class RecipeStepsListViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final TextView recipeStepsTextView;

        RecipeStepsListViewHolder(View view) {
            super(view);
            recipeStepsTextView = view.findViewById(R.id.recipe_list_tv); //assign view
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            int adapterPos = getAdapterPosition();
            mClickListener.onClick(adapterPos);
        }
    }
    //adapter constructor
    public RecipeStepsListRVAdapter(Context context, ArrayList<RecipeSteps> recipeStepsList, RecipeStepsListRVAdapterClickListener clickListener){
        this.mContext = context;
        this.mRecipeStepsList = recipeStepsList;
        this.mClickListener = clickListener;
    }

    @Override
    public RecipeStepsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View recipeItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_steps_list_item, parent, false);
        return new RecipeStepsListViewHolder(recipeItemView);
    }

    @Override
    public void onBindViewHolder(final RecipeStepsListViewHolder recipeHolder, int position){
        RecipeSteps currentRecipeStep = mRecipeStepsList.get(position);
        recipeHolder.recipeStepsTextView.setText(currentRecipeStep.getDescription());
    }

    @Override
    public int getItemCount(){
        return mRecipeStepsList.size();
    }

    public void setmRecipeList(ArrayList<RecipeSteps> recipeStepsList){
        mRecipeStepsList = recipeStepsList;
    }

}
