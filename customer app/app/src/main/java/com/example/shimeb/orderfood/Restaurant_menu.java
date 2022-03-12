package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Restaurant_menu extends AppCompatActivity implements RV_Adabter_menu_cat.Recycler_on_click {


    Toolbar toolbar;

    ImageView image;
    TextView title;
    TextView name;
    TextView about_rest;
    RatingBar rating;
    CardView cardView;

    RecyclerView recyclerView;
    RV_Adabter_menu_cat adabter;
    RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReferenceRate;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_title;


    List<add_food_add_catagory> data;

    FloatingActionButton fab;
    TextView basket_item_num;
    FrameLayout fab_fram_layout;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        databaseReferenceRate = FirebaseDatabase.getInstance().getReference("Rate").child(MainActivity.CURRENT_SELECTED_REST_ID);

        databaseReference_title = FirebaseDatabase.getInstance().getReference("Restaurant");

        image = (ImageView) findViewById(R.id.menu_image_view);
        rating = (RatingBar) findViewById(R.id.menu_rating_bar);
        name = (TextView) findViewById(R.id.menu_name);
        title = (TextView) findViewById(R.id.menu_title);
        about_rest = (TextView) findViewById(R.id.rm_more_about_rest);
        cardView = (CardView) findViewById(R.id.menu_card_view);

        fab = (FloatingActionButton) findViewById(R.id.menu_fab);
        basket_item_num = (TextView) findViewById(R.id.menu_basketNumTv);
        fab_fram_layout = (FrameLayout) findViewById(R.id.menu_fab_frame);

        BasketCount basketCount = new BasketCount();
        if (basketCount.count_basket_item() > 0) {
            basket_item_num.setText(updat_fab() + "");
            fab_fram_layout.setVisibility(View.VISIBLE);
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(fab_fram_layout.getContext(), R.anim.shack);
            anim.setDuration(200L);
            fab_fram_layout.startAnimation(anim);
        }

        String n = MainActivity.CURRENT_SELECTED_REST_NAME, t = "", r = "", url = "";

        databaseReference_title.child(MainActivity.CURRENT_SELECTED_REST_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restorantdata d = dataSnapshot.getValue(Restorantdata.class);
                Message.log(dataSnapshot.toString());
                if (d != null) {
                    String n = d.getName();
                    String t = d.getTitle();
                    String r = d.getRating();
                    String url = d.getImageUrl();
                    Picasso.with(getApplicationContext()).load(url).into(image);
                    name.setText(n);
                    Float rr = Float.valueOf(r);
                    title.setText(t);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReferenceRate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
Float r_value=0f;
int r_divider=0;

                for (DataSnapshot data:dataSnapshot.getChildren()){
                    RatingData ratingData=data.getValue(RatingData.class);
                    if (ratingData!=null){
                        r_value=r_value+Float.parseFloat(ratingData.getRateNumber());
                        r_divider++;
                    }
                }
                r_value=r_value/r_divider;
                rating.setRating(r_value);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        toolbar = (Toolbar) findViewById(R.id.menu_app_bar);
        toolbar.setTitle(n + " menu");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.sherd_element_transition));
        }

        ///////////recycler

        databaseReference = FirebaseDatabase.getInstance().getReference("Food_catagory").child(n);
        data = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.rm_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adabter = new RV_Adabter_menu_cat(getData(), this);
        adabter.set_RV_item_onClick(this);
        recyclerView.setAdapter(adabter);
        recyclerView.setLayoutManager(layoutManager);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewBasket.class));
            }
        });


        about_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutRestaurant.class));
            }
        });


    }

    List<add_food_add_catagory> getData() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (int i = 0; i < 2; i++) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        add_food_add_catagory dd = d.getValue(add_food_add_catagory.class);
                        data.add(dd);
                    }
                }
                adabter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return data;
    }

    @Override
    public void on_rv_item_click(View view, int position) {
        add_food_add_catagory d = data.get(position);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putString("c_id", d.getId()).apply();
        sp.edit().putString("c_name", d.getCat_name()).apply();
        sp.edit().putString("res_id", MainActivity.CURRENT_SELECTED_REST_ID).apply();

        Intent intent = new Intent(getApplicationContext(), Food_full_items_view.class);
        startActivity(intent);
    }


    int updat_fab() {
        BasketCount bc = new BasketCount();
        List<BaskateData> data = bc.get_basket();

        int c = 0;

        for (BaskateData d : data) {
            c = c + Integer.parseInt(d.getTotal_number());
        }

        return c;
    }

    @Override
    protected void onResume() {
        BasketCount basketCount = new BasketCount();
        if (basketCount.count_basket_item() > 0) {
            basket_item_num.setText(updat_fab() + "");
            fab_fram_layout.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (android.R.id.home == item.getItemId()) {
            //    Toast.makeText(getApplicationContext(),"back pressed",Toast.LENGTH_SHORT).show();
            BasketCount basketCount = new BasketCount();
            basketCount.delete_basket();
        }


        return super.onOptionsItemSelected(item);
    }

    private void show_dialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter METHODS to set the dialog characteristics
        builder.setMessage("Are you sure to go out from the restorant?" +
                " the selected item gone to be removed")
                .setTitle("selected item removed");

        // Set the action buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Account.FROME_VIEW_BASKET = 1;
                startActivity(intent);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        return false;
    }
}
