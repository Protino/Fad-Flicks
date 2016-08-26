package com.calgen.prodek.fadflicks.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.Parser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        //Get backdrop_path and movie_title from intent
        String movieDetails = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        String backdropUrl = "";
        String movieTitle = "";
        try {
            JSONObject jsonObject = new JSONObject(movieDetails);
            backdropUrl = jsonObject.getString("backdrop_path");
            movieTitle = jsonObject.getString("original_title");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: JSONException", e);
        }
        collapsingToolbarLayout.setTitle(movieTitle);
        ImageView imageView = (ImageView) collapsingToolbarLayout.findViewById(R.id.image_backdrop);
        imageView.setAdjustViewBounds(true);
        Picasso.with(this)
                .load(Parser.formatImageUrl(backdropUrl))
                .placeholder(new ColorDrawable(0xFFFFFF))
                .into(imageView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

}
