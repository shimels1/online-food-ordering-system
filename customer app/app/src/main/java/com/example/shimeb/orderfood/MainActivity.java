package com.example.shimeb.orderfood;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RV_Adabter.Recycler_Item_Click {


    static int OPEN_CLOTH = 0;
    static int HAVE_ORDER = 0;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager rv_layoutManager;

    RV_Adabter rv_adabter;

    List<Restorantdata> data;
    List<RatingViewData> ratingData;

    DatabaseReference databaseReference;

    DatabaseReference s;

    static int FROM_REQUESTED_ORER = 0;

    public static Context context;

    ProgressDialog progressDialog;

    ViewGroup root;

    public static String CURRENT_SELECTED_REST_ID = "";
    public static String CURRENT_SELECTED_REST_NAME = "";

    LayoutInflater layoutInflater;

    DatabaseReference test;
    DatabaseReference databaseReferenceRate;
    DatabaseReference databaseReference_requested_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //  startActivity(new Intent(getApplicationContext(),RestorantRespond.class));

        layoutInflater = LayoutInflater.from(this);

        context = getApplicationContext();
        progressDialog = new ProgressDialog(MainActivity.this);
        // progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Finding restaurant...");

        test = FirebaseDatabase.getInstance().getReference("hello");
        databaseReference_requested_order = FirebaseDatabase.getInstance().getReference("Requested_order");
        databaseReferenceRate = FirebaseDatabase.getInstance().getReference("Rate");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        databaseReference = FirebaseDatabase.getInstance().getReference("Restaurant");

        navigationView.setNavigationItemSelectedListener(this);

        data = new ArrayList<>();
        ratingData = new ArrayList<RatingViewData>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        rv_layoutManager = new LinearLayoutManager(this);
        getRatingData();
        rv_adabter = new RV_Adabter(getData(), getRatingData(), this);
        rv_adabter.setRecycler_item_click(this);
        recyclerView.setAdapter(rv_adabter);
        recyclerView.setLayoutManager(rv_layoutManager);

        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setSharedElementReenterTransition(TransitionInflater.from(this).inflateTransition(R.transition.sherd_element_transition));
        }
        root = (ViewGroup) findViewById(R.id.recycler_view);

        View v = navigationView.getHeaderView(0);
        TextView textView = (TextView) v.findViewById(R.id.heder_menu_user_name);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sp.getString("name", "anonymous user").toString() + "";
        // us.setText(name+"");

        if (name.equals("")) {

            textView.setText("Hello gust user");
        } else {
            textView.setText("Hello " + name);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "your order is...", Toast.LENGTH_LONG).show();
            }
        });

        chack_order();


    }

    private List<Restorantdata> getData() {

        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    Restorantdata dd = d.getValue(Restorantdata.class);
if (dd.getStatus().equals("on")){
                    data.add(dd);}
                }

                rv_adabter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return data;
    }

    private List<RatingViewData> getRatingData() {

        progressDialog.show();

        databaseReferenceRate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ratingData.clear();

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Float r_value = 0f;
                    int r_divider = 0;
                    for (DataSnapshot data : d.getChildren()) {
                        Message.log(data.toString());
                        if (data!=null){
                        RatingData rd = data.getValue(RatingData.class);
                        if (rd != null) {
                            r_value = r_value + Float.parseFloat(rd.getRateNumber());
                            r_divider++;
                        }}
                    }
                    r_value = r_value / r_divider;

                    RatingViewData rd=new RatingViewData(d.getKey(),r_value+"",r_divider);
                    ratingData.add(rd);
                }
                rv_adabter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return ratingData;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        actionBarDrawerToggle.onConfigurationChanged(newConfig);

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.your_location_menu) {
            drawerLayout.closeDrawer(GravityCompat.START);

            startActivity(new Intent(getApplicationContext(), MapsActivity_All_restorant_location.class));
        }
        if (item.getItemId() == R.id.orders_menu) {
            drawerLayout.closeDrawer(GravityCompat.START);

           /* if (Bill.SENT_REQUEST==0){
            Toast.makeText(getApplicationContext(),"There is no oreder",Toast.LENGTH_SHORT).show();
            }else{
                startActivity(new Intent(getApplicationContext(), RestorantRespond.class));
            }*/
            startActivity(new Intent(getApplicationContext(), Requested_order.class));
        }
        if (item.getItemId() == R.id.account_menu) {
            drawerLayout.closeDrawer(GravityCompat.START);

            Intent intent = new Intent(getApplicationContext(), Account.class);
            Account.FROME_HOME = 1;
            startActivity(intent);
        }if (item.getItemId() == R.id.nav_menu_help) {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), help.class);
            startActivity(intent);
        }if (item.getItemId() == R.id.nav_menu_transfer_balance) {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(getApplicationContext(), TransferBalance.class);
            startActivity(intent);
        }if (item.getItemId() == R.id.exit) {
            drawerLayout.closeDrawer(GravityCompat.START);
            exit();
        }
        return false;
    }

    @Override

    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRecyclerItemClick(View v, int position) {

        Restorantdata d = data.get(position);


        Log.i("retorant is : ", d.getId());

        Intent intent = new Intent(getApplicationContext(), Restaurant_menu.class);
        intent.putExtra("name", d.getName());
        intent.putExtra("title", d.getTitle());
        intent.putExtra("reting", d.getRating());
        intent.putExtra("url", d.getImageUrl());
        //  Explode fede = new Explode();
        //fede.setDuration(10000);
        //TransitionManager.beginDelayedTransition(root);

        Pair[] pair = new Pair[5];
        pair[0] = new Pair<View, String>(v.findViewById(R.id.rv_item_image), "image");
        pair[1] = new Pair<View, String>(v.findViewById(R.id.rv_contener_card_view), "rv_contener_card_view");
        pair[2] = new Pair<View, String>(v.findViewById(R.id.rv_item_name), "resturant_name");
        pair[3] = new Pair<View, String>(v.findViewById(R.id.rv_item_title), "title");
        pair[4] = new Pair<View, String>(v.findViewById(R.id.rv_item_rating), "rating_bar");
        // pair[5]=new Pair<View,String>(v.findViewById(R.id.rv_item_dlv_Time),"fullView6");
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair);

        CURRENT_SELECTED_REST_ID = d.getId();
        CURRENT_SELECTED_REST_NAME = d.getName();

        startActivity(intent, activityOptionsCompat.toBundle());
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
                            ch[0] = true;
                            MainActivity.HAVE_ORDER = 1;
                        }
                    }
                    if (ch[0] == false) {
                        MainActivity.HAVE_ORDER = 0;
                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            exit();

        return false;
        // Disable back button..............
    }




    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setMessage("Are you sure you want to exit??")
                .setTitle("Exit!");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
