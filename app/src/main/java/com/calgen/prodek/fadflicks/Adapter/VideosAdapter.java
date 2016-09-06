package com.calgen.prodek.fadflicks.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.model.Video;
import com.calgen.prodek.fadflicks.model.VideoResponse;

import java.util.List;

/**
 * Created by Gurupad on 07-Sep-16.
 * You asked me to change it for no reason.
 */
public class VideosAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<Video> videoList;

    public VideosAdapter(Context context, VideoResponse videoResponse) {
        this.context = context;
        this.videoList = videoResponse.getVideos();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
