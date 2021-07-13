package com.example.demo.domain;


import com.example.demo.utils.MyUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 *

 */
@Entity(name="stock_yyb_info_record")
public class StockYybInfoRecord implements Serializable {
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
    @Column(nullable = false,columnDefinition="varchar(200) COMMENT '柚子'")
    private String yybName;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '类型'")
    private Integer yzType;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '上榜后第一天收盘'")
    private Integer oneDay;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二天'")
    private Integer twoDay;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三天'")
    private Integer threeDay;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第五天'")
    private Integer fiveDay;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第十天'")
    private Integer tenDay;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二十天'")
    private Integer twentyDay;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三十天'")
    private Integer thirtyDay;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '净额'")
    private Integer sumAmount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '买入'")
    private Integer tradeAmount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '卖出'")
    private Integer saleAmount;

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
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日涨幅'")
    private Integer yesterdayGains;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日成交额'")
    private Integer yesterdayTurnover;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日换手率'")
    private Integer yesterdayTurnoverRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日成交量'")
    private Integer yesterdayVolume;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日开盘'")
    private Integer todayOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日收盘'")
    private Integer todayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日涨幅'")
    private Integer todayGains;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '今日成交额'")
    private Integer todayTurnover;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '今日换手率'")
    private Integer todayTurnoverRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '今日成交量'")
    private Integer todayVolume;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最高'")
    private Integer todayHighPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最低'")
    private Integer todayLowPrice;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天开盘'")
    private Integer tomorrowOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天收盘'")
    private Integer tomorrowClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日涨幅'")
    private Integer tomorrowGains;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '明日成交额'")
    private Integer tomorrowTurnover;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '明日换手率'")
    private Integer tomorrowTurnoverRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '明日成交量'")
    private Integer tomorrowVolume;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最高'")
    private Integer tomorrowHighPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最低'")
    private Integer tomorrowLowPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日开盘'")
    private Integer threeOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日收盘'")
    private Integer threeClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日涨幅'")
    private Integer threeGains;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日成交额'")
    private Integer threeTurnover;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日换手率'")
    private Integer threeTurnoverRate;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日成交量'")
    private Integer threeVolume;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日最高'")
    private Integer threeHighPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最低'")
    private Integer threeLowPrice;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '688'")
    private Integer yn;

    public Integer getYzType() {
        return yzType;
    }

    public void setYzType(Integer yzType) {
        this.yzType = yzType;
    }

    public Integer getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Integer saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Integer getYesterdayGains() {
        return yesterdayGains;
    }

    public void setYesterdayGains(Integer yesterdayGains) {
        this.yesterdayGains = yesterdayGains;
    }

    public Integer getYesterdayTurnover() {
        return yesterdayTurnover;
    }

    public void setYesterdayTurnover(Integer yesterdayTurnover) {
        this.yesterdayTurnover = yesterdayTurnover;
    }

    public Integer getYesterdayTurnoverRate() {
        return yesterdayTurnoverRate;
    }

    public void setYesterdayTurnoverRate(Integer yesterdayTurnoverRate) {
        this.yesterdayTurnoverRate = yesterdayTurnoverRate;
    }

    public Integer getYesterdayVolume() {
        return yesterdayVolume;
    }

    public void setYesterdayVolume(Integer yesterdayVolume) {
        this.yesterdayVolume = yesterdayVolume;
    }

    public Integer getTodayGains() {
        return todayGains;
    }

    public void setTodayGains(Integer todayGains) {
        this.todayGains = todayGains;
    }

    public Integer getTodayTurnover() {
        return todayTurnover;
    }

    public void setTodayTurnover(Integer todayTurnover) {
        this.todayTurnover = todayTurnover;
    }

    public Integer getTodayTurnoverRate() {
        return todayTurnoverRate;
    }

    public void setTodayTurnoverRate(Integer todayTurnoverRate) {
        this.todayTurnoverRate = todayTurnoverRate;
    }

    public Integer getTodayVolume() {
        return todayVolume;
    }

    public void setTodayVolume(Integer todayVolume) {
        this.todayVolume = todayVolume;
    }

    public Integer getTomorrowGains() {
        return tomorrowGains;
    }

    public void setTomorrowGains(Integer tomorrowGains) {
        this.tomorrowGains = tomorrowGains;
    }

    public Integer getTomorrowTurnover() {
        return tomorrowTurnover;
    }

    public void setTomorrowTurnover(Integer tomorrowTurnover) {
        this.tomorrowTurnover = tomorrowTurnover;
    }

    public Integer getTomorrowTurnoverRate() {
        return tomorrowTurnoverRate;
    }

    public void setTomorrowTurnoverRate(Integer tomorrowTurnoverRate) {
        this.tomorrowTurnoverRate = tomorrowTurnoverRate;
    }

    public Integer getTomorrowVolume() {
        return tomorrowVolume;
    }

    public void setTomorrowVolume(Integer tomorrowVolume) {
        this.tomorrowVolume = tomorrowVolume;
    }

    public Integer getThreeGains() {
        return threeGains;
    }

    public void setThreeGains(Integer threeGains) {
        this.threeGains = threeGains;
    }

    public Integer getThreeTurnover() {
        return threeTurnover;
    }

    public void setThreeTurnover(Integer threeTurnover) {
        this.threeTurnover = threeTurnover;
    }

    public Integer getThreeTurnoverRate() {
        return threeTurnoverRate;
    }

    public void setThreeTurnoverRate(Integer threeTurnoverRate) {
        this.threeTurnoverRate = threeTurnoverRate;
    }

    public Integer getThreeVolume() {
        return threeVolume;
    }

    public void setThreeVolume(Integer threeVolume) {
        this.threeVolume = threeVolume;
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

    public String getYybName() {
        return yybName;
    }

    public void setYybName(String yybName) {
        this.yybName = yybName;
    }

    public Integer getTwoDay() {
        return twoDay;
    }

    public void setTwoDay(Integer twoDay) {
        this.twoDay = twoDay;
    }

    public Integer getThreeDay() {
        return threeDay;
    }

    public void setThreeDay(Integer threeDay) {
        this.threeDay = threeDay;
    }

    public Integer getFiveDay() {
        return fiveDay;
    }

    public void setFiveDay(Integer fiveDay) {
        this.fiveDay = fiveDay;
    }

    public Integer getTenDay() {
        return tenDay;
    }

    public void setTenDay(Integer tenDay) {
        this.tenDay = tenDay;
    }

    public Integer getTwentyDay() {
        return twentyDay;
    }

    public void setTwentyDay(Integer twentyDay) {
        this.twentyDay = twentyDay;
    }

    public Integer getThirtyDay() {
        return thirtyDay;
    }

    public void setThirtyDay(Integer thirtyDay) {
        this.thirtyDay = thirtyDay;
    }

    public Integer getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Integer tradeAmount) {
        this.tradeAmount = tradeAmount;
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
