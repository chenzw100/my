package com.example.demo.service.tgb;

import com.example.demo.dao.StockCurrentRepository;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.table.StockCurrent;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 * 此乃题材挖掘利器
 */
@Component
public class TgbService extends QtService {
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockCurrentRepository stockCurrentRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;


    //获取24小时的热搜数据
    public void dayDate(){
        try {
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/search/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            StockInfo tgbHotSeven = null;
            Integer hotSeven =100;
            for(int i=10;i<20;i++){
                Element element = elements.get(i);
                Element parent =element.parent();
                Elements tds =parent.siblingElements();
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                String currentPrice = getCurrentPrice(code);
                if(currentPrice == "-1"){
                    continue;
                }
                StockInfo tgbStock = new StockInfo(code,stockName,NumberEnum.StockType.STOCK_DAY.getCode());
                tgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
                tgbStock.setHotSort(i - 9);
                tgbStock.setHotValue(Integer.parseInt(tds.get(2).text()));
                tgbStock.setHotSeven(Integer.parseInt(tds.get(3).text()));
                log.info(i+"=sum==>WORKDAY:"+code);
                List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(code,MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
                if(xgbStocks!=null && xgbStocks.size()>0){
                    StockLimitUp xgbStock =xgbStocks.get(0);
                    tgbStock.setPlateName(xgbStock.getPlateName());
                    tgbStock.setOneFlag(xgbStock.getOpenCount());
                    tgbStock.setContinuous(xgbStock.getContinueBoardCount());
                    tgbStock.setLimitUp(1);
                }else {
                    xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(code);
                    if(xgbStocks!=null && xgbStocks.size()>0){
                        tgbStock.setPlateName(xgbStocks.get(0).getPlateName());
                    }else {
                        tgbStock.setPlateName("");
                    }
                    tgbStock.setOneFlag(1);
                    tgbStock.setContinuous(0);
                    tgbStock.setLimitUp(0);
                }
                log.error("==>WORKDAY dodo xinfo"+tgbStock.getCode());
                StockInfo StockInfo = stockInfoService.findStockDaysByCodeYesterdayFormat(tgbStock.getCode());
                if(StockInfo!=null){
                    tgbStock.setShowCount(StockInfo.getShowCount()+1);
                }else {
                    tgbStock.setShowCount(1);
                }
                if(tgbStock.getHotSeven()>hotSeven){
                    tgbHotSeven=tgbStock;
                    hotSeven=tgbHotSeven.getHotSeven();
                }
                stockInfoService.save(tgbStock);

            }
            if(tgbHotSeven!=null){
                StockInfo tgbStock = new StockInfo();
                BeanUtils.copyProperties(tgbHotSeven,tgbStock);
                tgbStock.setId(null);
                tgbStock.setStockType(NumberEnum.StockType.STOCK_DAY_HOT.getCode());
                log.error("==>WORKDAY dodo hot7"+tgbStock.getCode());
                stockInfoService.save(tgbStock);
            }
        } catch (IOException e) {
            log.error("==>WORKDAY fail "+e.getMessage());
            /*try {
                Thread.sleep(500000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            dayDate();
            log.info("==>重新执行");*/
        }
    }
    //获取实时的数据
    public void currentDate(){
        try {
            //log.info("==>currentDate start:");
            Document doc = Jsoup.connect("https://www.taoguba.com.cn/search/hotPop").get();
            Elements elements = doc.getElementsByClass("tbleft");
            for(int i=0;i<10;i++){
                Element element = elements.get(i);
                Element parent =element.parent();
                Elements tds =parent.siblingElements();
                String stockName = element.text();
                String url = element.getElementsByAttribute("href").attr("href");
                int length = url.length();
                String code = url.substring(length-9,length-1);
                String currentPrice = getCurrentPrice(code);
                if(currentPrice == null){
                    continue;
                }
                StockCurrent currentStock = new StockCurrent(code,stockName,MyUtils.getCurrentDate());
                currentStock.setHotSort(i +1);
                currentStock.setHotValue(Integer.parseInt(tds.get(2).text()));
                currentStock.setHotSeven(Integer.parseInt(tds.get(3).text()));
                stockCurrentRepository.save(currentStock);
            }
            log.info("==>TgbService currentDate end.");
        } catch (IOException e) {
            log.error("==>TgbService current fail "+e.getMessage());
        }
    }

    public void dayFive(){
        String end = MyUtils.getDayFormat();
        String start =MyUtils.getPreFiveDayFormat();
        List<MyTotalStock> totalStocks =  stockInfoService.fiveDayInfo(start, end);
        log.info(start+"-"+end+",TgbService dayFive size:"+totalStocks.size());
        for(MyTotalStock myTotalStock : totalStocks){
            StockInfo dayDb =stockInfoService.findStockDayByCodeTodayFormat(myTotalStock.getCode());
            if(dayDb==null){
                log.info(start+"-"+end+",TgbService dayFive not exist 非当日的结束:"+myTotalStock.getCode()+","+myTotalStock.getName());
                continue;
            }
            StockInfo fiveTgbStock = new StockInfo(myTotalStock.getCode(),myTotalStock.getName(), NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
            fiveTgbStock.setHotSort(myTotalStock.getTotalCount());
            fiveTgbStock.setHotValue(myTotalStock.getHotValue());
            fiveTgbStock.setHotSeven(myTotalStock.getHotSeven());
            fiveTgbStock.setShowCount(dayDb.getShowCount());
            String currentPrice = getCurrentPrice(myTotalStock.getCode());
            fiveTgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
            List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myTotalStock.getCode(),MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
            if(xgbStocks!=null && xgbStocks.size()>0){
                StockLimitUp xgbStock =xgbStocks.get(0);
                fiveTgbStock.setPlateName(xgbStock.getPlateName());
                fiveTgbStock.setOneFlag(xgbStock.getOpenCount());
                fiveTgbStock.setContinuous(xgbStock.getContinueBoardCount());
                fiveTgbStock.setLimitUp(1);
            }else {
                xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(myTotalStock.getCode());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    fiveTgbStock.setPlateName(xgbStocks.get(0).getPlateName());
                }else {
                    fiveTgbStock.setPlateName("");
                }
                fiveTgbStock.setOneFlag(1);
                fiveTgbStock.setContinuous(0);
                fiveTgbStock.setLimitUp(0);
            }
            fiveTgbStock.setDayFormat(MyUtils.getDayFormat());
            StockInfo fiveTgbStockTemp =stockInfoService.findStockDayFiveByCodeAndYesterdayFormat(myTotalStock.getCode());
            if(fiveTgbStockTemp!=null){
                fiveTgbStock.setShowCount(fiveTgbStockTemp.getShowCount() + 1);
            }else {
                fiveTgbStock.setShowCount(1);
            }
            stockInfoService.save(fiveTgbStock);
            log.info("TgbService dayFive end code 当日的保存:"+fiveTgbStock.getCode()+","+fiveTgbStock.getName());

        }
    }
    public void dayThree(){
        String end = MyUtils.getDayFormat();
        String start =MyUtils.getPreThreeDayFormat();
        List<MyTotalStock> totalStocks =  stockInfoService.threeDayInfo(start, end);
        log.info(start+"-"+end+",dayThree size:"+totalStocks.size());
        for(MyTotalStock myTotalStock : totalStocks){
            StockInfo dayDb =stockInfoService.findStockDayByCodeTodayFormat(myTotalStock.getCode());
            if(dayDb==null){
                log.info(start+"-"+end+",dayThree not exist 非当日的结束:"+myTotalStock.getCode());
                continue;
            }
            StockInfo fiveTgbStock = new StockInfo(myTotalStock.getCode(),myTotalStock.getName(), NumberEnum.StockType.STOCK_DAY_THREE.getCode());
            fiveTgbStock.setHotSort(myTotalStock.getTotalCount());
            fiveTgbStock.setHotValue(myTotalStock.getHotValue());
            fiveTgbStock.setHotSeven(myTotalStock.getHotSeven());
            fiveTgbStock.setShowCount(dayDb.getShowCount());
            String currentPrice = getCurrentPrice(myTotalStock.getCode());
            fiveTgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
            List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myTotalStock.getCode(),MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
            if(xgbStocks!=null && xgbStocks.size()>0){
                StockLimitUp xgbStock =xgbStocks.get(0);
                fiveTgbStock.setPlateName(xgbStock.getPlateName());
                fiveTgbStock.setOneFlag(xgbStock.getOpenCount());
                fiveTgbStock.setContinuous(xgbStock.getContinueBoardCount());
                fiveTgbStock.setLimitUp(1);
            }else {
                xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(myTotalStock.getCode());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    fiveTgbStock.setPlateName(xgbStocks.get(0).getPlateName());
                }else {
                    fiveTgbStock.setPlateName("");
                }
                fiveTgbStock.setOneFlag(1);
                fiveTgbStock.setContinuous(0);
                fiveTgbStock.setLimitUp(0);
            }
            fiveTgbStock.setDayFormat(MyUtils.getDayFormat());
            StockInfo fiveTgbStockTemp =stockInfoService.findStockDayFiveByCodeAndYesterdayFormat(myTotalStock.getCode());
            if(fiveTgbStockTemp!=null){
                fiveTgbStock.setShowCount(fiveTgbStockTemp.getShowCount() + 1);
            }else {
                fiveTgbStock.setShowCount(1);
            }
            stockInfoService.save(fiveTgbStock);
            log.info("TgbService dayThree end 当日的保存:"+fiveTgbStock.getCode()+","+fiveTgbStock.getName());

        }
    }


    public void currentDataDeal(){
        for(NumberEnum.StockCurrentType s:NumberEnum.StockCurrentType.values()){
            currentDealData(s.getCode());
        }
    }
    private void currentDealData(int type){

        List<MyTotalStock> totalStocks =  getCurrentData(type);
        for(MyTotalStock myTotalStock : totalStocks){
            StockInfo fiveTgbStock;
            StockInfo fiveTgbStockTemp;
            if(type == NumberEnum.StockCurrentType.ONE_DAY.getCode()){
                fiveTgbStock = new StockInfo(myTotalStock.getCode(),myTotalStock.getName(), NumberEnum.StockType.STOCK_CURRENT.getCode());
                fiveTgbStockTemp =stockInfoService.findStockCurrentByCodeAndYesterdayFormat(fiveTgbStock.getCode());
            }else {
                fiveTgbStock = new StockInfo(myTotalStock.getCode(),myTotalStock.getName(), NumberEnum.StockType.STOCK_CURRENT_FIVE.getCode());
                fiveTgbStockTemp =stockInfoService.findStockCurrentFiveByCodeAndYesterdayFormat(fiveTgbStock.getCode());
            }
            fiveTgbStock.setHotSort(myTotalStock.getTotalCount());
            fiveTgbStock.setHotValue(myTotalStock.getHotValue());
            fiveTgbStock.setHotSeven(myTotalStock.getHotSeven());
            String currentPrice = getCurrentPrice(myTotalStock.getCode());
            fiveTgbStock.setYesterdayClosePrice(MyUtils.getCentBySinaPriceStr(currentPrice));
            List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myTotalStock.getCode(),MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
            if(xgbStocks!=null && xgbStocks.size()>0){
                StockLimitUp xgbStock =xgbStocks.get(0);
                fiveTgbStock.setPlateName(xgbStock.getPlateName());
                fiveTgbStock.setOneFlag(xgbStock.getOpenCount());
                fiveTgbStock.setContinuous(xgbStock.getContinueBoardCount());
                fiveTgbStock.setLimitUp(1);
            }else {
                xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(myTotalStock.getCode());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    fiveTgbStock.setPlateName(xgbStocks.get(0).getPlateName());
                }else {
                    fiveTgbStock.setPlateName("");
                }
                fiveTgbStock.setOneFlag(1);
                fiveTgbStock.setContinuous(0);
                fiveTgbStock.setLimitUp(0);
            }
            fiveTgbStock.setDayFormat(MyUtils.getDayFormat());

            if(fiveTgbStockTemp!=null){
                fiveTgbStock.setShowCount(fiveTgbStockTemp.getShowCount() + 1);
            }else {
                fiveTgbStock.setShowCount(1);
            }
            stockInfoService.save(fiveTgbStock);
            log.info("currentFive end:"+totalStocks.size());
        }
    }
    List<MyTotalStock> getCurrentData(int type){
        String end = MyUtils.getDayFormat();
        String start ="";
        List<MyTotalStock> totalStocks= null;
        if(type == NumberEnum.StockCurrentType.ONE_DAY.getCode()){
            start =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay());
            totalStocks =  stockCurrentRepository.oneInfo(start, end);
        }else {

            start =MyUtils.getPreFiveDayFormat();
            totalStocks =  stockCurrentRepository.fiveInfo(start, end);
        }
        log.info(NumberEnum.StockCurrentType.getStockCurrentType(type)+"==>"+start+"-"+end+",currentData size:"+totalStocks.size());
        return totalStocks;
    }

}
