package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity_All_restorant_location extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Toolbar toolbar;
    DatabaseReference databaseReferenceRestaurant;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_all_restorant_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("finding restaurant locations...");
        progressDialog.show();

        databaseReferenceRestaurant = FirebaseDatabase.getInstance().getReference("Restaurant");




        toolbar = (Toolbar) findViewById(R.id.map_res_app_bar);
        toolbar.setTitle("All Restaurnt Locaions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng zoomLatLng = new LatLng(11.596243,37.385133);
        //mMap.addMarker(new MarkerOptions().position(sydney).title(""+MainActivity.CURRENT_SELECTED_REST_NAME));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomLatLng,12));

        databaseReferenceRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()){
                if (d != null) {
                progressDialog.dismiss();
                    Restorantdata rs = d.getValue(Restorantdata.class);
                   LatLng sydney = new LatLng(Float.parseFloat(rs.getLatitiud()), Float.parseFloat(rs.getLongtiude()));
                   mMap.addMarker(new MarkerOptions().position(sydney).title(""+rs.getName()));
                    Message.log("check "+rs.getName());
                }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
