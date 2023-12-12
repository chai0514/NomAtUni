package com.example.groupassignment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.groupassignment.mealPlanning.Breakfast;
import com.example.groupassignment.mealPlanning.Dinner;
import com.example.groupassignment.mealPlanning.Lunch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class calorie_tracker extends AppCompatActivity {

    private String data;

    private String name,mealType,calories;

    double progressValue=0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_tracker);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.rl_calorieTrack);
        // Get the color value from the color resource
        int colorRes = getResources().getColor(R.color.bg_color);

        Drawable vector_bg1= getResources().getDrawable(R.drawable.vector_bg1);
        Drawable vector_bg2= getResources().getDrawable(R.drawable.vector_bg2);
        //LAYER THE VECTOR IMAGE
        Drawable[] layers = new Drawable[]{new ColorDrawable(colorRes), vector_bg1, vector_bg2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        mainLayout.setBackground(layerDrawable);


        Button bt_budgetEdit = (Button) findViewById(R.id.bt_budget);

        bt_budgetEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String newBudget = CalorieBudget(data).toString();

                double Cals= Double.parseDouble(data);
                int BudgetCalorie = (int) Math.round(Cals);
                budget_Dialog(Integer.valueOf(BudgetCalorie));

            }
        });

        Button bt_breakfast = (Button) findViewById(R.id.bt_breakfast);
        Button bt_lunch = (Button) findViewById(R.id.bt_lunch);
        Button bt_dinner = (Button) findViewById(R.id.bt_dinner);


        bt_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent start_breakfast = new Intent(calorie_tracker.this, Breakfast.class);
                startActivity(start_breakfast);
            }
        });


        bt_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start_lunch= new Intent(calorie_tracker.this, Lunch.class);
                startActivity(start_lunch);
            }
        });


        bt_dinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start_dinner= new Intent(calorie_tracker.this, Dinner.class);
                startActivity(start_dinner);
            }
        });


        Button cancel = findViewById(R.id.bt_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), main_menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        //Retrieve data from CaloriePlanner shared preferences
        super.onResume();

        //Shared Preferences from CaloriePlanner
        SharedPreferences preferences = getSharedPreferences("CalorieOnly", Context.MODE_PRIVATE);
        data = preferences.getString("key", "0");

        //Update the Budget
        updateMaxBudget(data);

        new NomSupabase.retrieveData(this, new Handler(Looper.getMainLooper())).start();


        // Register the receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // The data is ready, retrieve it from SharedPreferences
                SharedPreferences sharedPref = getSharedPreferences("calorieTracker", Context.MODE_PRIVATE);
                String data = sharedPref.getString("keyCalorie", null);

                // Clear the layout
                LinearLayout ll_breakfast = (LinearLayout) findViewById(R.id.ll_breakfast);
                ll_breakfast.removeAllViews();
                LinearLayout ll_dinner = (LinearLayout) findViewById(R.id.ll_dinner);
                ll_dinner.removeAllViews();
                LinearLayout ll_lunch = (LinearLayout) findViewById(R.id.ll_lunch);
                ll_lunch.removeAllViews();

                try {
                    JSONArray jsonArray = new JSONArray(data);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        name = jsonObject.getString("Name");
                        mealType = jsonObject.getString("meal_type");
                        calories = jsonObject.getString("Calories");

                        // Update meals after processing each JSONObject
                        updateMeals(name, mealType);
                        updateProgressBar(calories);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new IntentFilter("dataReady"));


    }

    private void updateMeals(String Name, String mealType){


        if(mealType !=null&& Name !=null){
            if(mealType.equalsIgnoreCase("Breakfast")){

                LinearLayout ll_breakfast = (LinearLayout) findViewById(R.id.ll_breakfast);
                TextView newText = new TextView(this);
                newText.setPadding(5,5,5,5);
                newText.setText(Name); // Set the text to the meal name
                ll_breakfast.addView(newText);


            }else if(mealType.equalsIgnoreCase("Lunch")){
                LinearLayout ll_lunch = (LinearLayout) findViewById(R.id.ll_lunch);
                TextView newText = new TextView(this);
                newText.setPadding(5,5,5,5);
                newText.setText(Name); // Set the text to the meal name
                ll_lunch.addView(newText);


            }else if(mealType.equalsIgnoreCase("Dinner")){
                LinearLayout ll_dinner = (LinearLayout) findViewById(R.id.ll_dinner);
                TextView newText = new TextView(this);
                newText.setPadding(5,5,5,5);
                newText.setText(Name); // Set the text to the meal name
                ll_dinner.addView(newText);
            }
        }else{
            Log.d("updateMeal", " is Currently Empty");
        }

    }

    private void updateProgressBar(String calories){

        ProgressBar calorieBar = findViewById(R.id.pg_progressBar);

        //Find the view
        TextView tv_progBar = findViewById(R.id.tv_progBar);

        if(calories!=null){
            double curCalories = Double.parseDouble(calories);
            //Setting the value
            progressValue = Math.round(curCalories) + progressValue;
            calorieBar.setProgress((int) progressValue);

            tv_progBar.setText(String.valueOf(progressValue));

        }

    }

    public int updateMaxBudget(String data){
        //This one is to set that the maximum budget is now == Data
        double maxCals= Double.parseDouble(data);
        int BudgetCalorie = (int) Math.round(maxCals);
        Log.d("whatishere", String.valueOf(BudgetCalorie));
        ProgressBar calorieBar = findViewById(R.id.pg_progressBar);
        calorieBar.setMax(BudgetCalorie);

        TextView tv_updtBudget = findViewById(R.id.tv_budgetBar);
        tv_updtBudget.setText("of "+String.valueOf(BudgetCalorie)+" Kcal");

        return BudgetCalorie;
    }


    private void budget_Dialog(Integer updateBudget ){
        AlertDialog.Builder builder = new AlertDialog.Builder (this);
        builder.setTitle("Calorie Budget");
        builder.setMessage(updateBudget+"Kcal" + " planned Daily Food Calorie Budget");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.dismiss();
            }
        });

        builder.setNegativeButton("My Plan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent my_plan = new Intent(calorie_tracker.this, calorie_planner.class);
                startActivity(my_plan);
            }
        });

        builder.show();
    }


}