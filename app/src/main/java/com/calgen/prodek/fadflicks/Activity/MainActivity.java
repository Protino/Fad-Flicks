package com.calgen.prodek.fadflicks.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.calgen.prodek.fadflicks.fragment.FavouriteFragment;
import com.calgen.prodek.fadflicks.fragment.PopularFragment;
import com.calgen.prodek.fadflicks.fragment.TopRatedFragment;
import com.calgen.prodek.fadflicks.utils.Cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String POSITION = "position";
    //@formatter:off
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.tabs) public TabLayout tabLayout;
    @BindView(R.id.viewpager) public ViewPager viewPager;
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
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PopularFragment(), getString(R.string.popular_tab_title));
        viewPagerAdapter.addFragment(new TopRatedFragment(), getString(R.string.top_rated_tab_title));
        viewPagerAdapter.addFragment(new FavouriteFragment(), getString(R.string.favourites_tab_title));
        viewPager.setAdapter(viewPagerAdapter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GridMovieAdapter.FAV_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra(getString(R.string.favourite_changed_key), true)) {
                    PopularFragment popularFragment = (PopularFragment) viewPagerAdapter.getFragment(viewPager.getCurrentItem());
                    popularFragment.notifyChange();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
