package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 3/7/2018.
 */

public class FeedbackData {

    private String userId;
    private String message;
    private String date;

    public  FeedbackData(){
    }
    public  FeedbackData(String userId,String message,String date){
        this.setUserId(userId);
        this.setDate(date);
        this.setMessage(message);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
