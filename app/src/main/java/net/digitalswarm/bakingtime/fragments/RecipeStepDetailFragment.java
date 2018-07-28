package net.digitalswarm.bakingtime.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.RecipeSteps;

public class RecipeStepDetailFragment extends Fragment {

    //keys for changing steps
    private static final String STEP_ID_KEY = "id";
    private static final String STEP_KEY = "step";

    private RecipeSteps currentStep;
    private int currentStepId;

    public RecipeStepDetailFragment() {
        //empty constructor
    }

    //recipe step detail instance
    public static RecipeStepDetailFragment newInstance(RecipeSteps step, int stepId) {
        RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(STEP_ID_KEY, stepId);
        bundle.putParcelable(STEP_KEY, step);
        recipeStepDetailFragment.setArguments(bundle);
        return recipeStepDetailFragment;
    }

    //Fragment on create setup
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //assign recipe data
        currentStepId = getArguments().getInt(STEP_ID_KEY);
        currentStep = getArguments().getParcelable(STEP_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout
        View rootView = inflater.inflate(R.layout.recipe_step_detail_fragment, container, false);
        //assign text view and set text
        TextView stepDescriptionTV = rootView.findViewById(R.id.recipe_step_description_tv);
        stepDescriptionTV.setText(currentStep.getDescription());



        return rootView;
    }

}
