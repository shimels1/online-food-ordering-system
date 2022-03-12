package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class add_restorant extends AppCompatActivity {

    EditText name;
    EditText title;
    EditText deliveryTime;
    EditText reting;
    Button brows;
    Button submit;

    String Image_url="";


    final int INTENT_NUM=2;

    ProgressDialog progressDialog;

    DatabaseReference databaseReference;
    StorageReference storageReference;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==INTENT_NUM && resultCode==RESULT_OK && data!=null){


            progressDialog.show();
            Uri uri=data.getData();

            StorageReference c=storageReference.child(uri.getLastPathSegment());

            c.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Image_url=taskSnapshot.getDownloadUrl().toString();
                    Log.i("image url ",Image_url);
                    progressDialog.setTitle("uploding...");
                    Toast.makeText(getApplicationContext(),"success"+Image_url,Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restorant);

        name=(EditText)findViewById(R.id.add_restorant_name);
        title=(EditText)findViewById(R.id.add_restorant_title);
        deliveryTime=(EditText)findViewById(R.id.add_restorant_del_time);
        reting=(EditText)findViewById(R.id.add_restorant_rating);
        brows=(Button)findViewById(R.id.add_brows_image);
        submit=(Button)findViewById(R.id.add_submit_btn);

        progressDialog=new ProgressDialog(this);

        progressDialog.setCanceledOnTouchOutside(false);
        databaseReference= FirebaseDatabase.getInstance().getReference("Restaurant");
        storageReference= FirebaseStorage.getInstance().getReference("Restaurant_Images/");


        brows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,INTENT_NUM);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_restorant();
            }
        });



    }


    void  add_restorant(){

        Log.i("database",">>>>>");

        progressDialog.show();
        String n=name.getText().toString();
        String t=title.getText().toString();
        String del_t=deliveryTime.getText().toString();
        String rat=reting.getText().toString();
        String u=Image_url;

        if(Image_url!="") {


            String id = databaseReference.push().getKey();
            Restorantdata restaurant_data = new Restorantdata(id, n, t, del_t, rat, u,"","","","","","","","");

            Log.i("database",">>>.."+restaurant_data.getName());
            databaseReference.child(id).setValue(restaurant_data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "add resturant success", Toast.LENGTH_SHORT).show();
                    Log.i("database","add resturant success");
                    progressDialog.dismiss();
                }
            });

        }else{
            Log.i("database","add resturant errror");
            Toast.makeText(getApplicationContext(), "image is not uploded", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }
}
