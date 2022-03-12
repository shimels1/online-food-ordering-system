package com.example.shimeb.orderfood;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phone_auth extends AppCompatActivity{

    Toolbar toolbar;

EditText phoneNum,Code;// two edit text one for enter phone number other for enter OTP code
        Button sent_,Verify;// sent button to request for verification and verify is for to verify code
private PhoneAuthProvider.ForceResendingToken mResendToken;
private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
private FirebaseAuth mAuth;
private String mVerificationId;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

    toolbar=(Toolbar)findViewById(R.id.phon_auth_AppBar);
    toolbar.setTitle("Phone Verification");
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    phoneNum =(EditText) findViewById(R.id.phone);
        Code =(EditText) findViewById(R.id.code);

        sent_ =(Button)findViewById(R.id.verifiy);
        Verify =(Button)findViewById(R.id.ver);

        callback_verificvation();               ///function initialization

        mAuth = FirebaseAuth.getInstance();
        sent_.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        String num=phoneNum.getText().toString();
        Toast.makeText(MainActivity.context,num,Toast.LENGTH_SHORT).show();
        startPhoneNumberVerification(num);          // call function for receive OTP 6 digit code
        }
        });
        Verify.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        String code=Code.getText().toString();
        verifyPhoneNumberWithCode(mVerificationId,code);            //call function for verify code

        }
        });
        }

private void startPhoneNumberVerification(String phoneNumber) {
    if (!phoneNumber.equals("")) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
    }else{
        Message.message("null");
    }

        }

private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
@Override
public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
        // Sign in success, update UI with the signed-in user's information

        FirebaseUser user = task.getResult().getUser();
            Toast.makeText(getApplicationContext(), "phone number verifide", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),signUp.class);
            intent.putExtra("phone",phoneNum.getText().toString());
            startActivity(intent);

        } else {
        // Sign in failed, display a message and update the UI

        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
        // The verification code entered was invalid
            Toast.makeText(getApplicationContext(), "sign in invalid", Toast.LENGTH_SHORT).show();
        }


        }
        }
        });
        }
private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
        }


private void callback_verificvation() {

    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verificaiton without
            //     user action.
            Toast.makeText(getApplicationContext(), "phone number verifide", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),signUp.class);
            intent.putExtra("phone",phoneNum.getText().toString());
            startActivity(intent);

            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            Toast.makeText(getApplicationContext(), "error pleas enter again", Toast.LENGTH_SHORT).show();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Toast.makeText(getApplicationContext(), "invalid request", Toast.LENGTH_SHORT).show();

            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded

                Toast.makeText(getApplicationContext(), "some problem9", Toast.LENGTH_SHORT).show();

            }

            // Show a message and update the UI

        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then CONSTRUCT a credential
            // by combining the code with a verification ID.

            Toast.makeText(getApplicationContext(), "code sent, verify it", Toast.LENGTH_SHORT).show();


            Code.setVisibility(View.VISIBLE);
            Verify.setVisibility(View.VISIBLE);
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;


        }
    };
}}