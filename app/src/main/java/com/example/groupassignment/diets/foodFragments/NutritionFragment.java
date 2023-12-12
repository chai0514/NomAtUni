package com.example.groupassignment.diets.foodFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.groupassignment.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class NutritionFragment extends Fragment {


    private JSONArray dataJson;
    private String[] nutritionArray;

    private PieChart pieChart;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.post(() -> {
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(widthMeasureSpec, heightMeasureSpec);

            ViewPager2 viewPager = (ViewPager2) getActivity().findViewById(R.id.view_pager);
            if (viewPager.getLayoutParams().height != view.getMeasuredHeight()) {
                viewPager.getLayoutParams().height = view.getMeasuredHeight();
                viewPager.requestLayout();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nutrition, container, false);



        SharedPreferences preferences = getActivity().getSharedPreferences("SharedData",Context.MODE_PRIVATE);
        String data = preferences.getString("key", "0");

        if(data != null){
            Log.d("This is in nutrition:",data);
        } else {
            Log.d("This is in nutrition:","No data found");
        }


        try {

            TableLayout tl_nutrition = view.findViewById(R.id.tl_nutrition);

            dataJson = new JSONArray(data);
            JSONObject jsonObject = dataJson.getJSONObject(0);
            String nutrition = jsonObject.get("Nutrition").toString();
            nutritionArray = nutrition.split(",");

            String[] splitValue = nutritionArray[0].split(" ");
            String calorie = splitValue[0];
            String value = splitValue[1];

            //Create a new row
            TableRow row = new TableRow(requireContext());

            // TextView for Calorie
            TextView tv_calorie = new TextView(requireContext());
            calorie = calorie.substring(0,1) + calorie.substring(1,7).toLowerCase();
            tv_calorie.setText(calorie + " Per serving");


            // TextView for Value
            TextView tv_value = new TextView(requireContext());
            tv_value.setText(value + "kCal");

            //Set the layout of the text so the text doesn't overflow
            tv_calorie.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            //Set the layout of the text so the text doesn't overflow
            tv_value.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            // Add padding to the TextView
            int padding_in_dp = 10;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            tv_calorie.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
            tv_value.setPadding(padding_in_px * 17, padding_in_px, padding_in_px, padding_in_px);

            //Add the TextView to the row
            row.addView(tv_calorie);
            row.addView(tv_value);

            tl_nutrition.addView(row);

            View divider = new View(requireContext());

            // Set the height of the divider
            int height_in_dp = 1;
            int height_in_px = (int) (height_in_dp * scale + 0.5f);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    height_in_px
            );

            tl_nutrition.addView(divider, params);



        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        // Extract individual values
        String calories = getValue(nutritionArray, "CALORIES");
        String fat = getValue(nutritionArray, "FAT");
        String carbohydrates = getValue(nutritionArray, "CARBOHYDRATES");
        String protein = getValue(nutritionArray, "PROTEIN");

        String[] nutrients = {"FAT","CARBOHYDRATES","PROTEIN"};

        //Calculate their % of calorie
        Float newCalories = Float.parseFloat(calories);
        Float newFat = Float.parseFloat(fat);
        Float newCarbohydrates = Float.parseFloat(carbohydrates);
        Float newProtein = Float.parseFloat(protein);

        float calcFat = ((newFat * 9) / newCalories) * 100;
        float calcCarb = ((newCarbohydrates * 4) / newCalories) * 100;
        float calcProt = ((newProtein * 4) / newCalories) * 100;

        float[] calculatedCalorie = {calcFat,calcCarb,calcProt};

        pieChart = view.findViewById(R.id.lt_nutriPie);

        ArrayList<PieEntry> entries = new ArrayList<>();

        for(int i =0; i< nutrients.length;i++){
            entries.add(new PieEntry(calculatedCalorie[i], (nutrients[i])));

        }


        //PIE CHART
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#00FFFF"));
        colors.add(Color.parseColor("#FD9A24"));
        colors.add(Color.parseColor("#FF7F7F"));

        // Create a PieDataSet with entries and label
        PieDataSet pieDataSet = new PieDataSet(entries,"");

        pieDataSet.setValueTextSize(12f);

        // Set individual colors for each segment
        pieDataSet.setColors(colors);


        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.setDrawSliceText(false);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setUsePercentValues(true);



        Legend legend = pieChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(12f);


        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();






        return view;


    }


    private String getValue(String[] nutritionArray, String nutrient) {
        for(int i = 0; i < nutritionArray.length; i++){
            String[] parts = nutritionArray[i].trim().split(" ");
            if(parts[0].equalsIgnoreCase(nutrient)){
                return parts[1];
            }
        }
        return null;
    }



}