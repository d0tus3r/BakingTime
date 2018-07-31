package net.digitalswarm.bakingtime.fragments;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.RecipeSteps;

public class RecipeStepDetailFragment extends Fragment {

    //keys for changing steps
    private static final String STEP_ID_KEY = "id";
    private static final String STEP_KEY = "step";
    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    private RecipeSteps currentStep;
    private int currentStepId;
    private FragmentActivity fragmentContext;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

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

}
