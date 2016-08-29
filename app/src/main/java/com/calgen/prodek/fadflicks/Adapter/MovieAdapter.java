package com.calgen.prodek.fadflicks.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.Activity.DetailActivity;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.Parser;
import com.calgen.prodek.fadflicks.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gurupad on 25-Aug-16.
 * You asked me to change it for no reason.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> implements AdapterView.OnItemClickListener {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private List<Movie> movieList;

    public MovieAdapter(Context mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        try {
            holder.title.setText(movie.getTitle());
            holder.rating.setText(movie.getVoteAverage().toString());
            Picasso.with(mContext).load(Parser.formatImageUrl(movie.getPosterPath(), mContext.getString(R.string.image_size_small))).into(holder.poster);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick: " + view.toString());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //@formatter:off
        @BindView(R.id.title) public TextView title;
        @BindView(R.id.user_rating) public TextView rating;
        @BindView(R.id.poster) public ImageView poster;
        @BindView(R.id.card_view) public CardView cardView;
        //@formatter:on

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.user_rating, R.id.title, R.id.poster})
        public void onClick(View view) {
            Movie movie = movieList.get(getLayoutPosition());
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movie);
            mContext.startActivity(intent);
        }
    }
}
