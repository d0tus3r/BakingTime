package net.digitalswarm.bakingtime.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.RecipeSteps;

public class RecipeStepDetailFragment extends Fragment {

    //keys for changing steps
    private static final String STEP_ID_KEY = "id";
    private static final String STEP_KEY = "step";
    //saved state bundle keys
    private static final String EXO_POS_KEY = "EXO_POS_KEY";
    private static final String EXO_FULLSCREEN_KEY = "EXO_FULLSCREEN_KEY";
    private static final String EXO_STATE_KEY = "EXO_STATE_KEY";

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    private RecipeSteps currentStep;
    private int currentStepId;
    private FragmentActivity fragmentContext;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    OnPrevStepsClickListener mPrevCallback;
    OnNextStepsClickListener mNextCallback;

    //exo player states
    private long mExoPosition;
    private boolean mExoFullscreen = false;
    private boolean mExoState = true;

    public interface OnPrevStepsClickListener {
        void onPrevStepSelected(int position);
    }

    public interface OnNextStepsClickListener {
        void onNextStepSelected(int position);
    }

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
        Button prevButton = rootView.findViewById(R.id.previous_step_btn);
        Button nextButton = rootView.findViewById(R.id.next_step_btn);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrevCallback.onPrevStepSelected(currentStepId);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextCallback.onNextStepSelected(currentStepId);
            }
        });
        //exoplayer
        mPlayerView = rootView.findViewById(R.id.recipe_step_exo_view);



        return rootView;
    }


    private void initMediaSession() {
        //create MediaSession
        mMediaSession = new MediaSessionCompat(fragmentContext, TAG);
        //enable callbacks from buttons and controls
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        //keep buttons from restarting player when not in focus
        mMediaSession.setMediaButtonReceiver(null);
        //set initial playback state
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE
        );
        //build initial state
        mMediaSession.setPlaybackState(mStateBuilder.build());
        //set callbacks
        mMediaSession.setCallback(new ExoCallBack());

        //Start media session
        mMediaSession.setActive(true);

    }

    //init player url etc
    private void initExoPlayer() {

    }

    private class ExoCallBack extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }
        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mPrevCallback = (RecipeStepDetailFragment.OnPrevStepsClickListener) context;
            mNextCallback = (RecipeStepDetailFragment.OnNextStepsClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepsClickListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putLong(EXO_POS_KEY, mExoPosition);
        bundle.putBoolean(EXO_FULLSCREEN_KEY, mExoFullscreen);
        bundle.putBoolean(EXO_STATE_KEY, mExoState);
    }

}
