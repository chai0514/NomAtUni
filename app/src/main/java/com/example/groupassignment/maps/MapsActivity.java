package com.example.groupassignment.maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groupassignment.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private ArrayList<String> restaurantList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        drawerLayout = findViewById(R.id.drawer_layout);
        listView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, restaurantList);
        listView.setAdapter(adapter);

        // Initialize the Fused Location Provider Client
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Enable Zoom Controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Get the current location of the device and set the position of the map.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Set the map's camera position to the current location of the device.
                Location lastKnownLocation = task.getResult();
                if (lastKnownLocation != null) {
                    LatLng currentLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

                    // Use the Places API to find restaurants nearby
                    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                            + "location=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude()
                            + "&radius=500"
                            + "&type=restaurant";
                            //+ "&key=AIzaSyAFv1kn-HjtXFp_gWb1-5voBe_U3bTlpK0";

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                            response -> {
                                try {
                                    JSONArray results = response.getJSONArray("results");
                                    for (int i = 0; i < results.length(); i++) {
                                        JSONObject result = results.getJSONObject(i);
                                        String name = result.getString("name");
                                        JSONObject geometry = result.getJSONObject("geometry");
                                        JSONObject location = geometry.getJSONObject("location");
                                        LatLng latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));

                                        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                                        restaurantList.add(name);
                                    }
                                    adapter.notifyDataSetChanged(); // Notify the adapter to update the ListView
                                    drawerLayout.openDrawer(GravityCompat.START); // Open the drawer
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, Throwable::printStackTrace);

                    RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                    queue.add(request);
                }
            } else {
                Log.d("MapsActivity", "Current location is null. Using defaults.");
                Log.e("MapsActivity", "Exception: %s", task.getException());
                mMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(new LatLng(-34, 151), 15));
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        });
    }
}