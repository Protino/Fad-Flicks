package com.calgen.prodek.fadflicks.model;

/**
 * Created by Gurupad on 25-Aug-16.
 * You asked me to change it for no reason.
 */
public class Movie {

    private String title;
    private Double rating;
    private String posterId;

    public Movie() {
    }

    public Movie(Double rating, String title, String posterId) {
        this.rating = rating;
        this.title = title;
        this.posterId = posterId;
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
