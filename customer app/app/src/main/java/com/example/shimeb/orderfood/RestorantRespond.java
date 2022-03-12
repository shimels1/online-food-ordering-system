package com.example.shimeb.orderfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class RestorantRespond extends AppCompatActivity {

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

    DatabaseReference databaseReferenceRes_respond;

    DatabaseReference databaseReference_user;
    DatabaseReference databaseReference_requested_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restorant_respond);

        res_respond_text = (TextView) findViewById(R.id.res_respond_comformation_text);
        res_respond_image = (ImageView) findViewById(R.id.res_respond_comformation_image);
        food_priparing_text = (TextView) findViewById(R.id.res_respond_priparing_text);
        food_priparing_image = (ImageView) findViewById(R.id.res_respond_priparing_image);
        delivery_man_text = (TextView) findViewById(R.id.res_respond_driver_text);
        delivery_man_image = (ImageView) findViewById(R.id.res_respond_driver_image);
        food_ready_text = (TextView) findViewById(R.id.res_respond_arrived_text);
        food_ready_image = (ImageView) findViewById(R.id.res_respond_arrived_image);

        gridLayout=(GridLayout)findViewById(R.id.res_respond_grid_layout);
        gridLayout.setVisibility(View.VISIBLE);

        cancel_order_btn = (Button) findViewById(R.id.res_respond_cancel_request_btn);
        track_driver_btn = (Button) findViewById(R.id.res_respond_track_driver);
        final_btn_show_driver = (Button) findViewById(R.id.res_respond_final_btn);

        track_driver_btn.setOnClickListener(new View.OnClickListener() {
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


        databaseReference_user = FirebaseDatabase.getInstance().getReference("User");
        databaseReference_requested_order = FirebaseDatabase.getInstance().getReference("Requested_order");
        databaseReferenceRes_respond = FirebaseDatabase.getInstance().getReference("Request").child(MainActivity.CURRENT_SELECTED_REST_ID).child(Bill.REQUEST_ID);

        Log.i(">>>>>>>request", "main " + MainActivity.CURRENT_SELECTED_REST_ID + " bill " + Bill.REQUEST_ID);
        toolbar = (Toolbar) findViewById(R.id.res_respond_appbar);
        toolbar.setTitle("Respond waiting...");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String user_id = sp.getString("id", "null");


        databaseReferenceRes_respond.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key="";
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.i(">>>>>>>request", data.toString());
                    key = data.getKey();
                    final SentRequest sentRequest = data.getValue(SentRequest.class);

                    if (sentRequest.getRestorant_respond().equals("true")) {

                        Drawable drawable = new ColorDrawable(getResources().getColor(R.color.dark_green));
                        res_respond_text.setTextColor(getResources().getColor(R.color.dark_green));
                        res_respond_image.setVisibility(View.VISIBLE);

                        cancel_order_btn.setVisibility(View.GONE);

                        food_priparing_text.setTextColor(getResources().getColor(R.color.black_text));

                        toolbar.setTitle("Food is being prepared...");

                        if (sentRequest.getPayment().equals("Not Payed")){
                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            final String id = sp.getString("id", "").toString();

                            final Float[] cc = {0f};
                            if (!id.equals("")) {
                                final String finalKey = key;
                                databaseReference_user.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        signup_data data = dataSnapshot.getValue(signup_data.class);
                                        Float m=data.getCoupon();
                                        m=m-Bill.total_float;
                                        data.setCoupon(m);


                                        int badg=Integer.parseInt(data.getBadge());
                                        badg=badg+1;
                                        data.setBadge(badg+"");

                                        Message.log("payment withdrawer + badg added"+m+" "+badg);

                                        databaseReference_user.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                sentRequest.setPayment(Bill.total_float+"");
                                                if (!finalKey.equals("")){
                                                databaseReferenceRes_respond.child(finalKey).setValue(sentRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Message.log("payment added");
                                                    }
                                                });}else{
                                                    Message.log("key is empty payment is not add");
                                                }
                                            }
                                        });

                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            } else {
                                Message.message("something error ");
                            }
                        }else{

                        }




                    }
                    if (sentRequest.getDriver_on_the_way().equals("true")) {

                        food_priparing_text.setTextColor(getResources().getColor(R.color.dark_green));
                        food_priparing_image.setVisibility(View.VISIBLE);

                        Drawable drawable = new ColorDrawable(getResources().getColor(R.color.dark_green));
                        delivery_man_text.setTextColor(getResources().getColor(R.color.dark_green));
                        delivery_man_image.setVisibility(View.VISIBLE);
                        track_driver_btn.setVisibility(View.VISIBLE);
                        food_ready_text.setTextColor(getResources().getColor(R.color.black_text));

                        toolbar.setTitle("Driver is on the way...");
                    }
                    if (sentRequest.getFinal_destination().equals("true")) {

                        Drawable drawable = new ColorDrawable(getResources().getColor(R.color.dark_green));
                        food_ready_text.setTextColor(getResources().getColor(R.color.dark_green));
                        food_ready_image.setVisibility(View.VISIBLE);
                        track_driver_btn.setVisibility(View.GONE);
                        final_btn_show_driver.setVisibility(View.VISIBLE);
                        toolbar.setTitle("Food is arrived");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cancel_order() {

        databaseReferenceRes_respond.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "request deleted ", Toast.LENGTH_SHORT).show();
                BasketCount basketCount = new BasketCount();
                basketCount.delete_basket();
             //   Bill.SENT_REQUEST = 0;
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        final String id = sp1.getString("id", "");
        databaseReference_requested_order.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    final Requested_order_data dd = data.getValue(Requested_order_data.class);

                    //Message.log("orders" + dd.getStatus()+" "+dd.getOrder_id()+" "+dd.getUser_id());
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
        });
    }


}
