package com.calgen.prodek.fadflicks.Adapter;

/**
 * Created by Gurupad on 05-Jul-16.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * A custom adapter that helps to load images into each gridView items.
 */
public class ImageAdapter extends BaseAdapter {

    private static final String TAG = ImageAdapter.class.getSimpleName();
    private Context mContext;
    public String[] posterURLs;

    public ImageAdapter(Context c) {
        mContext = c;
        posterURLs = new String[0];
    }

    public int getCount() {
        return posterURLs.length;
    }

    public Object getItem(int position)
    {
        if (posterURLs == null)
            return null;
        return posterURLs[position];
    }

    public long getItemId(int position) {
        return posterURLs[position].hashCode();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(3, 3, 3, 3);
        } else {
            imageView = (ImageView) convertView;
        }

        //Use Picasso Image library to load the images into the imageView
        //URL must be encoded as uri to make
        if (posterURLs != null) {
            Picasso.with(mContext).load(posterURLs[position])
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, "onError: Picasso couldn't load images");
                        }
                    });
        }
        return imageView;
    }

    public void update(String[] urls) {
        if (urls == null) {
            return;
        }
        posterURLs = Arrays.copyOf(urls, urls.length);
    }
}
