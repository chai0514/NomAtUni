package com.example.groupassignment.diets.keto;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupassignment.food_page;
import com.example.groupassignment.R;

public class keto_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keto_menu);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.rl_ketoMenu);
        // Get the color value from the color resource
        int colorRes = getResources().getColor(R.color.bg_color);

        Drawable vector_bg1= getResources().getDrawable(R.drawable.vector_bg1);
        Drawable vector_bg2= getResources().getDrawable(R.drawable.vector_bg2);
        //LAYER THE VECTOR IMAGE
        Drawable[] layers = new Drawable[]{new ColorDrawable(colorRes), vector_bg1, vector_bg2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        mainLayout.setBackground(layerDrawable);

        //Set on clicks to the all the image buttons
        int [] imgButtonIds = {R.id.ib_kfood1,R.id.ib_kfood2,R.id.ib_kfood3,R.id.ib_kfood4,R.id.ib_kfood5,R.id.ib_kfood6};
        ImageButton[] imgButtons = new ImageButton[imgButtonIds.length];

        for(int i =0; i< imgButtonIds.length; i++){
            imgButtons[i] = findViewById(imgButtonIds[i]);
        }


        for (int i = 0; i < imgButtons.length; i++) {
            //Get the position of index
            final int buttonIndex = i;
            imgButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imgButtonIds[buttonIndex] == R.id.ib_kfood1){
                        Intent keto_food1 = new Intent(keto_menu.this, foodDetails.class);
                        keto_food1.putExtra("image_food",R.drawable.ketofood1);
                        keto_food1.putExtra("output_food", "ketoEggMuffins");
                        keto_food1.putExtra("food_name","Keto Egg Muffins");
                        keto_food1.putExtra("menu_type", "keto");
                        keto_food1.putExtra("food_desc","Keto Egg Muffins are an easy low carb breakfast option" +
                                "for busy mornings. Packed with protein and chockfull of flavor, they help fuel you through the day!");
                        startActivity(keto_food1);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_kfood2){
                        Intent keto_food2 = new Intent(keto_menu.this, foodDetails.class);
                        keto_food2.putExtra("image_food",R.drawable.ketofood3);
                        keto_food2.putExtra("output_food", "avocadoTunaSalad");
                        keto_food2.putExtra("food_name","Avocado Tuna Salad");
                        keto_food2.putExtra("menu_type", "keto");
                        keto_food2.putExtra("food_desc","Avocado tuna salad is a delicious and nutritious dish that combines the rich, creamy texture of " +
                                "ripe avocados with the savory goodness of tuna.");
                        startActivity(keto_food2);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_kfood3){
                        Intent keto_food3 = new Intent(keto_menu.this, foodDetails.class);
                        keto_food3.putExtra("image_food",R.drawable.ketofood5);
                        keto_food3.putExtra("output_food", "chickenCaesarSalad");
                        keto_food3.putExtra("food_name","Chicken Caesar Salad");
                        keto_food3.putExtra("menu_type", "keto");
                        keto_food3.putExtra("food_desc","Chicken Caesar Salad is a classic and popular dish known for its " +
                                "combination of crisp romaine lettuce" +
                                ", tender grilled chicken, crunchy croutons, and flavorful Caesar dressing");
                        startActivity(keto_food3);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_kfood4){
                        Intent keto_food4 = new Intent(keto_menu.this, foodDetails.class);
                        keto_food4.putExtra("image_food",R.drawable.ketofood2);
                        keto_food4.putExtra("output_food", "ketoSeafoodChowder");
                        keto_food4.putExtra("food_name","Keto Seafood Chowder");
                        keto_food4.putExtra("menu_type", "keto");
                        keto_food4.putExtra("food_desc","A Keto Seafood Chowder is a flavorful and creamy soup that features a variety of seafood " +
                                "while adhering to the low-carb" +
                                ", high-fat requirements of the ketogenic diet.");
                        startActivity(keto_food4);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_kfood5){
                        Intent keto_food5 = new Intent(keto_menu.this, foodDetails.class);
                        keto_food5.putExtra("image_food",R.drawable.ketofood4);
                        keto_food5.putExtra("output_food", "cauliflowerToast");
                        keto_food5.putExtra("food_name","Cauliflower Toast");
                        keto_food5.putExtra("menu_type", "keto");
                        keto_food5.putExtra("food_desc","Cauliflower toast is a creative and low-carb alternative to traditional bread toast, making it a popular " +
                                "choice for those following a ketogenic or low-carbohydrate diet.");
                        startActivity(keto_food5);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_kfood6){
                        Intent keto_food6 = new Intent(keto_menu.this, foodDetails.class);
                        keto_food6.putExtra("image_food",R.drawable.ketofood6);
                        keto_food6.putExtra("output_food", "bakedAvocadoBoats");
                        keto_food6.putExtra("food_name","Baked Avocado Boats");
                        keto_food6.putExtra("menu_type", "keto");
                        keto_food6.putExtra("food_desc","Baked avocado boats are a delicious and nutritious " +
                                "dish that transforms ripe avocados into a warm and savory treat");
                        startActivity(keto_food6);
                    }


                }
            });

        }


        //Back button
        Button bt_back = (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(keto_menu.this, food_page.class);
                startActivity(intent1);
                finish();
            }
        });

    }
}