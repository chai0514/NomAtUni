package com.example.groupassignment.diets.foodFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.groupassignment.NomSupabase;
import com.example.groupassignment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IngredientFragment extends Fragment {


    private NomSupabase nomSupabase;

    private JSONArray jsonArray;


    public static IngredientFragment newInstance(String foodName) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
        args.putString("foodName", foodName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);

        String foodName = getArguments().getString("foodName");

        //Pass the data to Supabase
        final Handler handler = new Handler();
        nomSupabase = new NomSupabase();
        NomSupabase.MyThread myThread = new NomSupabase.MyThread(foodName, handler,this);
        myThread.start();


        return view;
    }

    public void updateData(String returned_result) throws JSONException {

        TableLayout tl_ingredient = getView().findViewById(R.id.tl_ingredient);

        jsonArray = new JSONArray(returned_result);

        SharedPreferences preferences = getActivity().getSharedPreferences("SharedData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("key", jsonArray.toString());
        editor.apply();

        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String ingredients = jsonObject.get("Ingredients").toString();
        String[] ingredientArray = ingredients.split(",");

        for (int i = 0; i < ingredientArray.length; i++) {
            // Create a new row
            TableRow row = new TableRow(requireContext());

            // TextView for ingredient
            TextView tv_ingredients = new TextView(requireContext());
            tv_ingredients.setText(ingredientArray[i].trim());

            //Set the layout of the text so the text doesn't overflow
            tv_ingredients.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            // Add padding to the TextView
            int padding_in_dp = 10;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            tv_ingredients.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);

            // Add the TextView to the row
            row.addView(tv_ingredients);



            //tl_ingredient.addView(divider, params);


            // Add the row to the table
            tl_ingredient.addView(row);
        }
    }




}