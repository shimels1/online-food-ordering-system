package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Update_user_info extends AppCompatActivity {

    EditText name;
    EditText pass1;
    EditText pass2;
    EditText oldPass;
    Button updateBtn;
String user_id;

    Toolbar toolbar;

    boolean flag;
    String currentPsw="";

    DatabaseReference databaseReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        progressDialog=new ProgressDialog(this);
        progressDialog.show();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id= sp.getString("id", "null");

        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user_id);

        name = (EditText) findViewById(R.id.update_ua_un);
        pass1 = (EditText) findViewById(R.id.update_ua_password);
        pass2 = (EditText) findViewById(R.id.update_ua_re_password);
        oldPass = (EditText) findViewById(R.id.update_ua_old_password);
        updateBtn = (Button) findViewById(R.id.update_ua_update_ntn);


        //flag
        // isAccountExist();
        flag = true;

        ////toolbar
        toolbar = (Toolbar) findViewById(R.id.update_ua_app_bar);
        toolbar.setTitle("Edite Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (form_verification()) {

                    signUp();
                }
            }

        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){
                    signup_data data=dataSnapshot.getValue(signup_data.class);
                    if (data!=null){
                        name.setText(data.getName());
                        currentPsw=data.getPassword();
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    boolean form_verification() {


        String n = name.getText().toString();
        String p1 = pass1.getText().toString();
        String p2 = pass2.getText().toString();
        String op = oldPass.getText().toString();
        String phIndexs0 ="d";
        String phIndexs1 ="d";
              if (n.length() < 4) {
            if (n.equals("")){
                Message.message("Name Require");
            }else{
                Message.message("name is too short");}
            return false;
        } else if (pass1.length() < 5) {
            Message.message("password length must be greater then 6 ");
            return false;
        } else if (!p1.equals(p2)) {
            Message.message("Password is not match");
            Message.log(p1+"  "+p2);
            return false;
        } else if (!op.equals(currentPsw)) {
            Message.message("old password is not correct");
            Message.log(p1+"  "+p2);
            return false;
        }
        return true;
    }

    void signUp() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean ex=false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot!=null){
                    signup_data signup_data=dataSnapshot.getValue(com.example.shimeb.orderfood.signup_data.class);
                    signup_data.setName(name.getText().toString());
                    signup_data.setPassword(pass1.getText().toString());

                    databaseReference.setValue(signup_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Update success",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),Account.class));
                        }
                    });
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "cant login,somthing wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
