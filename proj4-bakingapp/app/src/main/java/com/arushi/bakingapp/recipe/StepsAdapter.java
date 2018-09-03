package com.arushi.bakingapp.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arushi.bakingapp.R;
import com.arushi.bakingapp.data.local.entity.StepEntity;

import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private ArrayList<StepEntity> mStepsList = new ArrayList<>();
    private Context mContext;
    private RecipeFragment.RecipeListener mStepClickListener;

    public StepsAdapter(Context context, RecipeFragment.RecipeListener listener) {
        this.mContext = context;
        this.mStepClickListener = listener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_step, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        StepEntity step = mStepsList.get(position);

        holder.itemView.setTag(position);

        if(position==0){
            // Do not show for 1st item in list
            holder.divider.setVisibility(View.GONE);
        }

        String stepNum = String.valueOf(position);
        holder.tvStepNum.setText(stepNum);
        holder.tvStep.setText(step.getShortDescription());

    }

    @Override
    public int getItemCount() {
        if(mStepsList ==null) return 0;
        return mStepsList.size();
    }


    public class StepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        View divider;
        TextView tvStepNum;
        TextView tvStep;

        public StepsViewHolder(View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.divider_step_item);
            tvStepNum = itemView.findViewById(R.id.tv_step_num);
            tvStep = itemView.findViewById(R.id.tv_step);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = Integer.parseInt(view.getTag().toString());
            mStepClickListener.showStepDetail(mStepsList, position);
        }
    }

    public void setStepsList(List<StepEntity> stepsList) {
        this.mStepsList.clear();
        this.mStepsList.addAll(stepsList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash
        notifyDataSetChanged();
    }

}
