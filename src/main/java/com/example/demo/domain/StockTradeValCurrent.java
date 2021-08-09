package com.example.demo.domain;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *

 */
@Entity(name="stock_trade_val_current")
public class StockTradeValCurrent implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Date created;





    @Excel(name = "排名类型", orderNum = "0")
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '排名类型'")
    private Integer rankType;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    @Excel(name = "日期", orderNum = "1")
    private String dayFormat;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次开盘竞价'")
    private Integer oneOpenRate;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次收盘收益'")
    private Integer oneCloseRate;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次次日开盘收益'")
    private Integer oneOpenIncomeRate;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次次日收盘盘收益'")
    private Integer oneCloseIncomeRate;


    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次再次日开盘'")
    private Integer oneNextOpenIncomeRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '第一次再次日收盘'")
    private Integer oneNextCloseIncomeRate;



    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '688'")
    private Integer yn;

    public Integer getRankType() {
        return rankType;
    }

    public void setRankType(Integer rankType) {
        this.rankType = rankType;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDayFormat() {
        return dayFormat;
    }

    public void setDayFormat(String dayFormat) {
        this.dayFormat = dayFormat;
    }


    public Integer getOneOpenRate() {
        return oneOpenRate;
    }

    public void setOneOpenRate(Integer oneOpenRate) {
        this.oneOpenRate = oneOpenRate;
    }

    public Integer getOneCloseRate() {
        return oneCloseRate;
    }

    public void setOneCloseRate(Integer oneCloseRate) {
        this.oneCloseRate = oneCloseRate;
    }

    public Integer getOneOpenIncomeRate() {
        return oneOpenIncomeRate;
    }

    public void setOneOpenIncomeRate(Integer oneOpenIncomeRate) {
        this.oneOpenIncomeRate = oneOpenIncomeRate;
    }

    public Integer getOneCloseIncomeRate() {
        return oneCloseIncomeRate;
    }

    public void setOneCloseIncomeRate(Integer oneCloseIncomeRate) {
        this.oneCloseIncomeRate = oneCloseIncomeRate;
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

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String dateStr = DateFormatUtils.format(getCreated(), "MM-dd HH:mm");
        sb.append(dateStr).append(" "+NumberEnum.StockTradeType.getDesc(rankType));

        sb.append("[竞价:").append(oneOpenRate).append("][竞收:").append(oneCloseRate).append("][1开:").append(oneOpenIncomeRate).append("][1收:").append(oneCloseIncomeRate)
                .append("][再开:").append(oneNextOpenIncomeRate).append("][再收:").append(oneNextCloseIncomeRate).append("]<br>");

        return sb.toString();
    }
}
