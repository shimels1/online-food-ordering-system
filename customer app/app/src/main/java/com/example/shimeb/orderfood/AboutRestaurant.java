package com.example.shimeb.orderfood;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutRestaurant extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 22;

    Toolbar toolbar;


    TextView discription;
    TextView location;
    TextView call_tv;
    TextView ratingStatus;
    TextView ratingTitle;
    TextView call_tv_icon;
    Button submit_rate;
    Button edite_rate_btn;
    LinearLayout linearLayout_ratingbar;

    String PHONE = "";
    static String LAT = "";
    static String LNG = "";
    RatingBar ratingBar;

    Float rating_num=0f;

    String user_id;

    int chack_ft=0;

    DatabaseReference databaseReferenceRestaurant;
    DatabaseReference databaseReferenceRate;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_restaurant);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("wait...");
        progressDialog.show();

        databaseReferenceRestaurant = FirebaseDatabase.getInstance().getReference("Restaurant").child(MainActivity.CURRENT_SELECTED_REST_ID);
        databaseReferenceRate = FirebaseDatabase.getInstance().getReference("Rate").child(MainActivity.CURRENT_SELECTED_REST_ID);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id= sp.getString("id", "null");

        toolbar = (Toolbar) findViewById(R.id.abt_res_appbar);
        toolbar.setTitle("" + MainActivity.CURRENT_SELECTED_REST_NAME);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ratingBar = (RatingBar) findViewById(R.id.abt_res_rateBar);
        ratingBar.setOnRatingBarChangeListener(this);

        discription = (TextView) findViewById(R.id.abt_res_abt_tv);
        location = (TextView) findViewById(R.id.abt_res_location_tv);
        call_tv = (TextView) findViewById(R.id.abt_res_phone_tv);
        ratingStatus = (TextView) findViewById(R.id.abt_res_rate_tv);
        ratingTitle = (TextView) findViewById(R.id.abt_res_rate_title);
        call_tv_icon = (TextView) findViewById(R.id.abt_res_call_icon_tv);
        submit_rate = (Button) findViewById(R.id.abt_res_submit_btn);
        edite_rate_btn = (Button) findViewById(R.id.abt_res_edit_btn);
        linearLayout_ratingbar = (LinearLayout) findViewById(R.id.abt_res_abt_ratingBar_linLayout);

        if (user_id.equals("")){
            linearLayout_ratingbar.setVisibility(View.GONE);
        }else{
            linearLayout_ratingbar.setVisibility(View.VISIBLE);
        }



        databaseReferenceRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    Restorantdata rd = dataSnapshot.getValue(Restorantdata.class);
                    discription.setText(rd.getDiscription() + rd.getDiscription() + rd.getDiscription() + rd.getDiscription());
                    PHONE = rd.getPhone();
                    LAT = rd.getLatitiud();
                    LNG = rd.getLongtiude();
                    call_tv.setText("" + rd.getPhone());

                    progressDialog.dismiss();
if (!user_id.equals("")){
                    databaseReferenceRate.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

if (dataSnapshot!=null){
                                RatingData ratingData=dataSnapshot.getValue(RatingData.class);
                            if (ratingData!=null){
                               ratingBar.setRating(Float.parseFloat(ratingData.getRateNumber()));
                                ratingTitle.setText("Rated");
                                ratingBar.setIsIndicator(true);
                                edite_rate_btn.setVisibility(View.VISIBLE);
                            }else{
                                chack_ft++;
                            }}
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });}

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity_restorant_location.class));
            }
        });



        submit_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user_id.equals(""))
                rate();
            }
        });

        edite_rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingTitle.setText("Rate Restaurant");
                ratingBar.setIsIndicator(false);
                edite_rate_btn.setVisibility(View.GONE);
            }
        });

        call_tv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

    }




    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//Message.message(ratingBar.getRating()+"");
        if (chack_ft!=0){
        ratingStatus.setVisibility(View.VISIBLE);
        submit_rate.setVisibility(View.VISIBLE);
        if (ratingBar.getRating() == 1) {
            ratingStatus.setText("Hated it");
        }else if (ratingBar.getRating() == 2){
            ratingStatus.setText("Disliked It");
        }else if (ratingBar.getRating() == 3){
            ratingStatus.setText("Its's OK");
        }else if (ratingBar.getRating() == 4){
            ratingStatus.setText("Liked it");
        }else if (ratingBar.getRating() == 5){
            ratingStatus.setText("Loved it");
        }
rating_num=ratingBar.getRating();}
chack_ft++;
    }

    private void call() {
        if (PHONE != "") {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + PHONE + ""));

            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AboutRestaurant.this,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                startActivity(callIntent);
                //    Toast.makeText(getApplicationContext(),PHONE,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "phone number not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void rate() {
        RatingData ratingData=new RatingData(user_id,rating_num+"");
        databaseReferenceRate.child(user_id).setValue(ratingData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                submit_rate.setVisibility(View.GONE);
                ratingStatus.setVisibility(View.GONE);
                edite_rate_btn.setVisibility(View.VISIBLE);
                ratingBar.setRating(rating_num);
                ratingBar.setIsIndicator(true);
                ratingTitle.setText("Rated");
                Message.message("tnx for rating");
            }
        });
    }

}
