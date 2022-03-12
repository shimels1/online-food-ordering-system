package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentMethod extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceDelectCoupone;
    DatabaseReference databaseReferenceSoldCoupone;
    DatabaseReference databaseReferenceUser;
    DatabaseReference databaseReferenceUserUpdate;

    EditText coupon_number;
    Button submit;
    Toolbar toolbar;

    static int FROM_ACCOUNT = 0;
    static int FROM_VIEWBASKET = 0;

    Float money;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        databaseReference = FirebaseDatabase.getInstance().getReference("Coupon");
        databaseReferenceDelectCoupone = FirebaseDatabase.getInstance().getReference("Coupon");
        databaseReferenceSoldCoupone = FirebaseDatabase.getInstance().getReference("SoldCoupon");
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("User");
        databaseReferenceUserUpdate = FirebaseDatabase.getInstance().getReference("User");

        progressDialog = new ProgressDialog(this);

        coupon_number = (EditText) findViewById(R.id.payment_coupon_card_et);
        submit = (Button) findViewById(R.id.payment_submit_btn);
        toolbar = (Toolbar) findViewById(R.id.payment_appbar);
        toolbar.setTitle("Phyment Method");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cnum=coupon_number.getText()+"";
                if (!cnum.equals("")) {
                    add_coupon(cnum);
                } else {
                    Toast.makeText(getApplicationContext(), "please enter card number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId== EditorInfo.IME_ACTION_GO){
                    String cnum=coupon_number.getText()+"";
                    if (!cnum.equals("")) {

                        add_coupon(cnum);
                    } else {
                        Toast.makeText(getApplicationContext(), "please enter card number", Toast.LENGTH_SHORT).show();
                    }
                }return false;
            }
        });
    }

    private void add_coupon(final String couponNumber) {
        final int[] count = {0};
        progressDialog.show();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren()){

                    final Coupon_data coupon_data=d.getValue(Coupon_data.class);
                if (coupon_data.getKey().equals(couponNumber)){
                    count[0]++;
                final String couponId = d.getKey();
                    money = Float.parseFloat(coupon_data.getMoney());
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
                    final String id = sp.getString("id", "");
                    if (!id.equals("")) {
                        databaseReferenceUser.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                signup_data data = dataSnapshot.getValue(signup_data.class);
                                Float m = data.getCoupon();
                                data.setCoupon(money + m);
                                databaseReferenceUserUpdate.child(id).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        databaseReferenceSoldCoupone.child(couponId).setValue(coupon_data);
                                        databaseReferenceDelectCoupone.child(couponId).removeValue();
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "money add success " + money, Toast.LENGTH_SHORT).show();
                                        if (PaymentMethod.FROM_VIEWBASKET == 1) {
                                            PaymentMethod.FROM_VIEWBASKET = 0;
                                            startActivity(new Intent(getApplicationContext(), ViewBasket.class));
                                        } else {
                                            PaymentMethod.FROM_ACCOUNT = 0;
                                            startActivity(new Intent(getApplicationContext(), Account.class));
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "wrong coupon number" + money, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {

                        Toast.makeText(getApplicationContext(), "user is not login", Toast.LENGTH_SHORT).show();
                    }
            }}

            if (count[0]==0){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Coupon number is not correct", Toast.LENGTH_SHORT).show();
            }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "something error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            if (PaymentMethod.FROM_VIEWBASKET == 1) {
                PaymentMethod.FROM_VIEWBASKET = 0;
                startActivity(new Intent(getApplicationContext(), ViewBasket.class));
            } else {
                PaymentMethod.FROM_ACCOUNT = 0;
                startActivity(new Intent(getApplicationContext(), Account.class));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
