package com.example.shimeb.orderfood;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shime.b on 7/15/2017.
 */

public class BasketCount {

    static List<BaskateData> data = new ArrayList<BaskateData>();
    ;

    public BasketCount() {

    }

    public void add_to_basket(BaskateData d) {
        int n = update_basket(d);
       // Toast.makeText(MainActivity.context, d.getTotal_number() + " item selected", Toast.LENGTH_SHORT).show();
        if (n == 1) {
            data.add(d);
        }
    }

    private int update_basket(BaskateData d) {
        int nn = 1;
        for (int i = 0; i < data.size(); i++) {
            BaskateData bb = data.get(i);
            if (d.getName().equals(bb.getName())) {
                //update the data file
                int n = Integer.parseInt(bb.getTotal_number()) + Integer.parseInt(d.getTotal_number());
                bb.setTotal_number(n + "");
                nn = 0;
            }
        }
        return nn;
    }


    public List<BaskateData> get_basket() {
        return data;
    }

    public void delete_basket() {
        data.clear();
    }

    public int count_basket_item() {
        return data.size();
    }

    public void removeItem(int position){
        data.remove(position);
    }

}
