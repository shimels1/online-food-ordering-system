package com.example.shimeb.orderfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.List;
import java.util.Random;

public class Bill extends AppCompatActivity {


    ListView listView;
    lv_adabter_basket_item adabter;
    Toolbar toolbar;
    TextView subtotal;
    TextView total;
    TextView add_more;
    TextView current_balance_title;
    TextView current_balance;
    TextView delivery_fee;
    TextView service_fee;
    TextView final_ballance_title;
    TextView final_ballance;
    TextView LFS_tv;
    Button send_to_rest;
    Button checkout;

    String REST_LAT="";
    String REST_LNG="";

    boolean money;
    static Float total_float;
    String user_id = "";
    Float currentBallance = 0f;


    DatabaseReference test;
    DatabaseReference databaseReferenceRequest;
    DatabaseReference databaseReferenceTime;

    DatabaseReference databaseReference_restaurant;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_requested_order;
    static String REQUEST_ID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String user_id = sp.getString("id", "null");
        databaseReference_requested_order = FirebaseDatabase.getInstance().getReference("Requested_order");
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReferenceRequest = FirebaseDatabase.getInstance().getReference("Request").child(MainActivity.CURRENT_SELECTED_REST_ID).child(user_id);
        databaseReferenceTime = FirebaseDatabase.getInstance().getReference("Time");
        test = FirebaseDatabase.getInstance().getReference("hello");

        databaseReference_restaurant = FirebaseDatabase.getInstance().getReference("Restaurant").child(MainActivity.CURRENT_SELECTED_REST_ID);

        listView = (ListView) findViewById(R.id.bill_lv);

        View header = getLayoutInflater().inflate(R.layout.basket_lv_header, null);
        View footer = getLayoutInflater().inflate(R.layout.basket_lv_footer, null);

        listView.addHeaderView(header);
        listView.addFooterView(footer);

        LFS_tv = (TextView) findViewById(R.id.basket_h_LFS_tv);
        send_to_rest = (Button) findViewById(R.id.basket_send_to_rs_btn);


        subtotal = (TextView) findViewById(R.id.basket_subtotal_tv);
        total = (TextView) findViewById(R.id.basket_total_tv);
        add_more = (TextView) findViewById(R.id.basket_add_more);
        delivery_fee = (TextView) findViewById(R.id.basket_delivery_fee_tv);
        current_balance = (TextView) findViewById(R.id.basket_current_balance_tv);
        service_fee = (TextView) findViewById(R.id.basket_service_fee_tv);
        current_balance_title = (TextView) findViewById(R.id.basket_current_balance_title);
        final_ballance = (TextView) findViewById(R.id.basket_final_balance);
        final_ballance_title = (TextView) findViewById(R.id.basket_final_balance_title);
        checkout = (Button) findViewById(R.id.basket_checkout_btn);

        toolbar = (Toolbar) findViewById(R.id.bill_app_bar);
        toolbar.setTitle("Making bill");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        databaseReference_restaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot!=null){
                    Restorantdata restorantdata=dataSnapshot.getValue(Restorantdata.class);
                    if (restorantdata!=null){

                        double lat = Double.parseDouble(  Maps_Chack_User_Location.LATITUEDE);
                        double lng = Double.parseDouble( Maps_Chack_User_Location.LONGTIUDE);

                        //////////distance
                        Location loc1 = new Location("");
                        loc1.setLatitude(lat);
                        loc1.setLongitude(lng);

                        Location loc22 = new Location("");
                        loc22.setLatitude(Double.parseDouble(restorantdata.getLatitiud()));
                        loc22.setLongitude(Double.parseDouble(restorantdata.getLongtiude()));

                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setMaximumFractionDigits(1);

                        final String mile = nf.format(loc22.distanceTo(loc1) / 1000) + "";

                        delivery_fee.setText(8*Float.parseFloat(mile)+"("+mile+" Km) Birr");

                        subtotal.setText(subtotal() + " Birr");
                        Float serviceFee=(subtotal()*5)/100;


                        Message.log(subtotal()+" "+serviceFee+" "+(8*Float.parseFloat(mile))+" "+(subtotal()+serviceFee+(8*Float.parseFloat(mile))));

                        total_float = subtotal()+serviceFee+(8*Float.parseFloat(mile));
                        total.setText(total_float + " Birr");
                        service_fee.setText(serviceFee+" Birr");

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        set_send_btn_visible();

        adabter = new lv_adabter_basket_item(this, getData());
        listView.setAdapter(adabter);



        Float cb = current_ballance();

        current_balance_title.setVisibility(View.VISIBLE);
        current_balance.setVisibility(View.VISIBLE);
        final_ballance.setVisibility(View.VISIBLE);
        final_ballance_title.setVisibility(View.VISIBLE);


        send_to_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Toast.makeText(getApplicationContext(),"go "+ k,Toast.LENGTH_SHORT).show();

                send_to_rest.setEnabled(false);
                send_to_rest.setTextColor(MainActivity.context.getResources().getColor(R.color.gray));
                send_to_rest.setTextColor(MainActivity.context.getResources().getColor(R.color.black));
                send_to_rest.setText("Sending...");

                BaskateData baskateData = new BaskateData();

                final BasketCount basketCount = new BasketCount();


                String timeId = databaseReferenceTime.push().getKey();

                final Time time = new Time(timeId, baskateData.getUserId(), "02:00");
                test.child("t").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Message.log("done");
                        test.child("t").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Long time = (Long) dataSnapshot.getValue();


                                Random random=new Random();

                                int num1=random.nextInt(9);
                                int num2=random.nextInt(9);
                                int num3=random.nextInt(9);
                                int num4=random.nextInt(9);

                                final String orderId=""+num1+num2+num3+num4;

                                Message.log(""+num4+num3+num2+num1);

                                final SentRequest sentRequest = new
                                        SentRequest(basketCount.get_basket(),
                                        "false", "false",
                                        "false", "false",
                                        time + "",
                                        Maps_Chack_User_Location.LATITUEDE,
                                        Maps_Chack_User_Location.LONGTIUDE,
                                        "01",
                                        "01",
                                        "Not Payd",
                                        orderId,
                                        "false");

                                    databaseReferenceRequest.setValue(sentRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            BasketCount basketCount = new BasketCount();
                                            basketCount.delete_basket();
                                          Message.log("request sent by me");
                                            startActivity(new Intent(getApplicationContext(), Requested_order.class));

                                               Requested_order_data requested_order_data=
                                                       new Requested_order_data(user_id,
                                                               MainActivity.CURRENT_SELECTED_REST_ID,
                                                               orderId,
                                                               "1");


                                            final String idd = databaseReference_requested_order.push().getKey().toString();
                                               databaseReference_requested_order.child(user_id).setValue(requested_order_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void aVoid) {
                                                   }
                                               });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

            }
        });
    }

    private List<BaskateData> getData() {
        BasketCount basketCount = new BasketCount();
        return basketCount.get_basket();
    }

    Float subtotal() {
        BasketCount basketCount = new BasketCount();
        Float total = 0.0f;
        for (BaskateData d : basketCount.get_basket()) {
            Float item = Float.parseFloat(d.getTotal_number()) * Float.parseFloat(d.getPrice());
            total = total + item;
        }
        return total;
    }

    void set_send_btn_visible() {
        send_to_rest.setVisibility(View.VISIBLE);
        LFS_tv.setVisibility(View.VISIBLE);
        add_more.setVisibility(View.GONE);
        checkout.setVisibility(View.GONE);
    }

    private Float current_ballance() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("id", "").toString();

        final Float[] cc = {0f};
        if (!id.equals("")) {
            databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    signup_data data = dataSnapshot.getValue(signup_data.class);

                    Float cb = data.getCoupon();
                    current_balance.setText(cb + " Birr");
                    Float fb = cb - total_float;
                    final_ballance.setText(fb + " Birr");

                    user_id = data.getPhone();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } else {
            Message.message("");
        }

        return cc[0];
    }

}
