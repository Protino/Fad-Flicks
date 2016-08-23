package com.calgen.prodek.fadflicks;

import android.test.AndroidTestCase;

import com.calgen.prodek.fadflicks.model.MovieResponse;
import com.calgen.prodek.fadflicks.model.Result;
import com.calgen.prodek.fadflicks.rest.ApiClient;
import com.calgen.prodek.fadflicks.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurupad on 23-Aug-16.
 * You asked me to change it for no reason.
 */
public class RetrofitTest extends AndroidTestCase {

    public static final int RESULT_SIZE = 20;

    public void testApiService() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMovies("popularity.desc", BuildConfig.MOVIE_DB_API_KEY, String.valueOf(1000));
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Result> results = response.body().getResults();
                assertEquals("Incorrect result obtained : Should've returned 20 results"
                        , RESULT_SIZE, results.size());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                assertTrue("Failed to fetch results : "+t.toString(),false);
            }
        });
    }
}
