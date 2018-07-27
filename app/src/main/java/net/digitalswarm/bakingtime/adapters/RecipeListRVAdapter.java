package net.digitalswarm.bakingtime.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.Recipe;
import android.view.View.OnClickListener;

import java.util.ArrayList;

public class RecipeListRVAdapter extends RecyclerView.Adapter<RecipeListRVAdapter.RecipeListViewHolder> {

    private ArrayList<Recipe> mRecipeList;
    private final Context mContext;
    private final RecipeListRVAdapterClickListener mClickListener;

    public interface RecipeListRVAdapterClickListener {
        void onClick(int pos);
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final TextView recipeTextView;

        RecipeListViewHolder(View view) {
            super(view);
            recipeTextView = view.findViewById(R.id.recipe_list_tv); //assign view
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            mClickListener.onClick(getAdapterPosition());
        }
    }
    //adapter constructor
    public RecipeListRVAdapter(Context context, ArrayList<Recipe> recipeList, RecipeListRVAdapterClickListener clickListener){
        this.mContext = context;
        this.mRecipeList = recipeList;
        this.mClickListener = clickListener;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View recipeItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeListViewHolder(recipeItemView);
    }

    @Override
    public void onBindViewHolder(final RecipeListViewHolder recipeHolder, int position){
        Recipe currentRecipe = mRecipeList.get(position);
        recipeHolder.recipeTextView.setText(currentRecipe.getName());
    }

    @Override
    public int getItemCount(){
        return mRecipeList.size();
    }

    public void setmRecipeList(ArrayList<Recipe> recipeList){
        mRecipeList = recipeList;
    }

}