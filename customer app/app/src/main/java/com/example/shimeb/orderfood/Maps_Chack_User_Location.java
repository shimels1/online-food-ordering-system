package com.example.shimeb.orderfood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.shimeb.orderfood.MainActivity.context;

public class Maps_Chack_User_Location extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_LOCATION = 21;
    private static final int REQUEST_CHECK_SETTINGS = 33;
    public static String LATITUEDE = "";
    public static String LONGTIUDE = "";
    LocationManager locationManager;
    String provide;
    Location location;

    int LOCATION_REFRASH_3=1000;

    int LOCATION_PERMISION_REQUESTED = 0;
    Toolbar toolbar;

    SupportMapFragment mapFragment;

    LinearLayout refrash_layout;
    LinearLayout L_found_layout;
    Button refrash_btn;
    Button next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps__chack__user__location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.find_loc_appbar);
        toolbar.setTitle("Finding your location");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refrash_layout = (LinearLayout) findViewById(R.id.FUL_refrash_btn_layout);
        L_found_layout = (LinearLayout) findViewById(R.id.FUL_L_found_layout);
        refrash_btn = (Button) findViewById(R.id.FUL_refrash_btn);
        next_btn = (Button) findViewById(R.id.FUL_L_found_next_btn);


        //////////////check permision
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            show_refrash_layout();
            return;

        } else if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            show_refrash_layout();
            return;
        }
        set_location_propertes();

        refrash_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_location_propertes();
                LOCATION_REFRASH_3=1000;
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Bill.class));
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Message.log("mapready");

    }

    ////////////////checkLocationEnabled

    private void checkLocationEnabled() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("to get your location,Turn on the location ?")
                .setTitle("Location");
        builder.setPositiveButton("location setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                LOCATION_PERMISION_REQUESTED = 1;
                startActivity(myIntent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent=new Intent(context,ViewBasket.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        if (LOCATION_PERMISION_REQUESTED == 1) {
            LOCATION_PERMISION_REQUESTED = 0;
            set_location_propertes();
        }
        super.onResume();
    }

    ///////////////////set_location_propertes
    private void set_location_propertes() {
        Message.log("hello");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provide = LocationManager.NETWORK_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            invisibl_location_found_layout();
            show_refrash_layout();
            return;
        }
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                mMap = googleMap;
                mMap.clear();
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);

                final Location myLoc = mMap.getMyLocation();
                if (myLoc != null) {
                    invisibl_refrash_layout();
                    show_location_found_layout();
                    Message.log("map on");
                    LATITUEDE=myLoc.getLatitude()+"";
                    LONGTIUDE=myLoc.getLongitude()+"";
                    LatLng lnglat = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(lnglat).title("your location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lnglat, 15));
                    //     mMap.addCircle(new CircleOptions().center(lnglat).radius(50));
                    mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location location) {

                            mMap = googleMap;
                            mMap.clear();

                            LATITUEDE=location.getLatitude()+"";
                            LONGTIUDE=location.getLongitude()+"";

                            LatLng lnglat = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(lnglat).title("your location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lnglat, 15));

                            Message.log("\n   loc lis \n");
                        }
                    });

                } else {

                    Message.log("map off");

                    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    boolean network_enabled = false;
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (!network_enabled) {
                        checkLocationEnabled();
                    }else{
                        if (LOCATION_REFRASH_3>0){
                        LOCATION_REFRASH_3--;
                        set_location_propertes();
                        }else{
                            invisibl_location_found_layout();
                            show_refrash_layout();
                        }
                    }

                }
            }


        });
    }

    private void invisibl_location_found_layout() {
        L_found_layout.setVisibility(View.GONE);
    }

    private void show_location_found_layout() {
        slideToTop(L_found_layout);
    }

    private void invisibl_refrash_layout() {
        refrash_layout.setVisibility(View.GONE);
    }

    private void show_refrash_layout() {
        slideToTop(refrash_layout);
    }


    //////////////////////////check_permision
    private void check_permision() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }
    }



    public static void slideToTop(View view) {
        Animation fadeIn = new AlphaAnimation(0, 2);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(800);
        view.startAnimation(fadeIn);
        view.setVisibility(View.VISIBLE);
    }

    public static void slideToBottom(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(1000);
        // animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

}
