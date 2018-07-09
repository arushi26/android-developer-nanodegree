package com.arushi.popularmovies.detail;
/*
 *
 *  This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *
 *  As part of Udacity Honor code, your submissions must be your own work, hence
 *  submitting this project as yours will cause you to break the Udacity Honor Code
 *  and the suspension of your account.
 *
 *  I, the author of the project, allow you to check the code as a reference, but if
 *  you submit it, it's your own responsibility if you get expelled.
 *
 *  Besides the above notice, the MIT license applies and this license notice
 *  must be included in all works derived from this project
 *
 *  Copyright (c) 2018 Arushi Pant
 *
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arushi.popularmovies.R;
import com.arushi.popularmovies.data.model.CastItem;
import com.arushi.popularmovies.utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder>  {
    private List<CastItem> mCastList = new ArrayList<>();
    private Context mContext;

    public CreditsAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public CreditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_cast, parent, false);
        return new CreditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditsViewHolder holder, int position) {
        CastItem cast = mCastList.get(position);
        GlideApp.with(mContext)
                .load(cast.getProfilePath())
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .dontAnimate()
                .into(holder.imgCast);
        holder.tvName.setText(cast.getName());
    }

    @Override
    public int getItemCount() {
        if(mCastList ==null) return 0;
        return mCastList.size();
    }

    public class CreditsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imgCast;
        TextView tvName;

        public CreditsViewHolder(View itemView) {
            super(itemView);
            imgCast = itemView.findViewById(R.id.img_cast);
            tvName = itemView.findViewById(R.id.tv_cast_name);
        }
    }

    public void setCastList(List<CastItem> castList) {
        this.mCastList.clear();
        this.mCastList.addAll(castList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash
        notifyDataSetChanged();
    }
}
