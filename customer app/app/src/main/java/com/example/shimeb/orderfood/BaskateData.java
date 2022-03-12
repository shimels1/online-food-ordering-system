package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 7/15/2017.
 */

public class BaskateData {
    private String name;
    private String price;
    private String id;
    private String userId;
    private String res_id;
    private String total_number;
    private String discription;
    private String imageUrl;
    private String speshalRequest;


    public BaskateData(){

    }

    public BaskateData(String id,String userId,String name,String price,String total_number,String discription,String imageUrl,String speshalRequest,String res_id){
        this.setId(id);
        this.userId=userId;
        this.setName(name);
        this.setPrice(price);
        this.setRes_id(res_id);
        this.setTotal_number(total_number);
        this.setDiscription(discription);
        this.imageUrl=imageUrl;

        this.setSpeshalRequest(speshalRequest);

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRes_id() {
        return res_id;
    }

    public void setRes_id(String res_id) {
        this.res_id = res_id;
    }

    public String getTotal_number() {
        return total_number;
    }

    public void setTotal_number(String total_number) {
        this.total_number = total_number;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSpeshalRequest() {
        return speshalRequest;
    }

    public void setSpeshalRequest(String speshalRequest) {
        this.speshalRequest = speshalRequest;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
