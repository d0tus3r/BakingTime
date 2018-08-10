package net.digitalswarm.bakingtime.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.Recipe;
import android.view.View.OnClickListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeListRVAdapter extends RecyclerView.Adapter<RecipeListRVAdapter.RecipeListViewHolder> {

    private ArrayList<Recipe> mRecipeList;
    private final RecipeListRVAdapterClickListener mClickListener;
    private final Context mContext;

    public interface RecipeListRVAdapterClickListener {
        void onClick(int pos);
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final TextView recipeTextView;
        final ImageView recipeImageView;

        RecipeListViewHolder(View view) {
            super(view);
            recipeTextView = view.findViewById(R.id.recipe_list_tv); //assign view
            recipeImageView = view.findViewById(R.id.recipe_list_iv);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            mClickListener.onClick(getAdapterPosition());
        }
    }
    //adapter constructor
    public RecipeListRVAdapter(Context context, ArrayList<Recipe> recipeList, RecipeListRVAdapterClickListener clickListener){
        mContext = context;
        this.mRecipeList = recipeList;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View recipeItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeListViewHolder(recipeItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeListViewHolder recipeHolder, int position){
        Recipe currentRecipe = mRecipeList.get(position);
        recipeHolder.recipeTextView.setText(currentRecipe.getName());
        if (!(currentRecipe.getImageId().isEmpty())) {
            Picasso.with(mContext)
                    .load(currentRecipe.getImageId())
                    .into(recipeHolder.recipeImageView);
        }
    }

    @Override
    public int getItemCount(){
        return mRecipeList.size();
    }

    public void setmRecipeList(ArrayList<Recipe> recipeList){
        mRecipeList = recipeList;
    }

}
