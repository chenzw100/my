package com.example.demo.domain.table;

import com.example.demo.utils.MyUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 股市看三日富可敌国，股市看两日稳定复利。
 * 昨天，今天，明天
 * 今天竞价涨幅，相对于昨天收盘的涨幅 (todayOpenPrice-yesterdayPrice)/yesterdayPrice
 * 明天竞价涨幅，相对于今天开盘的涨幅 (tomorrowPrice-todayOpenPrice)/todayOpenPrice;此处就代表了盈利幅度
 *
 SELECT id,day_format,today_open_price,today_close_price,today_close_yield,tomorrow_open_price,tomorrow_close_price
 FROM stock_info s WHERE day_format ='20200410' and `name` ='云内动力';

 UPDATE stock_info SET tomorrow_open_price=1069, tomorrow_close_price=1181
 WHERE  day_format ='20200403' and `name` ='继峰股份';

 UPDATE stock_info SET today_open_price=1089,today_close_price=1201,day_format='20200407'
 WHERE  id=29855
 */
@Entity(name="stock_yyb_info")
public class StockYybInfo implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="bigint(11) DEFAULT 0 COMMENT 'yybid'")
    private Long stockYybId;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
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

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '开盘竞价'")
    private Integer todayOpenRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '收盘收益'")
    private Integer todayCloseYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日开盘收益'")
    private Integer tomorrowOpenYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日收盘收益'")
    private Integer tomorrowCloseYield;

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

    public Integer getTodayCloseYield() {
        //(todayClosePrice-todayPrice)/todayPrice

        return todayCloseYield;
    }

    public void setTodayCloseYield(Integer todayCloseYield) {
        this.todayCloseYield = todayCloseYield;
    }

    public Integer getTomorrowOpenYield() {
        //(tomorrowPrice-todayPrice)/todayPrice

        return tomorrowOpenYield;
    }

    public void setTomorrowOpenYield(Integer tomorrowOpenYield) {
        this.tomorrowOpenYield = tomorrowOpenYield;
    }

    public Integer getTomorrowCloseYield() {
        //(tomorrowPrice-todayPrice)/todayPrice

        return tomorrowCloseYield;
    }

    public void setTomorrowCloseYield(Integer tomorrowCloseYield) {
        this.tomorrowCloseYield = tomorrowCloseYield;
    }


    public StockYybInfo(){
        this.dayFormat = MyUtils.getTomorrowDayFormat();
        this.yesterdayClosePrice=10;
        this.todayOpenPrice=10;
        this.todayClosePrice=10;
        this.tomorrowOpenPrice=10;
        this.tomorrowClosePrice=10;
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
        this.todayOpenRate =MyUtils.getIncreaseRateCent(this.todayOpenPrice,this.yesterdayClosePrice).intValue();
    }

    public Integer getTodayClosePrice() {
        return todayClosePrice;
    }

    public void setTodayClosePrice(Integer todayClosePrice) {
        this.todayClosePrice = todayClosePrice;
        this.todayCloseYield =MyUtils.getIncreaseRateCent(this.todayClosePrice,this.todayOpenPrice).intValue();
    }

    public Integer getTomorrowOpenPrice() {
        return tomorrowOpenPrice;
    }

    public void setTomorrowOpenPrice(Integer tomorrowOpenPrice) {
        this.tomorrowOpenPrice = tomorrowOpenPrice;
        this.tomorrowOpenYield= MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.todayOpenPrice).intValue();
    }

    public Integer getTomorrowClosePrice() {
        return tomorrowClosePrice;
    }

    public void setTomorrowClosePrice(Integer tomorrowClosePrice) {
        this.tomorrowClosePrice = tomorrowClosePrice;
        this.tomorrowCloseYield= MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.todayOpenPrice).intValue();
    }

    public Integer getTodayOpenRate() {
        //(todayPrice-yesterdayPrice)/yesterdayPrice
        return todayOpenRate;
    }
    public void setTodayOpenRate(Integer todayOpenRate) {
        this.todayOpenRate = todayOpenRate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockYybId() {
        return stockYybId;
    }

    public void setStockYybId(Long stockYybId) {
        this.stockYybId = stockYybId;
    }
}
