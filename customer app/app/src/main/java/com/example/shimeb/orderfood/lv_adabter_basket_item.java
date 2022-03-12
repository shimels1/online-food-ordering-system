package com.example.shimeb.orderfood;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by shime.b on 7/17/2017.
 */

public class lv_adabter_basket_item extends ArrayAdapter<BaskateData> {

    Activity context;
    List<BaskateData> data;

    int k=2;



    public lv_adabter_basket_item(Activity context, List<BaskateData> data) {
        super(context, R.layout.lv_basket_item_lists,data);
        this.context=context;
        this.data=data;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();

        View view=inflater.inflate(R.layout.lv_basket_item_lists,null,true);

        TextView num_of_itewms=(TextView)view.findViewById(R.id.lv_basket_no_of_items);
        TextView name=(TextView)view.findViewById(R.id.lv_basket_name);
        TextView price=(TextView)view.findViewById(R.id.lv_basket_total_money);

    //    final ImageView del_image=(ImageView)view.findViewById(R.id.lv_basket_delete_image);
        TextView edit=(TextView) view.findViewById(R.id.basket_header_edit);

        BaskateData d=data.get(position);
        num_of_itewms.setText(d.getTotal_number()+"*1");
        name.setText(d.getName());
        Float pr=Float.parseFloat(d.getPrice())*Float.parseFloat(d.getTotal_number());
        price.setText(pr+" Birr");

        return view;
    }
}
