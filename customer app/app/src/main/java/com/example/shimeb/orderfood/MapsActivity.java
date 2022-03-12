package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.shimeb.orderfood.MainActivity.context;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final int REQUEST_LOCATION = 22;

    int LOCATION_PERMISION_IS_ON = 0;
    int LOCATION_PERMISION_REQUESTED = 0;

int REFRASH_LOCATION_NUM=1000;
    private GoogleMap mMap;

    static int FROM_BILL = 0;
    static int FROM_LOCATION = 0;

    LocationManager locationManager;
    String provider;
    Location location;

    SupportMapFragment mapFragment;


    Toolbar toolbar;

    ProgressDialog progressDialog;

    AlertDialog dialog;
    AlertDialog.Builder builder;

    int isLocationFound = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("finding your location...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        toolbar = (Toolbar) findViewById(R.id.map_appbar);
        toolbar.setTitle("Your Location");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        builder = new AlertDialog.Builder(this);


        //////////////check permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            progressDialog.dismiss();
            return;

        } else if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
            progressDialog.dismiss();
            return;
        } else {
            set_location_propertes();
        }
       }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        Location myLoc = mMap.getMyLocation();
        if (myLoc != null) {
            LOCATION_PERMISION_IS_ON = 1;
            isLocationFound = 1;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (FROM_BILL == 1) {
                FROM_BILL = 0;
                startActivity(new Intent(getApplicationContext(), Bill.class));
            } else if (FROM_LOCATION == 1) {
                FROM_LOCATION = 0;
                startActivity(new Intent(getApplicationContext(), FindingUserLocation.class));
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if (LOCATION_PERMISION_REQUESTED == 1) {
            //Toast.makeText(getApplicationContext(), "resume", Toast.LENGTH_SHORT).show();
            LOCATION_PERMISION_REQUESTED = 0;
            set_location_propertes();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (LOCATION_PERMISION_IS_ON == 1) {
        }
    }

    @Override
    public void onLocationChanged(final Location location) {

        if (location != null) {

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
Message.message("provider dissabled");
        LocationManager lm = (LocationManager) context.getSystemService(this.LOCATION_SERVICE);
        boolean network_enabled = false;
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!network_enabled) {
            checkLocationEnabled();
        }
    }


    ////////////////checkLocationEnabled

    private void checkLocationEnabled() {


        builder.setMessage("to get your location,Turn on the location ?")
                .setTitle("Location");
        builder.setPositiveButton("location setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                LOCATION_PERMISION_REQUESTED = 1;
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);


            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                refrash_location();
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    ////////////////refresh location

    private void refrash_location() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Location is not found.")
                .setTitle("Location");
        builder.setPositiveButton("Refrash", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                set_location_propertes();
                REFRASH_LOCATION_NUM=1000;
            }
        }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    ///////////////////set_location_properties
    private void set_location_propertes() {
progressDialog.show();
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
                    LatLng lnglat = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(lnglat).title("your location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lnglat, 15));
                    //     mMap.addCircle(new CircleOptions().center(lnglat).radius(50));
                    LOCATION_PERMISION_IS_ON = 1;
                    isLocationFound = 1;

                    progressDialog.dismiss();


                    mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location location) {
                            mMap = googleMap;
                            mMap.clear();
                            LatLng lnglat = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(lnglat).title("your location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lnglat, 15));
                        }
                    });

                } else {

                    if (Location_is_enabled()) {
                        if (REFRASH_LOCATION_NUM>0){
                            set_location_propertes();
                            REFRASH_LOCATION_NUM--;
                        }else{
                            progressDialog.dismiss();
                        refrash_location();}
                    } else {
                        progressDialog.dismiss();
                        checkLocationEnabled();
                    }
                }
            }


        });
    }

    private boolean Location_is_enabled() {
        LocationManager lm = (LocationManager) context.getSystemService(this.LOCATION_SERVICE);
        boolean network_enabled = false;
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return network_enabled;
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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    set_location_propertes();
                } else {
                    Toast.makeText(getApplicationContext(), "location permision is not granted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                return;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        progressDialog.dismiss();
        REFRASH_LOCATION_NUM=0;}
        return false;
    }

}
