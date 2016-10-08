package com.calgen.prodek.fadflicks.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.adapter.GridMovieAdapter;
import com.calgen.prodek.fadflicks.fragment.GridFragment;
import com.calgen.prodek.fadflicks.fragment.MovieDetailFragment;
import com.calgen.prodek.fadflicks.utils.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MainActivity extends AppCompatActivity {


    public static final String MOVIE_DETAIL_FRAGMENT_TAG = "M_D_F_TAG";
    //@formatter:off
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAVOURITE_FRAGMENT_POSITION = 2;
    private static final int TOP_RATED_FRAGMENT_POSITION = 1;
    private static final int POPULAR_FRAGMENT_POSITION = 0;
    private static final int PAGE_LIMIT = 2;
    public static boolean twoPane;
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.tabs) public TabLayout tabLayout;
    @BindView(R.id.viewpager) public ViewPager viewPager;
    @State public int currentItemPosition;
    //@formatter:on
    public ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onStart() {
        super.onStart();
        Cache.getFavouriteMovies(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        currentItemPosition = viewPager.getCurrentItem();
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        viewPager.setCurrentItem(currentItemPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //check if it is a two pane mode
        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailFragment(), MOVIE_DETAIL_FRAGMENT_TAG)
                        .commit();
            }
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        } else {
            twoPane = false;
        }
        setSupportActionBar(toolbar);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        GridFragment popularFragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.fragment_data_key), getString(R.string.popularData));
        popularFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(popularFragment, getString(R.string.popular_tab_title));


        GridFragment topRatedFragment = new GridFragment();
        bundle = new Bundle();
        bundle.putString(getString(R.string.fragment_data_key), getString(R.string.topRatedData));
        topRatedFragment.setArguments(bundle);
        viewPagerAdapter.addFragment(topRatedFragment, getString(R.string.top_rated_tab_title));

        GridFragment favouriteFragment = new GridFragment();
        bundle = new Bundle();
        bundle.putString(getString(R.string.fragment_data_key), getString(R.string.favouritesData));
        favouriteFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(favouriteFragment, getString(R.string.favourites_tab_title));

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(PAGE_LIMIT);
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GridMovieAdapter.FAV_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(getString(R.string.favourite_changed_key), true)) {
                int movieId = data.getIntExtra(getString(R.string.favourite_movie_id_key), -1);
                boolean isFavourite;
                if (movieId == -1)
                    return;
                if (data.hasExtra(getString(R.string.fav_movie_bool_key)))
                    isFavourite = data.getBooleanExtra(getString(R.string.fav_movie_bool_key), false);
                else
                    return;

                notifyFavouriteChange(movieId, isFavourite);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void notifyFavouriteChange(int movieId, boolean isFavourite) {
        GridFragment gridFragment;
        currentItemPosition = viewPager.getCurrentItem();
        switch (currentItemPosition) {
            case 0:
            case 1:
                gridFragment = (GridFragment) viewPagerAdapter.getFragment(currentItemPosition);
                gridFragment.notifyChange(movieId, isFavourite);
                break;
            case 2:
                gridFragment = (GridFragment) viewPagerAdapter.getFragment(FAVOURITE_FRAGMENT_POSITION);
                gridFragment.notifyChange(movieId, isFavourite);

                gridFragment = (GridFragment) viewPagerAdapter.getFragment(TOP_RATED_FRAGMENT_POSITION);
                gridFragment.notifyChange(movieId, isFavourite);

                gridFragment = (GridFragment) viewPagerAdapter.getFragment(POPULAR_FRAGMENT_POSITION);
                gridFragment.notifyChange(movieId, isFavourite);
                return;
        }
        gridFragment = (GridFragment) viewPagerAdapter.getFragment(FAVOURITE_FRAGMENT_POSITION);
        gridFragment.notifyChange(movieId, isFavourite);

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();
        private Map<Integer, String> fragmentTags;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentTags = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment fragment = (Fragment) obj;
                String tag = fragment.getTag();
                fragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = fragmentTags.get(position);
            if (tag == null)
                return null;
            return getSupportFragmentManager().findFragmentByTag(tag);
        }
    }
}
