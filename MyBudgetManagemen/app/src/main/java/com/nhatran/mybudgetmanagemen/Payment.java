package com.nhatran.mybudgetmanagemen;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Payment {
    Date paymentTime;
    long moneyAmount;
    String paymentUnit;
    int id = -1;

    public Payment(Date paymentTime, long moneyAmount, String paymentUnit) {
        this.paymentTime = paymentTime;
        this.moneyAmount = moneyAmount;
        this.paymentUnit = paymentUnit;
    }

    public Payment( int id,Date paymentTime, long moneyAmount, String paymentUnit) {
        this.paymentTime = paymentTime;
        this.moneyAmount = moneyAmount;
        this.paymentUnit = paymentUnit;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Payment() {
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public long getMoneyAmount() {
        return moneyAmount;
    }

    public String getMoneyAsString(){
        return String.valueOf(moneyAmount) + " " + paymentUnit;
    }

    public String getTimeAsString(){
        return convertDateToString(paymentTime);
    }

    public void setMoneyAmount(long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getPaymentUnit() {
        return paymentUnit;
    }

    public void setPaymentUnit(String paymentUnit) {
        this.paymentUnit = paymentUnit;
    }

    public static String convertDateToString(Date time ){

        if (time != null) {
            SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
            return mdformat.format(time);
        }
        return "";
    }
}
