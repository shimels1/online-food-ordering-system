package com.example.shimeb.orderfood;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by shime.b on 7/6/2017.
 */

public class RV_Adabter_full_foods_view extends RecyclerView.Adapter<RV_Adabter_full_foods_view.MyViewHolder> {

    LayoutInflater layoutInflater;
    List<Add_food_item_data> data = Collections.emptyList();

    public static int PLUS = 1;
    public static int MINUS = 0;
    public static String NAME = "";


    RV_Adabter_full_foods_view.Recycler_on_click recycler_on_click;

    public RV_Adabter_full_foods_view(List<Add_food_item_data> data, Context context) {
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RV_Adabter_full_foods_view.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.rv_full_view_items, parent, false);
        RV_Adabter_full_foods_view.MyViewHolder myViewHolder = new RV_Adabter_full_foods_view.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RV_Adabter_full_foods_view.MyViewHolder holder, int position) {

        Add_food_item_data d = data.get(position);
        holder.name.setText(d.getName());
        holder.price.setText(d.getPrice() + " Birr");
        Picasso.with(MainActivity.context).load(d.getImageUrl()).into(holder.image);

        int n = (int) Float.parseFloat(holder.quantity.getText().toString());
        if (PLUS == 1 && holder.name.getText().toString().equals(NAME)) {
            n = n + 1;
            PLUS = 0;
            holder.quantity.setText(n + "");
        }
        if (MINUS == 1 && holder.name.getText().toString().equals(NAME)) {
            if (n != 1) {
                n = n - 1;
                holder.quantity.setText(n + "");
                MINUS = 0;
            }
        }

        if (position == data.size()-1) {
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) holder.cardView.getLayoutParams();
            layoutParams.setMargins(23, 20,23, 50);
            holder.cardView.requestLayout();
        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void set_RV_item_onClick(RV_Adabter_full_foods_view.Recycler_on_click rv_item_onClick) {
        this.recycler_on_click = rv_item_onClick;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView price;
        TextView quantity;
        CardView cardView;
        ImageButton plus;
        ImageButton minus;
        Button add_to_b_btn;

        View itemView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            image = (ImageView) itemView.findViewById(R.id.rv_full_v_image);
            name = (TextView) itemView.findViewById(R.id.rv_full_v_name);
            price = (TextView) itemView.findViewById(R.id.rv_full_v_price);
            quantity = (TextView) itemView.findViewById(R.id.rv_full_v_itemQuantity);
            cardView = (CardView) itemView.findViewById(R.id.rv_full_view_card);

            plus = (ImageButton) itemView.findViewById(R.id.rv_full_v_plusBtn);
            minus = (ImageButton) itemView.findViewById(R.id.rv_full_v_mines);
            add_to_b_btn = (Button) itemView.findViewById(R.id.rv_full_v_addBtn);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycler_on_click != null) {
                        recycler_on_click.on_rv_item_click(itemView, getPosition(), "image", quantity.getText().toString());
                    }
                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycler_on_click != null) {
                        recycler_on_click.on_rv_item_click(itemView, getPosition(), "plus", quantity.getText().toString());
                    }
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycler_on_click != null) {
                        recycler_on_click.on_rv_item_click(itemView, getPosition(), "minus", quantity.getText().toString());
                    }
                }
            });
            add_to_b_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycler_on_click != null) {
                        recycler_on_click.on_rv_item_click(itemView, getPosition(), "addToBasket", quantity.getText().toString());
                    }
                }
            });
        }

    }

    public interface Recycler_on_click {
        void on_rv_item_click(View view, int position, String btn, String quantity);
    }
}
