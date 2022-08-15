package com.example.demo.service.pan;

import com.example.demo.dao.*;
import com.example.demo.domain.QtStock;
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
    StockYybInfoRepository stockYybInfoRepository;

    @Autowired
    StockLimitUpRepository stockLimitUpRepository;

    @Autowired
    SinaService sinaService;

    public void open(){
        PRICE_CACHE.clear();
        log.info("data open start");
        openStockInfo();
        openStockLimitUp();
        openStockYybInfo();
        log.info("data open end");
        PRICE_CACHE.clear();
    }
    public void close(){
        SINA_CACHE.clear();
        PRICE_CACHE.clear();
        log.info("data close start");
        fiveStatistic();
        closeStockInfo();
        closeStockLimitUp();
        closeStockYybInfo();
        log.info("data close end");
        SINA_CACHE.clear();
        PRICE_CACHE.clear();
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
                myStock.getTomorrowOpenYield();
                stockInfoService.save(myStock);
            }
        }
    }
    private void openStockYybInfo(){
        List<StockYybInfo> todayStocks = stockYybInfoRepository.findByDayFormat(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(StockYybInfo myStock :todayStocks){
                myStock.setTodayOpenPrice(getIntCurrentPrice(myStock.getCode()));
                stockYybInfoRepository.save(myStock);
            }
        }
        List<StockYybInfo> yesterdayStocks = stockYybInfoRepository.findByDayFormat(MyUtils.getYesterdayDayFormat());
        if(yesterdayStocks!=null){
            for(StockYybInfo myStock :yesterdayStocks){
                myStock.setTomorrowOpenPrice(getIntCurrentPrice(myStock.getCode()));
                myStock.getTomorrowOpenYield();
                stockYybInfoRepository.save(myStock);
            }
        }
        List<StockYybInfo> threeStocks = stockYybInfoRepository.findByDayFormat(MyUtils.getPre2DayFormat());
        if(yesterdayStocks!=null){
            for(StockYybInfo myStock :threeStocks){
                myStock.setThreeOpenPrice(getIntCurrentPrice(myStock.getCode()));
                stockYybInfoRepository.save(myStock);
            }
        }
    }

    private void closeStockInfo(){
        List<StockInfo> todayStocks = stockInfoService.findStockInfosByTodayFormat();
        if(todayStocks!=null){
            for(StockInfo myStock :todayStocks){
                QtStock qtStock = getQtInfo(myStock.getCode());
                myStock.setTodayClosePrice(MyUtils.getCentByYuanStr(qtStock.getCurrentPrice()));
                myStock.getTodayCloseYield();
                myStock.setTodayState(qtStock.getTodayState());
                stockInfoService.save(myStock);
            }
        }
        List<StockInfo> yesterdayStocks = stockInfoService.findStockInfosByYesterdayFormat();
        if(yesterdayStocks!=null){
            for(StockInfo myStock :yesterdayStocks){
                myStock.setTomorrowClosePrice(getIntCurrentPrice(myStock.getCode()));
                myStock.getTomorrowCloseYield();
                stockInfoService.save(myStock);
            }
        }
    }

    private void closeStockYybInfo(){
        List<StockYybInfo> todayStocks = stockYybInfoRepository.findByDayFormat(MyUtils.getDayFormat());
        if(todayStocks!=null){
            for(StockYybInfo myStock :todayStocks){
                myStock.setTodayClosePrice(getIntCurrentPrice(myStock.getCode()));
                stockYybInfoRepository.save(myStock);
            }
        }
        List<StockYybInfo> yesterdayStocks = stockYybInfoRepository.findByDayFormat(MyUtils.getYesterdayDayFormat());
        if(yesterdayStocks!=null){
            for(StockYybInfo myStock :yesterdayStocks){
                myStock.setTomorrowClosePrice(getIntCurrentPrice(myStock.getCode()));
                stockYybInfoRepository.save(myStock);
            }
        }
        List<StockYybInfo> threeStocks = stockYybInfoRepository.findByDayFormat(MyUtils.getPre2DayFormat());
        if(yesterdayStocks!=null){
            for(StockYybInfo myStock :threeStocks){
                myStock.setThreeClosePrice(getIntCurrentPrice(myStock.getCode()));
                stockYybInfoRepository.save(myStock);
            }
        }
    }


    private void fiveStatistic(){
        String end=MyUtils.getDayFormat();
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(3,MyUtils.getYesterdayDate()));
        List<StockInfo> xgbFiveUpStocks = stockInfoService.fiveStatistic(start, end);
        log.info(start+"-"+end+",--->fourStatistic count:"+xgbFiveUpStocks.size());
        if(xgbFiveUpStocks.size()>0){
            for (StockInfo xgbFiveUpStock : xgbFiveUpStocks){
                /*SinaTinyInfoStock tinyInfoStock = sinaService.getTiny(xgbFiveUpStock.getCode());
                log.info(xgbFiveUpStock.getShowCount()+xgbFiveUpStock.getCode()+",fiveStatistic High:"+xgbFiveUpStock.getFiveHighPrice()+",low:"+xgbFiveUpStock.getFiveLowPrice()+"==>new High:"+tinyInfoStock.getHighPrice()+",new Low:"+tinyInfoStock.getLowPrice());
                if(tinyInfoStock.getHighPrice()>xgbFiveUpStock.getFiveHighPrice().intValue()){
                    xgbFiveUpStock.setFiveHighPrice(tinyInfoStock.getHighPrice());
                    xgbFiveUpStock.getFiveHighYield();
                }
                if(tinyInfoStock.getLowPrice()>xgbFiveUpStock.getFiveLowPrice().intValue()){
                    xgbFiveUpStock.setFiveLowPrice(tinyInfoStock.getLowPrice());
                    xgbFiveUpStock.getFiveLowYield();
                }*/
                if(xgbFiveUpStock.getTodayOpenPrice().intValue()==10){
                    xgbFiveUpStock.setTodayOpenPrice(getIntCurrentPrice(xgbFiveUpStock.getCode()));
                }
                stockInfoService.save(xgbFiveUpStock);
            }
        }
    }

    private void openStockLimitUp(){
        List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(MyUtils.getYesterdayDayFormat(),1);
        if(xgbStocks!=null){
            for(StockLimitUp myStock :xgbStocks){
                myStock.setTodayOpenPrice(getIntCurrentPrice(myStock.getCode()));
                stockLimitUpRepository.save(myStock);
            }
        }
        xgbStocks = stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(MyUtils.getPre2DayFormat(),1);
        log.info("data open start openStockLimitUp size:"+xgbStocks.size());
        if(xgbStocks!=null){
            for(StockLimitUp myStock :xgbStocks){
                myStock.setTomorrowOpenPrice(getIntCurrentPrice(myStock.getCode()));
                log.info(myStock.getCode()+" data open start openStockLimitUp "+myStock.getTomorrowOpenPrice()+",OpenEarnings:"+myStock.getTomorrowOpenEarnings());
                stockLimitUpRepository.save(myStock);
            }
        }
    }

    private void closeStockLimitUp(){
        List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(MyUtils.getYesterdayDayFormat(),1);
        if(xgbStocks!=null){
            for(StockLimitUp myStock :xgbStocks){
                myStock.setTodayClosePrice(getIntCurrentPrice(myStock.getCode()));
                stockLimitUpRepository.save(myStock);
            }
        }
        xgbStocks = stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(MyUtils.getPre2DayFormat(),1);
        log.info("data open start closeStockLimitUp size:"+xgbStocks.size());
        if(xgbStocks!=null){
            for(StockLimitUp myStock :xgbStocks){
                myStock.setTomorrowClosePrice(getIntCurrentPrice(myStock.getCode()));
                log.info(myStock.getCode()+ "data open start openStockLimitUp "+myStock.getTomorrowClosePrice()+",CloseEarnings:"+myStock.getTomorrowCloseEarnings());
                stockLimitUpRepository.save(myStock);
            }
        }
    }


}
