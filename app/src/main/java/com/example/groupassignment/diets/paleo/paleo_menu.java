package com.example.groupassignment.diets.paleo;

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
import com.example.groupassignment.diets.keto.foodDetails;

public class paleo_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paleo_menu);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.rl_paleoMenu);
        // Get the color value from the color resource
        int colorRes = getResources().getColor(R.color.bg_color);

        Drawable vector_bg1= getResources().getDrawable(R.drawable.vector_bg1);
        Drawable vector_bg2= getResources().getDrawable(R.drawable.vector_bg2);
        //LAYER THE VECTOR IMAGE
        Drawable[] layers = new Drawable[]{new ColorDrawable(colorRes), vector_bg1, vector_bg2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        mainLayout.setBackground(layerDrawable);

        //Set on clicks to the all the image buttons
        int [] imgButtonIds = {R.id.ib_pfood1,R.id.ib_pfood2,R.id.ib_pfood3,R.id.ib_pfood4,R.id.ib_pfood5,R.id.ib_pfood6};
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
                    if(imgButtonIds[buttonIndex] == R.id.ib_pfood1){
                        Intent paleo_food1 = new Intent(paleo_menu.this, foodDetails.class);
                        paleo_food1.putExtra("image_food",R.drawable.paleofood1);
                        paleo_food1.putExtra("output_food", "poachedEggsWithMushroomAndSpinach");
                        paleo_food1.putExtra("food_name","Poached Eggs With Mushroom And Spinach");
                        paleo_food1.putExtra("menu_type", "paleo");
                        paleo_food1.putExtra("food_desc","Poached eggs with mushrooms and spinach is a delightful and nutritious dish that combines the creamy " +
                                "texture of poached eggs with the earthy flavors of mushrooms and the freshness of spinach");
                        startActivity(paleo_food1);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_pfood2){
                        Intent paleo_food2 = new Intent(paleo_menu.this, foodDetails.class);
                        paleo_food2.putExtra("image_food",R.drawable.paleofood3);
                        paleo_food2.putExtra("output_food", "friedCabbageWithSausage");
                        paleo_food2.putExtra("food_name","Fried Cabbage With Sausage");
                        paleo_food2.putExtra("menu_type", "paleo");
                        paleo_food2.putExtra("food_desc","Fried cabbage with sausage is a hearty and flavorful dish that's easy to prepare and perfect for a comforting meal");
                        startActivity(paleo_food2);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_pfood3){
                        Intent paleo_food3 = new Intent(paleo_menu.this, foodDetails.class);
                        paleo_food3.putExtra("image_food",R.drawable.paleofood5);
                        paleo_food3.putExtra("output_food", "lemonPepperChickenThighs");
                        paleo_food3.putExtra("food_name","Lemon Pepper Chicken Thighs");
                        paleo_food3.putExtra("menu_type", "paleo");
                        paleo_food3.putExtra("food_desc","Lemon pepper chicken thighs are a delicious and flavorful dish that combines " +
                                "the zesty brightness of lemon with the aromatic warmth of black pepper.");
                        startActivity(paleo_food3);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_pfood4){
                        Intent paleo_food4 = new Intent(paleo_menu.this, foodDetails.class);
                        paleo_food4.putExtra("image_food",R.drawable.paleofood2);
                        paleo_food4.putExtra("output_food", "veggieOmelet");
                        paleo_food4.putExtra("food_name","Veggie Omelet");
                        paleo_food4.putExtra("menu_type", "paleo");
                        paleo_food4.putExtra("food_desc","A veggie omelet is a delicious and nutritious dish that allows " +
                                "you to customize your breakfast with a variety of vegetables.");
                        startActivity(paleo_food4);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_pfood5){
                        Intent paleo_food5 = new Intent(paleo_menu.this, foodDetails.class);
                        paleo_food5.putExtra("image_food",R.drawable.paleofood4);
                        paleo_food5.putExtra("output_food", "grandmaChickenAndVegetableStirFry");
                        paleo_food5.putExtra("food_name","Grandma's Chicken and Vegetable Stir Fry");
                        paleo_food5.putExtra("menu_type", "paleo");
                        paleo_food5.putExtra("food_desc","Grandma's chicken and vegetable stir fry is a comforting and flavorful dish that " +
                                "often carries a unique touch of home-style cooking");
                        startActivity(paleo_food5);
                    }else if(imgButtonIds[buttonIndex] == R.id.ib_pfood6){
                        Intent paleo_food6 = new Intent(paleo_menu.this, foodDetails.class);
                        paleo_food6.putExtra("image_food",R.drawable.paleofood6);
                        paleo_food6.putExtra("output_food", "paleoAvocadoToast");
                        paleo_food6.putExtra("food_name","Paleo Avocado Toast");
                        paleo_food6.putExtra("menu_type", "paleo");
                        paleo_food6.putExtra("food_desc","Paleo avocado toast is a grain-free, low-carb version of the popular breakfast dish" +
                                ", making it suitable for those following a paleolithic (paleo) diet");
                        startActivity(paleo_food6);
                    }


                }
            });

        }

        //Back button
        Button bt_back = (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(paleo_menu.this, food_page.class);
                startActivity(intent1);
                finish();
            }
        });

    }
}