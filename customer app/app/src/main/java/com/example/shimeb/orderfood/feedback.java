package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class feedback extends AppCompatActivity {

    Toolbar toolbar;
    EditText message_et;
    Button sendBtn;

    String userId;

    DatabaseReference databaseReferenceFeedback;
    DatabaseReference time;

ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        databaseReferenceFeedback= FirebaseDatabase.getInstance().getReference("Feedback");
        time = FirebaseDatabase.getInstance().getReference("hello");
        ////////check the user is login or not
        SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        userId=sp.getString("id","");

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Sending...");

        toolbar=(Toolbar)findViewById(R.id.feedback_appbar);
        toolbar.setTitle("Feedback");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        message_et=(EditText)findViewById(R.id.feedback_message_et);
        sendBtn=(Button)findViewById(R.id.feedback_send_btn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.equals("")){
                    Message.message("login first");
                }else if(message_et.getText().length()<6){
                    Message.message("message is too short");
                }else{
                    sendFeedback();
                }
            }
        });




    }

    private void sendFeedback() {
progressDialog.show();
        final String id=databaseReferenceFeedback.push().getKey().toString();
        time.child("tt").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                time.child("t").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long time = (Long) dataSnapshot.getValue();

                        FeedbackData feedbackData=new FeedbackData(userId,message_et.getText().toString(),time+"");
                        databaseReferenceFeedback.child(id).setValue(feedbackData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Message.message("tnxs for you feedback");
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                progressDialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
