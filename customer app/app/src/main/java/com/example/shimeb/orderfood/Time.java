package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 7/22/2017.
 */

public class Time {

    private String id;
    private String user_id;
    private String time;

    public Time(){

    }

    public Time(String id,String user_id,String time){
        this.setId(id);
        this.setUser_id(user_id);
        this.setTime(time);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
