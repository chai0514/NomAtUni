package com.example.groupassignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.groupassignment.diets.foodFragments.IngredientFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NomSupabase extends AppCompatActivity {




    public static class MyThread extends Thread{

        private String name;
        private Handler handler;
        private IngredientFragment context;


        HttpURLConnection urlConnection = null;

        public MyThread(String name, Handler handler, IngredientFragment context){
            this.name = name;
            this.handler = handler;
            this.context = context;
        }

        @Override
        public void run() {


            try {
                URL url = new URL("https://qeqpcapyxfnjtlasxsdj.supabase.co/rest/v1/ketoMenu"
                        + "?Name=eq."+name);

                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setRequestProperty("apikey", context.getString(R.string.SUPABASE_KEY));
                urlConnection.setRequestProperty("Authorization","Bearer "+ context.getString(R.string.SUPABASE_KEY));


                int code = urlConnection.getResponseCode();

                InputStream input = urlConnection.getInputStream();
                String returned_result = readStream(input);

                if(code == 200){
                    Log.i("######### TAG #########", "Response code:" + code);
                    Log.i("######### TAG #########", "Returned result:" + returned_result);

                    context.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                context.updateData(returned_result);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                }else if(code==201){
                    Log.i("######### TAG #########", "Successfully inserted:" + code);
                }else{
                    throw new IOException("Invalid response from the supabase Server");
                }

                input.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally{
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
            }
        }
    }


    public static class searchDB extends Thread{

        private String name;
        private Handler handler;
        private Context context;

        private OnDataStoredListener onDataStoredListener;

        HttpURLConnection urlConnection = null;

        public searchDB(String name, Handler handler, Context context, OnDataStoredListener onDataStoredListener){
            this.name = name;
            this.handler = handler;
            this.context = context;
            this.onDataStoredListener = onDataStoredListener;
        }

        @Override
        public void run() {


            try {
                URL url = new URL("https://qeqpcapyxfnjtlasxsdj.supabase.co/rest/v1/ketoMenu"
                        + "?Name=ilike.*" + name + "*");

                urlConnection = (HttpURLConnection) url.openConnection();


                urlConnection.setRequestProperty("apikey",context.getString(R.string.SUPABASE_KEY));
                urlConnection.setRequestProperty("Authorization","Bearer "+ context.getString(R.string.SUPABASE_KEY));


                int code = urlConnection.getResponseCode();

                InputStream input = urlConnection.getInputStream();
                String returned_result1 = readStream(input);

                if(code == 200){
                    Log.i("######### TAG #########", "Response code:" + code);
                    Log.i("######### TAG #########", "Returned result:" + returned_result1);



                    try{
                        JSONArray jsonArray = new JSONArray(returned_result1);
                        SharedPreferences preferences = context.getSharedPreferences("searchedData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            editor.putString("search" + i, jsonObject.toString());
                        }
                        editor.commit();
                        if(onDataStoredListener != null){
                            onDataStoredListener.onDataStored();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }




                }else if(code==201){
                    Log.i("######### TAG #########", "Successfully inserted:" + code);
                }else{
                    throw new IOException("Invalid response from the supabase Server");
                }

                input.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally{
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
            }
        }
    }


    public static class storeData extends Thread{
        private String name, meal_type;
        private double  calorie;
        private Handler handler;
        private Context context;

        HttpURLConnection urlConnection = null;

        public storeData(String name, String meal_type,Double calorie,Context context, Handler handler){
            this.name = name;
            this.meal_type=meal_type;
            this.calorie=calorie;
            this.context = context;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://qeqpcapyxfnjtlasxsdj.supabase.co/rest/v1/caloric");

                urlConnection = (HttpURLConnection) url.openConnection();

                // Create JSON
                JSONObject json = new JSONObject();
                json.put("Name", this.name);
                json.put("meal_type", this.meal_type);
                json.put("Calories", this.calorie);


                Log.i("thisisssittt","this is name"+name +"this is meal_type"+meal_type+"thisiscalorie"+calorie);

                urlConnection.setRequestProperty("apikey",context.getString(R.string.SUPABASE_KEY) );
                urlConnection.setRequestProperty("Authorization", "Bearer " + context.getString(R.string.SUPABASE_KEY));



                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Prefer","return=minimal");
                urlConnection.setDoOutput(true);


                OutputStream output = urlConnection.getOutputStream();
                output.write(json.toString().getBytes());
                output.flush();

                int code = urlConnection.getResponseCode();
                //InputStream input = urlConnection.getInputStream();
                //String returned_result = readStream(input);


                if(code == 200 || code == 201){
                    Log.i("##### Tag #####", "response code: " + code);



                } else {
                    throw new IOException("Invalid response from Supabase Server!");
                }
                //input.close();
            } catch (IOException | JSONException e) {
                Log.e("##### Tag #####", "Error", e);


            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
        }
    }


    public static class retrieveData extends Thread {
        private Handler handler;
        private Context context;

        HttpURLConnection urlConnection = null;

        public retrieveData(Context context, Handler handler) {
            this.context = context;
            this.handler = handler;
        }

        @Override
        public void run() {
            try {
                URL url = new URL("https://qeqpcapyxfnjtlasxsdj.supabase.co/rest/v1/caloric?select=*");

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestProperty("apikey", context.getString(R.string.SUPABASE_KEY));
                urlConnection.setRequestProperty("Authorization", "Bearer " + context.getString(R.string.SUPABASE_KEY));

                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Prefer", "return=minimal");

                int code = urlConnection.getResponseCode();
                if (code == 200) {
                    InputStream input = urlConnection.getInputStream();
                    String returned_result = readStream(input);
                    Log.i("##### Tag #####", "response: " + returned_result);


                    SharedPreferences sharedPref = context.getSharedPreferences("calorieTracker", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("keyCalorie",returned_result);
                    editor.clear();
                    editor.commit();

                    // Send a broadcast
                    Intent intent = new Intent("dataReady");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


                } else {
                    throw new IOException("Invalid response from Supabase Server!");
                }
            } catch (IOException e) {
                Log.e("##### Tag #####", "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

    }



    public interface OnDataStoredListener {
        void onDataStored() throws JSONException;
    }


    public static String readStream(InputStream input_stream){
        try{
            ByteArrayOutputStream byte_output = new ByteArrayOutputStream();
            int i = input_stream.read();

            while( i !=-1){
                byte_output.write(i);
                i=input_stream.read();
            }
            return byte_output.toString();


        } catch (IOException e) {
            return "";
        }
    }

}