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

package com.calgen.prodek.fadflicks.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.calgen.prodek.fadflicks.R;
import com.calgen.prodek.fadflicks.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurupad on 07-Sep-16.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {
    private Context context;
    private List<Review> reviewList;
    private boolean glimpse;

    public ReviewAdapter(Context context, ArrayList<Review> reviewList, boolean glimpse) {
        super(context, 0, reviewList);
        this.context = context;
        this.reviewList = reviewList;
        this.glimpse = glimpse;
    }

    @Override
    public int getCount() {
        int size = reviewList.size();
        int reviewLimit = 3;
        if (glimpse)
            return (size > 2) ? reviewLimit : size;
        else
            return size;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Review review = reviewList.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_item_review, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (glimpse) {
            viewHolder.reviewText.setMaxLines(3);
            viewHolder.reviewText.setEllipsize(TextUtils.TruncateAt.END);
            viewHolder.listItemLayout.setEnabled(false);
            viewHolder.listItemLayout.setLongClickable(false);
        }

        viewHolder.author.setText(review.getAuthor());
        viewHolder.reviewText.setText(review.getContent());
        viewHolder.author.setTag(position);
        viewHolder.reviewText.setTag(position);
        return convertView;
    }

    class ViewHolder {

        //@formatter:off
        @BindView(R.id.review_author) public TextView author;
        @BindView(R.id.review_text) public TextView reviewText;
        @BindView(R.id.list_item_layout) public LinearLayout listItemLayout;
        //@formatter:on

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
