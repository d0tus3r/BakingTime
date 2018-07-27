package net.digitalswarm.bakingtime.fragments;
/**
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.RecipeListActivity;
import net.digitalswarm.bakingtime.adapters.RecipeListRVAdapter;

public class RecipeListDetailFragment extends Fragment {
    //vars
    private RecyclerView mRecipeListRV;
    private RecipeListRVAdapter.RecipeListRVAdapterClickListener recipeListRVAdapterClickListener;
    //may change to linear, testing multi recipe look. tie to resolution width
    private GridLayoutManager mGridManager;
    private Parcelable mRVSavedInstanceState;
    private static final String RV_LAYOUT_KEY = "RV_LAYOUT_KEY";
    //default empty constructor
    public RecipeListDetailFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate recipe list fragment layout
        View rootView = inflater.inflate(R.layout.recipe_list_fragment, container, false);
        //setup rv and grid layout
        mGridManager = new GridLayoutManager(rootView.getContext(), 2);
        mRecipeListRV = (RecyclerView) rootView;
        mRecipeListRV.setLayoutManager(mGridManager);
        mRecipeListRV.setAdapter(new RecipeListRVAdapter(getContext(), RecipeListActivity.recipeList, recipeListRVAdapterClickListener));

        return rootView;
    }



    //instance state save and restore
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        mRVSavedInstanceState = mGridManager.onSaveInstanceState();
        currentState.putParcelable(RV_LAYOUT_KEY, mRVSavedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedState) {
        super.onViewStateRestored(savedState);
        //check for saved state
        if (savedState != null){
            mRVSavedInstanceState = savedState.getParcelable(RV_LAYOUT_KEY);
            mRecipeListRV.getLayoutManager().onRestoreInstanceState(mRVSavedInstanceState);
        }
    }


}
*/
