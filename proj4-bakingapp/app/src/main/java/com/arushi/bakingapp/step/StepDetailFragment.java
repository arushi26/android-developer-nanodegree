package com.arushi.bakingapp.step;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.arushi.bakingapp.R;
import com.arushi.bakingapp.data.local.entity.StepEntity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import timber.log.Timber;

// Material referenced (Full screen video): https://geoffledak.com/blog/2017/09/11/how-to-add-a-fullscreen-toggle-button-to-exoplayer-in-android/
public class StepDetailFragment extends Fragment
    implements Player.EventListener {

    private static final String TAG = StepActivity.class.getSimpleName();

    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private TextView mTvDesc;
    private FrameLayout mMediaFrame;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private StepEntity mStep;
    // Dialog to display full screen video in landscape
    private Dialog mFullScreenDialog;
    private boolean mIsVideoFullscreen;
    private boolean mTwoPane = false;
    private boolean mIsAutoFullscreen = false;
    private ImageView mToggleFullscreenIcon;
    StepListener mStepListener;

    private static final String KEY_DATA = "Data";
    private static final String KEY_STEP = "Step";
    private static final String KEY_FULLSCREEN = "IsFullScreen";
    private static final String KEY_AUTOFULLSCREEN = "IsAutoFullScreen";
    private static final String KEY_TWOPANE = "TwoPane";

    // Mandatory constructor
    public StepDetailFragment() {}

    public interface StepListener {
        boolean isCurrentFocus(int id);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            // Fragment recreated
            Bundle bundle =savedInstanceState.getBundle(KEY_DATA);
            if(bundle!=null) {
                mStep = bundle.getParcelable(KEY_STEP);
                mIsVideoFullscreen = bundle.getBoolean(KEY_FULLSCREEN);
                mIsAutoFullscreen = bundle.getBoolean(KEY_AUTOFULLSCREEN);
                mTwoPane = bundle.getBoolean(KEY_TWOPANE);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        initViews(rootView);
        bindData();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Initialize Media Session.
        initializeMediaSession();
        // Initialize the player
        String videoUrl = mStep.getVideoURL();
        if( !TextUtils.isEmpty(videoUrl) ) {
            initializePlayer(Uri.parse(videoUrl));
        }

        // Keep false for two-pane layout
        Boolean isCurrentFocus = (mStepListener!=null
                                    && mStepListener.isCurrentFocus(mStep.getId()));

        if(isCurrentFocus){
            // Only if user on current fragment, or layout single pane
            Boolean isConfigLandscape = (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE);

            if ( isConfigLandscape && !TextUtils.isEmpty(videoUrl)) {
                // Show full screen video in landscape orientation
                if (!mIsVideoFullscreen) {
                    mIsAutoFullscreen = true;
                }
                openFullscreenDialog();
            } else if ( mIsVideoFullscreen && !mIsAutoFullscreen) {
                // Continue showing full screen video id requested by user
                openFullscreenDialog();
            }
        }
    }

    private void initViews(View rootView){
        mPlayerView = (PlayerView) rootView.findViewById(R.id.playerView);
        mMediaFrame = rootView.findViewById(R.id.step_frame);

        String videoUrl = mStep.getVideoURL();

        if( videoUrl==null || TextUtils.isEmpty(videoUrl) ) {
            // Video not available for this step
            mPlayerView.setVisibility(View.GONE);
            mMediaFrame.setVisibility(View.GONE);
        }

        mTvDesc = rootView.findViewById(R.id.tv_description);
    }

    private void initPlayerControls(){
        PlayerControlView controlView = mPlayerView.findViewById(R.id.exo_controller);
        FrameLayout mToggleFullscreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mToggleFullscreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);

        mToggleFullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsVideoFullscreen) {
                    // User requested full screen video
                    mIsAutoFullscreen = false;
                    openFullscreenDialog();
                } else {
                    // User requested to close full screen video
                    closeFullscreenDialog();
                }
            }
        });
    }

    private void bindData(){
        mTvDesc.setText(mStep.getDescription());
    }

    /**
     * Initialize Media Session
     */
    private void initializeMediaSession() {

        try {
            // Create a MediaSessionCompat object
            mMediaSession = new MediaSessionCompat(this.getContext(), TAG);

            // Enable callbacks from MediaButtons and TransportControls.
            mMediaSession.setFlags(
                    MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

            // Prevent MediaButtons from restarting player when the app is not visible.
            mMediaSession.setMediaButtonReceiver(null);

            // Set available actions and an initial PlaybackState
            mStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mMediaSession.setPlaybackState(mStateBuilder.build());


            // Handle callbacks from a media controller.
            mMediaSession.setCallback(new CustomSessionCallback());

            // Start the Media Session
            mMediaSession.setActive(true);
        } catch (Exception e) {
            Timber.e("Exception while initializing Media Session - %s", e.getMessage());
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param videoUri URI of video to be played
     */
    private void initializePlayer(Uri videoUri) {
        if (mExoPlayer == null) {
            // Initialize Full screen dialog
            initFullscreenVideoDialog();
            initPlayerControls();
            // Create an instance of the ExoPlayer.
            RenderersFactory renderersFactory = new DefaultRenderersFactory(this.getContext());
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory,
                    trackSelector,
                    loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this.getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(this.getContext(), userAgent))
                    .createMediaSource(videoUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    // Release ExoPlayer
    private void releasePlayer() {
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
        if(mMediaSession!=null) mMediaSession.setActive(false);

        if (mFullScreenDialog != null) {
            mFullScreenDialog.dismiss();
        }
    }

    /* ExoPlayer Event Listeners */
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
        if((playbackState == Player.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == Player.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        if(mMediaSession!=null)  mMediaSession.setPlaybackState(mStateBuilder.build());
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

    // Media Session Callbacks, where all external clients control the player.
    private class CustomSessionCallback extends MediaSessionCompat.Callback {
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

    /* To be called from Activity to set current recipe step data */
    public void setCurrentStep(StepEntity stepEntity){
        mStep = stepEntity;
    }

    /* To be called from Activity to set mTwoPane */
    public void setTwoPane(boolean isTwoPane){
        mTwoPane = isTwoPane;
    }

    private void initFullscreenVideoDialog() {
        if(mPlayerView.getVisibility()!=View.VISIBLE) return;

        mFullScreenDialog = new Dialog(this.getContext(),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mIsVideoFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        if(mPlayerView.getVisibility()!=View.VISIBLE) return;

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        mToggleFullscreenIcon.setImageDrawable(ContextCompat.getDrawable(this.getContext(),
                R.drawable.ic_fullscreen_exit));
        mIsVideoFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        if(mPlayerView.getVisibility()!=View.VISIBLE) return;

        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mMediaFrame.addView(mPlayerView);
        mIsVideoFullscreen = false;
        mFullScreenDialog.dismiss();
        mToggleFullscreenIcon.setImageDrawable(ContextCompat.getDrawable(this.getContext(),
                                                                R.drawable.ic_fullscreen));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_STEP, mStep);
        bundle.putBoolean(KEY_FULLSCREEN, mIsVideoFullscreen);
        bundle.putBoolean(KEY_AUTOFULLSCREEN, mIsAutoFullscreen);
        bundle.putBoolean(KEY_TWOPANE, mTwoPane);
        outState.putBundle(KEY_DATA, bundle);
        super.onSaveInstanceState(outState);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mStepListener = (StepListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StepListener");
        }
    }
}
