package com.example.groupassignment.diets.keto;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.groupassignment.food_page;
import com.example.groupassignment.R;
import com.example.groupassignment.diets.MyViewPagerAdapter;
import com.example.groupassignment.diets.paleo.paleo_menu;
import com.google.android.material.tabs.TabLayout;

public class foodDetails extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private MyViewPagerAdapter myViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);


        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.lt_main);
        // Get the color value from the color resource
        int colorRes = getResources().getColor(R.color.bg_color);

        Drawable vector_bg1= getResources().getDrawable(R.drawable.vector_bg1);
        Drawable vector_bg2= getResources().getDrawable(R.drawable.vector_bg2);
        //LAYER THE VECTOR IMAGE
        Drawable[] layers = new Drawable[]{new ColorDrawable(colorRes), vector_bg1, vector_bg2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        mainLayout.setBackground(layerDrawable);



        Intent intent_receive = getIntent();

        //Retrieve the value of the data
        String food_name = intent_receive.getStringExtra("output_food");
        int food_image = intent_receive.getIntExtra("image_food",0);  //Receive the Index of the image
        String food_title = intent_receive.getStringExtra("food_name");
        String food_desc = intent_receive.getStringExtra("food_desc");

        //Set the image
        ImageView currentImage = (ImageView) findViewById(R.id.iv_currentfood);
        currentImage.setImageResource(food_image);

        //Set the title of the food
        TextView currentFoodName = (TextView) findViewById(R.id.tv_foodName);
        currentFoodName.setText(food_title);

        //Set the description of the food
        TextView currentFoodDesc = (TextView) findViewById(R.id.tv_description);
        currentFoodDesc.setText(food_desc);


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        myViewPagerAdapter = new MyViewPagerAdapter(this,food_name);
        viewPager2.setAdapter(myViewPagerAdapter);




        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();


            }
        });


        Button bt_back = (Button) findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuType = getIntent().getStringExtra("menu_type");
                Intent intent;
                if ("keto".equals(menuType)) {
                    intent = new Intent(foodDetails.this, keto_menu.class);
                } else if ("paleo".equals(menuType)) {
                    intent = new Intent(foodDetails.this, paleo_menu.class);
                } else {
                    intent = new Intent(foodDetails.this, food_page.class);
                }
                startActivity(intent);
                finish();
            }
        });

    }


}