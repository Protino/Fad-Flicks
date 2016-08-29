package com.calgen.prodek.fadflicks.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.Parser;
import com.calgen.prodek.fadflicks.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        //Get backdrop_path and movie_title from intent
        Movie movie = (Movie) getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        collapsingToolbarLayout.setTitle(movie.title);
        ImageView imageView = (ImageView) collapsingToolbarLayout.findViewById(R.id.image_backdrop);
        imageView.setAdjustViewBounds(true);
        Picasso.with(this)
                .load(Parser.formatImageUrl(movie.backdropPath, getString(R.string.image_size_medium)))
                .placeholder(new ColorDrawable(0xFFFFFF))
                .into(imageView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

}
