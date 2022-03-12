package com.example.shimeb.orderfood;

/**
 * Created by shime on 12/8/2017.
 */

public class Requested_order_data {
    private String user_id;
    private String order_id;
    private String restaurant_id;
    private String status;

    Requested_order_data(){

    }
    Requested_order_data(String user_id,String restaurant_id,String order_id,String status){
        this.setOrder_id(order_id);
        this.setRestaurant_id(restaurant_id);
        this.setUser_id(user_id);
        this.setStatus(status);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
