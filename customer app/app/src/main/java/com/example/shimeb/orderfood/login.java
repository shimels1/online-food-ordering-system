package com.example.shimeb.orderfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    EditText name;
    EditText password;
    Button login;
    TextView creat_account;

    Toolbar toolbar;


    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.email_ET);
        password = (EditText) findViewById(R.id.password_ET);
        login = (Button) findViewById(R.id.login_btn);
        creat_account = (TextView) findViewById(R.id.signupTv);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.login_AppBar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference("User");


        creat_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signUp.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    login();
                    return true;
                }
                return false;
            }
        });
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                   password.setFocusable(true);
                   name.setFocusable(false);
                    return true;
                }
                return false;
            }
        });


    }


    void login() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot d : dataSnapshot.getChildren()) {

                    signup_data dd = d.getValue(signup_data.class);
                    if (dd.getName().equals(name.getText().toString()) && dd.getPassword().equals(password.getText().toString())) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        sp.edit().putString("id", dd.getPhone().toString()).apply();
                        sp.edit().putString("name", dd.getName()).apply();
                        sp.edit().putString("phone", dd.getPhone()).apply();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));


                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "cant login,somthing wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (Account.FROME_VIEW_BASKET == 1) {
                Account.FROME_VIEW_BASKET = 0;
                startActivity(new Intent(getApplicationContext(), ViewBasket.class));
            } else if (Account.FROME_HOME == 1) {
                Account.FROME_HOME = 0;
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }


        return super.onOptionsItemSelected(item);
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