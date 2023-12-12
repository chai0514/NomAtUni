package com.example.groupassignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class calorie_planner extends AppCompatActivity {

    //BT activity data
    private Button bt_currentWeight;
    private Button bt_targetWeight;
    private Button bt_height;
    private Button bt_targetDay;
    private Button bt_age;
    private Button bt_gender;


    //Dialog data
    private String dialog_weight;
    private String dialog_tweight;
    private String dialog_height;
    private String dialog_targetDay;
    private String dialog_age;
    private String dialog_gender;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_planner);



        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.rl_caloriePlanner);
        // Get the color value from the color resource
        int colorRes = getResources().getColor(R.color.bg_color);

        Drawable vector_bg1= getResources().getDrawable(R.drawable.vector_bg1);
        Drawable vector_bg2= getResources().getDrawable(R.drawable.vector_bg2);
        //LAYER THE VECTOR IMAGE
        Drawable[] layers = new Drawable[]{new ColorDrawable(colorRes), vector_bg1, vector_bg2};
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        mainLayout.setBackground(layerDrawable);



        Button bt_save = (Button) findViewById(R.id.bt_save);
        Button bt_cancel = (Button) findViewById(R.id.bt_cancel);

         bt_currentWeight = (Button) findViewById(R.id.bt_currentWeight);
         bt_targetWeight = (Button) findViewById(R.id.bt_targetWeight);
         bt_height = (Button) findViewById(R.id.bt_height);
         bt_targetDay = (Button) findViewById(R.id.bt_targetDay);
         bt_age = (Button) findViewById(R.id.bt_age);
         bt_gender = (Button) findViewById(R.id.bt_gender);




        TextView tv_currentWeight = (TextView) findViewById(R.id.tv_currentWeight);
        tv_currentWeight.setText("Current Weight");
        TextView tv_targetWeight = (TextView) findViewById(R.id.tv_targetWeight);
        tv_targetWeight.setText("Target Weight");
        TextView tv_height = (TextView) findViewById(R.id.tv_height);
        tv_height.setText("Height");
        TextView tv_targetDay = (TextView) findViewById(R.id.tv_targetDay);
        tv_targetDay.setText("Target Days");



        bt_currentWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curWeight_dialog();
            }
        });

        bt_targetWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetWeight_dialog();
            }
        });

        bt_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height_dialog();
            }
        });

        bt_targetDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetDay_dialog();
            }
        });

        bt_age.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age_dialog();
            }
        });

        bt_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender_dialog();
            }
        });


        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // Create a new JSONObject


                    JSONObject jsonObject = new JSONObject();

                    // Put elements into the JSON object
                    jsonObject.put("dialog_weight", dialog_weight);
                    jsonObject.put("dialog_tweight", dialog_tweight);
                    jsonObject.put("dialog_height", dialog_height);
                    jsonObject.put("dialog_targetDay", dialog_targetDay);
                    jsonObject.put("dialog_age",dialog_age);
                    jsonObject.put("dialog_gender",dialog_gender);

                    // Convert JSONObject to String
                    String jsonString = jsonObject.toString();
                    double data = CalorieBudget(jsonString);


                    SharedPreferences preferences = getSharedPreferences("CalorieOnly", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.putString("key", String.valueOf(data));
                    editor.commit();




                    Intent i = new Intent(calorie_planner.this, calorie_tracker.class);
                    startActivity(i);


                    // Now jsonString contains your data in JSON format
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Button bt_calculate = findViewById(R.id.bt_calculate);
        EditText eth_height = findViewById(R.id.eth_height);
        EditText eth_weight = findViewById(R.id.eth_weight);


        bt_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String height = eth_height.getText().toString();
                String weight = eth_weight.getText().toString();
                bmi_calculator(height, weight);
            }
        });


        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent start = new Intent(calorie_planner.this, calorie_tracker.class);
                startActivity(start);
            }
        });

    }

    private void bmi_calculator(String eth_height, String eth_weight){
        TextView tv_bmi = findViewById(R.id.tv_caloriebmi);
        //tv_bmi.setTypeface(Inter);

        double weight = Double.parseDouble(eth_weight);
        double height = Double.parseDouble(eth_height); //m^2

        double bmi = weight/ (Math.pow(height,2));


        if(bmi < 18.5){
            tv_bmi.setText("You are severely Underweight. Take care! " + bmi);
        } else if(bmi >= 18.5 && bmi <= 25){
            tv_bmi.setText("Your are normal weighted with a bmi of " + bmi);
        }else if(bmi > 25 && bmi <= 30){
            tv_bmi.setText("You are over-weighted with a bmi of " + bmi);
        }else if(bmi >30){
            tv_bmi.setText("You are severely obese. Start lifting! bmi of " + bmi);
        }

    }


    private Double CalorieBudget(String data) {

        Double newCalorieBudget = 0.0;


        try {

            // Create a new JSONObject
            JSONObject jsonObject = new JSONObject(data);

            // Get the values from the JSONObject
            String dialog_weight = jsonObject.getString("dialog_weight");
            String dialog_tweight = jsonObject.getString("dialog_tweight");
            String dialog_height = jsonObject.getString("dialog_height");
            String dialog_targetDay = jsonObject.getString("dialog_targetDay");
            String dialog_age = jsonObject.getString("dialog_age");
            String dialog_gender = jsonObject.getString("dialog_gender");

            //Change to correct Data
            Double weight = Double.parseDouble(dialog_weight);
            Double height = Double.parseDouble(dialog_height);
            Integer age = Integer.parseInt(dialog_age);
            Integer noOfDays = Integer.parseInt(dialog_targetDay);
            Double targetWeight = Double.parseDouble(dialog_tweight);

            //Calculate formula based on gender
            if (dialog_gender.equals("Male")) {
                Double BMR_male = 66 + (13.7 * weight) + (5 * (height * 100)) - (6.8 * age);
                //Then calculate the AMR
                Double AMR_MALE = BMR_male * 1.375;

                Double CalorieBudget = (weight-targetWeight) * (7700);
                Double DailyCalorieDeficit = (CalorieBudget / noOfDays);

                newCalorieBudget = AMR_MALE- DailyCalorieDeficit;

            } else if (dialog_gender.equals("Female")) {
                Double BMR_female = 655 + (9.6 * weight) + (1.8 * (height * 100)) - (4.7 * age);
                //Then calculate the AMR
                Double AMR_female = BMR_female * 1.375;

                Double CalorieBudget = (weight-targetWeight) * (7700);
                Double DailyCalorieDeficit = (CalorieBudget / noOfDays);

                newCalorieBudget = AMR_female - DailyCalorieDeficit;
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }



        return Double.valueOf(newCalorieBudget);
    }

    //Alert Dialog box for input
    private void curWeight_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.curweight_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.editText_curweight);
                dialog_weight = editText.getText().toString();

                bt_currentWeight.setText(dialog_weight);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void targetWeight_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.targetweight_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.editText_tw);
                dialog_tweight = editText.getText().toString();

                bt_targetWeight.setText(dialog_tweight);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void height_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.height_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.editText_height);
                dialog_height = editText.getText().toString();

                bt_height.setText(dialog_height);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void targetDay_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.targetday_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.editText_days);
                dialog_targetDay = editText.getText().toString();

                bt_targetDay.setText(dialog_targetDay);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void age_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.age_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.editText_age);
                dialog_age = editText.getText().toString();

                bt_age.setText(dialog_age);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void gender_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.gender_dialog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RadioGroup rg_gender = (RadioGroup) customLayout.findViewById(R.id.rg_gender);
                int selectedId = rg_gender.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) customLayout.findViewById(selectedId);
                dialog_gender = radioButton.getText().toString();

                bt_gender.setText(dialog_gender);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}