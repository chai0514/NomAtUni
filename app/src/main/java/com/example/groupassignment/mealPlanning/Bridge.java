package com.example.groupassignment.mealPlanning;

import android.view.View;

public interface Bridge {

    void onItemClick(View v, int position);

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

}
