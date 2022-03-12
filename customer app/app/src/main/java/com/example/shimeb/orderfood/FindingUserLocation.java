package com.example.shimeb.orderfood;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static com.example.shimeb.orderfood.MainActivity.context;

public class FindingUserLocation extends AppCompatActivity implements LocationListener {


    private static final int REQUEST_LOCATION = 21;
    private static final int REQUEST_CHECK_SETTINGS = 33;
    public static String LATITUEDE = "";
    public static String LONGTIUDE = "";
    LocationManager locationManager;
    String provide;
    Location location;

    Toolbar toolbar;

    LinearLayout refrash_layout;
    LinearLayout L_found_layout;
    Button refrash_btn;
    Button LF_check_L_btn;
    Button next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_user_location);

        toolbar = (Toolbar) findViewById(R.id.find_loc_appbar);
        toolbar.setTitle("Finding your location");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refrash_layout = (LinearLayout) findViewById(R.id.FUL_refrash_btn_layout);
        L_found_layout = (LinearLayout) findViewById(R.id.FUL_L_found_layout);
        refrash_btn = (Button) findViewById(R.id.FUL_refrash_btn);
        LF_check_L_btn = (Button) findViewById(R.id.FUL_L_found_check_btn);
        next_btn = (Button) findViewById(R.id.FUL_L_found_next_btn);


        //////////////check permission
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
        } else {
            set_location_propertes();
        }


        refrash_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_location_propertes();
            }
        });

        LF_check_L_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                MapsActivity.FROM_LOCATION = 1;
                startActivity(intent);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        Toast.makeText(getApplicationContext(), "loaction changed", Toast.LENGTH_SHORT).show();

        LATITUEDE = lat+"" ;
        LONGTIUDE = lng+"";


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    ////////////////check Location Enabled

    private void checkLocationEnabled() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("to get your location,Turn on the location ?")
                .setTitle("Location");
        builder.setPositiveButton("location setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    ///////////////////set_location_properties
    private void set_location_propertes() {

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
        location = locationManager.getLastKnownLocation(provide);
        if (location != null) {
            Toast.makeText(getApplicationContext(), "location found", Toast.LENGTH_SHORT).show();
            invisibl_refrash_layout();
            show_location_found_layout();
            LATITUEDE = location.getLatitude()+"" ;
            LONGTIUDE = location.getLongitude()+"";



        } else {
            invisibl_location_found_layout();
            show_refrash_layout();
            Toast.makeText(getApplicationContext(), "location is not found", Toast.LENGTH_SHORT).show();

            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean network_enabled = false;
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!network_enabled) {
                checkLocationEnabled();
            }

        }
    }

    private void invisibl_location_found_layout() {
        L_found_layout.setVisibility(View.GONE);
    }

    private void show_location_found_layout() {
        L_found_layout.setVisibility(View.VISIBLE);
    }

    private void invisibl_refrash_layout() {
        refrash_layout.setVisibility(View.GONE);
    }

    private void show_refrash_layout() {
        refrash_layout.setVisibility(View.VISIBLE);
    }


    //////////////////////////check_permission
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


}
