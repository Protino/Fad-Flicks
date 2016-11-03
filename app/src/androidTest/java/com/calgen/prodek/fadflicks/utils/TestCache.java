/*
 *    Copyright 2016 Gurupad Mamadapur
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.calgen.prodek.fadflicks.utils;

import android.test.AndroidTestCase;

import com.calgen.prodek.fadflicks.model.Movie;
import com.calgen.prodek.fadflicks.model.MovieBundle;
import com.calgen.prodek.fadflicks.model.Video;
import com.calgen.prodek.fadflicks.model.VideoResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Gurupad on 12-Sep-16.
 * You asked me to change it for no reason.
 */
public class TestCache extends AndroidTestCase {


    private static HashMap<Integer, Boolean> generateTestData() {
        Random random = new Random();
        HashMap<Integer, Boolean> map = new HashMap<>();
        for (int i = 0; i < random.nextInt(10) + 10; i++) {
            map.put(random.nextInt(999999), random.nextBoolean());
        }
        return map;
    }

    public void testStoreFavouriteMovie() {
        HashMap<Integer, Boolean> originalMap = Cache.getFavouriteMovies(mContext);

        if (originalMap == null) {
            originalMap = new HashMap<>();
        }
        Random random = new Random();
        for (int i = 0; i < random.nextInt(10) + 10; i++) {
            int randomId = random.nextInt();
            boolean randomFav = random.nextBoolean();
            originalMap.put(randomId, randomFav);
            Cache.setFavouriteMovie(mContext, randomId, randomFav);
        }

        assertTrue("Incorrect storage of favourite movies", originalMap.equals(Cache.getFavouriteMovies(mContext)));
    }

    public void testBulkInsertFavouriteMovies() {
        HashMap<Integer, Boolean> originalMap = Cache.getFavouriteMovies(mContext);
        if (originalMap == null) {
            originalMap = new HashMap<>();
        }
        HashMap<Integer, Boolean> testData = generateTestData();
        originalMap.putAll(testData);

        Cache.bulkInsertFavouriteMovies(mContext, testData);

        assertTrue("Bulk insert error.", originalMap.equals(Cache.getFavouriteMovies(mContext)));
    }

    public void testClearCache() {
        Cache.purgeCache(mContext);
        assertTrue("Could not delete the favourite movies correctly", (Cache.getFavouriteMovies(mContext)).isEmpty());
    }

    public void testCacheMovieData() {

        //Generate fake MovieBundle object with fake data
        MovieBundle movieBundle = new MovieBundle();
        Movie movie = new Movie();
        movie.setId(123);
        movie.setTitle("Taxi Driver");
        movie.setAdult(true);
        movieBundle.movie = movie;

        Video video = new Video();
        video.setName("Teaser Trailer");
        List<Video> videoList = new ArrayList<>();
        videoList.add(video);
        VideoResponse videoResponse = new VideoResponse();
        videoResponse.setVideos(videoList);
        movieBundle.videoResponse = videoResponse;

        //store fake data
        Cache.cacheMovieData(mContext, movieBundle);

        //retrieve data back by id

        MovieBundle cachedData = Cache.getMovieData(mContext, movieBundle.movie.getId());

        assertEquals(cachedData.movie.getTitle(), movieBundle.movie.getTitle());
        assertEquals(cachedData.movie.getAdult(), movieBundle.movie.getAdult());

        assertEquals(cachedData.videoResponse.getVideos().get(0).getName(), movieBundle.videoResponse.getVideos().get(0).getName());
    }
}
