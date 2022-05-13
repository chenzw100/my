package com.example.demo.domain.table;

import com.example.demo.enums.NumberEnum;
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
@Entity(name="stock_opt")
public class StockOpt implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日热搜'")
    private Integer hotValue;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '1-月,2,3-周'")
    private Integer hotType;
    @Transient
    private String plateName;
    @Transient
    private String today;

    public String getToday() {
        if(today==null){
            return "";
        }
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public Integer getHotType() {
        return hotType;
    }

    public void setHotType(Integer hotType) {
        this.hotType = hotType;
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

    public Integer getHotValue() {
        return hotValue;
    }

    public void setHotValue(Integer hotValue) {
        this.hotValue = hotValue;
    }

    @Override
    public String toString() {
        return
                dayFormat +
                        " Type=" + hotType +
                " [" + code +
                " " + name +
                        "] hot=" + hotValue +
                        " " + plateName +
                        " " + getToday() +
                "<br>";
    }
}
