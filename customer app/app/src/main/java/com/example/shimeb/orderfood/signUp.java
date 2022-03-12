package com.example.shimeb.orderfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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

public class signUp extends AppCompatActivity {

    EditText name;
    EditText pass1;
    EditText pass2;
    EditText phoneTv;
    Button signup;

    String phone;

    Toolbar toolbar;

    boolean flag;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = (EditText) findViewById(R.id.su_name);
        pass1 = (EditText) findViewById(R.id.su_password);
        pass2 = (EditText) findViewById(R.id.su_re_password);
        phoneTv = (EditText) findViewById(R.id.su_phone);
        signup = (Button) findViewById(R.id.su_signup);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        //flag
        // isAccountExist();
        flag = true;
        //intent

        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");

        ////toolbar
        toolbar = (Toolbar) findViewById(R.id.signup_appbar);
        toolbar.setTitle("SignUp");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (form_verification()) {

                     signUp();
                }
            }

        });

        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    pass1.setFocusable(true);
                    name.setFocusable(false);
                    return true;
                }else{
                    name.setFocusable(true);
                }
                return false;
            }
        });
        pass1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    pass2.setFocusable(true);
                    pass1.setFocusable(false);
                    return true;
                }else{
                    pass1.setFocusable(true);
                }
                return false;
            }
        });
        pass2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    phoneTv.setFocusable(true);
                    pass2.setFocusable(false);
                    return true;
                }else{
                    pass2.setFocusable(true);
                }
                return false;
            }
        });
        phoneTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (form_verification()) {

                        signUp();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void isAccountExist() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dd : dataSnapshot.getChildren()) {
                    signup_data data = dd.getValue(signup_data.class);

                    String p = data.getPhone().toString();
                    if (p.equals(phone)) {
                        flag = false;
                        //       Toast.makeText(getApplicationContext(),"phone number dublicated",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), data.getPhone() + " " + data.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void signup(View v) {

        final String n = name.getText().toString();
        final String p1 = pass1.getText().toString();
        final String ph = phoneTv.getText().toString();

        final String id = databaseReference.push().getKey().toString();


        Float f = Float.valueOf(0);
        signup_data data = new signup_data(ph,n, p1, f,"0");
        final View vv = v;
        databaseReference.child(ph).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                Toast.makeText(getApplicationContext(), "sign up success", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "welcome", Toast.LENGTH_SHORT).show();


                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
                sp.edit().putString("id", ph).apply();
                sp.edit().putString("name", n).apply();
                sp.edit().putString("phone", phone).apply();
                if (Account.FROME_VIEW_BASKET == 1) {
                    Account.FROME_VIEW_BASKET = 0;
                    startActivity(new Intent(getApplicationContext(), ViewBasket.class));
                } else if (Account.FROME_HOME == 1) {
                    Account.FROME_HOME = 0;
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }


    boolean form_verification() {


        String n = name.getText().toString();
        String p1 = pass1.getText().toString();
        String p2 = pass2.getText().toString();
        String ph = phoneTv.getText().toString();
        String phIndexs0 ="d";
        String phIndexs1 ="d";
        if (ph.length() > 3) {
             phIndexs0 = String.valueOf(ph.charAt(0));
             phIndexs1 = String.valueOf(ph.charAt(1));}

        if (n.length() < 4) {
            if (n.equals("")){
                Toast.makeText(getApplicationContext(), "Name Require", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "name is too short", Toast.LENGTH_SHORT).show();
                }
            return false;
        } else if (pass1.length() < 5) {Toast.makeText(getApplicationContext(), "password length must be greater then 6 ", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!p1.equals(p2)) {
            Toast.makeText(getApplicationContext(), "Password is not match", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ph.length() < 9 || ph.length() > 11 || ( ph.length()== 10? !phIndexs0.equals("0") ||  !phIndexs1.equals("9") : !phIndexs0.equals("9")) || phIndexs1.equals("d")) {

               if (ph.equals("")){
                   Toast.makeText(getApplicationContext(),"Phone number Requere", Toast.LENGTH_SHORT).show();

               }else{
                   Toast.makeText(getApplicationContext(), "Phone number is invalid", Toast.LENGTH_SHORT).show();
               }
                return false;
        }
        return true;
    }

    void signUp() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean ex=false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    signup_data dd = d.getValue(signup_data.class);

                    String phone=phoneTv.getText().toString();
                    if (dd.getPhone().equals(phone)) {
                        Toast.makeText(getApplicationContext(), "account is exist", Toast.LENGTH_SHORT).show();

                        ex=true;
                    }else {
                    }
                }
                if (ex){
                }else {
                    signup(getCurrentFocus());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "cant login,somthing wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
