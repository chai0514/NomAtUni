package com.example.groupassignment.mealPlanning;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groupassignment.calorie_tracker;
import com.example.groupassignment.NomSupabase;
import com.example.groupassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Lunch extends AppCompatActivity {



    private static final String API_KEY = "AMjcgWPG5aAejnRcZpaPYYJC699H7Gpty3c219vO";  // Replace with your API key
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<String> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch);

        foodList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(foodList, this::onItemClick);
        recyclerView.setAdapter(foodAdapter);

        SearchView searchView = findViewById(R.id.sc_searchLch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new FetchFoodTask().execute(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Button bt_cancel = findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancel = new Intent(Lunch.this, calorie_tracker.class);
                startActivity(cancel);
            }
        });

    }



    public void onItemClick(View v, int position) {
        String food = foodList.get(position);
        String[] parts = food.split(" - FDC ID: ");
        int fdcId = Integer.parseInt(parts[1]);
        Context context = Lunch.this;

        new Breakfast.FetchNutrientsTask() {
            @Override
            protected void onPostExecute(List<String> result) {
                if (result != null) {
                    // Calculate the calories based on the nutrients
                    double calories = calculateCalories(result);

                    // Show a confirmation dialog
                    new AlertDialog.Builder(Lunch.this)
                            .setTitle("Add to Lunch")
                            .setMessage("Are you sure you want to add " + food + " to Lunch?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User clicked the Yes button

                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("Name", food);
                                        jsonObject.put("meal_type", "Lunch");
                                        jsonObject.put("Calories", calories);

                                        // Create a new Handler
                                        Handler handler = new Handler(Looper.getMainLooper());

                                        // Create a new instance of storeData
                                        NomSupabase.storeData thread = new NomSupabase.storeData(food, "Lunch", calories, context, handler);
                                        thread.start(); // Start the thread

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    Intent start_calorie = new Intent(Lunch.this, calorie_tracker.class);
                                    startActivity(start_calorie);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Toast.makeText(Lunch.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(fdcId);
    }


    private class FetchFoodTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            String query = params[0];
            HttpURLConnection connection = null;
            List<String> foodList = new ArrayList<>();

            try {
                URL url = new URL("https://api.nal.usda.gov/fdc/v1/foods/search?api_key=" + API_KEY + "&query=" + query);
                connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                JSONObject jsonObject = new JSONObject(builder.toString());
                JSONArray foods = jsonObject.getJSONArray("foods");

                for (int i = 0; i < foods.length(); i++) {
                    JSONObject food = foods.getJSONObject(i);
                    int fdcId = food.getInt("fdcId");
                    String description = food.getString("description");

                    // Log the food name and FDC ID
                    Log.d("FoodData", "Food Name: " + description + ", FDC ID: " + fdcId);

                    foodList.add(description + " - FDC ID: " + fdcId);
                }

                return foodList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null) {
                foodAdapter.updateData(result);
            } else {
                Toast.makeText(Lunch.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private double calculateCalories(List<String> result) {
        double protein = 0; // amount of protein in grams
        double carbs = 0; // amount of carbohydrates in grams
        double fat = 0; // amount of fat in grams

        for (String nutrient : result) {
            String[] parts = nutrient.split(": ");
            String nutrientName = parts[0];
            double amount = Double.parseDouble(parts[1].split(" ")[0]);

            switch (nutrientName) {
                case "Protein":
                    protein = amount;
                    break;
                case "Total lipid (fat)":
                    fat = amount;
                    break;
                case "Carbohydrate, by difference":
                    carbs = amount;
                    break;
            }
        }

        double totalCalories = 4 * protein + 4 * carbs + 9 * fat;
        return totalCalories;
    }

    public static class FetchNutrientsTask extends AsyncTask<Integer, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Integer... params) {
            int fdcId = params[0];
            HttpURLConnection connection = null;
            List<String> nutrientList = new ArrayList<>();

            try {
                URL url = new URL("https://api.nal.usda.gov/fdc/v1/food/" + fdcId + "?api_key=" + API_KEY);
                connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                JSONObject jsonObject = new JSONObject(builder.toString());
                if (jsonObject.has("foodNutrients")) {
                    JSONArray foodNutrients = jsonObject.getJSONArray("foodNutrients");

                    for (int i = 0; i < foodNutrients.length(); i++) {
                        JSONObject nutrient = foodNutrients.getJSONObject(i);
                        if (nutrient.has("nutrient")) {
                            JSONObject nutrientDetails = nutrient.getJSONObject("nutrient");
                            String nutrientName = nutrientDetails.getString("name");

                            // Only add the nutrients you're interested in
                            if (nutrientName.equals("Protein") || nutrientName.equals("Total lipid (fat)") || nutrientName.equals("Carbohydrate, by difference")) {
                                String unitName = nutrientDetails.getString("unitName");
                                double value = nutrient.getDouble("amount");

                                // Log the nutrient information
                                Log.d("FoodData", "Nutrient: " + nutrientName + ", Amount: " + value + " " + unitName);

                                nutrientList.add(nutrientName + ": " + value + " " + unitName);
                            }
                        }
                    }
                }
                return nutrientList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}