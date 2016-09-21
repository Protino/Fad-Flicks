package com.calgen.prodek.fadflicks.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.calgen.prodek.fadflicks.utils.Parser;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    //@formatter:off
    @BindView(R.id.fav_fab) FloatingActionButton fav_fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image_backdrop) ImageView backdropImage;
    @BindDrawable(R.drawable.ic_favorite_border_white_24dp) Drawable notFavouriteDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp) Drawable favouriteDrawable;
    //@formatter:on
    public static boolean isFavourite;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        movie = (Movie) getIntent().getSerializableExtra(Intent.EXTRA_TEXT);
        Picasso.with(this)
                .load(Parser.formatImageUrl(movie.backdropPath, getString(R.string.image_size_large)))
                .placeholder(new ColorDrawable(0xFFFFFF))
                .into(backdropImage);
        isFavourite = false; // check if movie id in fav, if set true else false
        onFabClick();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick(R.id.fav_fab)
    public void onFabClick() {
        //store movie Id in prefs for now
        fav_fab.setImageDrawable((isFavourite) ? favouriteDrawable : notFavouriteDrawable);
        isFavourite = !isFavourite;
    }

    @Override
    protected void onDestroy() {
        Cache.setFavouriteMovie(this, movie.getId(), isFavourite);
        super.onDestroy();
    }
}
