package com.calgen.prodek.fadflicks.model;

import java.io.Serializable;

/**
 * Created by Gurupad on 02-Sep-16.
 * You asked me to change it for no reason.
 */
public class MovieBundle implements Serializable {

    public Movie movie;
    public VideoResponse videoResponse;
    public ReviewResponse reviewResponse;
    public Credits credits;
    public MovieDetails movieDetails;

    public MovieBundle() {

    }
}
