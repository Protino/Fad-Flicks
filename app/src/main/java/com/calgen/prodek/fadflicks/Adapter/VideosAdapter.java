/*
 *    Copyright 2016 Gurupad Mamadapur
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.calgen.prodek.fadflicks.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.calgen.prodek.fadflicks.BuildConfig;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.Video;
import com.calgen.prodek.fadflicks.model.VideoResponse;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gurupad on 07-Sep-16.
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    public static Map<YouTubeThumbnailView, YouTubeThumbnailLoader> viewLoaderMap = new HashMap<>();
    private Context context;
    private List<String> keys;
    private Callback callback;


    public VideosAdapter(Context context, VideoResponse videoResponse, Callback callback) {
        this.context = context;
        keys = new ArrayList<>();
        for (Video video : videoResponse.getVideos()) {
            keys.add(video.getKey());
        }
        this.callback = callback;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie_videos, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {

        holder.youTubeThumbnailView.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                viewLoaderMap.put(youTubeThumbnailView, youTubeThumbnailLoader);
                youTubeThumbnailLoader.setVideo(keys.get(holder.getAdapterPosition()));
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                // TODO: 11/1/2016 Show Youtube loader error
                holder.errorPlaceHolder.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        //@formatter:off
        @BindView(R.id.videoView) public YouTubeThumbnailView youTubeThumbnailView;
        @BindView(R.id.placeholder) public ImageView errorPlaceHolder;
        @BindView(R.id.item_video) public CardView itemVideo;
        //@formatter:on

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.videoView,R.id.placeholder,R.id.item_video})
        public void onClick() {
            try {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                        (Activity) context,
                        BuildConfig.YOUTUBE_API_KEY,
                        keys.get(getAdapterPosition()), 0, false, false);
                context.startActivity(intent);
            }catch (Exception e){
                callback.createAlertDialog();
            }
        }
    }
    public interface Callback{
        void createAlertDialog();
    }
}
