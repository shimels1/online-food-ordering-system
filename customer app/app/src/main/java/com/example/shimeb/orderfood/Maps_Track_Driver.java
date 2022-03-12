package com.example.shimeb.orderfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Maps_Track_Driver extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    DatabaseReference databaseReferenceRes_respond;

    DatabaseReference databaseReferenceDriver;
    /////////////////////fabs
    FloatingActionButton call;
    FloatingActionsMenu floatingActionsMenu;

String  DRIVER_PHONE_NUMBER="";

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 22;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__track__driver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        String userId=sp.getString("id","null");
        Message.log("user id "+userId);
        databaseReferenceRes_respond= FirebaseDatabase.getInstance().getReference("Request").child(Requested_order.RESTAURANT_ID).child(userId);
        databaseReferenceDriver = FirebaseDatabase.getInstance().getReference("Driver");
        call = (FloatingActionButton) findViewById(R.id.lt_call);
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions_down);

        call.setImageResource(R.drawable.call);

        toolbar = (Toolbar) findViewById(R.id.map_tracker_appbar);
        toolbar.setTitle("Tracking Driver...");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReferenceRes_respond.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              //  for(DataSnapshot data:dataSnapshot.getChildren()) {

                    SentRequest sr = dataSnapshot.getValue(SentRequest.class);

                Message.log("key "+dataSnapshot.getKey()+" "+(sr!=null)+" "+Requested_order.RESTAURANT_ID);
                    if (sr!=null){
                    mMap.clear();
                        String DRIVER_ID="";
                    DRIVER_ID=sr.getDriver_respond();

                    Log.i(">>>>>>>request"," "+sr.getLongtiude()+" "+sr.getLatitiud());
                    double lat = Double.parseDouble(sr.getLatitiud());
                    double lng = Double.parseDouble(sr.getLongtiude());
                    LatLng loc = new LatLng(lat, lng);
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(loc)
                            .title("Your Location"));

                    double lat2 = Double.parseDouble(sr.getDriver_latitiud());
                    double lng2 = Double.parseDouble(sr.getDriver_longtiude());

                    LatLng loc2 = new LatLng(lat2, lng2);
                    Marker marker2 = mMap.addMarker(new MarkerOptions()
                            .position(loc2)
                            .title("Driver Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    Marker[] markers = new Marker[2];
                    markers[0] = marker;
                    markers[1] = marker2;

                    for (Marker m : markers) {
                        builder.include(m.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    int padding = 85;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    if (mMap != null) {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                    }

                        final String finalDRIVER_ID = DRIVER_ID;
                        databaseReferenceDriver.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            final Driver_data driver_data = data.getValue(Driver_data.class);
                        if (driver_data.getId().equals(finalDRIVER_ID)){
                            DRIVER_PHONE_NUMBER=driver_data.getPhone();
                            Message.log(data.child("phone").toString()+" "+driver_data.getPhone()+" "+driver_data.getName()+" "+data.toString());
                        }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                    }}
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ///////////////////fab on clicks


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // call_to_customer();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (DRIVER_PHONE_NUMBER!=""){

                    Message.log(DRIVER_PHONE_NUMBER+"///////////////////");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + DRIVER_PHONE_NUMBER + ""));

                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Maps_Track_Driver.this,
                            new String[]{android.Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    startActivity(callIntent);
                    //    Toast.makeText(getApplicationContext(),PHONE,Toast.LENGTH_SHORT).show();
                }}else{
                    Toast.makeText(getApplicationContext(),"phone number not found",Toast.LENGTH_SHORT).show();
                }
                floatingActionsMenu.collapse();
            }
        });


                }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
