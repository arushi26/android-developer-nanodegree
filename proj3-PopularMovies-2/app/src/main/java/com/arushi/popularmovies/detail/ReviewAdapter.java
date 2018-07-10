package com.arushi.popularmovies.detail;
/*
 * This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * I, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Besides the above notice, the MIT license applies and this license notice
 * must be included in all works derived from this project
 *
 * Copyright (c) 2018 Arushi Pant
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arushi.popularmovies.R;
import com.arushi.popularmovies.data.model.ReviewItem;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>  {
    private List<ReviewItem> mReviewList = new ArrayList<>();
    private Context mContext;

    public ReviewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewItem item = mReviewList.get(position);
        holder.tvReviewContent.setText(item.getContent());
        holder.tvAuthor.setText(item.getAuthor());
        holder.itemView.setTag(item.getUrl());
    }

    @Override
    public int getItemCount() {
        if(mReviewList ==null) return 0;
        return mReviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        LinearLayout reviewCard;
        TextView tvReviewContent, tvAuthor;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewCard = itemView.findViewById(R.id.reviewCard);
            tvReviewContent = itemView.findViewById(R.id.tv_review);
            tvAuthor = itemView.findViewById(R.id.tv_author);

            reviewCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Open review in browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(view.getTag().toString()));
            if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                mContext.startActivity(intent);
            }
        }
    }

    public void setReviewList(List<ReviewItem> reviewList) {
        this.mReviewList.clear();
        this.mReviewList.addAll(reviewList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash
        notifyDataSetChanged();
    }

}
