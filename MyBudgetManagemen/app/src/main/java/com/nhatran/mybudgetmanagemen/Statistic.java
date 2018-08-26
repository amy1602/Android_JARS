package com.nhatran.mybudgetmanagemen;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Statistic {
    Date statisticTime;
    long moneyAmount;
    String paymentUnit;
    int id = -1;

    public Statistic(Date statisticTime, long moneyAmount, String paymentUnit) {
        this.statisticTime = statisticTime;
        this.moneyAmount = moneyAmount;
        this.paymentUnit = paymentUnit;
    }

    public Statistic( int id, Date statisticTime, long moneyAmount, String paymentUnit) {
        this.statisticTime = statisticTime;
        this.moneyAmount = moneyAmount;
        this.paymentUnit = paymentUnit;
        this.id = id;
    }

    public String getTimeAsString(){
        return convertDateToString(statisticTime);
    }

    public String getTimeAsStringFromMonth(){
        return convertDateToStringFromMonth(statisticTime);
    }

    public static String convertDateToString(Date time ){

        if (time != null) {
            SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
            return mdformat.format(time);
        }
        return "";
    }

    public static String convertDateToStringFromMonth(Date time ){

        if (time != null) {
            SimpleDateFormat mdformat = new SimpleDateFormat("MM/yyyy");
            return mdformat.format(time);
        }
        return "";
    }

    public String getMoneyAsString(){
        return String.valueOf(moneyAmount) + " " + paymentUnit;
    }


    public Date getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(Date statisticTime) {
        this.statisticTime = statisticTime;
    }

    public long getMoneyAmount() {
        return moneyAmount;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
