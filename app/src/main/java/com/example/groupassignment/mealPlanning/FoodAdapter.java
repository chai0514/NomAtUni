package com.example.groupassignment.mealPlanning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<String> foodList;
    private Bridge.OnItemClickListener listener;

    public FoodAdapter(List<String> foodList, Bridge.OnItemClickListener listener) {
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        String food = foodList.get(position);
        holder.foodTextView.setText(food);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(v, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public void updateData(List<String> newFoodList) {
        foodList.clear();
        foodList.addAll(newFoodList);
        notifyDataSetChanged();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodTextView;

        FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}