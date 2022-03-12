package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 8/1/2017.
 */

public class Driver_data {

    private String name;
    private String user_name;
    private String id;
    private String password;
    private String status;
    private String latitud;
    private String longtiud;
    private String restaurant_id;
private String phone;


    public Driver_data(){

    }
    public Driver_data(String phone,String name, String user_name, String id, String password,
                       String status, String latitud, String longtiud, String restaurant_id){
        this.setName(name);
        this.setUser_name(user_name);
        this.setId(id);
        this.setPassword(password);
        this.setStatus(status);
        this.setLatitud(latitud);
        this.setLongtiud(longtiud);
        this.setRestaurant_id(restaurant_id);
        this.setPhone(phone);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongtiud() {
        return longtiud;
    }

    public void setLongtiud(String longtiud) {
        this.longtiud = longtiud;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
