package com.calgen.prodek.fadflicks.Adapter;

/**
 * Created by Gurupad on 05-Jul-16.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Arrays;

/**
 * A custom adapter that helps to load images into each gridView items.
 */
public class ImageAdapter extends BaseAdapter {

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
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        //Use Picasso Image library to load the images into the imageView
        if (posterURLs != null)
            Glide.with(mContext)
                    .load(posterURLs[position])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        return imageView;
    }

    public void update(String[] urls) {
        if (urls == null) {
            return;
        }
        posterURLs = Arrays.copyOf(urls, urls.length);
    }
}
