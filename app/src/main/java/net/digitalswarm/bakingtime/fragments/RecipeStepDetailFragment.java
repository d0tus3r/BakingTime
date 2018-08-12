package net.digitalswarm.bakingtime.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.squareup.picasso.Picasso;
import net.digitalswarm.bakingtime.R;
import net.digitalswarm.bakingtime.models.Recipe;
import net.digitalswarm.bakingtime.models.RecipeStep;

public class RecipeStepDetailFragment extends Fragment implements Player.EventListener {

    //saved state bundle keys
    private static final String EXO_POS_KEY = "EXO_POS_KEY";
    private static final String EXO_STATE_KEY = "EXO_STATE_KEY";
    private static final String CURRENT_RECIPE = "CURRENT_RECIPE";
    private static final String CURRENT_STEP = "CURRENT_STEP";
    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private Recipe mCurrentRecipe;
    private RecipeStep mCurrentStep;
    //get value of stepId to handle previous and next buttons
    int mStepId;
    private String currentStepVideoUrl;
    private String currentStepImageUrl;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    SimpleExoPlayerView mPlayerView;
    private Context mContext;
    //exo player states
    private long mExoPosition;
    private boolean mExoState = true;

    public RecipeStepDetailFragment() {
        //empty constructor
    }

    //Setters for incoming recipe + step :used in construction of fragment
    public void setCurrentRecipe(Recipe recipe){
        mCurrentRecipe = recipe;
    }
    public void setCurrentStep(RecipeStep recipeStep) {
        mCurrentStep = recipeStep;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //inflate layout
        View rootView = inflater.inflate(R.layout.recipe_step_detail_fragment, container, false);

        //grab recipe, currentStep, and exo values from bundle if available
        if (savedInstanceState != null) {
            mCurrentRecipe = savedInstanceState.getParcelable(CURRENT_RECIPE);
            mCurrentStep = savedInstanceState.getParcelable(CURRENT_STEP);
            mExoPosition = savedInstanceState.getLong(EXO_POS_KEY);
            mExoState = savedInstanceState.getBoolean(EXO_STATE_KEY);
        }
        //if incoming recipe step doesn't exist, use initial step
        if (mCurrentStep == null) {
            mCurrentStep = mCurrentRecipe.getRecipeSteps().get(0);
        }

        //Views
        mContext = getContext();
        mPlayerView = rootView.findViewById(R.id.recipe_step_exo_view);
        final ImageView currentStepImageView = rootView.findViewById(R.id.recipe_step_iv);
        final TextView stepDescriptionTV = rootView.findViewById(R.id.recipe_step_description_tv);
        //Buttons
        final Button prevButton = rootView.findViewById(R.id.previous_step_btn);
        final Button nextButton = rootView.findViewById(R.id.next_step_btn);
        //Urls
        currentStepVideoUrl = mCurrentStep.getVideoUrl();
        Log.d(TAG, "videoUrl = " + currentStepVideoUrl);
        currentStepImageUrl = mCurrentStep.getThumbnailUrl();
        //get value of stepId to handle previous and next buttons
        mStepId = Integer.parseInt(mCurrentStep.getId());

        //hide image view if no data / else display image
        if (currentStepImageUrl.isEmpty()) {
            currentStepImageView.setVisibility(View.GONE);
        } else {
            Picasso.with(mContext)
                    .load(currentStepImageUrl)
                    .into(currentStepImageView);
        }
        //hide view if no videoUrl
        if (currentStepVideoUrl.isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
        } else {
            initExoPlayer(Uri.parse(currentStepVideoUrl), mPlayerView);
        }
        //set text
        stepDescriptionTV.setText(mCurrentStep.getDescription());

        //setup buttons | on click listeners and hide buttons if buttons aren't needed
        if (mStepId == 0) {
            prevButton.setVisibility(View.INVISIBLE);
        } else if  (mStepId == mCurrentRecipe.getRecipeSteps().size() - 1) {
            nextButton.setVisibility(View.INVISIBLE);
        }
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make sure there's a step to go to
                if (mStepId >= 1) {
                    //set step Id to 1 less
                    mStepId--;
                    //get new step
                    mCurrentStep = mCurrentRecipe.getRecipeSteps().get(mStepId);
                    //Hide button visibility
                    if (mStepId <= 0) {
                        prevButton.setVisibility(View.INVISIBLE);
                    }
                    //get new video url & image url + step text
                    mExoPlayer.stop();
                    currentStepVideoUrl = mCurrentStep.getVideoUrl();
                    currentStepImageUrl = mCurrentStep.getThumbnailUrl();
                    stepDescriptionTV.setText(mCurrentStep.getDescription());
                    if (currentStepVideoUrl.isEmpty()) {
                        mPlayerView.setVisibility(View.GONE);
                    } else {
                        mPlayerView.setVisibility(View.VISIBLE);
                        updateUrl(Uri.parse(currentStepVideoUrl));
                    }
                    //make next button visible
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //make sure there's a step to go to
                if (mStepId < mCurrentRecipe.getRecipeSteps().size() -1) {
                    //set step Id to 1 more
                    mStepId++;
                    //get new step
                    mCurrentStep = mCurrentRecipe.getRecipeSteps().get(mStepId);
                    //Hide button visibility
                    if (mStepId == mCurrentRecipe.getRecipeSteps().size() -1) {
                        nextButton.setVisibility(View.INVISIBLE);
                    }
                    //get new video url & image url + step text
                    mExoPlayer.stop();
                    currentStepVideoUrl = mCurrentStep.getVideoUrl();
                    currentStepImageUrl = mCurrentStep.getThumbnailUrl();
                    stepDescriptionTV.setText(mCurrentStep.getDescription());
                    if (currentStepVideoUrl.isEmpty()) {
                        mPlayerView.setVisibility(View.GONE);
                    } else {
                        mPlayerView.setVisibility(View.VISIBLE);
                        updateUrl(Uri.parse(currentStepVideoUrl));
                    }
                    //make prev button visible
                    prevButton.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }

    private void initMediaSession() {
        //create MediaSession
        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        //enable callbacks from buttons and controls
        mMediaSession.setFlags(
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

    //init player w/ url etc
    private void initExoPlayer(Uri videoUrl, SimpleExoPlayerView exoView) {
        //test for already established exo player
        if (mExoPlayer == null) {
            initMediaSession();
            //create trackSelector
            TrackSelector trackSelector = new DefaultTrackSelector();
            //create new instance of exo player with trackselector
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            //assign player to view
            exoView.setPlayer(mExoPlayer);
            //add listener for callbacks
            mExoPlayer.addListener(this);
            //add media source with video url
            MediaSource mediaSource = new ExtractorMediaSource(videoUrl, new DefaultDataSourceFactory(
                    getContext(), "BakingTime"), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(mExoState);
            mExoPlayer.seekTo(mExoPosition);
        }
    }
    //set video url for exo player [ after exo player has already been init ]
    private void updateUrl(Uri videoUrl) {
        //add media source with video url
        MediaSource mediaSource = new ExtractorMediaSource(videoUrl, new DefaultDataSourceFactory(
                getContext(), "BakingTime"), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(mExoState);
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
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putLong(EXO_POS_KEY, mExoPosition);
        bundle.putBoolean(EXO_STATE_KEY, mExoState);
        bundle.putParcelable(CURRENT_RECIPE, mCurrentRecipe);
        bundle.putParcelable(CURRENT_STEP, mCurrentStep);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == Player.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPause() {
        super.onPause();
        //save position
        if (mExoPlayer != null) {
            mExoPosition = mExoPlayer.getCurrentPosition();
            //save state
            mExoState = mExoPlayer.getPlayWhenReady();
        }
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            //change media session state to not active
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        initExoPlayer(Uri.parse(currentStepVideoUrl), mPlayerView);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }
    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }
    @Override
    public void onPositionDiscontinuity(int reason) {

    }
    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
    @Override
    public void onSeekProcessed() {

    }
}
