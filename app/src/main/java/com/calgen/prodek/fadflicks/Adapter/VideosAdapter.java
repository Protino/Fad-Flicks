package com.calgen.prodek.fadflicks.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.BuildConfig;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.Video;
import com.calgen.prodek.fadflicks.model.VideoResponse;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gurupad on 07-Sep-16.
 * You asked me to change it for no reason.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private Context context;
    private List<String> keys;


    public VideosAdapter(Context context, VideoResponse videoResponse) {
        this.context = context;
        keys = new ArrayList<>();
        for (Video video : videoResponse.getVideos()) {
            keys.add(video.getKey());
        }
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie_videos, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, final int position) {

        holder.youTubeThumbnailView.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(keys.get(position));
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        //@formatter:off
        @BindView(R.id.videoView) YouTubeThumbnailView youTubeThumbnailView;
        //@formatter:on

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.videoView)
        public void onClick() {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                    (Activity) context,
                    BuildConfig.YOUTUBE_API_KEY,
                    keys.get(getLayoutPosition()),0,false,false);
            context.startActivity(intent);
        }
    }
}
