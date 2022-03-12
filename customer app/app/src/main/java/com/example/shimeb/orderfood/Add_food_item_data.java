package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 7/6/2017.
 */

public class Add_food_item_data {

    private String id;
    private String name;
    private String price;
    private String discription;
    private String catagory;
    private String imageUrl;
    private String status;

    public Add_food_item_data(){

    }
    public Add_food_item_data(String id, String name, String price, String discription, String catagory, String imageUrl, String status){
        this.id=id;
        this.setStatus(status);
        this.name=name;
        this.price=price;
        this.discription=discription;
        this.catagory=catagory;
        this.imageUrl=imageUrl;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
