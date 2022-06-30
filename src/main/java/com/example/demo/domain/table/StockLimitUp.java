package com.example.demo.domain.table;

import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name="stock_limit_up")
public class StockLimitUp implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开板次数'")
    private Integer openCount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连板'")
    private Integer continueBoardCount;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日收盘'")
    private Integer yesterdayClosePrice;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日开盘'")
    private Integer todayOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日收盘'")
    private Integer todayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天开盘'")
    private Integer tomorrowOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天收盘'")
    private Integer tomorrowClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '收盘收益'")
    private Integer todayOpenYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '收盘收益'")
    private Integer todayCloseYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日开盘收益'")
    private Integer tomorrowOpenYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日收盘收益'")
    private Integer tomorrowCloseYield;
    public StockLimitUp(){}
    public StockLimitUp(String code, String name,Date date){
        this.code =code;
        this.name = name;
        this.yesterdayClosePrice=10;
        this.todayOpenPrice=10;
        this.todayClosePrice=10;
        this.tomorrowOpenPrice=10;
        this.tomorrowClosePrice=10;
        this.openCount=-1;
        this.dayFormat = MyUtils.getDayFormat(date);
    }

    public Integer getTodayOpenYield() {
        return todayOpenYield;
    }

    public void setTodayOpenYield(Integer todayOpenYield) {
        this.todayOpenYield = todayOpenYield;
    }

    public Integer getTodayCloseYield() {
        return todayCloseYield;
    }

    public void setTodayCloseYield(Integer todayCloseYield) {
        this.todayCloseYield = todayCloseYield;
    }

    public Integer getTomorrowOpenYield() {
        return tomorrowOpenYield;
    }

    public void setTomorrowOpenYield(Integer tomorrowOpenYield) {
        this.tomorrowOpenYield = tomorrowOpenYield;
    }

    public Integer getTomorrowCloseYield() {
        return tomorrowCloseYield;
    }

    public void setTomorrowCloseYield(Integer tomorrowCloseYield) {
        this.tomorrowCloseYield = tomorrowCloseYield;
    }

    public Integer getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public void setTodayOpenPrice(Integer todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
        this.todayOpenYield =MyUtils.getIncreaseRateCent(todayOpenPrice,getYesterdayClosePrice()).intValue();
    }

    public Integer getTodayClosePrice() {
        return todayClosePrice;
    }

    public void setTodayClosePrice(Integer todayClosePrice) {
        this.todayClosePrice = todayClosePrice;
        this.todayCloseYield =MyUtils.getIncreaseRateCent(todayClosePrice,getTodayOpenPrice()).intValue();
    }

    public Integer getTomorrowOpenPrice() {
        return tomorrowOpenPrice;
    }

    public void setTomorrowOpenPrice(Integer tomorrowOpenPrice) {
        this.tomorrowOpenPrice = tomorrowOpenPrice;
        this.tomorrowOpenYield= MyUtils.getIncreaseRateCent(tomorrowOpenPrice,getTodayOpenPrice()).intValue();
    }

    public Integer getTomorrowClosePrice() {
        return tomorrowClosePrice;
    }

    public void setTomorrowClosePrice(Integer tomorrowClosePrice) {
        this.tomorrowClosePrice = tomorrowClosePrice;
        this.tomorrowCloseYield= MyUtils.getIncreaseRateCent(tomorrowClosePrice,getTodayOpenPrice()).intValue();
    }



   /* @Transient
    private String todayOpenRate;
    @Transient
    private String todayCloseRate;
    @Transient
    private String tomorrowOpenRate;
    @Transient
    private String tomorrowCloseRate;

    public String getTodayOpenRate() {
        return MyUtils.getIncreaseRate(getTodayOpenPrice(), getYesterdayClosePrice()).toString();
    }

    public void setTodayOpenRate(String todayOpenRate) {
        this.todayOpenRate = todayOpenRate;
    }

    public String getTodayCloseRate() {
        return MyUtils.getIncreaseRate(getTodayClosePrice(),getTodayOpenPrice()).toString();
    }

    public void setTodayCloseRate(String todayCloseRate) {
        this.todayCloseRate = todayCloseRate;
    }

    public String getTomorrowOpenRate() {
        return MyUtils.getIncreaseRate(getTomorrowOpenPrice(),getTodayOpenPrice()).toString();
    }

    public void setTomorrowOpenRate(String tomorrowOpenRate) {
        this.tomorrowOpenRate = tomorrowOpenRate;
    }

    public String getTomorrowCloseRate() {
        return MyUtils.getIncreaseRate(getTomorrowClosePrice(),getTodayOpenPrice()).toString();
    }

    public void setTomorrowCloseRate(String tomorrowCloseRate) {
        this.tomorrowCloseRate = tomorrowCloseRate;
    }*/



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDayFormat() {
        return dayFormat;
    }

    public void setDayFormat(String dayFormat) {
        this.dayFormat = dayFormat;
    }

    public Integer getYesterdayClosePrice() {
        return yesterdayClosePrice;
    }

    public void setYesterdayClosePrice(Integer yesterdayClosePrice) {
        this.yesterdayClosePrice = yesterdayClosePrice;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }


    public Integer getContinueBoardCount() {
        if(continueBoardCount == null){
            continueBoardCount =-1;
        }
        return continueBoardCount;
    }

    public void setContinueBoardCount(Integer continueBoardCount) {
        this.continueBoardCount = continueBoardCount;
    }

    public Integer getOpenCount() {
        return openCount;
    }

    public void setOpenCount(Integer openCount) {
        this.openCount = openCount;
    }

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




    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(code).append(name).append(dayFormat).
                append(",open:").append(getOpenCount()).append(",continue:").append(getContinueBoardCount()).append(",price:").append(MyUtils.getYuanByCent(getYesterdayClosePrice())).append(plateName).append("<br>");
        return sb.toString();
    }
    public StockInfo toStockLimitUpFive(){
        StockInfo xgbFiveUpStock = new StockInfo(code,name, NumberEnum.StockType.STOCK_LIMIT_UP_FIVE.getCode());
        xgbFiveUpStock.setContinuous(continueBoardCount);
        xgbFiveUpStock.setShowCount(continueBoardCount-4);
        xgbFiveUpStock.setOpenCount(openCount);
        xgbFiveUpStock.setYesterdayClosePrice(yesterdayClosePrice);
        xgbFiveUpStock.setPlateName(plateName);
        xgbFiveUpStock.setFiveHighPrice(10);
        xgbFiveUpStock.setFiveLowPrice(10);
        xgbFiveUpStock.setTodayOpenPrice(10);
        return xgbFiveUpStock;
    }
}
