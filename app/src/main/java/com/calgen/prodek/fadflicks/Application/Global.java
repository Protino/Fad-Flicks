package com.calgen.prodek.fadflicks.application;

import android.app.Application;

import com.calgen.prodek.fadflicks.utils.ApplicationConstants;
import com.calgen.prodek.fadflicks.utils.Cache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by Gurupad on 12-Jul-16.
 */
public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // if (LeakCanary.isInAnalyzerProcess(this))
        //   return;
        //LeakCanary.install(this);


        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(false);
        Picasso.setSingletonInstance(built);

        if (ApplicationConstants.PURGE_CACHE || Cache.isPurgeRequired(getApplicationContext())) {
            Cache.purgeCache(getApplicationContext());
        } else {
            Cache.cacheTimeOfLastUsage(getApplicationContext());
        }
    }
}
