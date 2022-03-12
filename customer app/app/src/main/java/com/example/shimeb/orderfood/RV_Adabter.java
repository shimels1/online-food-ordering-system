package com.example.shimeb.orderfood;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by shime.b on 6/30/2017.
 */

 public  class RV_Adabter extends RecyclerView.Adapter<RV_Adabter.MyViewHolder> {


    LayoutInflater layoutInflater;

    List<Restorantdata> restaurant_data = Collections.emptyList();

    Recycler_Item_Click recycler_item_click;
DatabaseReference time;

List<RatingViewData> rating_data;

    public RV_Adabter(List<Restorantdata> restaurant_data, List<RatingViewData> ratingData, Context context) {
        this.restaurant_data = restaurant_data;
        layoutInflater = LayoutInflater.from(context);
this.rating_data=ratingData;
        time= FirebaseDatabase.getInstance().getReference("hello");

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recycler_item_restorants, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Restorantdata data = restaurant_data.get(position);

        Float rate=0f;
        int rateCount=0;
        for (int i = 0; i <rating_data.size() ; i++) {
            RatingViewData rd = rating_data.get(i);
            if (rd.getUserId().equals(data.getId())){
                rate= Float.parseFloat(rd.getRateNumber());
                rateCount=rd.getUserNumber();
            }
        }
        holder.rating.setRating(rate);
        holder.rate_user_Number.setText("("+rateCount+")");

        Picasso.with(MainActivity.context).load(data.getImageUrl()).into(holder.image);
        holder.title.setText(data.getTitle());
        holder.delivery_time.setText(data.getDelivery_time()+" Min");
        holder.name.setText(data.getName());
        Float r = Float.valueOf(data.getRating());

        time.child("t").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                time.child("t").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long name = (Long) dataSnapshot.getValue();

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                        String dateString = sdf.format(new Date(name));

                        String[] hour = dateString.split(":");
                        Message.log(" hour "+hour[0]);

                        String[] pmam = dateString.split(" ");
                        Message.log(" pmam "+pmam[1]);

                        int h=Integer.parseInt(hour[0]);

                        if ((h>=8 && pmam[1].equals("a.m.")) || (h<=12 && pmam[1].equals("p.m."))|| !(h<8 && pmam[1].equals("p.m."))){
                            holder.ststus.setText("Open Now");
                            holder.ststus.setVisibility(View.VISIBLE);
                            MainActivity.OPEN_CLOTH=1;
                        }else{
                            MainActivity.OPEN_CLOTH=0;
                            holder.ststus.setText("Cloth Now");
                            holder.ststus.setVisibility(View.VISIBLE);
                        }
                        Message.log(" time "+dateString);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {

        return restaurant_data.size();
    }

    public void setRecycler_item_click(Recycler_Item_Click recycler_item_click) {
        this.recycler_item_click = recycler_item_click;
    }
     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title;
        TextView delivery_time;
        TextView name;
        TextView ststus;
        TextView rate_user_Number;
        RatingBar rating;
         CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.rv_item_image);
            rating = (RatingBar) itemView.findViewById(R.id.rv_item_rating);
            name = (TextView) itemView.findViewById(R.id.rv_item_name);
            rate_user_Number = (TextView) itemView.findViewById(R.id.rv_item_rating_number_tv);
            title = (TextView) itemView.findViewById(R.id.rv_item_title);
            ststus = (TextView) itemView.findViewById(R.id.rv_item_open_cloth);
            delivery_time = (TextView) itemView.findViewById(R.id.rv_item_dlv_Time);
            cardView = (CardView) itemView.findViewById(R.id.rv_contener_card_view);
           cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (recycler_item_click != null) {
                recycler_item_click.onRecyclerItemClick(v, getPosition());
            }
        }
    }
   public  interface Recycler_Item_Click {
        void onRecyclerItemClick(View v, int position);
    }
}


