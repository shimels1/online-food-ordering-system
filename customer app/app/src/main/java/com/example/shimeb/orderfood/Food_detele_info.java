package com.example.shimeb.orderfood;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Food_detele_info extends AppCompatActivity {


    Toolbar toolbar;

    ImageView image;
    TextView name;
    TextView price;
    TextView quantity;
    TextView discription;
    EditText speshal_request;
    ImageButton pluse;
    ImageButton minus;
    Button addToBasket;
    // FloatingActionButton fab;
    //   TextView basket_item_num;
    // FrameLayout fab_fram_layout;

    String id;
    String n;
    String p;
    String res_id;
    String imageUrl;
    String activity;

    int ACTIVITY = 0;
    //0 for native
    //1 for basket bill

    DatabaseReference databaseReferenceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detele_info);

        databaseReferenceRequest = FirebaseDatabase.getInstance().getReference("Request");


        if (Build.VERSION.SDK_INT > 21) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.sherd_element_transition));
        }

        toolbar = (Toolbar) findViewById(R.id.detail_item_appbar);
        toolbar.setTitle("Detail Item View");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        image = (ImageView) findViewById(R.id.food_detail_image);
        name = (TextView) findViewById(R.id.food_detail_name);
        quantity = (TextView) findViewById(R.id.food_detail_quantityTV);
        price = (TextView) findViewById(R.id.food_detail_price);
        discription = (TextView) findViewById(R.id.food_detail_discription);
        speshal_request = (EditText) findViewById(R.id.food_detail_spesial_request);

        pluse = (ImageButton) findViewById(R.id.food_detail_plus);
        minus = (ImageButton) findViewById(R.id.food_detail_minus);
        addToBasket = (Button) findViewById(R.id.food_detail_AddToBasketBtn);

        BasketCount basketCount = new BasketCount();
        if (basketCount.count_basket_item() > 0) {
            updat_fab();
        }

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("url");
        n = intent.getStringExtra("name");
        p = intent.getStringExtra("price");
        String disc = intent.getStringExtra("dis");
        String qnt = intent.getStringExtra("qnt");
        String spr = intent.getStringExtra("spr");
        res_id = intent.getStringExtra("res_id");
        activity = intent.getStringExtra("activity");

        if (activity.equals("basket")) {

            addToBasket.setText("Update Basket item");
            ACTIVITY = 1;
        }


        quantity.setText(qnt);


        speshal_request.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    String ch = get_peshal_request(n);
                    if (ch.equals("")) {
                        speshal_request.setText("");
                    }
                }
            }
        });

        if (!get_peshal_request(n).equals("")) {
            speshal_request.setText(get_peshal_request(n));
        }

        name.setText(n);
        price.setText(p + " Birr");

        discription.setText(disc);
        Picasso.with(MainActivity.context).load(imageUrl).into(image);


        pluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(quantity.getText().toString()) + 1;
                quantity.setText(q+ "");
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int q = Integer.parseInt(quantity.getText().toString()) - 1;
                if (q != 0) {
                    quantity.setText(q + "");
                }
            }
        });

        addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivity.OPEN_CLOTH == 1) {
                    if (MainActivity.HAVE_ORDER!=1){
                    if (addToBasket.getText().equals("Update Basket item")) {

                        update_item(n);
                    } else {
                        String dis = discription.getText().toString();
                        if (dis.equals("")) {
                            dis = "null";
                        }
                        String spr = speshal_request.getText().toString();
                        if (spr.equals("")) {
                            spr = "null";
                        }else if (spr.equals("type hear for speshal request... ")){
                            spr = "null";
                        }

                        String i = databaseReferenceRequest.push().getKey().toString();

                        BaskateData baskateData = new BaskateData(i, id, n, p, (String) quantity.getText(), dis, imageUrl, spr, res_id);

                        BasketCount basketCount = new BasketCount();
                        basketCount.add_to_basket(baskateData);

                        startActivity(new Intent(getApplicationContext(), Food_full_items_view.class));

                    }}else{
                        Snackbar.make(findViewById(android.R.id.content), "Sorry you cannot order if you have unfinishd order", Snackbar.LENGTH_LONG)
                                .setActionTextColor(MainActivity.context.getResources().getColor(R.color.red))
                                .show();
                    }
                } else if (MainActivity.OPEN_CLOTH == 0) {
                    Snackbar.make(findViewById(android.R.id.content), "Sorry we are cloth", Snackbar.LENGTH_LONG)
                            .setActionTextColor(MainActivity.context.getResources().getColor(R.color.red))
                            .show();
                } else {
                    Message.message("something error");
                }
            }
        });
    }

    private String get_peshal_request(String name) {
        String sp = "";
        BasketCount bc = new BasketCount();
        List<BaskateData> data = bc.get_basket();
        for (int i = 0; i < data.size(); i++) {
            BaskateData bb = data.get(i);
            if (name.equals(bb.getName())) {
                sp = bb.getSpeshalRequest();
                quantity.setText(bb.getTotal_number());
                addToBasket.setText("Update Basket item");
            }
        }
        return sp;
    }

    private void update_item(String name) {

        BasketCount bc = new BasketCount();
        List<BaskateData> data = bc.get_basket();
        for (int i = 0; i < data.size(); i++) {
            BaskateData bb = data.get(i);
            if (name.equals(bb.getName())) {
                //update the data file
                bb.setTotal_number(quantity.getText().toString());
                bb.setDiscription(discription.getText().toString());
                bb.setSpeshalRequest(speshal_request.getText().toString());

            }
        }
        if (ACTIVITY == 1) {
            startActivity(new Intent(getApplicationContext(), ViewBasket.class));
        } else {
            startActivity(new Intent(getApplicationContext(), Food_full_items_view.class));
        }
    }


    void updat_fab() {
        BasketCount bc = new BasketCount();
        List<BaskateData> data = bc.get_basket();

        int c = 0;

        for (BaskateData d : data) {

            if (name.equals(d.getName())) {
                c = c + Integer.parseInt(d.getTotal_number());
            }
            //     basket_item_num.setText(c + "");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //   Toast.makeText(getApplicationContext(), "in " , Toast.LENGTH_SHORT).show();

        if (item.getItemId() == android.R.id.home) {
            if (activity.equals("basket")) {
                startActivity(new Intent(getApplicationContext(), ViewBasket.class));
            } else {
                startActivity(new Intent(getApplicationContext(), Food_full_items_view.class));
            }

        }
        return super.onOptionsItemSelected(item);
    }


}
