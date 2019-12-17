package com.example.demo.service;

import com.example.demo.dao.StockInfoRepository;
import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

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
    public StockInfo save(StockInfo stockInfo){
        return stockInfoRepository.save(stockInfo);
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
    public List<StockInfo> findStockSpaceHeightsByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode());
    }
    public List<StockInfo> findStockSpaceHeightsByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode());
    }
    public StockInfo findStockSpaceHeightByCodeAndYesterdayFormat(String code){
        return stockInfoRepository.findByCodeAndDayFormatAndStockType(code,MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode());
    }
    //---STOCK_LIMIT_UP_FIVE---start ---
    public List<StockInfo> findStockLimitUpFivesByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_LIMIT_UP_FIVE.getCode());
    }
    public List<StockInfo> findStockLimitUpFivesByYesterdayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getYesterdayDayFormat(), NumberEnum.StockType.STOCK_LIMIT_UP_FIVE.getCode());
    }

    //---STOCK_DAY---start ---
    public List<StockInfo> findStockDaysByTodayFormat(){
        return stockInfoRepository.findByDayFormatAndStockTypeOrderByOpenBidRate(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_DAY.getCode());
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
