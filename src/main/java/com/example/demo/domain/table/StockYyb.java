package com.example.demo.domain.table;



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
@Entity(name="stock_yyb")
public class StockYyb implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(100)")
    private String yybName;
    @Column(nullable = false,columnDefinition="varchar(20) COMMENT '柚子'")
    private String yzName;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT 'yybId'")
    private Integer yybId;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '买入'")
    private Integer tradeAmount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '净额'")
    private Integer sumAmount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一天'")
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

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '688'")
    private Integer yn;

    public String getYzName() {
        return yzName;
    }

    public void setYzName(String yzName) {
        this.yzName = yzName;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Integer getYybId() {
        return yybId;
    }

    public void setYybId(Integer yybId) {
        this.yybId = yybId;
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

    public String getYybName() {
        return yybName;
    }

    public void setYybName(String yybName) {
        this.yybName = yybName;
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

    public Integer getOneDay() {
        return oneDay;
    }

    public void setOneDay(Integer oneDay) {
        this.oneDay = oneDay;
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

    @Override
    public String toString() {
        return
                ", yybId=" + yybId +
                ", yybName='" + yybName + '\'' +
                ", dayFormat='" + dayFormat + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", tradeAmount=" + tradeAmount +
                ", sumAmount=" + sumAmount +
                ", oneDay=" + oneDay +
                ", twoDay=" + twoDay +
                ", threeDay=" + threeDay +
                ", fiveDay=" + fiveDay +
                ", tenDay=" + tenDay +
                ", twentyDay=" + twentyDay +
                ", thirtyDay=" + thirtyDay +
                '}';
    }
}
