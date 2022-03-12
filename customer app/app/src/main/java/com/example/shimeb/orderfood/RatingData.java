package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 3/5/2018.
 */

public class RatingData {


    private String userId;
    private String rateNumber;

    public RatingData(){
    }
    public RatingData(String userId,String rateNumber){
        this.setRateNumber(rateNumber);
        this.setUserId(userId);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRateNumber() {
        return rateNumber;
    }

    public void setRateNumber(String rateNumber) {
        this.rateNumber = rateNumber;
    }
}
