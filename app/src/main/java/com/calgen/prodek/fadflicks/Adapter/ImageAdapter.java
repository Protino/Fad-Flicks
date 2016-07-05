package com.calgen.prodek.fadflicks.Adapter;

/**
 * Created by Gurupad on 05-Jul-16.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.calgen.prodek.fadflicks.R;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

/**
 * A custom adapter that helps to load images into each gridView items.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private String[] posterURLs;

    public ImageAdapter(Context c) {
        mContext = c;
        posterURLs = new String[0];
    }

    public int getCount() {
        return posterURLs.length;
    }

    public Object getItem(int position) {
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
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(3, 3, 3, 3);
        } else {
            imageView = (ImageView) convertView;
        }

        //Use Picasso Image library to load the images into the imageView
        if (posterURLs != null)
            Picasso.with(mContext).load(posterURLs[position]).placeholder(mContext.getResources().getDrawable(R.drawable.loading)).into(imageView);
        return imageView;
    }

    public void clear() {
        posterURLs = new String[0];
    }

    public void update(String[] urls) {
        posterURLs = Arrays.copyOf(urls, urls.length);
    }
}
