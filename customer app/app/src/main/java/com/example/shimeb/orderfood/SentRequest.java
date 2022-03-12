package com.example.shimeb.orderfood;

import java.util.List;

public class SentRequest {

    private List<BaskateData> baskateData;
    private String restorant_respond;
    private String driver_respond;
    private String payment;
    private String driver_on_the_way;
    private String final_destination;
    private String time;
    private String latitiud;
    private String longtiude;
    private String orderId;
    private String isFnished;


    private String driver_latitiud;
    private String driver_longtiude;

    public SentRequest(){
    }
    public SentRequest(List<BaskateData> baskateData,
                       String restorant_respond,
                       String driver_respond,
                       String driver_on_the_way,
                       String final_destination,
                       String time, String latitiud,
                       String longtiude,
                       String driver_latitiud,
                       String driver_longtiude,
                       String payment,
                       String orderId,
                       String isFnished){

        this.baskateData=baskateData;
        this.setRestorant_respond(restorant_respond);
        this.setDriver_respond(driver_respond);
        this.setDriver_on_the_way(driver_on_the_way);
        this.setFinal_destination(final_destination);
        this.setTime(time);
        this.latitiud=latitiud;
        this.longtiude=longtiude;
        this.setDriver_latitiud(driver_latitiud);
        this.setDriver_longtiude(driver_longtiude);
        this.setPayment(payment);
        this.setOrderId(orderId);
        this.isFnished=isFnished;

    }


    public String getRestorant_respond() {
        return restorant_respond;
    }

    public void setRestorant_respond(String restorant_respond) {
        this.restorant_respond = restorant_respond;
    }

    public String getDriver_respond() {
        return driver_respond;
    }

    public void setDriver_respond(String driver_respond) {
        this.driver_respond = driver_respond;
    }

    public String getFinal_destination() {
        return final_destination;
    }

    public void setFinal_destination(String final_destination) {
        this.final_destination = final_destination;
    }

    public List<BaskateData> getBaskateData() {
        return baskateData;
    }

    public void setBaskateData(List<BaskateData> baskateData) {
        this.baskateData = baskateData;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getDriver_on_the_way() {
        return driver_on_the_way;
    }

    public void setDriver_on_the_way(String driver_on_the_way) {
        this.driver_on_the_way = driver_on_the_way;
    }

    public String getDriver_latitiud() {
        return driver_latitiud;
    }

    public void setDriver_latitiud(String driver_latitiud) {
        this.driver_latitiud = driver_latitiud;
    }

    public String getDriver_longtiude() {
        return driver_longtiude;
    }

    public void setDriver_longtiude(String driver_longtiude) {
        this.driver_longtiude = driver_longtiude;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIsFnished() {
        return isFnished;
    }

    public void setIsFnished(String isFnished) {
        this.isFnished = isFnished;
    }
}
