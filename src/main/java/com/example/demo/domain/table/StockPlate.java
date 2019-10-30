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
 */
@Entity(name="stock_plate")
public class StockPlate implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(10)")
    private String plateCode;
    @Column(nullable = false,columnDefinition="varchar(20)")
    private String plateName;
    @Column(nullable = true,columnDefinition="varchar(50) COMMENT '描述'")
    private String description;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连续出现次数'")
    private Integer continuousCount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '热度排序'")
    private Integer hotSort;


    public StockPlate() {
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

    public String getPlateCode() {
        return plateCode;
    }

    public void setPlateCode(String plateCode) {
        this.plateCode = plateCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public Integer getContinuousCount() {
        return continuousCount;
    }

    public void setContinuousCount(Integer continuousCount) {
        this.continuousCount = continuousCount;
    }

    public Integer getHotSort() {
        return hotSort;
    }

    public void setHotSort(Integer hotSort) {
        this.hotSort = hotSort;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(plateCode).append(plateName).append("<br>");
        return sb.toString();
    }



}
