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

package com.calgen.prodek.fadflicks.api;

import com.calgen.prodek.fadflicks.utils.ApplicationConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ===========
 * Attribution
 * ===========
 * Thanks to Thomas kioko for the tutorial
 * on using interceptors
 * @see <a href="http://www.thomaskioko.com/mobile_phone/android-unit-testing/">Blog post</a>
 */
public class ApiClient {
    private Retrofit retrofit;
    private boolean isDebug;
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    /**
     * Set the {@link Retrofit} log level.
     *
     * @param isDebug If true, the log level is set to
     *                {@link HttpLoggingInterceptor.Level#BODY}. Otherwise
     *                {@link HttpLoggingInterceptor.Level#NONE}.
     */
    public ApiClient setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (retrofit != null) {
            httpLoggingInterceptor.
                    setLevel(isDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        }
        return this;
    }

    /**
     * Configure OkHttpClient
     *
     * @return OkHttpClient
     */
    private OkHttpClient okHttpClient() {

        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .connectTimeout(ApplicationConstants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ApplicationConstants.WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ApplicationConstants.READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    /**
     * Create a new {@link Retrofit.Builder}. Override this to e.g. set your own client or executor.
     *
     * @return A {@link Retrofit.Builder} with no modifications.
     */
    private Retrofit.Builder newRestAdapterBuilder() {
        return new Retrofit.Builder();
    }

    /**
     * Return the current {@link Retrofit} instance. If none exists (first call, API key changed),
     * builds a new one.
     * <p/>
     * When building, sets the endpoint and a {@link HttpLoggingInterceptor} which adds the API key as query param.
     */
    private Retrofit getRestAdapter() {

        Retrofit.Builder builder = null;
        if (retrofit == null) {

            //Create a new instance of the Rest Adapter class
            builder = newRestAdapterBuilder();

            builder.baseUrl(ApplicationConstants.END_POINT);
            builder.client(okHttpClient());
            builder.addConverterFactory(GsonConverterFactory.create());
        }

        if (isDebug && builder != null) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        if (builder != null) {
            retrofit = builder.build();
        }
        return retrofit;
    }

    /**
     * Create Movie service instance.
     *
     * @return Movie Service.
     */
    public ApiInterface movieInterface() {
        return getRestAdapter().create(ApiInterface.class);
    }
}
