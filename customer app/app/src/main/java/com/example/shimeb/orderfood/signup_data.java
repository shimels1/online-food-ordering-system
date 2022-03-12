package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 7/8/2017.
 */

public class signup_data {

    private String name;
    private String phone;
    private String password;
    private String badge;
    private Float coupon;

    public signup_data(){

    }
    public signup_data(String phone,String name,String password,Float coupon,String badge){
        this.setName(name);
        this.setPhone(phone);
        this.setPassword(password);
        this.setCoupon(coupon);
        this.setBadge(badge);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Float getCoupon() {
        return coupon;
    }

    public void setCoupon(Float coupon) {
        this.coupon = coupon;
    }



    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
