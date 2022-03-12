package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ViewBasket extends AppCompatActivity {

    ListView listView;
    lv_adabter_basket_item adabter;
    Toolbar toolbar;
    TextView subtotal;
    TextView subtotal_title;
    TextView total;
    TextView total_title;
    TextView service_fee;
    TextView service_fee_title;
    TextView deleivery_fee;
    TextView deleivery_fee_title;
    TextView add_more;
    Button checkout;
    TextView edite;
    // ImageView deleteImage;


    int k = 2;

    boolean money;
    Float total_float;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference_requested_order;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_basket);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference_requested_order = FirebaseDatabase.getInstance().getReference("Requested_order");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Chacking accounts...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);


        money = is_have_enough_money();
        this.setFinishOnTouchOutside(false);

        listView = (ListView) findViewById(R.id.basket_lv);

        View header = getLayoutInflater().inflate(R.layout.basket_lv_header, null);
        View footer = getLayoutInflater().inflate(R.layout.basket_lv_footer, null);
        listView.addHeaderView(header);
        listView.addFooterView(footer);

        subtotal = (TextView) findViewById(R.id.basket_subtotal_tv);
        subtotal_title = (TextView) findViewById(R.id.basket_subtotal_title);
        total = (TextView) findViewById(R.id.basket_total_tv);
        service_fee = (TextView) findViewById(R.id.basket_service_fee_tv);
        service_fee_title = (TextView) findViewById(R.id.basket_service_fee_title);
        deleivery_fee = (TextView) findViewById(R.id.basket_delivery_fee_tv);
        deleivery_fee_title = (TextView) findViewById(R.id.basket_delivery_fee_title);

        subtotal_title.setVisibility(View.GONE);
        subtotal.setVisibility(View.GONE);
        service_fee_title.setVisibility(View.GONE);
        service_fee.setVisibility(View.GONE);
        deleivery_fee_title.setVisibility(View.GONE);
        deleivery_fee.setVisibility(View.GONE);


        add_more = (TextView) findViewById(R.id.basket_add_more);
        checkout = (Button) findViewById(R.id.basket_checkout_btn);
        edite = (TextView) findViewById(R.id.basket_header_edit);
        // deleteImage=(ImageView)findViewById(R.id.lv_basket_delete_image);

        toolbar = (Toolbar) findViewById(R.id.basket_app_bar);
        toolbar.setTitle("Basket Bill");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adabter = new lv_adabter_basket_item(this, getData());
        listView.setAdapter(adabter);

        Float total1 = subtotal();
        subtotal.setText(total1 + " Birr");
        total_float = total1;
        total.setText(total_float + " Birr");

        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Restaurant_menu.class));
            }
        });
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_login()) {
                    checkout.setEnabled(false);
                    checkout.setTextColor(MainActivity.context.getResources().getColor(R.color.black));
                    checkout.setBackgroundColor(MainActivity.context.getResources().getColor(R.color.gray));
                    checkout.setText("Please wait...");
                   chack_order();
                } else {
                    show_sign_in_dialog();
                }
            }
        });

        listView.setLongClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BasketCount basketCount = new BasketCount();
                List<BaskateData> baskateData = basketCount.get_basket();
                BaskateData d = baskateData.get(position - 1);
                //  Toast.makeText(getApplicationContext(),d.getName(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), Food_detele_info.class);
                intent.putExtra("id", d.getId());
                intent.putExtra("name", d.getName());
                intent.putExtra("dis", d.getDiscription());
                intent.putExtra("url", d.getImageUrl());
                intent.putExtra("price", d.getPrice());
                intent.putExtra("qnt", d.getTotal_number());
                intent.putExtra("res_id", MainActivity.CURRENT_SELECTED_REST_ID);
                intent.putExtra("activity", "basket");
                startActivity(intent);
            }
        });


        ///////////edite btn on click


/*
        edite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (k%2==0){
                    deleteImage.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.context,"true",Toast.LENGTH_SHORT).show();
                }else{
                    deleteImage.setVisibility(View.GONE);
                    //listView.setClickable(true);
                    Toast.makeText(MainActivity.context,"false",Toast.LENGTH_SHORT).show();
                }
                k++;
            }
        });

*/

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //   listView.setOnItemClickListener(null);

                show_delete_item_dialog(position);

                return true;
            }
        });


    }

    private boolean is_have_enough_money() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("id", "").toString();
        if (is_login()) {
            if (!id.equals("")) {
                databaseReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        signup_data data = dataSnapshot.getValue(signup_data.class);
                        if (data.getCoupon() > total_float) {
                            money = true;
                            progressDialog.dismiss();
                        }
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            } else {
                progressDialog.show();
            }
        } else {
            progressDialog.dismiss();
            show_sign_in_dialog();
        }
        return money;
    }

    private void show_sign_in_dialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter METHODS to set the dialog characteristics
        builder.setMessage("You are not sign in,You want to sign in?")
                .setTitle("Sign in");

        // Set the action buttons
        builder.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Intent intent = new Intent(getApplicationContext(), Account.class);
                Account.FROME_VIEW_BASKET = 1;
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void show_mone_is_not_enough() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       // builder.setCanceledOnTouchOutside(false);
// 2. Chain together various setter METHODS to set the dialog characteristics
        builder.setMessage("Your coupone is not enough,You want to add coupone?")
                .setTitle("Payment method");

        // Set the action buttons
        builder.setPositiveButton("Add Coupon", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                PaymentMethod.FROM_VIEWBASKET = 1;
                Intent intent = new Intent(getApplicationContext(), PaymentMethod.class);
                startActivity(intent);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
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

    boolean is_login() {

        Boolean result = false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("id", "");
        if (!id.equals("")) {
            result = true;
        }
        return result;
    }


    ////////////////////delete item dialog

    private void show_delete_item_dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete?")
                .setTitle("Delete item");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                BasketCount basketCount = new BasketCount();
                basketCount.removeItem(position - 1);
                adabter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
                refrash_Adabter();
            }

        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    ////////////////////have order

    private void show_have_order_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You can not order, if you have unfinished order!")
                .setTitle("Order");
        builder.setPositiveButton("Goto Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

               startActivity(new Intent(getApplicationContext(),Requested_order.class));
            }

        }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        Message.log(">>>->>  have order dialog");
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void chack_order() {
        final boolean[] ch = {false};

        Message.log("function active");
        databaseReference_requested_order.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                SharedPreferences sp1 = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
                String id = sp1.getString("id", "");

                Message.log("id "+id+" "+!id.equals(""));
                if (!id.equals("")) {


                    Message.log(">>>->>  have orde "+dataSnapshot.toString());

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Requested_order_data dd = data.getValue(Requested_order_data.class);


                        if (dd.getStatus().equals("1") && id.equals(dd.getUser_id())) {
show_have_order_dialog();
Message.log(">>>->>  have order");
                            ch[0] = true;
                            return;
                        }

                    }

                    Message.log(">>>->>  no order");
                    if (money) {
                        BasketCount basketCount = new BasketCount();
                        if (basketCount.count_basket_item() > 0) {
                            startActivity(new Intent(getApplicationContext(), Maps_Chack_User_Location.class));
                        } else {
                            Message.message("Empty Basket");
                            checkout.setEnabled(true);
                            checkout.setTextColor(MainActivity.context.getResources().getColor(R.color.green));
                            checkout.setBackgroundColor(MainActivity.context.getResources().getColor(R.color.white));
                            checkout.setText("PROCEED TO CHECKOUT");
                        }
                    } else {
                        show_mone_is_not_enough();
                        checkout.setEnabled(true);
                        checkout.setTextColor(MainActivity.context.getResources().getColor(R.color.green));
                        checkout.setBackgroundColor(MainActivity.context.getResources().getColor(R.color.white));
                        checkout.setText("PROCEED TO CHECKOUT");
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void refrash_Adabter() {
        listView.setClickable(true);
        Float total1 = subtotal();
        subtotal.setText(total1 + " Birr");
        total_float = total1 + 30;
        total.setText(total_float + " Birr");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            startActivity(new Intent(getApplicationContext(),Restaurant_menu.class));
        return false;
    }


}