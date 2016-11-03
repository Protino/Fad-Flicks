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

/**
 * Created by Gurupad on 01-Sep-16.
 */
public class ApplicationConstants {

    /**
     * Set to true if debugging is needed
     */
    public static final boolean DEBUG = false;

    /**
     * When true, forces to clear defaultSharedPreferences
     */
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
