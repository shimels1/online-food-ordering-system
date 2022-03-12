package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 7/6/2017.
 */

public class add_food_add_catagory {

     String cat_name;
     String image_url;
     String id;

    public add_food_add_catagory(){

    }
    public  add_food_add_catagory(String id,String cat_name,String image_url){
        this.cat_name=cat_name;
        this.image_url=image_url;
        this.id=id;
    }




    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
