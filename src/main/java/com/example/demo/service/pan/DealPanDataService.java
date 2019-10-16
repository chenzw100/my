package com.example.demo.service.pan;

import com.example.demo.dao.*;
import com.example.demo.domain.SinaTinyInfoStock;
import com.example.demo.domain.table.*;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.qt.QtService;
import com.example.demo.service.sina.SinaService;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DealPanDataService extends QtService {

    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;

    @Autowired
    SinaService sinaService;

    public void open(){
        PRICE_CACHE.clear();
        log.info("data open start");
        openStockInfo();
        log.info("data open end");
        PRICE_CACHE.clear();
    }
    public void close(){
        SINA_CACHE.clear();
        log.info("data close start");
        fiveStatistic();
        closeStockInfo();
        log.info("data close end");
        SINA_CACHE.clear();
    }
    private void openStockInfo(){
        List<StockInfo> todayStocks = stockInfoService.findStockInfosByTodayFormat();
        if(todayStocks!=null){
            for(StockInfo myStock :todayStocks){
                myStock.setTodayOpenPrice(getIntCurrentPrice(myStock.getCode()));
                stockInfoService.save(myStock);
            }
        }
        List<StockInfo> yesterdayStocks = stockInfoService.findStockInfosByYesterdayFormat();
        if(yesterdayStocks!=null){
            for(StockInfo myStock :yesterdayStocks){
                myStock.setTomorrowOpenPrice(getIntCurrentPrice(myStock.getCode()));
                stockInfoService.save(myStock);
            }
        }
    }

    private void closeStockInfo(){
        List<StockInfo> todayStocks = stockInfoService.findStockInfosByTodayFormat();
        if(todayStocks!=null){
            for(StockInfo myStock :todayStocks){
                myStock.setTodayClosePrice(getIntCurrentPrice(myStock.getCode()));
                stockInfoService.save(myStock);
            }
        }
        List<StockInfo> yesterdayStocks = stockInfoService.findStockInfosByYesterdayFormat();
        if(yesterdayStocks!=null){
            for(StockInfo myStock :yesterdayStocks){
                myStock.setTomorrowClosePrice(getIntCurrentPrice(myStock.getCode()));
                stockInfoService.save(myStock);
            }
        }
    }


    private void fiveStatistic(){
        String end=MyUtils.getDayFormat();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(3,MyUtils.getCurrentDate()));
        List<StockInfo> xgbFiveUpStocks = stockInfoService.fiveStatistic(start, end);
        log.info(start+"-"+end+",--->fiveStatistic count:"+xgbFiveUpStocks.size());
        if(xgbFiveUpStocks.size()>0){
            for (StockInfo xgbFiveUpStock : xgbFiveUpStocks){
                SinaTinyInfoStock tinyInfoStock = sinaService.getTiny(xgbFiveUpStock.getCode());
                log.info(xgbFiveUpStock.getShowCount()+xgbFiveUpStock.getCode()+",fiveStatistic High:"+xgbFiveUpStock.getFiveHighPrice()+",low:"+xgbFiveUpStock.getFiveLowPrice()+"==>new High:"+tinyInfoStock.getHighPrice()+",new Low:"+tinyInfoStock.getLowPrice());
                if(tinyInfoStock.getHighPrice()>xgbFiveUpStock.getFiveHighPrice().intValue()){
                    xgbFiveUpStock.setFiveHighPrice(tinyInfoStock.getHighPrice());
                }
                if(tinyInfoStock.getLowPrice()>xgbFiveUpStock.getFiveLowPrice().intValue()){
                    xgbFiveUpStock.setFiveLowPrice(tinyInfoStock.getLowPrice());
                }
                if(xgbFiveUpStock.getTodayOpenPrice().intValue()==10){
                    xgbFiveUpStock.setTodayOpenPrice(tinyInfoStock.getOpenPrice());
                }
                stockInfoService.save(xgbFiveUpStock);
            }
        }
    }


}
