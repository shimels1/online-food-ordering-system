package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Food_full_items_view extends AppCompatActivity implements RV_Adabter_full_foods_view.Recycler_on_click {

    RecyclerView recyclerView;
    RV_Adabter_full_foods_view adabter;
    Toolbar toolbar;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    TextView basket_item_num;
    FrameLayout fab_farm_layout;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceRequest;

    String cat_id = "";
    String res_id = "";

    List<Add_food_item_data> data;

    ProgressDialog progressDialog;

    ViewGroup root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_full_items_view);



        databaseReference = FirebaseDatabase.getInstance().getReference("Food_item");
        databaseReferenceRequest = FirebaseDatabase.getInstance().getReference("Request");



        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        cat_id = sp.getString("c_id", "null");
        String name = sp.getString("c_name", "null");
        res_id = sp.getString("res_id", "null");

        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setSharedElementReenterTransition(TransitionInflater.from(this).inflateTransition(R.transition.sherd_element_transition));
        }
        root = (ViewGroup) findViewById(R.id.rv_full_view_card);

        fab = (FloatingActionButton) findViewById(R.id.full_vi_fab);
        basket_item_num = (TextView) findViewById(R.id.full_vi_basketNumTv);
        fab_farm_layout = (FrameLayout) findViewById(R.id.full_Vi_fab_frame);



        BasketCount basketCount = new BasketCount();

        if (basketCount.count_basket_item() > 0) {
            basket_item_num.setText(updat_fab() + "");
            fab_farm_layout.setVisibility(View.VISIBLE);
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(fab_farm_layout.getContext(),  R.anim.shack);
            anim.setDuration(200L);
            fab_farm_layout.startAnimation(anim);
        }
        progressDialog = new ProgressDialog(this);
        toolbar = (Toolbar) findViewById(R.id.full_v_toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_full_v_recycler_view);
        adabter = new RV_Adabter_full_foods_view(getData(), this);
        adabter.set_RV_item_onClick(this);
        recyclerView.setAdapter(adabter);
        recyclerView.setLayoutManager(layoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewBasket.class));
            }
        });



    }

    private List<Add_food_item_data> getData() {

        progressDialog.show();
        databaseReference.child(cat_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot dd : dataSnapshot.getChildren()) {
                    Add_food_item_data d = dd.getValue(Add_food_item_data.class);
                    if (d.getStatus().equals("on")){
                    data.add(d);}
                }
                progressDialog.dismiss();
                adabter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return data;
    }


    @Override
    public void on_rv_item_click(View v, int position, String btn, String quantity) {
        Add_food_item_data d = data.get(position);

        if (btn.equals("image")) {
            Intent intent = new Intent(getApplicationContext(), Food_detele_info.class);
            intent.putExtra("id", d.getId());
            intent.putExtra("name", d.getName());
            intent.putExtra("dis", d.getDiscription());
            intent.putExtra("url", d.getImageUrl());
            intent.putExtra("price", d.getPrice());
            intent.putExtra("qnt", quantity);
            intent.putExtra("res_id", res_id);
            intent.putExtra("activity", "fullView");

            ////sherd transition
            Pair[] pair = new Pair[5];
            pair[0] = new Pair<View, String>(v.findViewById(R.id.rv_full_view_card), "rv_contener_card_view");
            pair[1] = new Pair<View, String>(v.findViewById(R.id.rv_full_v_image), "image");
            pair[2] = new Pair<View, String>(v.findViewById(R.id.rv_full_v_name), "resturant_name");
            pair[3] = new Pair<View, String>(v.findViewById(R.id.rv_full_v_price), "delivery_time_title");
            pair[4] = new Pair<View, String>(v.findViewById(R.id.rv_full_v_pm_cv), "cvt");
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair);
            startActivity(intent,activityOptionsCompat.toBundle());
        }
        if (btn.equals("plus")) {
            adabter.PLUS = 1;
            adabter.NAME = d.getName();
            adabter.notifyDataSetChanged();
        }
        if (btn.equals("minus")) {
            adabter.MINUS = 1;
            adabter.NAME = d.getName();
            adabter.notifyDataSetChanged();
        }
        if (btn.equals("addToBasket")) {
          /*  SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
            sp.edit().putString("fname",d.getName()).apply();
            sp.edit().putString("fprice",d.getPrice()).apply();
            sp.edit().putString("fid",d.getId()).apply();
            sp.edit().putString("res_id",res_id).apply();
            NUMBER_OF_ITEMA_IN_THE_BASKET++;*/

          String id=databaseReferenceRequest.push().getKey().toString();
            BaskateData baskateData = new BaskateData(id,d.getId(), d.getName(), d.getPrice(), quantity.toString(), d.getDiscription().toString(), d.getImageUrl(),"null", res_id);

            BasketCount basketCount = new BasketCount();
            basketCount.add_to_basket(baskateData);
            fab_farm_layout.setVisibility(View.VISIBLE);
            basket_item_num.setText(updat_fab() + "");


            ////////////fab animation
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(fab_farm_layout.getContext(),  R.anim.shack);
            anim.setDuration(200L);
            fab_farm_layout.startAnimation(anim);

        }
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
            fab_farm_layout.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }
}
