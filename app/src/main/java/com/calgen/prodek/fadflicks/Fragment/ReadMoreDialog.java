package com.calgen.prodek.fadflicks.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.calgen.prodek.fadflicks.model.MovieDetails;
import com.calgen.prodek.fadflicks.utils.Parser;
import com.calgen.prodek.fadflicks.utils.UI;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurupad on 08-Sep-16.
 */
public class ReadMoreDialog extends DialogFragment {

    public static final String TAG = ReadMoreDialog.class.getSimpleName();
    public MovieBundle movieBundle;
    public MovieDetails movieDetails;
    //@formatter:off
    @BindView(R.id.status_bar) public View status_bar;
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.actors) public TextView actors;
    @BindView(R.id.directors) public TextView directors;
    @BindView(R.id.writers) public TextView writers;
    @BindView(R.id.musicians) public TextView musicians;
    @BindView(R.id.producers) public TextView producers;
    @BindView(R.id.genre) public TextView genre;
    @BindView(R.id.votes) public TextView votes;
    @BindView(R.id.rating) public TextView rating;
    @BindView(R.id.duration) public TextView duration;
    @BindView(R.id.language) public TextView language;
    @BindView(R.id.website) public TextView website;
    @BindView(R.id.release_date) public TextView releaseDate;
    @BindView(R.id.plot_text) public TextView plotText;
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

        plotText.setText(movieDetails.getOverview());
        actors.setText(Parser.getActors(movieBundle.credits));
        directors.setText(Parser.getDirectors(movieBundle.credits));
        producers.setText(Parser.getProducers(movieBundle.credits));
        musicians.setText(Parser.getMusicians(movieBundle.credits));
        writers.setText(Parser.getWriters(movieBundle.credits));
        genre.setText(Parser.getGenre(movieDetails.getGenres()));
        votes.setText(movieDetails.getVoteAverage() + "/" + getString(R.string.max_votes));
        rating.setText((movieDetails.getAdult()) ? getString(R.string.adult_rating) : getString(R.string.non_adult_rating));
        duration.setText(String.format(getString(R.string.runtime), movieDetails.getRuntime()));
        language.setText(movieDetails.getOriginalLanguage());
        website.setText((movieDetails.getHomepage().isEmpty()) ? "-" : movieDetails.getHomepage());
        releaseDate.setText(movieDetails.getReleaseDate());

        Linkify.addLinks(website, Linkify.ALL);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(5);
            handleStatusBar();
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }

    private void handleStatusBar() {
        if (!getContext().getResources().getBoolean(R.bool.large_layout)) {
            status_bar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UI.getStatusBarHeight(getContext())));
        }
    }

}
