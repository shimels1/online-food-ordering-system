package com.example.shimeb.orderfood;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by shime.b on 7/6/2017.
 */

public class RV_Adabter_menu_cat extends Adapter<RV_Adabter_menu_cat.MyViewHolder>{

    LayoutInflater layoutInflater;
    List<add_food_add_catagory> data= Collections.emptyList();

    Recycler_on_click recycler_on_click;

    public  RV_Adabter_menu_cat(List<add_food_add_catagory> data ,Context context){
        this.data=data;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.rv_res_menus,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        add_food_add_catagory d=data.get(position);

        holder.title.setText(d.getCat_name());
        //Picasso.with(MainActivity.context).load(d.getImage_url()).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void set_RV_item_onClick(Recycler_on_click rv_item_onClick){
        this.recycler_on_click=rv_item_onClick;
    }


    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.rv_res_menu_image_view);
            title=(TextView) itemView.findViewById(R.id.rv_res_menu_text_view);
            cardView=(CardView) itemView.findViewById(R.id.rv_res_menu_layout);

            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
if(recycler_on_click!=null){
    recycler_on_click.on_rv_item_click(v,getPosition());
}
        }
    }

    public interface  Recycler_on_click{
        void on_rv_item_click(View view,int position);
    }
}
