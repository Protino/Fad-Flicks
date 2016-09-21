
package com.calgen.prodek.fadflicks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Crew implements Serializable{

    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("job")
    @Expose
    private String job;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_path")
    @Expose
    private Object profilePath;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Crew() {
    }

    /**
     * 
     * @param id
     * @param profilePath
     * @param department
     * @param name
     * @param job
     * @param creditId
     */
    public Crew(String creditId, String department, Integer id, String job, String name, Object profilePath) {
        this.creditId = creditId;
        this.department = department;
        this.id = id;
        this.job = job;
        this.name = name;
        this.profilePath = profilePath;
    }

    /**
     * 
     * @return
     *     The creditId
     */
    public String getCreditId() {
        return creditId;
    }

    /**
     * 
     * @param creditId
     *     The credit_id
     */
    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    /**
     * 
     * @return
     *     The department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * 
     * @param department
     *     The department
     */
    public void setDepartment(String department) {
        this.department = department;
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
     *     The job
     */
    public String getJob() {
        return job;
    }

    /**
     * 
     * @param job
     *     The job
     */
    public void setJob(String job) {
        this.job = job;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The profilePath
     */
    public Object getProfilePath() {
        return profilePath;
    }

    /**
     * 
     * @param profilePath
     *     The profile_path
     */
    public void setProfilePath(Object profilePath) {
        this.profilePath = profilePath;
    }

}
