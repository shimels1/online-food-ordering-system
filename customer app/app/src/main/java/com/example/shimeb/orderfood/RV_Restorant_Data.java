package com.example.shimeb.orderfood;

import android.graphics.Bitmap;

/**
 * Created by shime.b on 6/30/2017.
 */

public class RV_Restorant_Data {

    private String id;
    private String name;
    private String title;
    private Bitmap image;
    private String rating;
    private String delivery_time;

    public RV_Restorant_Data(){

    }

    public RV_Restorant_Data(String id,String name,String title,String delivery_time,String rating,Bitmap image){
        this.id=id;
        this.name=name;
        this.title=title;
        this.setImage(image);
        this.setRating(rating);
        this.setDelivery_time(delivery_time);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
