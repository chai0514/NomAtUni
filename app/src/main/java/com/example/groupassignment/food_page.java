package com.example.groupassignment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupassignment.diets.keto.foodDetails;
import com.example.groupassignment.diets.keto.keto_menu;
import com.example.groupassignment.diets.paleo.paleo_menu;
import com.example.groupassignment.maps.MapsActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class food_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_page);


        //FIRST, GET BACKGROUND UI
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.fd_relative_layout);
        // Get the color value from the color resource
        int colorRes = getResources().getColor(R.color.bg_color);

        Drawable vector_bg1= getResources().getDrawable(R.drawable.vector_bg1);
        Drawable vector_bg2= getResources().getDrawable(R.drawable.vector_bg2);
        //LAYER THE VECTOR IMAGE
        Drawable[] layers = new Drawable[]{new ColorDrawable(colorRes), vector_bg1, vector_bg2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);


        //TO SET THE TEXT AFTER "\n" TO BE SMALLER
        TextView textView = findViewById(R.id.tv_diet);
        String text = "Diets\nEnrich your health";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new RelativeSizeSpan(0.6f), text.indexOf('\n'), text.length(), 0);
        textView.setText(spannableString);


        //START OF CODE FOR USAGES
        //SEARCH BUTTON
        SearchView sc_click = (SearchView) findViewById(R.id.sc_searchView);

        sc_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchPage = new Intent(food_page.this, search_page.class);
                startActivity(searchPage);
            }
        });

        //Set the Search ICON not CLICKABLE
        int searchIconId = sc_click.getContext().getResources().getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = (ImageView) sc_click.findViewById(searchIconId);
        searchIcon.setClickable(false);
        searchIcon.setEnabled(false);


        //FOOD RECOMMENDATION IMAGE BUTTONS
        recommendMeal();


        //DIET BUTTONS

        ShapeableImageView ib_paleoMenu =(ShapeableImageView) findViewById(R.id.ibt_paleo);
        ShapeableImageView ib_ketoMenu = (ShapeableImageView) findViewById(R.id.ibt_keto);

        //Paleo
        ib_paleoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paleoMenu = new Intent(food_page.this, paleo_menu.class);
                startActivity(paleoMenu);
            }
        });

        //keto
        ib_ketoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ketoMenu = new Intent(food_page.this, keto_menu.class);
                startActivity(ketoMenu);
            }
        });


        //MEAL NEARBY BUTTON
        ShapeableImageView ib_mealNearby =(ShapeableImageView) findViewById(R.id.fd_nearby);

        ib_mealNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mealNearby = new Intent(food_page.this, MapsActivity.class);
                startActivity(mealNearby);
            }
        });


        Button bt_cancel = findViewById(R.id.bt_cancelit);

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent p = new Intent(food_page.this,main_menu.class);
                startActivity(p);
            }
        });



        mainLayout.setBackground(layerDrawable);

        setContentView(mainLayout);

    }


    private void recommendMeal(){

        //4 Image buttons. 12 images to loop.

        int [] imgButtonIds = {R.id.ibt_recommend1,R.id.ibt_recommend2,R.id.ibt_recommend3,R.id.ibt_recommend4};
        ShapeableImageView[] buttons = new ShapeableImageView[imgButtonIds.length];

        Integer[] imgResourcesIds = {
                R.drawable.ketofood1,
                R.drawable.ketofood2,
                R.drawable.ketofood3,
                R.drawable.ketofood4,
                R.drawable.ketofood5,
                R.drawable.ketofood6,
                R.drawable.paleofood1,
                R.drawable.paleofood2,
                R.drawable.paleofood3,
                R.drawable.paleofood4,
                R.drawable.paleofood5,
                R.drawable.paleofood6
        };


        List<Integer> imgResourceIds = new ArrayList<>(Arrays.asList(imgResourcesIds));
        Collections.shuffle(imgResourceIds);


        for(int i =0; i< imgButtonIds.length; i++){
            buttons[i] = findViewById(imgButtonIds[i]);
            buttons[i].setBackgroundResource(imgResourceIds.get(i));

        }



        for (int i = 0; i < buttons.length; i++) {
            final int imageIndex = i; // To capture the current value of i for the onClick listener
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Determine which image was clicked based on imageIndex
                    int clickedImageResource = imgResourceIds.get(imageIndex);

                    // Start the intent based on the clicked image
                    if(clickedImageResource == R.drawable.ketofood1){
                        Intent keto_food1 = new Intent(food_page.this, foodDetails.class);
                        keto_food1.putExtra("image_food",R.drawable.ketofood1);
                        keto_food1.putExtra("output_food", "ketoEggMuffins");
                        keto_food1.putExtra("food_name","Keto Egg Muffins");
                        startActivity(keto_food1);
                    }else if(clickedImageResource == R.drawable.ketofood3){
                        Intent keto_food2 = new Intent(food_page.this, foodDetails.class);
                        keto_food2.putExtra("image_food",R.drawable.ketofood3);
                        keto_food2.putExtra("output_food", "avocadoTunaSalad");
                        keto_food2.putExtra("food_name","Avocado Tuna Salad");
                        startActivity(keto_food2);
                    }else if(clickedImageResource == R.drawable.ketofood5){
                        Intent keto_food3 = new Intent(food_page.this, foodDetails.class);
                        keto_food3.putExtra("image_food",R.drawable.ketofood5);
                        keto_food3.putExtra("output_food", "chickenCaesarSalad");
                        keto_food3.putExtra("food_name","Chicken Caesar Salad");
                        startActivity(keto_food3);
                    }else if(clickedImageResource == R.drawable.ketofood2){
                        Intent keto_food4 = new Intent(food_page.this, foodDetails.class);
                        keto_food4.putExtra("image_food",R.drawable.ketofood2);
                        keto_food4.putExtra("output_food", "ketoSeafoodChowder");
                        keto_food4.putExtra("food_name","Keto Seafood Chowder");
                        startActivity(keto_food4);
                    }else if(clickedImageResource == R.drawable.ketofood4){
                        Intent keto_food5 = new Intent(food_page.this, foodDetails.class);
                        keto_food5.putExtra("image_food",R.drawable.ketofood4);
                        keto_food5.putExtra("output_food", "cauliflowerToast");
                        keto_food5.putExtra("food_name","Cauliflower Toast");
                        startActivity(keto_food5);
                    }else if(clickedImageResource == R.drawable.ketofood6){
                        Intent keto_food6 = new Intent(food_page.this, foodDetails.class);
                        keto_food6.putExtra("image_food",R.drawable.ketofood6);
                        keto_food6.putExtra("output_food", "bakedAvocadoBoats");
                        keto_food6.putExtra("food_name","Baked Avocado Boats");
                        startActivity(keto_food6);
                    }else if(clickedImageResource == R.drawable.paleofood1){
                        Intent paleo_food1 = new Intent(food_page.this, foodDetails.class);
                        paleo_food1.putExtra("image_food",R.drawable.paleofood1);
                        paleo_food1.putExtra("output_food", "poachedEggsWithMushroomAndSpinach");
                        paleo_food1.putExtra("food_name","Poached Eggs With Mushroom And Spinach");
                        startActivity(paleo_food1);
                    }else if(clickedImageResource == R.drawable.paleofood3){
                        Intent paleo_food2 = new Intent(food_page.this, foodDetails.class);
                        paleo_food2.putExtra("image_food",R.drawable.paleofood3);
                        paleo_food2.putExtra("output_food", "friedCabbageWithSausage");
                        paleo_food2.putExtra("food_name","Fried Cabbage With Sausage");
                        startActivity(paleo_food2);
                    }else if(clickedImageResource == R.drawable.paleofood5){
                        Intent paleo_food3 = new Intent(food_page.this, foodDetails.class);
                        paleo_food3.putExtra("image_food",R.drawable.paleofood5);
                        paleo_food3.putExtra("output_food", "lemonPepperChickenThighs");
                        paleo_food3.putExtra("food_name","Lemon Pepper Chicken Thighs");
                        startActivity(paleo_food3);
                    }else if(clickedImageResource == R.drawable.paleofood2){
                        Intent paleo_food4 = new Intent(food_page.this, foodDetails.class);
                        paleo_food4.putExtra("image_food",R.drawable.paleofood2);
                        paleo_food4.putExtra("output_food", "veggieOmelet");
                        paleo_food4.putExtra("food_name","Veggie Omelet");
                        startActivity(paleo_food4);
                    }else if(clickedImageResource == R.drawable.paleofood4){
                        Intent paleo_food5 = new Intent(food_page.this, foodDetails.class);
                        paleo_food5.putExtra("image_food",R.drawable.paleofood4);
                        paleo_food5.putExtra("output_food", "grandmaChickenAndVegetableStirFry");
                        paleo_food5.putExtra("food_name","Grandma's Chicken and Vegetable Stir Fry");
                        startActivity(paleo_food5);
                    }else if(clickedImageResource == R.drawable.paleofood6){
                        Intent paleo_food6 = new Intent(food_page.this, foodDetails.class);
                        paleo_food6.putExtra("image_food",R.drawable.paleofood6);
                        paleo_food6.putExtra("output_food", "paleoAvocadoToast");
                        paleo_food6.putExtra("food_name","Paleo Avocado Toast");
                        startActivity(paleo_food6);
                    }


                }
            });
        }





    }
}



