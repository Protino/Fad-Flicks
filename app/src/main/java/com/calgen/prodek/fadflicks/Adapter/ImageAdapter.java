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

/**
 * A custom adapter that helps to load images into each gridView items.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private String imageURL = "http://i.imgur.com/DvpvklR.png";

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        // TODO: 03-Jul-16 change the hardcode
        return 9;
    }

    public Object getItem(int position) {
        return imageURL;
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
        // TODO: 03-Jul-16 Now the url is hardcoded. It needs to be fetched from the themoviedb.org
        Picasso.with(mContext).load(imageURL).placeholder(mContext.getResources().getDrawable(R.drawable.loading)).into(imageView);
        return imageView;
    }


}
