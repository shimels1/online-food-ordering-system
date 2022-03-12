package com.example.shimeb.orderfood;

/**
 * Created by shime.b on 3/5/2018.
 */

public class RatingViewData {


    private String userId;
    private String rateNumber;
    private int userNumber;


    public RatingViewData(){
    }
    public RatingViewData(String userId, String rateNumber,int userNumber){
        this.setRateNumber(rateNumber);
        this.setUserId(userId);
        this.setUserNumber(userNumber);
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

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }
}
