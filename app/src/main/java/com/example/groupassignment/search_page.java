package com.example.groupassignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groupassignment.diets.keto.foodDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class search_page extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);


        //FIRST, GET BACKGROUND UI
        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.rl_searchPg);
        // Get the color value from the color resource
        int colorRes = getResources().getColor(R.color.bg_color);

        Drawable vector_bg1= getResources().getDrawable(R.drawable.vector_bg1);
        Drawable vector_bg2= getResources().getDrawable(R.drawable.vector_bg2);
        //LAYER THE VECTOR IMAGE
        Drawable[] layers = new Drawable[]{new ColorDrawable(colorRes), vector_bg1, vector_bg2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        mainLayout.setBackground(layerDrawable);

        HashMap<String, Pair<Integer, Class>> map = new HashMap<>();
        //Keto Mappings
        map.put("ketoEggMuffins", new Pair<>(R.drawable.ketofood1, foodDetails.class));
        map.put("ketoSeafoodChowder", new Pair<>(R.drawable.ketofood2, foodDetails.class));
        map.put("avocadoTunaSalad", new Pair<>(R.drawable.ketofood3, foodDetails.class));
        map.put("chickenCaesarSalad", new Pair<>(R.drawable.ketofood5, foodDetails.class));
        map.put("cauliflowerToast", new Pair<>(R.drawable.ketofood4, foodDetails.class));
        map.put("bakedAvocadoBoats", new Pair<>(R.drawable.ketofood6, foodDetails.class));

        //Paleo Mappings
        map.put("poachedEggsWithMushroomAndSpinach", new Pair<>(R.drawable.paleofood1, foodDetails.class));
        map.put("friedCabbageWithSausage", new Pair<>(R.drawable.paleofood3, foodDetails.class));
        map.put("lemonPepperChickenThighs", new Pair<>(R.drawable.paleofood5, foodDetails.class));
        map.put("veggieOmelet", new Pair<>(R.drawable.paleofood2, foodDetails.class));
        map.put("grandmaChickenAndVegetableStirFry", new Pair<>(R.drawable.paleofood4, foodDetails.class));
        map.put("paleoAvocadoToast", new Pair<>(R.drawable.paleofood6, foodDetails.class));

        LinearLayout layout = (LinearLayout) findViewById(R.id.lt_searchOutput);
        SearchView searchView = (SearchView) findViewById(R.id.sc_search);
        //searchView.setQueryHint("Search for recipes");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("This is search data", query);
                searchView.clearFocus();

                // Clear the layout and the names list before a new search
                layout.removeAllViews();

                // Connect to supaBase
                final Handler handler = new Handler();
                NomSupabase.searchDB mySearch = new NomSupabase.searchDB(query, handler, search_page.this, new NomSupabase.OnDataStoredListener() {

                    @Override
                    public void onDataStored() throws JSONException {
                        // Retrieve the data from the shared preferences
                        SharedPreferences preferences = getSharedPreferences("searchedData", Context.MODE_PRIVATE);
                        for(int i = 0; i < preferences.getAll().size(); i++){
                            String data = preferences.getString("search" + i, null);
                            if (data != null) {
                                JSONObject jsonObject = new JSONObject(data);
                                String name = jsonObject.getString("Name");
                                if (map.containsKey(name)) {

                                    //Create a button for the food names
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ImageButton button = new ImageButton(search_page.this);
                                            button.setImageResource(map.get(name).first); // Get the drawable from the map
                                            button.setTag(name); // Set the name as the tag of the button
                                            button.setOnClickListener(new View.OnClickListener() {
                                                public void onClick(View v) {
                                                    // Create an Intent and start the activity
                                                    Intent intent = new Intent(search_page.this, map.get(name).second); // Get the activity class from the map
                                                    intent.putExtra("image_food", map.get(name).first); // Get the drawable from the map
                                                    intent.putExtra("output_food", name);
                                                    intent.putExtra("food_name", name);
                                                    startActivity(intent);
                                                }
                                            });
                                            layout.addView(button);
                                        }
                                    });

                                }

                            }
                        }
                    }
                });
                mySearch.start();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        Button bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



}