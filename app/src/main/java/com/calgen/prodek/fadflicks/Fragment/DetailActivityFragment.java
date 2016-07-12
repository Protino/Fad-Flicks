package com.calgen.prodek.fadflicks.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.Activity.DetailActivity;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.MovieDataParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String TAG = DetailActivity.class.getSimpleName();
    Typeface robotoFont;
    String posterUrl;
    String userRating;
    String releaseDate;
    String moviePlot;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        try {
            JSONObject movieDetails = new JSONObject(intent.getStringExtra(intent.EXTRA_TEXT));
            posterUrl = MovieDataParser.formatImageUrl(movieDetails.getString("poster_path"));
            userRating = movieDetails.getString("vote_average");
            releaseDate = movieDetails.getString("release_date");
            moviePlot = movieDetails.getString("overview");
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: JSONException", e);
        }

        //set poster image
        ImageView imageView = (ImageView) rootView.findViewById(R.id.image_poster);
        imageView.setAdjustViewBounds(true);
        Picasso.with(getContext()).load(posterUrl).into(imageView);

        //set ratings, title and plot
        ((TextView) rootView.findViewById(R.id.user_rating)).setText(userRating);
        ((TextView) rootView.findViewById(R.id.release_date)).setText(releaseDate);
        ((TextView) rootView.findViewById(R.id.plot)).setText(moviePlot);

        return rootView;
    }
}
