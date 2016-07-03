package com.calgen.prodek.fadflicks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Fetch reference to the GridView and set a custom adapter to initialize views.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView_posters);
        gridView.setAdapter(new ImageAdapter());

        return rootView;
    }

    /**
     * A custom adapter that helps to load images into each gridView items.
     */
    private class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter() {
            mContext = getContext();
        }

        public int getCount() {
            // TODO: 03-Jul-16 change the hardcode
            return 9;
        }

        public Object getItem(int position) {
            return null;
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
            Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
            return imageView;
        }
    }
}
