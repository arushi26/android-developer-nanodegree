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
