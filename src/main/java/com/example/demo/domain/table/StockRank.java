package com.example.demo.domain.table;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.example.demo.enums.RankTypeEnum;
import com.example.demo.utils.MyUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 *

 */
@Entity(name="stock_rank")
public class StockRank implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Excel(name = "排名类型", orderNum = "0")
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '排名类型'")
    private Integer rankType;
    @Excel(name = "排行", orderNum = "1")
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '热门排序'")
    private Integer hotSort;
    @Excel(name = "代码", orderNum = "2")
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Excel(name = "热搜值", orderNum = "3")
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '热搜值'")
    private Integer hotValue;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连板'")
    private Integer continuous;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '出现次数'")
    private Integer showCount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '当日涨跌停-1，0，1情况'")
    private Integer todayState;
    @Excel(name = "日期", orderNum = "4")
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;

    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
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

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日开盘'")
    private Integer threeOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日收盘'")
    private Integer threeClosePrice;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '688'")
    private Integer yn;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次收盘'")
    private Integer firstOpenRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次收盘'")
    private Integer firstCloseRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次开盘'")
    private Integer secondOpenRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次收盘'")
    private Integer secondCloseRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三次开盘'")
    private Integer thirdOpenRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三次收盘'")
    private Integer thirdCloseRate;

    public Integer getTodayState() {
        return todayState;
    }

    public void setTodayState(Integer todayState) {
        this.todayState = todayState;
    }

    public Integer getShowCount() {
        return showCount;
    }

    public void setShowCount(Integer showCount) {
        this.showCount = showCount;
    }

    public String getYesterdayClosePriceText() {
        return MyUtils.getYuanByCent(this.yesterdayClosePrice);
    }

    public String getTodayOpenPriceText() {
        return MyUtils.getYuanByCent(this.todayOpenPrice);
    }
    public String getTodayClosePriceText() {
        return MyUtils.getYuanByCent(this.todayClosePrice);
    }

    public String getTomorrowOpenPriceText() {
        return MyUtils.getYuanByCent(this.tomorrowOpenPrice);
    }
    public String getTomorrowClosePriceText() {
        return MyUtils.getYuanByCent(this.tomorrowClosePrice);
    }

    public String getThreeOpenPriceText() {
        return MyUtils.getYuanByCent(this.threeOpenPrice);
    }
    public String getThreeClosePriceText() {
        return MyUtils.getYuanByCent(this.threeClosePrice);
    }

    public String getRankTypeName(){
        if(this.rankType==null){
            return "--";
        }
        return RankTypeEnum.getStockType(this.rankType);
    }




    @Override
    public String toString() {
        this.firstOpenRate = MyUtils.getIncreaseRateCent(this.todayOpenPrice,this.yesterdayClosePrice).intValue();
        this.firstCloseRate = MyUtils.getIncreaseRateCent(this.todayClosePrice,this.yesterdayClosePrice).intValue();
        this.secondOpenRate = MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.yesterdayClosePrice).intValue();
        this.secondCloseRate = MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.yesterdayClosePrice).intValue();
        this.thirdOpenRate = MyUtils.getIncreaseRateCent(this.threeOpenPrice,this.yesterdayClosePrice).intValue();
        this.thirdCloseRate = MyUtils.getIncreaseRateCent(this.threeClosePrice,this.yesterdayClosePrice).intValue();

        return "RANK{" +
                ", firstOpenRate=" + firstOpenRate +
                "firstCloseRate=" + firstCloseRate +
                ", secondOpenRate=" + secondOpenRate +
                ", secondCloseRate=" + secondCloseRate +
                ", thirdOpenRate=" + thirdOpenRate +
                ", thirdCloseRate=" + thirdCloseRate +
                '}';
    }

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

    public Integer getHotSort() {
        return hotSort;
    }

    public void setHotSort(Integer hotSort) {
        this.hotSort = hotSort;
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

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Integer getFirstOpenRate() {
        return firstOpenRate;
    }

    public void setFirstOpenRate(Integer firstOpenRate) {
        this.firstOpenRate = firstOpenRate;
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

    public Integer getRankType() {
        return rankType;
    }

    public void setRankType(Integer rankType) {
        this.rankType = rankType;
    }

    public Integer getHotValue() {
        return hotValue;
    }

    public void setHotValue(Integer hotValue) {
        this.hotValue = hotValue;
    }

    public Integer getContinuous() {
        return continuous;
    }

    public void setContinuous(Integer continuous) {
        this.continuous = continuous;
    }

    public String toInfo() {
        return
                "[id=" + id +
                        "dayFormat=" + dayFormat +
                        "name=" + name +
                        "code=" + code
                ;
    }
    public void toOneDay() {
        this.firstOpenRate = MyUtils.getIncreaseRateCent(this.todayOpenPrice,this.yesterdayClosePrice).intValue();
        this.firstCloseRate = MyUtils.getIncreaseRateCent(this.todayClosePrice,this.yesterdayClosePrice).intValue();
    }
    public void toOneIncome() {
        this.secondOpenRate = MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.yesterdayClosePrice).intValue();
        this.secondCloseRate = MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.yesterdayClosePrice).intValue();
    }

}
