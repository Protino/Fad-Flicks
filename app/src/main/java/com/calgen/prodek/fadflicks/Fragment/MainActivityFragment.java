package com.calgen.prodek.fadflicks.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.calgen.prodek.fadflicks.Adapter.ImageAdapter;
import com.calgen.prodek.fadflicks.BuildConfig;
import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.Utility.Cache;
import com.calgen.prodek.fadflicks.Utility.MovieDataParser;
import com.calgen.prodek.fadflicks.Utility.Network;

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
    public GridView gridView;
    public ImageAdapter imageAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Fetch reference to the GridView and set a custom adapter to initialize views.
        imageAdapter = new ImageAdapter(getContext());
        gridView = (GridView) rootView.findViewById(R.id.gridView_posters);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieData();
    }

    private void updateMovieData() {
        //Check for network connection beforehand
        if (Network.isConnected(getContext())) {
            FetchMovieData fetchMovieData = new FetchMovieData();
            fetchMovieData.execute();
        } else {
            //load cached data
            String cachedMovieData = Cache.getMovieData(getContext());
            imageAdapter.update(MovieDataParser.getAllMoviePosterUrls(cachedMovieData));
            imageAdapter.notifyDataSetChanged();
        }
    }

    private class FetchMovieData extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            dialog.show();
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
            5. Close streams finally.
             */

            //constants for URL parameters
            final String BASE_URL = "http://api.themoviedb.org/3/movie/popular?";
            final String API_KEY = "api_key";

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;
            String JSONStrMovieData = null;


            //step 1
            Uri uri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendQueryParameter(API_KEY, BuildConfig.MOVIE_DB_API_KEY)
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
                StringBuffer stringBuffer = new StringBuffer();
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
                Cache.cacheMovieData(getContext(), s);
                String[] posterUrls = MovieDataParser.getAllMoviePosterUrls(s);
                imageAdapter.update(posterUrls);
                imageAdapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        }
    }
}
