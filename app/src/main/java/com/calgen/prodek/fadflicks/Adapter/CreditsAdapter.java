package com.calgen.prodek.fadflicks.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.model.Credits;

/**
 * Created by Gurupad on 07-Sep-16.
 * You asked me to change it for no reason.
 */
public class CreditsAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final Credits credits;

    public CreditsAdapter(Context context, Credits credits) {
        this.context = context;
        this.credits = credits;

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
