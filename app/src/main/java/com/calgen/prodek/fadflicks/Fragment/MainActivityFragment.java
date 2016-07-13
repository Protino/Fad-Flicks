package com.calgen.prodek.fadflicks.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.calgen.prodek.fadflicks.Activity.DetailActivity;
import com.calgen.prodek.fadflicks.Adapter.ImageAdapter;
import com.calgen.prodek.fadflicks.BuildConfig;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.Cache;
import com.calgen.prodek.fadflicks.Utility.Network;
import com.calgen.prodek.fadflicks.Utility.Parser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getSimpleName();
    private static final String MOVIE_DATA = "movie_data";
    public static ImageAdapter imageAdapter;
    public GridView gridView;
    LinearLayout linearLayoutProgressBar;
    private String memoryCachedMovieData;
    private String sort_type = "";
    private View rootView;

    public MainActivityFragment() {
    }

    public static void updateAdapter(String data) {
        imageAdapter.update(Parser.getAllMoviePosterUrls(data));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String currentSortType = sort_type;

        switch (id) {
            case R.id.menuSortPopular:
                sort_type = getString(R.string.sort_type_popular);
                break;
            case R.id.menuSortTopRated:
                sort_type = getString(R.string.sort_type_top_rated);
                break;
        }
        if (!currentSortType.equals(sort_type)) {
            updateMovieData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(MOVIE_DATA, memoryCachedMovieData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            sort_type = getString(R.string.sort_type_default);
            updateMovieData();
        } else {
            memoryCachedMovieData = savedInstanceState.getString(MOVIE_DATA);
            if (memoryCachedMovieData != null)
                updateAdapter(memoryCachedMovieData);
            else
                updateMovieData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        linearLayoutProgressBar = (LinearLayout) rootView.findViewById(R.id.linear_layout_progress);

        //Fetch reference to the GridView and set a custom adapter to initialize views.
        imageAdapter = new ImageAdapter(getContext());
        gridView = (GridView) rootView.findViewById(R.id.gridView_posters);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String posterUrl = (String) imageAdapter.getItem(position);
                JSONObject jsonObject = Parser.getMovieDetailsByUrl(memoryCachedMovieData, posterUrl);
                if (jsonObject != null) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, jsonObject.toString());
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    private void updateMovieData() {
        //Check for network connection beforehand
        if (Network.isConnected(getContext())) {
            FetchMovieData fetchMovieData = new FetchMovieData();
            fetchMovieData.execute(sort_type);
        } else {
            //Also display snackBar to get latest content
            final Snackbar snackbar = Snackbar.make(rootView.findViewById(R.id.fragment),
                    getString(R.string.internet_error_message),
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.try_again, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateMovieData();
                }
            }).show();
            //load cached data and update adapter
            memoryCachedMovieData = Cache.getMovieData(getContext());
            updateAdapter(Cache.getMovieData(getContext()));
        }
    }

    private class FetchMovieData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linearLayoutProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            //Ex: url http://api.themoviedb.org/3/movie/popular?api_key=[APP_ID]
            /*
            Steps to fetch and read data from an url
            1. Build the uri.
            2. Open httpURLConnection using earlier uri.
            3. Create inputStream to read data
            4. Read data into a stringBuffer by using readLine function
            5. Finally close the streams.
             */

            //constants for URL parameters
            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_BY = "sort_by";
            //Adding this parameter so that good top_rated movies are returned. Movie "Lady Gaga" is rated 10.0.
            final String MIN_VOTES = "vote_count.gte";
            final String votesValue = (params[0].equals("popularity.desc")) ? "0" : "1000";
            final String API_KEY = "api_key";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String JSONStrMovieData = null;


            //step 1
            Uri uri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(SORT_BY, params[0])
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                    .appendQueryParameter(MIN_VOTES, votesValue)
                    .build();

            try {
                //step 2
                URL url = new URL(uri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                //step3
                InputStream inputStream = httpURLConnection.getInputStream();

                //step4
                StringBuilder stringBuffer = new StringBuilder();
                if (inputStream == null)
                    return null;

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                int ch;
                while ((ch = bufferedReader.read()) != -1) {
                    stringBuffer.append((char) ch);
                }

                if (stringBuffer.length() == 0)
                    return null;

                JSONStrMovieData = stringBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(TAG, "Malformed URL", e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "IOException", e);
            }
            //step 5
            finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }
            return JSONStrMovieData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                memoryCachedMovieData = s;
                Cache.cacheMovieData(getContext(), s);
                MainActivityFragment.updateAdapter(s);
            }
            linearLayoutProgressBar.setVisibility(View.GONE);
        }
    }
}
