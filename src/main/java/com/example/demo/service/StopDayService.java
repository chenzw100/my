package com.example.demo.service;

import com.example.demo.dao.StockInfoRepository;
import com.example.demo.dao.StockStopInfoRepository;
import com.example.demo.dao.StockStopTemperatureRepository;
import com.example.demo.dao.StockTemperatureRepository;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockStopInfo;
import com.example.demo.domain.table.StockStopTemperature;
import com.example.demo.domain.table.StockTemperature;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public class StopDayService {
    @Autowired
    StockTemperatureRepository stockTemperatureRepository;
    @Autowired
    StockStopTemperatureRepository stockStopTemperatureRepository;
    @Autowired
    StockStopInfoRepository stockStopInfoRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;
    public void doDay(){
        List<StockTemperature> hotDays = stockTemperatureRepository.hotDay();
        for(StockTemperature s:hotDays){
            StockStopTemperature stockStopTemperature = new StockStopTemperature();
            BeanUtils.copyProperties(s,stockStopTemperature);
            stockStopTemperature.setId(null);
            stockStopTemperature.setType(1);
            Date now = MyUtils.getFormatDate(s.getDayFormat());
            ChineseWorkDay todayWorkDay  = new ChineseWorkDay(now);
            Date tomorrowDate = todayWorkDay.nextWorkDay();
            stockStopTemperature.setNextDayFormat(MyUtils.getDayFormat(tomorrowDate));

            stockStopTemperatureRepository.save(stockStopTemperature);
        }
        List<StockTemperature> iceDays = stockTemperatureRepository.iceDay();
        for(StockTemperature s:iceDays){
            StockStopTemperature stockStopTemperature = new StockStopTemperature();
            BeanUtils.copyProperties(s,stockStopTemperature);
            stockStopTemperature.setId(null);
            stockStopTemperature.setType(2);
            Date now = MyUtils.getFormatDate(s.getDayFormat());
            ChineseWorkDay todayWorkDay  = new ChineseWorkDay(now);
            Date tomorrowDate = todayWorkDay.nextWorkDay();
            stockStopTemperature.setNextDayFormat(MyUtils.getDayFormat(tomorrowDate));

            stockStopTemperatureRepository.save(stockStopTemperature);
        }
    }
    public void doOptDay(){
        List<StockInfo> days =stockInfoRepository.findByStockTypeOrderByDayFormatDesc(10);
        for(StockInfo s:days){
            StockStopInfo stop = new StockStopInfo();
            BeanUtils.copyProperties(s,stop);
            stop.setId(null);
            Date now = MyUtils.getFormatDate(s.getDayFormat());
            ChineseWorkDay todayWorkDay  = new ChineseWorkDay(now);
            Date tomorrowDate = todayWorkDay.nextWorkDay();
            stop.setNextDayFormat(MyUtils.getDayFormat(tomorrowDate));
            stop.getDownUp();
            stockStopInfoRepository.save(stop);
        }
        List<StockInfo> holds =stockInfoRepository.findByStockTypeOrderByDayFormatDesc(80);
        for(StockInfo s:holds){
            StockStopInfo stop = new StockStopInfo();
            BeanUtils.copyProperties(s,stop);
            stop.setId(null);
            Date now = MyUtils.getFormatDate(s.getDayFormat());
            ChineseWorkDay todayWorkDay  = new ChineseWorkDay(now);
            Date tomorrowDate = todayWorkDay.nextWorkDay();
            stop.setNextDayFormat(MyUtils.getDayFormat(tomorrowDate));
            stop.getDownUp();
            stockStopInfoRepository.save(stop);
        }
    }
}
