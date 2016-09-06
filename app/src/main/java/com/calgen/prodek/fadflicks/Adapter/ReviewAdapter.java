package com.calgen.prodek.fadflicks.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.model.Review;
import com.calgen.prodek.fadflicks.model.ReviewResponse;

import java.util.List;

/**
 * Created by Gurupad on 07-Sep-16.
 * You asked me to change it for no reason.
 */

// TODO: 07-Sep-16 LOGIC : add min of two reviews and show more button if more exists
public class ReviewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, ReviewResponse review) {
        this.context = context;
        reviews = review.getReviewResponses();
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
