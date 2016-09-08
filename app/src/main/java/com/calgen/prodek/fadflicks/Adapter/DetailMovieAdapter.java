package com.calgen.prodek.fadflicks.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.Parser;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.model.MovieDetails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gurupad on 02-Sep-16.
 * You asked me to change it for no reason.
 */
public class DetailMovieAdapter extends RecyclerView.Adapter<DetailMovieAdapter.BaseViewHolder> {

    private static final String TAG = DetailMovieAdapter.class.getSimpleName();
    private MovieDetails movieDetails;
    private Context context;

    public DetailMovieAdapter(Context context, MovieDetails movieDetails) {
        this.context = context;
        this.movieDetails = movieDetails;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_detail_base, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Movie movie = movieDetails.movie;

        Picasso.with(context).load(Parser.formatImageUrl(movie.getPosterPath(), context.getString(R.string.image_size_small)))
                .into(holder.poster);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        // TODO: 06-Sep-16 fetch more info and store into movie object before passing it to the adapter
        // FIXME: 06-Sep-16 runtime shows original language
        holder.runtime.setText(movie.getOriginalLanguage());
        holder.movieRating.setRating((float) (movie.getVoteAverage() / 2));
        holder.plot.setText(movie.getOverview());

        // Now initialize recycler views and assign them new adapters
        ReviewAdapter reviewAdapter = new ReviewAdapter(context, movieDetails.reviewResponse);
        holder.cardReview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.cardReview.setNestedScrollingEnabled(false);
        holder.cardReview.setAdapter(reviewAdapter);

        CreditsAdapter creditsAdapter = new CreditsAdapter(context, movieDetails.credits);
        holder.cardCast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.cardCast.setHasFixedSize(true);
        holder.cardCast.setNestedScrollingEnabled(false);
        holder.cardCast.setAdapter(creditsAdapter);

        VideosAdapter videosAdapter = new VideosAdapter(context, movieDetails.videoResponse);
        holder.cardVideo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.cardVideo.setHasFixedSize(true);
        holder.cardVideo.setNestedScrollingEnabled(false);
        holder.cardVideo.setAdapter(videosAdapter);


    }

    @Override
    public int getItemCount() {
        return (movieDetails.movie == null) ? 0 : 1;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        //@formatter:off
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.release_date) TextView releaseDate;
        @BindView(R.id.runtime) TextView runtime;
        @BindView(R.id.movie_rating) RatingBar movieRating;
        @BindView(R.id.plot_text) TextView plot;
        @BindView(R.id.read_more_details) Button moreDetails;
        @BindView(R.id.cast_recycler) RecyclerView cardCast;
        @BindView(R.id.trailer_recycler) RecyclerView cardVideo;
        @BindView(R.id.review_recycler) RecyclerView cardReview;
        //@formatter:on
        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.read_more_details)
        public void OnClick() {

        }
    }
}
