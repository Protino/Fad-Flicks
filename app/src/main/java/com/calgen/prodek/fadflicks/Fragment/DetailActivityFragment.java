package com.calgen.prodek.fadflicks.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.Activity.DetailActivity;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.Parser;
import com.calgen.prodek.fadflicks.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String TAG = DetailActivity.class.getSimpleName();
    String posterUrl;
    String userRating;
    String releaseDate;
    String moviePlot;
    Movie movie;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            movie = (Movie) intent.getSerializableExtra(Intent.EXTRA_TEXT);
            posterUrl = Parser.formatImageUrl(movie.posterPath, getString(R.string.image_size_small));
            userRating = movie.voteAverage.toString();
            releaseDate = movie.releaseDate;
            moviePlot = movie.overview;
        }

        //set poster image
        ImageView imageView = (ImageView) rootView.findViewById(R.id.poster);
        imageView.setAdjustViewBounds(true);
        Picasso.with(getContext()).load(posterUrl).into(imageView);


        //set ratings, title and plot
        SpannableString spannableString = Parser.formatIntoSpannableString("User Rating\n" + userRating, 0, 11);
        ((TextView) rootView.findViewById(R.id.user_rating)).setText(spannableString);

        releaseDate = Parser.formatReleaseDate(releaseDate);
        spannableString = Parser.formatIntoSpannableString("Release Date\n" + releaseDate, 0, 12);
        ((TextView) rootView.findViewById(R.id.release_date)).setText(spannableString);

        spannableString = Parser.formatIntoSpannableString("Plot \n" + moviePlot, 0, 4);
        ((TextView) rootView.findViewById(R.id.plot)).setText(spannableString);

        return rootView;
    }


}
