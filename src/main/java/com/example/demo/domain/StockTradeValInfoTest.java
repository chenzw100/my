package com.example.demo.domain;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.example.demo.utils.MyUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 *

 */
@Entity(name="stock_trade_val_test")
public class StockTradeValInfoTest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '排名类型'")
    private Integer rankType;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '排名'")
    private Integer rank;

    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    @Excel(name = "日期", orderNum = "2")
    private String dayFormat;

    @Excel(name = "股票代码", orderNum = "0")
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Excel(name = "股票简称", orderNum = "1")
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;

    @Transient
    private String yesterdayTurnoverStr;
    @Transient
    private String yesterdayVolumeStr;

    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;

    @Transient
    private String tradeAmountStr;

    @Excel(name = "起始价", orderNum = "4")
    @Transient
    private String priceStr;




    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '买入'")
    private Integer tradeAmount;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次开盘竞价'")
    private Integer oneOpenRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次开盘竞价'")
    private Integer twoOpenRate;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次收盘收益'")
    private Integer oneCloseRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次收盘收益'")
    private Integer twoCloseRate;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次次日开盘收益'")
    private Integer oneOpenIncomeRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次次日开盘收益'")
    private Integer twoOpenIncomeRate;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次次日收盘盘收益'")
    private Integer oneCloseIncomeRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次次日收盘收益'")
    private Integer twoCloseIncomeRate;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次再次日开盘'")
    private Integer oneNextOpenIncomeRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次再次日收盘'")
    private Integer oneNextCloseIncomeRate;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日收盘'")
    private Integer yesterdayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日涨幅'")
    private Integer yesterdayGains;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日成交额'")
    private Integer yesterdayTurnover;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日换手率'")
    private Integer yesterdayTurnoverRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日成交量'")
    private Integer yesterdayVolume;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日开盘'")
    private Integer todayOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日收盘'")
    private Integer todayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日涨幅'")
    private Integer todayGains;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日成交额'")
    private Integer todayTurnover;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日换手率'")
    private Integer todayTurnoverRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日成交量'")
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
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日成交额'")
    private Integer tomorrowTurnover;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日换手率'")
    private Integer tomorrowTurnoverRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日成交量'")
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
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日成交额'")
    private Integer threeTurnover;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日换手率'")
    private Integer threeTurnoverRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日成交量'")
    private Integer threeVolume;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日最高'")
    private Integer threeHighPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日最低'")
    private Integer threeLowPrice;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '688'")
    private Integer yn;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日10点收盘'")
    private Integer tomorrowTenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日2点收盘'")
    private Integer tomorrowTwoPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日10点收盘'")
    private Integer threeTenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第三日2点收盘'")
    private Integer threeTwoPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次次10点日收盘盘收益'")
    private Integer oneTenIncomeRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次次日10点收盘收益'")
    private Integer twoTenIncomeRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次次日2点收盘盘收益'")
    private Integer oneTwoIncomeRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第二次次日2点收盘收益'")
    private Integer twoTwoIncomeRate;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '当日尾盘买收益'")
    private Integer todayIncomeRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '次日尾盘买收益'")
    private Integer tomorrowIncomeRate;

    @Transient
    private String codeStr;

    public String getCodeStr() {

        return codeStr;
    }

    public Integer getTodayIncomeRate() {
        this.todayIncomeRate = MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.todayClosePrice).intValue();
        return todayIncomeRate;
    }

    public void setTodayIncomeRate(Integer todayIncomeRate) {
        this.todayIncomeRate = todayIncomeRate;
    }

    public Integer getTomorrowIncomeRate() {
        this.tomorrowIncomeRate = MyUtils.getIncreaseRateCent(this.threeClosePrice,this.tomorrowClosePrice).intValue();
        return tomorrowIncomeRate;
    }

    public void setTomorrowIncomeRate(Integer tomorrowIncomeRate) {
        this.tomorrowIncomeRate = tomorrowIncomeRate;
    }

    public void setCodeStr(String codeStr) {
        this.codeStr = codeStr;
    }

    public Integer getTomorrowTenPrice() {
        return tomorrowTenPrice;
    }

    public void setTomorrowTenPrice(Integer tomorrowTenPrice) {
        this.tomorrowTenPrice = tomorrowTenPrice;
        this.oneTenIncomeRate = MyUtils.getIncreaseRateCent(this.tomorrowTenPrice,this.todayOpenPrice).intValue();
    }

    public Integer getTomorrowTwoPrice() {
        return tomorrowTwoPrice;
    }

    public void setTomorrowTwoPrice(Integer tomorrowTwoPrice) {
        this.tomorrowTwoPrice = tomorrowTwoPrice;
        this.oneTwoIncomeRate = MyUtils.getIncreaseRateCent(this.tomorrowTwoPrice,this.todayOpenPrice).intValue();
    }

    public Integer getThreeTenPrice() {
        return threeTenPrice;
    }

    public void setThreeTenPrice(Integer threeTenPrice) {
        this.threeTenPrice = threeTenPrice;
        this.twoTenIncomeRate = MyUtils.getIncreaseRateCent(this.threeTenPrice,this.tomorrowOpenPrice).intValue();
    }

    public Integer getThreeTwoPrice() {
        return threeTwoPrice;
    }

    public void setThreeTwoPrice(Integer threeTwoPrice) {
        this.threeTwoPrice = threeTwoPrice;
        this.twoTwoIncomeRate = MyUtils.getIncreaseRateCent(this.threeTwoPrice,this.tomorrowOpenPrice).intValue();
    }

    public Integer getOneTenIncomeRate() {
        return oneTenIncomeRate;
    }

    public void setOneTenIncomeRate(Integer oneTenIncomeRate) {
        this.oneTenIncomeRate = oneTenIncomeRate;
    }

    public Integer getTwoTenIncomeRate() {
        return twoTenIncomeRate;
    }

    public void setTwoTenIncomeRate(Integer twoTenIncomeRate) {
        this.twoTenIncomeRate = twoTenIncomeRate;
    }

    public Integer getOneTwoIncomeRate() {
        return oneTwoIncomeRate;
    }

    public void setOneTwoIncomeRate(Integer oneTwoIncomeRate) {
        this.oneTwoIncomeRate = oneTwoIncomeRate;
    }

    public Integer getTwoTwoIncomeRate() {
        return twoTwoIncomeRate;
    }

    public void setTwoTwoIncomeRate(Integer twoTwoIncomeRate) {
        this.twoTwoIncomeRate = twoTwoIncomeRate;
    }
    public Integer getRankType() {
        return rankType;
    }

    public void setRankType(Integer rankType) {
        this.rankType = rankType;
    }





    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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


    public Integer getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Integer tradeAmount) {
        this.tradeAmount = tradeAmount;
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
        if(oneOpenIncomeRate==null){
            oneOpenIncomeRate=0;
        }
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
        if(oneNextOpenIncomeRate==null){
            oneNextOpenIncomeRate=0;
        }
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
        getTomorrowIncomeRate();
        getTodayIncomeRate();
        return "StockYybRecord{" +
                "dayFormat=" + dayFormat +
                "name=" + name +
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
    public void toOneDay() {
        this.oneOpenRate = MyUtils.getIncreaseRateCent(this.todayOpenPrice,this.yesterdayClosePrice).intValue();
        this.oneCloseRate = MyUtils.getIncreaseRateCent(this.todayClosePrice,this.todayOpenPrice).intValue();
    }
    public void toOneOpen() {
        this.oneOpenRate = MyUtils.getIncreaseRateCent(this.todayOpenPrice,this.yesterdayClosePrice).intValue();
    }
    public void toOneIncome() {
        this.oneOpenIncomeRate = MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.todayOpenPrice).intValue();
        this.oneCloseIncomeRate = MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.todayOpenPrice).intValue();
        this.twoOpenRate = MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.todayClosePrice).intValue();
        this.twoCloseRate = MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.tomorrowOpenPrice).intValue();
    }
    public void toOneIncomeOpen() {
        this.oneOpenIncomeRate = MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.todayOpenPrice).intValue();
    }
    public void toOneNextOpenIncomeRateOpen() {
        this.oneNextOpenIncomeRate = MyUtils.getIncreaseRateCent(this.threeOpenPrice,this.todayOpenPrice).intValue();
    }
    public String toInfo() {
        return
                "[id=" + id +
                "dayFormat=" + dayFormat +
                "name=" + name +
                "yesterdayGains=" + yesterdayGains
               ;
    }

    public String getYesterdayTurnoverStr() {
        return yesterdayTurnoverStr;
    }

    public void setYesterdayTurnoverStr(String yesterdayTurnoverStr) {
        this.yesterdayTurnoverStr = yesterdayTurnoverStr;
    }

    public String getTradeAmountStr() {
        return tradeAmountStr;
    }

    public void setTradeAmountStr(String tradeAmountStr) {
        this.tradeAmountStr = tradeAmountStr;
    }

    public String getYesterdayVolumeStr() {
        return yesterdayVolumeStr;
    }

    public void setYesterdayVolumeStr(String yesterdayVolumeStr) {
        this.yesterdayVolumeStr = yesterdayVolumeStr;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public String toInfoTen() {
        return
                "[id=" + id +
                        "dayFormat=" + dayFormat +
                        "name=" + name + "code=" + code +
                        ",oneTenIncomeRate=" + oneTenIncomeRate +
                        ",tomorrowTenPrice=" + tomorrowTenPrice+
                        ",twoTenIncomeRate=" + twoTenIncomeRate +
                        ",threeTenPrice=" + threeTenPrice

                ;
    }
}
