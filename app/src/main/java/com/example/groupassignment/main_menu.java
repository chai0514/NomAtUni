package com.example.groupassignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class main_menu extends AppCompatActivity {
    View.OnClickListener buttonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Link buttons in xml to program
        Button btMealRecommend = findViewById(R.id.bt_meal_recommend);
        Button btNutritionTrack = findViewById(R.id.bt_nutrition_track);
        Button btCommunity = findViewById(R.id.bt_community);
        Button btProfile = findViewById(R.id.bt_profile);
        Button btShopList = findViewById(R.id.bt_shop_list);

        // Define button listener
        buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btSelection = (Button) v;

                if (btSelection.getId() == R.id.bt_meal_recommend) {
                    Intent intent = new Intent(v.getContext(), food_page.class);
                    startActivity(intent);
                }
                if (btSelection.getId() == R.id.bt_nutrition_track) {
                    Intent intent = new Intent(v.getContext(), calorie_tracker.class);
                    startActivity(intent);
                }
                if (btSelection.getId() == R.id.bt_community) {
                    Intent intent = new Intent(v.getContext(), community_activity.class);
                    startActivity(intent);
                }

                if (btSelection.getId() == R.id.bt_profile) {
                    Intent intent = new Intent(v.getContext(), account_page.class);
                    startActivity(intent);
                }

                if (btSelection.getId() == R.id.bt_shop_list) {
                    Intent intent = new Intent(v.getContext(), shopping_list.class);
                    startActivity(intent);
                }

            }
        };

        // Activation
        btMealRecommend.setOnClickListener(buttonListener);
        btNutritionTrack.setOnClickListener(buttonListener);
        btCommunity.setOnClickListener(buttonListener);
        btProfile.setOnClickListener(buttonListener);
        btShopList.setOnClickListener(buttonListener);
    }
}
