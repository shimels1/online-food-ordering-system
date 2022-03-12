package com.example.shimeb.orderfood;

public class Restorantdata {

    private String id;
    private String name;
    private String user_name;
    private String title;
    private String discription;
    private String status;
    private String working_hour;
    private String phone;
    private String delivery_time;
    private String imageUrl;
    private String password;
    private String rating;
    private String latitiud;
    private String longtiude;


    public Restorantdata(){

    }
    public Restorantdata(String id, String name, String user_name, String title,
                         String discription, String status, String working_hour,
                         String phone, String delivery_time, String imageUrl, String password, String rating, String latitiud
            , String longtiude){
        this.setId(id);
        this.setName(name);
        this.setTitle(title);
        this.setUser_name(user_name);
        this.setDelivery_time(delivery_time);
        this.setImageUrl(imageUrl);
        this.setPassword(password);
        this.setRating(rating);
        this.discription=discription;
        this.status=status;
        this.working_hour=working_hour;
        this.phone=phone;
        this.setLatitiud(latitiud);
        this.setLongtiude(longtiude);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorking_hour() {
        return working_hour;
    }

    public void setWorking_hour(String working_hour) {
        this.working_hour = working_hour;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitiud() {
        return latitiud;
    }

    public void setLatitiud(String latitiud) {
        this.latitiud = latitiud;
    }

    public String getLongtiude() {
        return longtiude;
    }

    public void setLongtiude(String longtiude) {
        this.longtiude = longtiude;
    }
}
