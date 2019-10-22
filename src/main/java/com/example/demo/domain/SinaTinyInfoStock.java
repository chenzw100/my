package com.example.demo.domain;

/**
 * Created by laikui on 2019/9/5.
 */
public class SinaTinyInfoStock {
    private String code;
    private int highPrice;
    private int lowPrice;
    private int openPrice;
    private int currentPrice;

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(int openPrice) {
        this.openPrice = openPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(int highPrice) {
        this.highPrice = highPrice;
    }

    public int getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(int lowPrice) {
        this.lowPrice = lowPrice;
    }

    @Override
    public String toString() {
        return "SinaTiny{" +
                "code='" + code + '\'' +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", openPrice=" + openPrice +
                ", currentPrice=" + currentPrice +
                '}';
    }
}
