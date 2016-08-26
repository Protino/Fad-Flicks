package com.calgen.prodek.fadflicks.Adapter;

/**
 * Created by Gurupad on 05-Jul-16.
 */

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * A custom adapter that helps to load images into each gridView items.
 */
public class ImageAdapter extends BaseAdapter {


    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String IMAGE_SIZE = "w185";

    private static final String TAG = ImageAdapter.class.getSimpleName();
    public String[] posterURLs = null;
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
        posterURLs = new String[0];
    }

    public int getCount() {
        return posterURLs.length;
    }

    public Object getItem(int position) {
        if (posterURLs == null)
            return null;
        return posterURLs[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        // Glide library caches images to disk perfectly without workarounds but it has image
        // flickering problems and uses lot of memory and disk space. Picasso's disk caching works
        // fine only on API 23. Needs workaround for API<23
        if (posterURLs != null)
            Picasso.with(mContext)
                    .load(posterURLs[position])
                    .placeholder(new ColorDrawable(0xFFFFFF))
                    .into(imageView);
        return imageView;
    }

    public void update(String[] urls) {
        if (urls == null) {
            return;
        }
        posterURLs = Arrays.copyOf(urls, urls.length);
        notifyDataSetChanged();
    }
}
