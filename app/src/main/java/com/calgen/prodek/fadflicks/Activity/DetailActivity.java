package com.calgen.prodek.fadflicks.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.adapter.VideosAdapter;
import com.calgen.prodek.fadflicks.fragment.MovieDetailFragment;
import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.calgen.prodek.fadflicks.utils.Parser;
import com.calgen.prodek.fadflicks.utils.UI;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
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
    @BindView(R.id.nestedScrollView) NestedScrollView nestedScrollView;
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
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(savedInstanceState == null){
            Bundle arguments = new Bundle();
            arguments.putSerializable(Intent.EXTRA_TEXT,movie);
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, movieDetailFragment)
                    .commit();
        }

        setUpLayoutMargins();
    }

    private void setUpLayoutMargins() {
        CoordinatorLayout.LayoutParams scrollViewParams = (CoordinatorLayout.LayoutParams) nestedScrollView.getLayoutParams();

        AppBarLayout.ScrollingViewBehavior behavior = (AppBarLayout.ScrollingViewBehavior) scrollViewParams.getBehavior();
        int overlayTopDimen = (int) getResources().getDimension(R.dimen.overlayTopDimen);
        int wideMargin = (int) getResources().getDimension(R.dimen.content_detail_wide_margin);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            behavior.setOverlayTop(UI.dpToPx(overlayTopDimen));
            scrollViewParams.setMargins(UI.dpToPx(wideMargin),0,UI.dpToPx(wideMargin),0);
        }else{
            behavior.setOverlayTop(0);
            scrollViewParams.setMargins(0,0,0,0);
        }
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
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.favourite_changed_key), true);
            intent.putExtra(getString(R.string.fav_movie_bool_key),isFavourite);
            intent.putExtra(getString(R.string.favourite_movie_id_key),movie.getId());
            setResult(RESULT_OK,intent);
            finish();
        }
        super.onBackPressed();
    }

    private void setFavButtonDrawable() {
        fav_fab.setImageDrawable((isFavourite) ? favouriteDrawable : notFavouriteDrawable);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (VideosAdapter.viewLoaderMap == null) return;
        for (YouTubeThumbnailLoader loader : VideosAdapter.viewLoaderMap.values()) {
            loader.release();
        }
    }
}
