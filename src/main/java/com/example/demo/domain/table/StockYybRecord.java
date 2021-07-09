package com.example.demo.domain.table;


import com.example.demo.utils.MyUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 *

 */
@Entity(name="stock_yyb_record")
public class StockYybRecord implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT 'yybId'")
    private Integer yybId;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
    @Column(nullable = false,columnDefinition="varchar(20) COMMENT '柚子'")
    private String yzName;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '上榜后第一天收盘'")
    private Integer oneDay;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '净额'")
    private Integer sumAmount;

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


    @Transient
    private Integer yesterdayClosePrice;
    @Transient
    private Integer todayOpenPrice;
    @Transient
    private Integer todayClosePrice;
    @Transient
    private Integer tomorrowOpenPrice;
    @Transient
    private Integer tomorrowClosePrice;
    @Transient
    private Integer threeOpenPrice;
    @Transient
    private Integer threeClosePrice;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Integer sumAmount) {
        this.sumAmount = sumAmount;
    }

    public Integer getYybId() {
        return yybId;
    }

    public void setYybId(Integer yybId) {
        this.yybId = yybId;
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

    public String getYzName() {
        return yzName;
    }

    public void setYzName(String yzName) {
        this.yzName = yzName;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public Integer getOneDay() {
        return oneDay;
    }

    public void setOneDay(Integer oneDay) {
        this.oneDay = oneDay;
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
}
