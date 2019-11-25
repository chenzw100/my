package com.example.demo.domain;

/**
 * Created by laikui on 2019/9/5.
 */
public class SinaTinyInfoStock {
    private String code;
    private String name;
    private int highPrice;
    private int lowPrice;
    private int openPrice;
    private int currentPrice;
    private int yesterdayPrice;

    public int getYesterdayPrice() {
        return yesterdayPrice;
    }

    public void setYesterdayPrice(int yesterdayPrice) {
        this.yesterdayPrice = yesterdayPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
