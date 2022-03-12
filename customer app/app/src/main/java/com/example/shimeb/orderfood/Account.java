package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Account extends AppCompatActivity {


    EditText name;
    EditText phone;
    EditText coupon;
    EditText badges;
    TextView edit;
    TextView addCoupone;

    LinearLayout linearLayout;
    Toolbar toolbar;

    static int FROME_HOME=0;
    static int FROME_VIEW_BASKET=0;
    String id="";

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        databaseReference= FirebaseDatabase.getInstance().getReference("User");

        //////////layout ilements
        linearLayout=(LinearLayout)findViewById(R.id.acc_LV) ;
        name=(EditText)findViewById(R.id.acc_name);
        phone=(EditText)findViewById(R.id.acc_phone);
        edit=(TextView)findViewById(R.id.acc_edite);
        coupon=(EditText)findViewById(R.id.acc_couponTv);
        addCoupone=(TextView)findViewById(R.id.acc_addCoupon);
        badges=(EditText) findViewById(R.id.acc_badg);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);


        ///toolbar
        toolbar=(Toolbar)findViewById(R.id.account_appbar);
        toolbar.setTitle("Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ////////check the user is login or not
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        String name=sp.getString("name","");
         id=sp.getString("id","");

        if(name.equals("") && FROME_HOME==1){
            startActivity(new Intent(getApplicationContext(),login.class));
        }if(name.equals("") && FROME_VIEW_BASKET==1){
            startActivity(new Intent(getApplicationContext(),login.class));
        }else{
            setAccountDetail();
        }

        ////////////database preference


        addCoupone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentMethod.FROM_ACCOUNT=1;
                startActivity(new Intent(getApplicationContext(),PaymentMethod.class));
            }
        });

edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),Update_user_info.class));
    }
});



    }

    private void setAccountDetail() {
progressDialog.show();

        if (!id.equals("")){
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
linearLayout.setVisibility(View.VISIBLE);
                signup_data data = dataSnapshot.getValue(signup_data.class);

                name.setText(data.getName());
                phone.setText(data.getPhone());
                coupon.setText(data.getCoupon()+" birr");
                badges.setText(data.getBadge()+" Badg");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.logout,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_logout){
            MainActivity.HAVE_ORDER=0;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
            sp.edit().putString("id", "").apply();
            sp.edit().putString("name", "").apply();
            sp.edit().putString("phone", "").apply();
            startActivity(new Intent(getApplicationContext(),login.class));
            Toast.makeText(getApplicationContext(),"bye "+name.getText(),Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
