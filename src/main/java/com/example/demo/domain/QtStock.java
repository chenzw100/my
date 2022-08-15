package com.example.demo.domain;

/**
 *     //http://qt.gtimg.cn/q=sz000858
 *  1: 名字
 *  2: 代码
 *  3: 当前价格
 *  4: 昨收
 *  5: 今开
 *  47: 涨停价
 * 48: 跌停价
 */
public class QtStock {
    private String code;
    private String name;
    private String currentPrice;
    private String yesterdayClosePrice;
    private String todayOpenPrice;
    private String upPrice;
    private String downPrice;
    private Integer isUp;
    private Integer isDown;
    private Integer todayState;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getYesterdayClosePrice() {
        return yesterdayClosePrice;
    }

    public void setYesterdayClosePrice(String yesterdayClosePrice) {
        this.yesterdayClosePrice = yesterdayClosePrice;
    }

    public String getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public void setTodayOpenPrice(String todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
    }

    public String getUpPrice() {
        return upPrice;
    }

    public void setUpPrice(String upPrice) {
        this.upPrice = upPrice;
    }

    public String getDownPrice() {
        return downPrice;
    }

    public void setDownPrice(String downPrice) {
        this.downPrice = downPrice;
    }

    public Integer getTodayState() {
        if(getIsUp()==1){
            todayState=1;
        }else {
            todayState=0;
        }
        if(getIsDown()==1){
            todayState=-1;
        }

        return todayState;
    }

    public void setTodayState(Integer todayState) {
        this.todayState = todayState;
    }

    public Integer getIsUp() {
        if(currentPrice.equals(upPrice)){
            this.isUp = 1;
        }else {
            this.isUp = -1;
        }
        return isUp;
    }

    public void setIsUp(Integer isUp) {
        this.isUp = isUp;
    }

    public Integer getIsDown() {
        if(currentPrice.equals(downPrice)){
            this.isDown = 1;
        }else {
            this.isDown = -1;
        }
        return isDown;
    }

    public void setIsDown(Integer isDown) {
        this.isDown = isDown;
    }

    @Override
    public String toString() {
        return "QtStock{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", currentPrice='" + currentPrice + '\'' +
                ", yesterdayClosePrice='" + yesterdayClosePrice + '\'' +
                ", todayOpenPrice='" + todayOpenPrice + '\'' +
                ", upPrice='" + upPrice + '\'' +
                ", downPrice='" + downPrice + '\'' +
                ", isUp=" + getIsUp() +
                ", isDown=" + getIsDown() +
                ", TodayState=" + getTodayState() +
                '}';
    }
}
