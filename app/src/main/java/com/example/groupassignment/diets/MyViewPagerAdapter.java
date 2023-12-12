package com.example.groupassignment.diets;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.groupassignment.diets.foodFragments.IngredientFragment;
import com.example.groupassignment.diets.foodFragments.NutritionFragment;


public class MyViewPagerAdapter extends FragmentStateAdapter {

    private String foodName;

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String foodName) {
        super(fragmentActivity);
        this.foodName = foodName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return IngredientFragment.newInstance(foodName);
            case 1:
                return new NutritionFragment();
            default:
                return IngredientFragment.newInstance(foodName);
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
