package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFood extends AppCompatActivity {


    Spinner catagory;
    Button add_image;
    Button add_btn;

    final int INT_COD = 22;

    String imageUrl;

    DatabaseReference databaseReference_food_cat;
    StorageReference storageReference;
    DatabaseReference databaseReference_food_item;

    String res_name = "destney burger";


    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        catagory = (Spinner) findViewById(R.id.add_food_cat_cat);
        add_image = (Button) findViewById(R.id.add_food_cat_image);
        add_btn = (Button) findViewById(R.id.add_food_cat_add_btn);

        progressDialog = new ProgressDialog(this);

        ///fire base references
        databaseReference_food_cat = FirebaseDatabase.getInstance().getReference("Food_catagory");
        databaseReference_food_item = FirebaseDatabase.getInstance().getReference("Food_item");
        storageReference = FirebaseStorage.getInstance().getReference("Food");


        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, INT_COD);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cat_name = catagory.getSelectedItem().toString();

                if (imageUrl != null) {
progressDialog.show();
                    String id = databaseReference_food_cat.push().getKey();

                    add_food_add_catagory res_cat=new add_food_add_catagory(id,cat_name,imageUrl);

                    databaseReference_food_cat.child(res_name).child(id).setValue(res_cat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "add resturant success", Toast.LENGTH_SHORT).show();
                            Log.i("database","add resturant success");
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "image is not inserted", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INT_COD && resultCode == RESULT_OK && data != null) {
            progressDialog.show();
            Uri uri = data.getData();

            storageReference.child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplication(), "image is uploded", Toast.LENGTH_SHORT).show();
                     imageUrl = taskSnapshot.getDownloadUrl().toString();
                    progressDialog.dismiss();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
