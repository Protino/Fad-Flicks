package com.calgen.prodek.fadflicks.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.activity.DetailActivity;
import com.calgen.prodek.fadflicks.activity.MainActivity;
import com.calgen.prodek.fadflicks.fragment.MovieDetailFragment;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.utils.Parser;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gurupad on 25-Aug-16.
 * You asked me to change it for no reason.
 */
public class GridMovieAdapter extends RecyclerView.Adapter<GridMovieAdapter.MovieViewHolder> implements AdapterView.OnItemClickListener {
    public static final int FAV_REQUEST_CODE = 2764;
    private static final String TAG = GridMovieAdapter.class.getSimpleName();
    private Context context;
    private List<Movie> movieList;

    public GridMovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movies_grid, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        try {
            holder.title.setText(movie.getTitle());
            holder.rating.setText(movie.getVoteAverage().toString());
            Drawable drawable = (movie.isFavourite)
                    ? context.getResources().getDrawable(R.drawable.ic_favorite_accent_24dp)
                    : context.getResources().getDrawable(R.drawable.ic_favorite_border_accent_24dp);
            holder.favouriteIcon.setImageDrawable(drawable);
            Picasso.with(context)
                    .load(Parser.formatImageUrl(movie.getPosterPath(), context.getString(R.string.image_size_small)))
                    .placeholder(new ColorDrawable(0xB6B6B6))
                    .into(holder.poster);
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

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        //@formatter:off
        @BindView(R.id.title) public TextView title;
        @BindView(R.id.movie_rating) public TextView rating;
        @BindView(R.id.poster) public ImageView poster;
        @BindView(R.id.favourite_icon) public ImageView favouriteIcon;
        @BindView(R.id.card_view) public CardView cardView;
        //@formatter:on

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.movie_rating, R.id.title, R.id.poster})
        public void onClick() {
            Movie movie = movieList.get(getLayoutPosition());
            if (MainActivity.twoPane) {
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                Bundle arguments = new Bundle();
                arguments.putSerializable(Intent.EXTRA_TEXT, movie);
                movieDetailFragment.setArguments(arguments);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, movieDetailFragment, MainActivity.MOVIE_DETAIL_FRAGMENT_TAG)
                        .commit();
            } else {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, movie);
                ((Activity) context).startActivityForResult(intent, FAV_REQUEST_CODE);
            }
        }
    }
}
