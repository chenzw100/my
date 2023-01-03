package com.example.demo.domain.table;


import com.example.demo.utils.MyUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 *

 */
@Entity(name="stock_info_record")
public class StockInfoRecord implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '热门排序'")
    private Integer hotSort;

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次开盘竞价'")
    private Integer oneOpenRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次开盘竞价'")
    private Integer twoOpenRate;

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次收盘收益'")
    private Integer oneCloseRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次收盘收益'")
    private Integer twoCloseRate;

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次次日开盘收益'")
    private Integer oneOpenIncomeRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次次日开盘收益'")
    private Integer twoOpenIncomeRate;

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次次日收盘盘收益'")
    private Integer oneCloseIncomeRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次次日收盘收益'")
    private Integer twoCloseIncomeRate;

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次再次日开盘'")
    private Integer oneNextOpenIncomeRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次再次日收盘'")
    private Integer oneNextCloseIncomeRate;


    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日收盘'")
    private Integer yesterdayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日开盘'")
    private Integer todayOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日收盘'")
    private Integer todayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最高'")
    private Integer todayHighPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最低'")
    private Integer todayLowPrice;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天开盘'")
    private Integer tomorrowOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天收盘'")
    private Integer tomorrowClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最高'")
    private Integer tomorrowHighPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最低'")
    private Integer tomorrowLowPrice;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日开盘'")
    private Integer threeOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日收盘'")
    private Integer threeClosePrice;
    private Integer threeHighPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最低'")
    private Integer threeLowPrice;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '688'")
    private Integer yn;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次收盘'")
    private Integer firstCloseRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次开盘'")
    private Integer secondOpenRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次收盘'")
    private Integer secondCloseRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第三次开盘'")
    private Integer thirdOpenRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第三次收盘'")
    private Integer thirdCloseRate;


    public Integer getHotSort() {
        return hotSort;
    }

    public void setHotSort(Integer hotSort) {
        this.hotSort = hotSort;
    }

    public Integer getTomorrowHighPrice() {
        return tomorrowHighPrice;
    }

    public void setTomorrowHighPrice(Integer tomorrowHighPrice) {
        this.tomorrowHighPrice = tomorrowHighPrice;
    }

    public Integer getTomorrowLowPrice() {
        return tomorrowLowPrice;
    }

    public void setTomorrowLowPrice(Integer tomorrowLowPrice) {
        this.tomorrowLowPrice = tomorrowLowPrice;
    }

    public Integer getThreeHighPrice() {
        return threeHighPrice;
    }

    public void setThreeHighPrice(Integer threeHighPrice) {
        this.threeHighPrice = threeHighPrice;
    }

    public Integer getThreeLowPrice() {
        return threeLowPrice;
    }

    public void setThreeLowPrice(Integer threeLowPrice) {
        this.threeLowPrice = threeLowPrice;
    }

    public Integer getTodayHighPrice() {
        return todayHighPrice;
    }

    public void setTodayHighPrice(Integer todayHighPrice) {
        this.todayHighPrice = todayHighPrice;
    }

    public Integer getTodayLowPrice() {
        return todayLowPrice;
    }

    public void setTodayLowPrice(Integer todayLowPrice) {
        this.todayLowPrice = todayLowPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }



    public String getDayFormat() {
        return dayFormat;
    }

    public void setDayFormat(String dayFormat) {
        this.dayFormat = dayFormat;
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



    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }



    public Integer getOneOpenRate() {
        return oneOpenRate;
    }

    public void setOneOpenRate(Integer oneOpenRate) {
        this.oneOpenRate = oneOpenRate;
    }

    public Integer getTwoOpenRate() {
        return twoOpenRate;
    }

    public void setTwoOpenRate(Integer twoOpenRate) {
        this.twoOpenRate = twoOpenRate;
    }

    public Integer getOneCloseRate() {
        return oneCloseRate;
    }

    public void setOneCloseRate(Integer oneCloseRate) {
        this.oneCloseRate = oneCloseRate;
    }

    public Integer getTwoCloseRate() {
        return twoCloseRate;
    }

    public void setTwoCloseRate(Integer twoCloseRate) {
        this.twoCloseRate = twoCloseRate;
    }

    public Integer getOneOpenIncomeRate() {
        return oneOpenIncomeRate;
    }

    public void setOneOpenIncomeRate(Integer oneOpenIncomeRate) {
        this.oneOpenIncomeRate = oneOpenIncomeRate;
    }

    public Integer getTwoOpenIncomeRate() {
        return twoOpenIncomeRate;
    }

    public void setTwoOpenIncomeRate(Integer twoOpenIncomeRate) {
        this.twoOpenIncomeRate = twoOpenIncomeRate;
    }

    public Integer getOneCloseIncomeRate() {
        return oneCloseIncomeRate;
    }

    public void setOneCloseIncomeRate(Integer oneCloseIncomeRate) {
        this.oneCloseIncomeRate = oneCloseIncomeRate;
    }

    public Integer getTwoCloseIncomeRate() {
        return twoCloseIncomeRate;
    }

    public void setTwoCloseIncomeRate(Integer twoCloseIncomeRate) {
        this.twoCloseIncomeRate = twoCloseIncomeRate;
    }

    public Integer getOneNextOpenIncomeRate() {
        return oneNextOpenIncomeRate;
    }

    public void setOneNextOpenIncomeRate(Integer oneNextOpenIncomeRate) {
        this.oneNextOpenIncomeRate = oneNextOpenIncomeRate;
    }

    public Integer getOneNextCloseIncomeRate() {
        return oneNextCloseIncomeRate;
    }

    public void setOneNextCloseIncomeRate(Integer oneNextCloseIncomeRate) {
        this.oneNextCloseIncomeRate = oneNextCloseIncomeRate;
    }


    public Integer getYesterdayClosePrice() {
        return yesterdayClosePrice;
    }

    public void setYesterdayClosePrice(Integer yesterdayClosePrice) {
        this.yesterdayClosePrice = yesterdayClosePrice;
    }

    public Integer getTodayOpenPrice() {
        return todayOpenPrice;
    }

    public void setTodayOpenPrice(Integer todayOpenPrice) {
        this.todayOpenPrice = todayOpenPrice;
    }

    public Integer getTodayClosePrice() {
        return todayClosePrice;
    }

    public void setTodayClosePrice(Integer todayClosePrice) {
        this.todayClosePrice = todayClosePrice;
    }

    public Integer getTomorrowOpenPrice() {
        return tomorrowOpenPrice;
    }

    public void setTomorrowOpenPrice(Integer tomorrowOpenPrice) {
        this.tomorrowOpenPrice = tomorrowOpenPrice;
    }

    public Integer getTomorrowClosePrice() {
        return tomorrowClosePrice;
    }

    public void setTomorrowClosePrice(Integer tomorrowClosePrice) {
        this.tomorrowClosePrice = tomorrowClosePrice;
    }

    public Integer getThreeOpenPrice() {
        return threeOpenPrice;
    }

    public void setThreeOpenPrice(Integer threeOpenPrice) {
        this.threeOpenPrice = threeOpenPrice;
    }

    public Integer getThreeClosePrice() {
        return threeClosePrice;
    }

    public void setThreeClosePrice(Integer threeClosePrice) {
        this.threeClosePrice = threeClosePrice;
    }

    @Override
    public String toString() {
        this.oneOpenRate = MyUtils.getIncreaseRateCent(this.todayOpenPrice,this.yesterdayClosePrice).intValue();
        this.firstCloseRate = MyUtils.getIncreaseRateCent(this.todayClosePrice,this.yesterdayClosePrice).intValue();
        this.secondOpenRate = MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.yesterdayClosePrice).intValue();
        this.secondCloseRate = MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.yesterdayClosePrice).intValue();
        this.thirdOpenRate = MyUtils.getIncreaseRateCent(this.threeOpenPrice,this.yesterdayClosePrice).intValue();
        this.thirdCloseRate = MyUtils.getIncreaseRateCent(this.threeClosePrice,this.yesterdayClosePrice).intValue();

        this.twoOpenRate = MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.todayClosePrice).intValue();

        this.oneCloseRate = MyUtils.getIncreaseRateCent(this.todayClosePrice,this.todayOpenPrice).intValue();
        this.twoCloseRate = MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.tomorrowOpenPrice).intValue();

        this.oneOpenIncomeRate = MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.todayOpenPrice).intValue();
        this.twoOpenIncomeRate = MyUtils.getIncreaseRateCent(this.threeOpenPrice,this.tomorrowOpenPrice).intValue();

        this.oneCloseIncomeRate = MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.todayOpenPrice).intValue();
        this.twoCloseIncomeRate = MyUtils.getIncreaseRateCent(this.threeClosePrice,this.tomorrowOpenPrice).intValue();

        this.oneNextOpenIncomeRate = MyUtils.getIncreaseRateCent(this.threeOpenPrice,this.todayOpenPrice).intValue();
        this.oneNextCloseIncomeRate = MyUtils.getIncreaseRateCent(this.threeClosePrice,this.todayOpenPrice).intValue();
        return "StockYybRecord{" +
                "oneOpenRate=" + oneOpenRate +
                ", twoOpenRate=" + twoOpenRate +
                ", oneCloseRate=" + oneCloseRate +
                ", twoCloseRate=" + twoCloseRate +
                ", oneOpenIncomeRate=" + oneOpenIncomeRate +
                ", twoOpenIncomeRate=" + twoOpenIncomeRate +
                ", oneCloseIncomeRate=" + oneCloseIncomeRate +
                ", twoCloseIncomeRate=" + twoCloseIncomeRate +
                ", oneNextOpenIncomeRate=" + oneNextOpenIncomeRate +
                ", oneNextCloseIncomeRate=" + oneNextCloseIncomeRate +
                '}';
    }

    public Integer getFirstCloseRate() {
        return firstCloseRate;
    }

    public void setFirstCloseRate(Integer firstCloseRate) {
        this.firstCloseRate = firstCloseRate;
    }

    public Integer getSecondOpenRate() {
        return secondOpenRate;
    }

    public void setSecondOpenRate(Integer secondOpenRate) {
        this.secondOpenRate = secondOpenRate;
    }

    public Integer getSecondCloseRate() {
        return secondCloseRate;
    }

    public void setSecondCloseRate(Integer secondCloseRate) {
        this.secondCloseRate = secondCloseRate;
    }

    public Integer getThirdOpenRate() {
        return thirdOpenRate;
    }

    public void setThirdOpenRate(Integer thirdOpenRate) {
        this.thirdOpenRate = thirdOpenRate;
    }

    public Integer getThirdCloseRate() {
        return thirdCloseRate;
    }

    public void setThirdCloseRate(Integer thirdCloseRate) {
        this.thirdCloseRate = thirdCloseRate;
    }
}
