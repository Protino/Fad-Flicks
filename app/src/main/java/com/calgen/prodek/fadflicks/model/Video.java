
package com.calgen.prodek.fadflicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Video {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideoResponse> videoResponses = new ArrayList<VideoResponse>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Video() {
    }

    /**
     * 
     * @param id
     * @param videoResponses
     */
    public Video(Integer id, List<VideoResponse> videoResponses) {
        this.id = id;
        this.videoResponses = videoResponses;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The results
     */
    public List<VideoResponse> getVideoResponses() {
        return videoResponses;
    }

    /**
     * 
     * @param videoResponses
     *     The results
     */
    public void setVideoResponses(List<VideoResponse> videoResponses) {
        this.videoResponses = videoResponses;
    }

}
