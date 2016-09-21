
package com.calgen.prodek.fadflicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoResponse implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Video> videos = new ArrayList<Video>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public VideoResponse() {
    }

    /**
     * 
     * @param id
     * @param videos
     */
    public VideoResponse(Integer id, List<Video> videos) {
        this.id = id;
        this.videos = videos;
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
    public List<Video> getVideos() {
        return videos;
    }

    /**
     * 
     * @param videos
     *     The results
     */
    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}
