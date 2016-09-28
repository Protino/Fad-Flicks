package com.calgen.prodek.fadflicks.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    //@formatter:on
    public boolean isFavourite;
    public boolean isFavouriteOriginal;
    //@formatter:off
    @BindView(R.id.fav_fab) FloatingActionButton fav_fab;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image_backdrop) ImageView backdropImage;
    @BindDrawable(R.drawable.ic_favorite_border_white_24dp) Drawable notFavouriteDrawable;
    @BindDrawable(R.drawable.ic_favorite_white_24dp) Drawable favouriteDrawable;
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
        isFavourite = movie.isFavourite;
        isFavouriteOriginal = isFavourite;
        setFavButtonDrawable();
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
        isFavourite = !isFavourite;
        setFavButtonDrawable();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isFavouriteOriginal != isFavourite) {
            Cache.setFavouriteMovie(this, movie.getId(), isFavourite);
            setResult(RESULT_OK, new Intent().putExtra(getString(R.string.favourite_changed_key), true));
            finish();
        }
        super.onBackPressed();
    }

    private void setFavButtonDrawable() {
        fav_fab.setImageDrawable((isFavourite) ? favouriteDrawable : notFavouriteDrawable);
    }
}
