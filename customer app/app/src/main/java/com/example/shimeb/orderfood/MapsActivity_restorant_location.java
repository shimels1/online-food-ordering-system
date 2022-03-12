package com.example.shimeb.orderfood;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity_restorant_location extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_restorant_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.map_res_app_bar);
        toolbar.setTitle(MainActivity.CURRENT_SELECTED_REST_NAME+" Location");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(Float.parseFloat(AboutRestaurant.LAT), Float.parseFloat(AboutRestaurant.LNG));
        mMap.addMarker(new MarkerOptions().position(sydney).title(""+MainActivity.CURRENT_SELECTED_REST_NAME));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
    }
}
