package com.example.demo.domain.table;

import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by chenzw on 2018/10/22.
 */
@Entity(name="stock_temperature")
public class StockTemperature {
    @Id
    @GeneratedValue
   private long id;
    @Column(nullable = false,columnDefinition="varchar(10)")
    private String dayFormat;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private int yesterdayShow;
    @Column(nullable = false)
    private int nowTemperature;
    @Column(nullable = false)
    private int raiseUp;
    @Column(nullable = false)
    private int downUp;
    @Column(nullable = false)
    private int limitUp;
    @Column(nullable = false)
    private int limitDown;
    @Column(nullable = false)
    private int open;
    @Column(nullable = false)
    private int brokenRatio;

    @Column(nullable = false)
    private int raise;
    @Column(nullable = false)
    private int down;
    @Column(nullable = false)
    private int type;
    @Column(nullable = false)
    private int tradeVal;
    @Column(nullable = false)
    private int tradeCYBVal;
    @Column(nullable = false)
    private String continueVal;
    @Column(nullable = false)
    private int strongDowns;
    @Column(nullable = false)
    private int continueCount;

    @Column(nullable = false)
    private int superCount;
    @Column(nullable = false)
    private int superUpCount;

    public int getTradeCYBVal() {
        return tradeCYBVal;
    }

    public void setTradeCYBVal(int tradeCYBVal) {
        this.tradeCYBVal = tradeCYBVal;
    }

    public int getSuperCount() {
        return superCount;
    }

    public void setSuperCount(int superCount) {
        this.superCount = superCount;
    }

    public int getSuperUpCount() {
        return superUpCount;
    }

    public void setSuperUpCount(int superUpCount) {
        this.superUpCount = superUpCount;
    }

    public int getContinueCount() {
        return continueCount;
    }

    public void setContinueCount(int continueCount) {
        this.continueCount = continueCount;
    }

    public int getStrongDowns() {
        return strongDowns;
    }

    public void setStrongDowns(int strongDowns) {
        this.strongDowns = strongDowns;
    }

    public int getTradeVal() {
        return tradeVal;
    }

    public void setTradeVal(int tradeVal) {
        this.tradeVal = tradeVal;
    }

    public StockTemperature(){
    }
    public StockTemperature(int type){
        this.type = type;
        this.created = MyUtils.getCurrentDate();
        this.dayFormat= MyUtils.getDayFormat();
    }

    public int getBrokenRatio() {
        return brokenRatio;
    }

    public void setBrokenRatio(int brokenRatio) {
        this.brokenRatio = brokenRatio;
    }

    public int getLimitUp() {
        return limitUp;
    }

    public void setLimitUp(int limitUp) {
        this.limitUp = limitUp;
    }

    public int getLimitDown() {
        return limitDown;
    }

    public void setLimitDown(int limitDown) {
        this.limitDown = limitDown;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getYesterdayShow() {
        return yesterdayShow;
    }

    public void setYesterdayShow(int yesterdayShow) {
        this.yesterdayShow = yesterdayShow;
    }

    public int getNowTemperature() {
        return nowTemperature;
    }
    public String getTemperature(){
        return MyUtils.getYuanByCent(getNowTemperature());
    }

    public void setNowTemperature(int nowTemperature) {
        this.nowTemperature = nowTemperature;
    }

    public int getRaiseUp() {
        return raiseUp;
    }

    public void setRaiseUp(int raiseUp) {
        this.raiseUp = raiseUp;
    }

    public int getDownUp() {
        return downUp;
    }

    public void setDownUp(int downUp) {
        this.downUp = downUp;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getRaise() {
        return raise;
    }

    public void setRaise(int raise) {
        this.raise = raise;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContinueVal() {
        return continueVal;
    }

    public void setContinueVal(String continueVal) {
        this.continueVal = continueVal;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        String dateStr = DateFormatUtils.format(getCreated(), "MM-dd HH:mm");
        sb.append(dateStr);
        sb.append("[上证:").append(getTradeVal()).append("亿、创:").append(getTradeCYBVal()).append("亿][强:").append(getSuperUpCount()).append(",大阳:").append(getSuperCount())
        .append(",正:").append(getContinueCount()).append("] [负:").append(getStrongDowns()).append(", 跌:").append(getLimitDown())
         .append("][涨:").append(getLimitUp()).append(", 炸:").append(getOpen())
        .append("] [昨:").append(MyUtils.getYuanByCent(getYesterdayShow()))
        .append("] [连:").append(getContinueVal())
        .append("] [炸:").append(MyUtils.getYuanByCent(getBrokenRatio()))
        .append("] [上涨:").append(getRaise()).append(", 下跌跌:").append(getDown()).append("]")
        .append("] 温度:").append(getTemperature()).append("<br>");

        return sb.toString();
    }
}
