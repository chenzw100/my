package com.example.demo.domain.table;

import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 股市看三日富可敌国，股市看两日稳定复利。
 * 昨天，今天，明天
 * 今天竞价涨幅，相对于昨天收盘的涨幅 (todayOpenPrice-yesterdayPrice)/yesterdayPrice
 * 明天竞价涨幅，相对于今天开盘的涨幅 (tomorrowPrice-todayOpenPrice)/todayOpenPrice;此处就代表了盈利幅度
 */
@Entity(name="stock_mood")
public class StockMood implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(50) COMMENT 'master1'")
    private String masterLine;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT ''")
    private Integer moodType;

    public String getMasterLine() {
        return masterLine;
    }

    public void setMasterLine(String masterLine) {
        this.masterLine = masterLine;
    }

    public StockMood(){
        this.dayFormat = MyUtils.getDayFormat();
        this.moodType = NumberEnum.MoodType.DOWN_DOWN.getCode();

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

    public Integer getMoodType() {
        return moodType;
    }

    public void setMoodType(Integer moodType) {
        this.moodType = moodType;
    }

    @Override
    public String toString() {
        if(moodType==null || moodType==0){
            return "主线:"+masterLine ;
        }
        return  "【"+NumberEnum.MoodType.getMoodType(moodType)+"主线:"+masterLine +"】";
    }
}
