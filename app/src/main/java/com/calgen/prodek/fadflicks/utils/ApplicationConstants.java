package com.calgen.prodek.fadflicks.utils;

/**
 * Created by Gurupad on 01-Sep-16.
 */
public class ApplicationConstants {

    public static final boolean DEBUG = false;

    public static final boolean PURGE_CACHE = false;

    public static final int CACHE_PURGE_PERIOD = 24*60*60*100;
    /**
     * API Endpoint
     */
    public static final String END_POINT = "http://api.themoviedb.org/3/";
    /**
     * Connection timeout duration
     */
    public static final int CONNECT_TIMEOUT = 60 * 1000;
    /**
     * Connection Read timeout duration
     */
    public static final int READ_TIMEOUT = 60 * 1000;
    /**
     * Connection write timeout duration
     */
    public static final int WRITE_TIMEOUT = 60 * 1000;

}
