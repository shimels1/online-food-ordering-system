package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 2/27/2018.
 */

public class Coupon_data {
    private String key;
    private String money;

    public Coupon_data(){
    }

    public Coupon_data(String key, String money){
        this.setKey(key);
        this.setMoney(money);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
