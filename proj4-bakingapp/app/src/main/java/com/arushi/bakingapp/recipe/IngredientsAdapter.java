/*
 *
 *  *
 *  *  This project was submitted by Arushi Pant as part of the Android Developer Nanodegree at Udacity.
 *  *
 *  *  As part of Udacity Honor code, your submissions must be your own work, hence
 *  *  submitting this project as yours will cause you to break the Udacity Honor Code
 *  *  and the suspension of your account.
 *  *
 *  *  I, the author of the project, allow you to check the code as a reference, but if
 *  *  you submit it, it's your own responsibility if you get expelled.
 *  *
 *  *  Besides the above notice, the MIT license applies and this license notice
 *  *  must be included in all works derived from this project
 *  *
 *  *  Copyright (c) 2018 Arushi Pant
 *  *
 *
 */

package com.arushi.bakingapp.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arushi.bakingapp.R;
import com.arushi.bakingapp.data.local.entity.IngredientEntity;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>  {
    private List<IngredientEntity> mIngredientList = new ArrayList<>();
    private Context mContext;

    public IngredientsAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_ingredient, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        IngredientEntity ingredient = mIngredientList.get(position);

        String description = ingredient.getIngredient()
                            + "  (" + String.valueOf(ingredient.getQuantity())
                            + " " + ingredient.getMeasure()
                            + ")";
        holder.tvIngredient.setText(description);
    }

    @Override
    public int getItemCount() {
        if(mIngredientList ==null) return 0;
        return mIngredientList.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        TextView tvIngredient;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            tvIngredient = itemView.findViewById(R.id.tv_ingredient);
        }
    }

    public void setIngredientList(List<IngredientEntity> ingredientList) {
        this.mIngredientList.clear();
        this.mIngredientList.addAll(ingredientList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash
        notifyDataSetChanged();
    }

}
