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

package com.calgen.prodek.fadflicks.application;

import android.app.Application;

import com.calgen.prodek.fadflicks.utils.ApplicationConstants;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Gurupad on 12-Jul-16.
 */
public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        setUpPicasso();
        setUpLeakCanary();
        setUpCache();
    }

    private void setUpPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(ApplicationConstants.DEBUG);
        built.setLoggingEnabled(ApplicationConstants.DEBUG);
        Picasso.setSingletonInstance(built);
    }


    private void setUpLeakCanary() {
        if (ApplicationConstants.DEBUG && !LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    private void setUpCache() {
        if (ApplicationConstants.PURGE_CACHE || Cache.isPurgeRequired(this)) {
            Cache.purgeCache(this);
        } else {
            Cache.cacheTimeOfLastUsage(this);
        }
    }
}
