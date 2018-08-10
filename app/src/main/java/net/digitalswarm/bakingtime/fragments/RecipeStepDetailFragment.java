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
import net.digitalswarm.bakingtime.models.RecipeSteps;

public class RecipeStepDetailFragment extends Fragment implements Player.EventListener {

    //keys for changing steps
    private static final String STEP_ID_KEY = "id";
    private static final String STEP_KEY = "step";
    //saved state bundle keys
    private static final String EXO_POS_KEY = "EXO_POS_KEY";
    private static final String EXO_STATE_KEY = "EXO_STATE_KEY";

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    private RecipeSteps currentStep;
    private int currentStepId;
    private String currentStepVideoUrl;
    private String currentStepImageUrl;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private OnPrevStepsClickListener mPrevCallback;
    private OnNextStepsClickListener mNextCallback;
    private Context mContext;
    SimpleExoPlayerView mPlayerView;

    //exo player states
    private long mExoPosition;
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
        mContext = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout
        View rootView = inflater.inflate(R.layout.recipe_step_detail_fragment, container, false);
        //exoplayer
        mPlayerView = rootView.findViewById(R.id.recipe_step_exo_view);
        ImageView currentStepImageView = rootView.findViewById(R.id.recipe_step_iv);
        currentStepVideoUrl = currentStep.getVideoUrl();
        Log.d(TAG, "videoUrl = " + currentStepVideoUrl);
        currentStepImageUrl = currentStep.getThumbnailUrl();
        //hide image view if no data / else display image
        if (currentStepImageUrl.isEmpty()) {
            currentStepImageView.setVisibility(View.GONE);
        } else {
            Picasso.with(mContext)
                    .load(currentStepImageUrl)
                    .into(currentStepImageView);
        }

        if (savedInstanceState != null) {
            mExoPosition = savedInstanceState.getLong(EXO_POS_KEY);
            mExoState = savedInstanceState.getBoolean(EXO_STATE_KEY);
        }
        //hide view if no videoUrl
        if (currentStepVideoUrl.isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
        } else {
            initExoPlayer(Uri.parse(currentStepVideoUrl), mPlayerView);
        }
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
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putLong(EXO_POS_KEY, mExoPosition);
        bundle.putBoolean(EXO_STATE_KEY, mExoState);
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

    }

    @Override
    public void onStop() {
        super.onStop();
        //stop video and release player
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            //change media session state to not active
            mMediaSession.setActive(false);
        }

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

    @Override
    public void onResume(){
        super.onResume();
        initExoPlayer(Uri.parse(currentStepVideoUrl), mPlayerView);
    }

}
