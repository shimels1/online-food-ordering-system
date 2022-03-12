package com.example.shimeb.orderfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class add_food_item extends AppCompatActivity {


    EditText name;
    EditText price;
    EditText discription;
    Spinner catagory;
    Button imageBtn;
    Button addBtn;

    String res_name = "destney burger";
    String cat_id="";


    final int INT_COD=22;
    ProgressDialog progressDialog;

    DatabaseReference databaseReference_food_cat;
    StorageReference storageReference;
    DatabaseReference databaseReference_food_item;

    String imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);


        name=(EditText)findViewById(R.id.add_food_item_name);
        price=(EditText)findViewById(R.id.add_food_item_price);
        discription=(EditText)findViewById(R.id.add_food_item_discription);
        catagory=(Spinner)findViewById(R.id.add_food_item_cat);
        imageBtn=(Button)findViewById(R.id.add_food_item_imge);
        addBtn=(Button)findViewById(R.id.add_food_item_add_btn);

        progressDialog=new ProgressDialog(this);

        progressDialog.setCanceledOnTouchOutside(false);
        ///fire base references
        databaseReference_food_cat = FirebaseDatabase.getInstance().getReference("Food_catagory").child(res_name);
        databaseReference_food_item = FirebaseDatabase.getInstance().getReference("Food_item");
        storageReference = d.getInstance().getReference("food_image/");


        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, INT_COD);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String n=name.getText().toString();
                final  String p=price.getText().toString();
                final String ds=discription.getText().toString();
                final String cat= catagory.getSelectedItem().toString();

                if (imageUrl != "") {
                    progressDialog.show();

                    databaseReference_food_cat.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {

                                add_food_add_catagory dd = d.getValue(add_food_add_catagory.class);

                                if (dd.getCat_name().equals(cat)){

                                    cat_id=dd.getId();
                                    Toast.makeText(getApplicationContext(), cat_id, Toast.LENGTH_SHORT).show();
////////////////////////add food item


                                    String id=databaseReference_food_item.push().getKey();
                                    Add_food_item_data fi=new Add_food_item_data(id,n,p,ds,cat,imageUrl,"on");
                                    databaseReference_food_item.child(cat_id).child(id).setValue(fi).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "food item is added", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            progressDialog.dismiss();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                  /*  String id = databaseReference_food_cat.push().getKey();

                    add_food_add_catagory res_cat=new add_food_add_catagory(id,cat_name,imageUrl);

                    databaseReference_food_cat.child(res_name).child(id).setValue(res_cat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "add resturant success", Toast.LENGTH_SHORT).show();
                            Log.i("database","add resturant success");
                            progressDialog.dismiss();
                        }
                    });*/
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

            ;
            StorageReference sp= storageReference.child(databaseReference_food_item.push().getKey());

            sp.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
