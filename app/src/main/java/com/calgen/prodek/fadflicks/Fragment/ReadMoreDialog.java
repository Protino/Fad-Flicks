package com.calgen.prodek.fadflicks.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.calgen.prodek.fadflicks.model.MovieDetails;
import com.calgen.prodek.fadflicks.utils.Parser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurupad on 08-Sep-16.
 * You asked me to change it for no reason.
 */
public class ReadMoreDialog extends DialogFragment {

    public static final String TAG = ReadMoreDialog.class.getSimpleName();
    public MovieBundle movieBundle;
    public MovieDetails movieDetails;
    //@formatter:off
    @BindView(R.id.status_bar) View status_bar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.plot_text) TextView plotText;
    @BindView(R.id.actors) TextView actors;
    @BindView(R.id.directors) TextView directors;
    @BindView(R.id.writers) TextView writers;
    @BindView(R.id.musicians) TextView musicians;
    @BindView(R.id.producers) TextView producers;
    @BindView(R.id.genre) TextView genre;
    @BindView(R.id.votes) TextView votes;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.duration) TextView duration;
    @BindView(R.id.language) TextView language;
    @BindView(R.id.website) TextView website;
    @BindView(R.id.release_date) TextView releaseDate;
    //@formatter:on

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieBundle = (MovieBundle) getArguments().getSerializable(Intent.EXTRA_TEXT);
        movieDetails = movieBundle.movieDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.more_movie_details, container, false);
        ButterKnife.bind(this, rootView);
        status_bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight() ));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5);
        }

        plotText.setText(movieDetails.getOverview());
        actors.setText(Parser.getActors(movieBundle.credits));
        directors.setText(Parser.getDirectors(movieBundle.credits));
        producers.setText(Parser.getProducers(movieBundle.credits));
        musicians.setText(Parser.getMusicians(movieBundle.credits));
        writers.setText(Parser.getWriters(movieBundle.credits));
        genre.setText(Parser.getGenre(movieDetails.getGenres()));
        votes.setText(movieDetails.getVoteAverage() + "/" + getString(R.string.max_votes));
        rating.setText((movieDetails.getAdult()) ? getString(R.string.adult_rating) : getString(R.string.non_adult_rating));
        duration.setText(movieDetails.getRuntime() + " " + getString(R.string.minutes));
        language.setText(movieDetails.getOriginalLanguage());
        website.setText(movieDetails.getHomepage());
        releaseDate.setText(movieDetails.getReleaseDate());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.d(TAG, "getStatusBarHeight: " + result);
        return result;
    }
}
