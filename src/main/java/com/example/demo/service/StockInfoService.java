package com.example.demo.service;

import com.example.demo.dao.StockInfoRepository;
import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 STOCK_DAY(10,"当天"),
 STOCK_CURRENT(20,"当天实时"),
 STOCK_DOWN(30,"弱势"),
 STOCK_SPACE_HEIGHT(40,"空间"),
 STOCK_LIMIT_UP_FIVE(50,"五版"),
 STOCK_CURRENT_FIVE(60,"五日实时"),
 STOCK_DAY_FIVE(70,"五日当天"),
 */
@Component
public class StockInfoService {

    @Autowired
    StockInfoRepository stockInfoRepository;
    public List<StockInfo> fupan(String dayFormat){
        Date endDate =  MyUtils.getFormatDate(dayFormat);
        String yesterday =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        List<StockInfo> result = new ArrayList<>();
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByOpenBidRateDesc(dayFormat,NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByOpenBidRate(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo("昨天情况"));
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(yesterday, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowOpenYield(yesterday, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo("收盘情况"));
        result.addAll(stockInfoRepository.findFirst3ByDayFormatAndStockTypeOrderByTodayCloseYieldDesc(dayFormat,NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTodayCloseYield(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo("昨天情况"));
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowCloseYieldDesc(yesterday, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowCloseYield(yesterday, NumberEnum.StockType.STOCK_DAY.getCode()));
        return result;
    }

    //竞价前3
    public List<StockInfo> openTop3(String dayFormat){
        List<StockInfo> result = new ArrayList<>();
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByOpenBidRateDesc(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByOpenBidRate(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowOpenYield(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        return result;
    }
    //竞价末3
    public List<StockInfo> closeTop3(String dayFormat){
        List<StockInfo> result = new ArrayList<>();
        result.addAll(stockInfoRepository.findFirst3ByDayFormatAndStockTypeOrderByTodayCloseYieldDesc(dayFormat,NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowCloseYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTodayCloseYield(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        result.add(new StockInfo());
        result.addAll(stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowCloseYield(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode()));
        return result;
    }



    public StockInfo findFirst1ByCodeAndDayFormat(String code, String dayFormat){
       return stockInfoRepository.findFirst1ByCodeAndDayFormat(code,dayFormat);
    }

    public StockInfo save(StockInfo stockInfo){
        return stockInfoRepository.save(stockInfo);
    }
    public List<StockInfo> findByDayFormatOrderByFiveHighYieldDescTop3(String dayFormat){
        return stockInfoRepository.findFirst3ByDayFormatOrderByFiveHighYieldDesc(dayFormat);
    }
    public List<StockInfo> findByDayFormatOrderByOpenBidRateDesc(String dayFormat){
        return stockInfoRepository.findByDayFormatOrderByOpenBidRateDesc(dayFormat);
    }
    public List<StockInfo> findStockInfosByDayFormatOrderByOpenBidRate(String dayFormat){
        return stockInfoRepository.findByDayFormatAndOpenBidRateLessThanOrderByOpenBidRateDesc(dayFormat,-698);
    }
    public List<StockInfo> findStockInfosByDayFormatOrderByStockType(String dayFormat){
        return stockInfoRepository.findByDayFormatOrderByStockTypeDesc(dayFormat);
    }
    public List<StockInfo> fiveStatistic(String start, String end){
        return stockInfoRepository.fiveStatistic(start,end);
    }
    public List<MyTotalStock> fiveDayInfo(String start, String end){
        return stockInfoRepository.fiveDayInfo(start,end);
    }
    public List<StockInfo> findStockInfosByTodayFormat(){
        return stockInfoRepository.findByDayFormatOrderByStockTypeDesc(MyUtils.getDayFormat());
    }
    public List<StockInfo> findStockInfosByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatOrderByStockTypeDesc(MyUtils.getYesterdayDayFormat());
    }
    public List<StockInfo> findByDayFormatAndStockTypeOrderByOpenBidRate(String dayFormat,int type){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(dayFormat, type);
    }
    public StockInfo findStockCurrentByCodeAndYesterdayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_CURRENT.getCode());
    }
    //---STOCK_DOWN---start ---
    public List<StockInfo> findStockDownsByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_DOWN.getCode());
    }
    public List<StockInfo> findStockDownsByTomorrowFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getTomorrowDayFormat(), NumberEnum.StockType.STOCK_DOWN.getCode());
    }
    public StockInfo findStockDownsByCodeTodayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_DOWN.getCode());
    }

    public List<StockInfo> findStockDownsByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_DOWN.getCode());
    }

    //---STOCK_SPACE_HEIGHT---start ---
    public StockInfo findStockSpaceHeightByDayFormat(String dayFormat){
        List<StockInfo> result = stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(dayFormat, NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode());
        if(result!=null && result.size()>0){
            return result.get(0);
        }
        return new StockInfo();
    }
    public List<StockInfo> findStockSpaceHeightsByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode());
    }
    public List<StockInfo> findStockSpaceHeightsByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode());
    }
    public StockInfo findStockSpaceHeightByCodeAndYesterdayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode());
    }
    public List<StockInfo> fiveHeightSpace(String start, String end){
        return stockInfoRepository.fiveHeightSpace(start,end);
    }
    //---STOCK_LIMIT_UP_FIVE---start ---
    public List<StockInfo> findStockLimitUpFivesByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_LIMIT_UP_FIVE.getCode());
    }
    public List<StockInfo> findStockLimitUpFivesByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_LIMIT_UP_FIVE.getCode());
    }

    //---STOCK_DAY---start ---
    public List<StockInfo> findStockDaysByDayFormatOpen(String dayFormat,int type){
        return stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByOpenBidRateDesc(dayFormat, type);
    }

    public List<StockInfo> findStockDaysByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_DAY.getCode());
    }
    public List<StockInfo> findStockDaysByDayFormat(String dayFormat){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode());
    }
    public List<StockInfo> findStockDaysByDayFormatTodayCloseYield(String dayFormat){
        return stockInfoRepository.findFirst3ByDayFormatAndStockTypeOrderByTodayCloseYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode());
    }
    public List<StockInfo> findStockDaysByDayFormatTomorrowOpenYield(String dayFormat){
        return stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode());
    }
    public List<StockInfo> findStockDaysByDayFormatTomorrowCloseYield(String dayFormat){
        return stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowCloseYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY.getCode());
    }
    public List<StockInfo> findStockDaysByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_DAY.getCode());
    }
    public StockInfo findStockDaysByCodeYesterdayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_DAY.getCode());
    }
    //---STOCK_CURRENT_FIVE---start ---
    public List<StockInfo> findStockCurrentFivesByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_CURRENT_FIVE.getCode());
    }
    public List<StockInfo> findStockCurrentFivesByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_CURRENT_FIVE.getCode());
    }
    public StockInfo findStockCurrentFiveByCodeAndYesterdayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_CURRENT_FIVE.getCode());
    }
    //---STOCK_DAY_FIVE---start ---
    public List<StockInfo> findStockDayFivesByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }
    public List<StockInfo> findStockDayFivesByDayFormat(String dayFormat){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(dayFormat, NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }
    public List<StockInfo> findStockDayFivesByDayFormatTodayCloseYield(String dayFormat){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByTodayCloseYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }
    public List<StockInfo> findStockDayFivesByDayFormatTomorrowOpenYield(String dayFormat){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }
    public List<StockInfo> findStockDayFivesHotSevenDesc(String dayFormat){
        return stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByHotSevenDesc(dayFormat, NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }
    public List<StockInfo> findStockFivesTomorrowOpenYield(String dayFormat){
        return stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }
    public List<StockInfo> findStockFivesTomorrowCloseYield(String dayFormat){
        return stockInfoRepository.findFirst2ByDayFormatAndStockTypeOrderByTomorrowCloseYieldDesc(dayFormat, NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }
    public List<StockInfo> findStockDayFivesByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }
    public StockInfo findStockDayFiveByCodeAndYesterdayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
    }

    public StockInfo findStockKplByCodeAndYesterdayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_KPL.getCode());
    }
    public StockInfo findStockKplByCodeAndTodayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_KPL.getCode());
    }

}
