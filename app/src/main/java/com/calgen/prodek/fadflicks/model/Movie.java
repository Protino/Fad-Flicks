package com.calgen.prodek.fadflicks.model;

import org.parceler.Parcel;

/**
 * Created by Gurupad on 25-Aug-16.
 * You asked me to change it for no reason.
 */
@Parcel
public class Movie {

    public String title;
    public Double rating;
    public String posterId;

    public Movie() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }
}
