package com.example.demo.domain.table;

import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;

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
@Entity(name="stock_info")
public class StockInfo implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,columnDefinition="varchar(10) COMMENT 'yyyymmdd'")
    private String dayFormat;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String code;
    @Column(nullable = false,columnDefinition="varchar(8)")
    private String name;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '热门排序'")
    private Integer hotSort;
    @Column(nullable = true,columnDefinition="varchar(200) COMMENT '板块'")
    private String plateName;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '0未涨停;1涨停'")
    private Integer limitUp;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开盘竞价'")
    private Integer openBidRate;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日热搜'")
    private Integer hotValue;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '最近7日'")
    private Integer hotSeven;

    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '昨日收盘'")
    private Integer yesterdayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '连板'")
    private Integer continuous;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '开板次数'")
    private Integer openCount;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '大于0开板'")
    private Integer oneFlag;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT 'stock_down，stock_space_height,stock_limit_up_five,stock_day，stock_current_five，stock_day_five'")
    private Integer stockType;




    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日开盘'")
    private Integer todayOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '今日收盘'")
    private Integer todayClosePrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天开盘'")
    private Integer tomorrowOpenPrice;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明天收盘'")
    private Integer tomorrowClosePrice;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '5日最高'")
    private Integer fiveHighPrice;
    @Column(nullable = false,columnDefinition="int(11) DEFAULT 0 COMMENT '5日最低'")
    private Integer fiveLowPrice;

    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '出现次数'")
    private Integer showCount;

    @Transient
    private String todayOpenRate;
    @Transient
    private String todayCloseRate;
    @Transient
    private String tomorrowOpenRate;
    @Transient
    private String tomorrowCloseRate;
    @Transient
    private Integer totalCount;
    @Transient
    private String fiveHighRate;
    @Transient
    private String fiveLowRate;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '收盘收益'")
    private Integer todayCloseYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日开盘收益'")
    private Integer tomorrowOpenYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '明日收盘收益'")
    private Integer tomorrowCloseYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '五日最高收益'")
    private Integer fiveHighYield;
    @Column(nullable = true,columnDefinition="int(11) DEFAULT 0 COMMENT '五日最低收益'")
    private Integer fiveLowYield;

    @Transient
    private String todayClose;
    /**
     * 昨日收盘价
     */
    @Transient
    private String yesterdayClose;
    /**
     * 今日收盘价
     */
    @Transient
    private String todayOpen;
    /**
     * 收盘收益
     */
    @Transient
    private String todayCloseEarnings;
    /**
     * 明日开盘收益
     */
    @Transient
    private String tomorrowOpenEarnings;
    /**
     * 明日收盘收益 Earnings
     */
    @Transient
    private String tomorrowCloseEarnings;

    public String getTodayCloseEarnings() {
        return MyUtils.getYuanByCent(getTodayCloseYield());
    }

    public void setTodayCloseEarnings(String todayCloseEarnings) {
        this.todayCloseEarnings = todayCloseEarnings;
    }

    public String getTomorrowOpenEarnings() {
        if(StringUtils.isNotBlank(tomorrowOpenEarnings)){
            return tomorrowOpenEarnings;
        }
        return MyUtils.getYuanByCent(getTomorrowOpenYield());
    }

    public void setTomorrowOpenEarnings(String tomorrowOpenEarnings) {
        this.tomorrowOpenEarnings = tomorrowOpenEarnings;
    }

    public String getTomorrowCloseEarnings() {
        return MyUtils.getYuanByCent(getTomorrowCloseYield());
    }

    public void setTomorrowCloseEarnings(String tomorrowCloseEarnings) {
        this.tomorrowCloseEarnings = tomorrowCloseEarnings;
    }

    public String getYesterdayClose() {
        return MyUtils.getYuanByCent(getYesterdayClosePrice());
    }

    public void setYesterdayClose(String yesterdayClose) {
        this.yesterdayClose = yesterdayClose;
    }

    public String getTodayOpen() {
        return MyUtils.getYuanByCent(getTodayOpenPrice());
    }

    public void setTodayOpen(String todayOpen) {
        this.todayOpen = todayOpen;
    }

    public String getTodayClose() {
        return MyUtils.getIncreaseRate(getTodayClosePrice(),getYesterdayClosePrice()).toString();
    }

    public void setTodayClose(String todayClose) {
        this.todayClose = todayClose;
    }

    public Integer getTodayCloseYield() {
        //(todayClosePrice-todayPrice)/todayPrice
        todayCloseYield =MyUtils.getIncreaseRateCent(getTodayClosePrice(),getTodayOpenPrice()).intValue();
        return todayCloseYield;
    }

    public void setTodayCloseYield(Integer todayCloseYield) {
        this.todayCloseYield = todayCloseYield;
    }

    public Integer getTomorrowOpenYield() {
        //(tomorrowPrice-todayPrice)/todayPrice
        tomorrowOpenYield= MyUtils.getIncreaseRateCent(getTomorrowOpenPrice(),getTodayOpenPrice()).intValue();
        return tomorrowOpenYield;
    }

    public void setTomorrowOpenYield(Integer tomorrowOpenYield) {
        this.tomorrowOpenYield = tomorrowOpenYield;
    }

    public Integer getTomorrowCloseYield() {
        //(tomorrowPrice-todayPrice)/todayPrice
        tomorrowCloseYield= MyUtils.getIncreaseRateCent(getTomorrowClosePrice(),getTodayOpenPrice()).intValue();
        return tomorrowCloseYield;
    }

    public void setTomorrowCloseYield(Integer tomorrowCloseYield) {
        this.tomorrowCloseYield = tomorrowCloseYield;
    }

    public Integer getFiveHighYield() {
        fiveHighYield= MyUtils.getIncreaseRateCent(getFiveHighPrice(),getTodayOpenPrice()).intValue();
        return fiveHighYield;
    }

    public void setFiveHighYield(Integer fiveHighYield) {
        this.fiveHighYield = fiveHighYield;
    }

    public Integer getFiveLowYield() {
        fiveLowYield=MyUtils.getIncreaseRateCent(getFiveLowPrice(),getTodayOpenPrice()).intValue();
        return fiveLowYield;
    }

    public void setFiveLowYield(Integer fiveLowYield) {
        this.fiveLowYield = fiveLowYield;
    }

    public StockInfo() {
    }
    public StockInfo(String name) {
        this.name = name;
    }

    public StockInfo(String code, String name, Integer stockType){
        this.code =code;
        this.name = name;
        this.stockType = stockType;
        if(stockType== NumberEnum.StockType.STOCK_DOWN.getCode()||stockType== NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode()||stockType== NumberEnum.StockType.STOCK_LIMIT_UP_FIVE.getCode()){
            this.dayFormat = MyUtils.getTomorrowDayFormat();
        }else {
            this.dayFormat = MyUtils.getDayFormat();
        }
        this.yesterdayClosePrice=10;
        this.todayOpenPrice=10;
        this.todayClosePrice=10;
        this.tomorrowOpenPrice=10;
        this.tomorrowClosePrice=10;
        this.fiveHighPrice=10;
        this.fiveLowPrice=10;
        this.oneFlag=-1;
        this.openCount=-1;
        this.continuous=-1;
        this.showCount=-1;
        this.hotSeven=-1;
        this.hotSort=-1;
        this.hotValue=-1;
    }

    public Integer getStockType() {
        return stockType;
    }
    public String getStockTypeName(){
        return NumberEnum.StockType.getStockType(getStockType());
    }

    public void setStockType(Integer stockType) {
        this.stockType = stockType;
    }

    public String getFiveHighRate() {
        return MyUtils.getIncreaseRate(getFiveHighPrice(),getTodayOpenPrice()).toString();
    }

    public void setFiveHighRate(String fiveHighRate) {
        this.fiveHighRate = fiveHighRate;
    }

    public String getFiveLowRate() {
        return MyUtils.getIncreaseRate(getFiveLowPrice(),getTodayOpenPrice()).toString();
    }

    public void setFiveLowRate(String fiveLowRate) {
        this.fiveLowRate = fiveLowRate;
    }

    public Integer getFiveHighPrice() {
        return fiveHighPrice;
    }

    public void setFiveHighPrice(Integer fiveHighPrice) {
        this.fiveHighPrice = fiveHighPrice;
    }

    public Integer getFiveLowPrice() {
        return fiveLowPrice;
    }

    public void setFiveLowPrice(Integer fiveLowPrice) {
        this.fiveLowPrice = fiveLowPrice;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getShowCount() {
        return showCount;
    }

    public void setShowCount(Integer showCount) {
        this.showCount = showCount;
    }

    public Integer getLimitUp() {
        return limitUp;
    }

    public void setLimitUp(Integer limitUp) {
        this.limitUp = limitUp;
    }

    public String getPlateName() {
        if(plateName==null){
            plateName="";
        }
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public Integer getHotSort() {
        return hotSort;
    }

    public void setHotSort(Integer hotSort) {
        this.hotSort = hotSort;
    }

    public Integer getOneFlag() {
        return oneFlag;
    }

    public void setOneFlag(Integer oneFlag) {
        this.oneFlag = oneFlag;
    }



    public Integer getContinuous() {
        return continuous;
    }

    public void setContinuous(Integer continuous) {
        this.continuous = continuous;
    }




    public Integer getHotValue() {
        return hotValue;
    }

    public void setHotValue(Integer hotValue) {
        this.hotValue = hotValue;
    }

    public Integer getHotSeven() {
        if(hotSeven == null){
            hotSeven =0;
        }
        return hotSeven;
    }

    public void setHotSeven(Integer hotSeven) {
        this.hotSeven = hotSeven;
    }

    public Integer getOpenCount() {
        return openCount;
    }

    public void setOpenCount(Integer openCount) {
        this.openCount = openCount;
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
        getOpenBidRate();
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
    public Integer getOpenBidRate() {
        openBidRate =MyUtils.getIncreaseRateCent(getTodayOpenPrice(),getYesterdayClosePrice()).intValue();
        return openBidRate;
    }

    public void setOpenBidRate(Integer openBidRate) {
        this.openBidRate = openBidRate;
    }
    public String getTodayOpenRate() {
        if(StringUtils.isNotBlank(todayOpenRate)){
            return todayOpenRate;
        }
        //(todayPrice-yesterdayPrice)/yesterdayPrice
        todayOpenRate =MyUtils.getYuanByCent(getOpenBidRate());
        return todayOpenRate;
    }
    public void setTodayOpenRate(String todayOpenRate) {
        this.todayOpenRate = todayOpenRate;
    }
    public String getTodayCloseRate() {
        //(todayClosePrice-todayPrice)/todayPrice
        return MyUtils.getIncreaseRate(getTodayClosePrice(),getTodayOpenPrice()).toString();
    }
    public void setTodayCloseRate(String todayCloseRate) {
        this.todayCloseRate = todayCloseRate;
    }

    public String getTomorrowOpenRate() {
        //(tomorrowPrice-todayPrice)/todayPrice
        return MyUtils.getIncreaseRate(getTomorrowOpenPrice(),getTodayOpenPrice()).toString();
    }
    public void setTomorrowOpenRate(String tomorrowOpenRate) {
        this.tomorrowOpenRate = tomorrowOpenRate;
    }

    public String getTomorrowCloseRate() {
        //(tomorrowPrice-todayPrice)/todayPrice
        return MyUtils.getIncreaseRate(getTomorrowClosePrice(),getTodayOpenPrice()).toString();
    }
    public void setTomorrowCloseRate(String tomorrowCloseRate) {
        this.tomorrowCloseRate = tomorrowCloseRate;
    }


    public String toString(){
        if(code==null){
            return name+"--<br>";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dayFormat).append(NumberEnum.StockType.getStockType(stockType)).append(code).append(name).append("[最高:").append(getFiveHighRate()).append("最低:").append(getFiveLowRate()).
                append(",连板:").append(getContinuous()).append(",昨收:").append(MyUtils.getYuanByCent(getYesterdayClosePrice())).append("][竞价:").append(getTodayOpenRate()).append(",收盘").append(getTodayClose()).
                append("],收盘收益:").append(getTodayCloseRate()).
                append(",明天:").append(getTomorrowOpenRate()).append(":").append(getTomorrowCloseRate()).
                append("[七日:").append(getHotSeven()).append(",热值:").append(getHotValue()).append(",热序").append(getHotSort()).append("]").
                append(getPlateName()).append("<br>");
        return sb.toString();
    }



}
