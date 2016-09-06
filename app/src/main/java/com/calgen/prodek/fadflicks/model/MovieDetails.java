package com.calgen.prodek.fadflicks.model;

/**
 * Created by Gurupad on 02-Sep-16.
 * You asked me to change it for no reason.
 */
public class MovieDetails {

    public Movie movie;
    public VideoResponse videoResponse;
    public ReviewResponse reviewResponse;
    public Credits credits;

    public MovieDetails(Movie movie, VideoResponse videoResponse, ReviewResponse reviewResponse, Credits credits) {
        this.movie = movie;
        this.videoResponse = videoResponse;
        this.reviewResponse = reviewResponse;
        this.credits = credits;
    }

    public MovieDetails() {

    }
}
