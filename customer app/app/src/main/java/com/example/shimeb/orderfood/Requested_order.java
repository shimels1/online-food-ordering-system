package com.example.shimeb.orderfood;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Requested_order extends AppCompatActivity {


     public static String  RESTAURANT_ID="";

    Toolbar toolbar;
    TextView res_respond_text;
    ImageView res_respond_image;
    TextView food_priparing_text;
    ImageView food_priparing_image;
    TextView delivery_man_text;
    ImageView delivery_man_image;
    TextView food_ready_text;
    ImageView food_ready_image;
    Button cancel_order_btn;
    Button track_driver_btn;
    Button final_btn_show_driver;

    GridLayout gridLayout;
    TextView order_ststus;
    TextView login_libk;
    ProgressDialog progressDialog;

    DatabaseReference databaseReferenceRes_respond;

    DatabaseReference databaseReference_user;
    DatabaseReference databaseReference_requested_order;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restorant_respond);


        databaseReference_requested_order = FirebaseDatabase.getInstance().getReference("Requested_order");
        databaseReference_user = FirebaseDatabase.getInstance().getReference("User");


        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Finding Your Request...");


        progressDialog.show();


        res_respond_text = (TextView) findViewById(R.id.res_respond_comformation_text);
        res_respond_image = (ImageView) findViewById(R.id.res_respond_comformation_image);
        food_priparing_text = (TextView) findViewById(R.id.res_respond_priparing_text);
        food_priparing_image = (ImageView) findViewById(R.id.res_respond_priparing_image);
        delivery_man_text = (TextView) findViewById(R.id.res_respond_driver_text);
        delivery_man_image = (ImageView) findViewById(R.id.res_respond_driver_image);
        food_ready_text = (TextView) findViewById(R.id.res_respond_arrived_text);
        food_ready_image = (ImageView) findViewById(R.id.res_respond_arrived_image);

        gridLayout = (GridLayout) findViewById(R.id.res_respond_grid_layout);
        order_ststus = (TextView) findViewById(R.id.res_respond_order_status);
        login_libk = (TextView) findViewById(R.id.res_respond_login_link);


        cancel_order_btn = (Button) findViewById(R.id.res_respond_cancel_request_btn);
        track_driver_btn = (Button) findViewById(R.id.res_respond_track_driver);
        final_btn_show_driver = (Button) findViewById(R.id.res_respond_final_btn);


        toolbar = (Toolbar) findViewById(R.id.res_respond_appbar);
        toolbar.setTitle("Your Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        track_driver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Maps_Track_Driver.class));
            }
        });final_btn_show_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Maps_Track_Driver.class));
            }
        });

        cancel_order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_order();
            }
        });


        chack_order();


    }

    private void cancel_order() {


        SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        final String id = sp1.getString("id", "");
        databaseReference_requested_order.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Message.log("request deleted 2");
                databaseReferenceRes_respond.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "request deleted ", Toast.LENGTH_SHORT).show();
                        BasketCount basketCount = new BasketCount();
                        basketCount.delete_basket();
                        // Bill.SENT_REQUEST = 0;
                        Message.log("request deleted");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
            }
        });

     /*   databaseReference_requested_order.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final Requested_order_data dd = data.getValue(Requested_order_data.class);

                    Message.log("orders" + dd.getStatus()+" "+dd.getOrder_id()+" "+dd.getUser_id());
                    if (dd.getUser_id().equals(id) && dd.getStatus().equals("1")) {
                        String key = data.getKey();
                        databaseReference_requested_order.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Message.log("removed");
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


    }

    public void chack_order() {
        final boolean[] ch = {false};

        databaseReference_requested_order.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
                String id = sp1.getString("id", "");
                if (!id.equals("")) {


                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Requested_order_data dd = data.getValue(Requested_order_data.class);


                        if (dd.getStatus().equals("1") && id.equals(dd.getUser_id())) {

RESTAURANT_ID=dd.getRestaurant_id();
                            ch[0] = true;
                            databaseReferenceRes_respond = FirebaseDatabase.getInstance().
                                    getReference("Request").child(dd.getRestaurant_id()).child(dd.getUser_id());
                            MainActivity.HAVE_ORDER = 1;

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            final String user_id = sp.getString("id", "null");




                            databaseReferenceRes_respond.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String key = "";
                                    //  for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    Message.log("datasnapshat " + dataSnapshot.toString());

                                    final SentRequest sentRequest = dataSnapshot.getValue(SentRequest.class);
                                    if (sentRequest != null) {

                                        setTitleForTitleBar(sentRequest.getOrderId());
                                        progressDialog.dismiss();
                                        gridLayout.setVisibility(View.VISIBLE);
                                        order_ststus.setVisibility(View.INVISIBLE);
                                        login_libk.setVisibility(View.INVISIBLE);
                                        key = dataSnapshot.getKey();


                                        Message.log(key + " " + sentRequest.getOrderId() + " " + sentRequest.getRestorant_respond() + " " + sentRequest.getRestorant_respond().equals("true"));
                                        if (!sentRequest.getRestorant_respond().equals("false")) {

                                            Drawable drawable = new ColorDrawable(getResources().getColor(R.color.dark_green));
                                            res_respond_text.setTextColor(getResources().getColor(R.color.dark_green));
                                            res_respond_image.setVisibility(View.VISIBLE);

                                            cancel_order_btn.setVisibility(View.GONE);

                                            food_priparing_text.setTextColor(getResources().getColor(R.color.black_text));

                                        }
                                        if (sentRequest.getDriver_on_the_way().equals("true")) {

                                            food_priparing_text.setTextColor(getResources().getColor(R.color.dark_green));
                                            food_priparing_image.setVisibility(View.VISIBLE);

                                            Drawable drawable = new ColorDrawable(getResources().getColor(R.color.dark_green));
                                            delivery_man_text.setTextColor(getResources().getColor(R.color.dark_green));
                                            delivery_man_image.setVisibility(View.VISIBLE);
                                            track_driver_btn.setVisibility(View.VISIBLE);
                                            food_ready_text.setTextColor(getResources().getColor(R.color.black_text));

                                        }
                                        if (sentRequest.getFinal_destination().equals("true")) {

                                            Drawable drawable = new ColorDrawable(getResources().getColor(R.color.dark_green));
                                            food_ready_text.setTextColor(getResources().getColor(R.color.dark_green));
                                            food_ready_image.setVisibility(View.VISIBLE);
                                            track_driver_btn.setVisibility(View.GONE);
                                            final_btn_show_driver.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        Message.log("datasnapshat is null");
                                    }
                                    //   }
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    }
                    if (ch[0] == false) {
                        gridLayout.setVisibility(View.INVISIBLE);
                        order_ststus.setVisibility(View.VISIBLE);
                        login_libk.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();
                        MainActivity.HAVE_ORDER = 0;
                        order_ststus.setText("There is no order.");
                    }
                } else {
                    gridLayout.setVisibility(View.INVISIBLE);
                    order_ststus.setVisibility(View.VISIBLE);
                    login_libk.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    order_ststus.setText("Login first to see your order.");
                    MainActivity.FROM_REQUESTED_ORER = 1;
                    login_libk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), login.class));
                        }
                    });

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


    private void setTitleForTitleBar(String order_id) {
        toolbar.setTitle("Order id #" + order_id);
    }
}
