package com.example.demo.domain.table;

import com.example.demo.utils.MyUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity(name="stock_truth")
public class StockTruth {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false,columnDefinition="varchar(10)")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(1000)")
    private String truthInfo;
    public StockTruth(){
        this.dayFormat = MyUtils.getDayFormat();
        this.truthInfo = "没有数据";

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDayFormat() {
        return dayFormat;
    }

    public void setDayFormat(String dayFormat) {
        this.dayFormat = dayFormat;
    }

    public String getTruthInfo() {
        return truthInfo;
    }

    public void setTruthInfo(String truthInfo) {
        this.truthInfo = truthInfo;
    }
}
