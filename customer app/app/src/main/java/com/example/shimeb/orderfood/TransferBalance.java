package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransferBalance extends AppCompatActivity {


    Toolbar toolbar;

    TextView phoneTv;
    TextView amountTv;
    EditText phoneEt;
    EditText amountEt;
    Button sendBtn;
    Button nextBtn;

    String user_id;
    String ph;
    Float amount = 0f;

    DatabaseReference databaseReferenceUser;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_balance);

        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("User");


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = sp.getString("id", "null");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("find the user");

        toolbar = (Toolbar) findViewById(R.id.transfer_appbar);
        toolbar.setTitle("Transfer Balance");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneTv = (TextView) findViewById(R.id.transfer_phone_tv);
        amountTv = (TextView) findViewById(R.id.transfer_amount_tv);
        amountEt = (EditText) findViewById(R.id.transfer_amount_et);
        phoneEt = (EditText) findViewById(R.id.transfer_phone_et);
        sendBtn = (Button) findViewById(R.id.transfer_send_btn);
        nextBtn = (Button) findViewById(R.id.transfer_next_btn);


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ph = phoneEt.getText().toString();
                if (!ph.equals("") && !(ph.length() < 2)) {
                    String phIndexs0 = "d";
                    String phIndexs1 = "d";
                    phIndexs0 = String.valueOf(ph.charAt(0));
                    phIndexs1 = String.valueOf(ph.charAt(1));
                    if (ph.length() < 9 || ph.length() > 11 || (ph.length() == 10 ? !phIndexs0.equals("0") || !phIndexs1.equals("9") : !phIndexs0.equals("9")) || phIndexs1.equals("d")) {
                        Message.message("phone number is not correct");
                    } else {
                        progressDialog.show();
                        findUser(ph);
                    }
                } else {
                    Message.message("enter the phone");
                }

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setTitle("transfering");
                progressDialog.show();
                databaseReferenceUser.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = amountEt.getText().toString();

                        amount = Float.parseFloat(s);
                        if (dataSnapshot != null) {
                            signup_data signup_data = dataSnapshot.getValue(signup_data.class);
                            Message.log(ph + " " + dataSnapshot.toString());
                            if (signup_data != null) {

                                Float m = signup_data.getCoupon();
                                if (m > amount) {
                                    databaseReferenceUser.child(ph).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot != null) {
                                                final signup_data ss = dataSnapshot.getValue(signup_data.class);
                                                if (ss != null) {
                                                    Float mm = Float.parseFloat(String.valueOf(ss.getCoupon())) + amount;
                                                    ss.setCoupon(mm);
                                                    databaseReferenceUser.child(ph).setValue(ss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            databaseReferenceUser.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot != null) {
                                                                        signup_data sss = dataSnapshot.getValue(signup_data.class);
                                                                        if (sss != null) {

                                                                            Float mmm = Float.parseFloat(String.valueOf(sss.getCoupon())) - amount;
                                                                            sss.setCoupon(mmm);

                                                                            databaseReferenceUser.child(user_id).setValue(sss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    progressDialog.dismiss();
                                                                                    sendBtn.setVisibility(View.GONE);
                                                                                    amountTv.setVisibility(View.GONE);
                                                                                    amountEt.setVisibility(View.GONE);
                                                                                    phoneTv.setText("Transfer succes to: " + ss.getName() + "\n" + "Amount: " + amount + " Birr.");
                                                                                }
                                                                            });

                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                    });


                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                } else {

                                    progressDialog.dismiss();
                                    Message.message("not enough money");
                                }
                            } else {

                                progressDialog.dismiss();
                                Message.message("someting wrong");
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                transfer();
            }
        });


    }

    private void transfer() {
    }

    private void findUser(final String ph) {

        databaseReferenceUser.child(ph).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    signup_data signup_data = dataSnapshot.getValue(signup_data.class);
                    progressDialog.setTitle("finding user");
                    progressDialog.dismiss();
                    Message.log(ph + " " + dataSnapshot.toString());
                    if (signup_data != null) {
                        phoneTv.setText("To: " + signup_data.getName());
                        phoneEt.setVisibility(View.GONE);
                        nextBtn.setVisibility(View.GONE);
                        amountEt.setVisibility(View.VISIBLE);
                        amountTv.setVisibility(View.VISIBLE);
                        sendBtn.setVisibility(View.VISIBLE);
                    } else {
                        Message.message("user is not found");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
