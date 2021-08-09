package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.domain.StockTradeValCurrent;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockMood;
import com.example.demo.domain.table.StockTemperature;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockPlateService;
import com.example.demo.service.sina.SinaService;
import com.example.demo.service.ths.StockTradeValCurrentService;
import com.example.demo.service.xgb.XgbCurrentService;
import com.example.demo.service.xgb.XgbService;
import com.example.demo.task.PanService;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class ClassifyController {
    private static String PRE_END="";
    @Autowired
    SinaService sinaService;
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockPlateService stockPlateService;
    @Autowired
    StockTemperatureRepository stockTemperatureRepository;
    @Autowired
    PanService panService;
    @Autowired
    XgbService xgbService;
    @Autowired
    XgbCurrentService xgbCurrentService;

    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    StockTruthRepository stockTruthRepository;
    @Autowired
    StockPlateStaRepository stockPlateStaRepository;
    @Autowired
    StockMoodRepository stockMoodRepository;
    @Autowired
    StockTradeValCurrentService stockTradeValCurrentService;


    @RequestMapping("my/{c}/{end}")
    String my(@PathVariable("c")Integer c,@PathVariable("end")String end) {
        //1.新股，2.空间股，3，人气股，4单日热门股
        String queryEnd = getQueryDate(end);
        List<StockTradeValCurrent> scs =stockTradeValCurrentService.findByDayFormat(queryEnd);
        Date queryDate =  MyUtils.getFormatDate(PRE_END);
        String week = MyUtils.getWeek(queryDate);
        String queryYesterday =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(queryDate));
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        System.out.println("=============queryEnd = [" + queryEnd + "]"+"queryYesterday"+queryYesterday);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        StringBuffer sb = new StringBuffer();
        String desc =week+"信念[空间与新题材模式，趋势持股看看一下，条件双十逻辑，涨停倍增逻辑] 提供20191015以后的数据=====>当前查询日期<br>";
        StockMood stockMood =stockMoodRepository.findByDayFormat(queryEnd);
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start, queryEnd);
        sb.append(desc).append(stockMood).append(queryEnd).append("===>【复盘情况】:<br>").append(temperaturesClose);
        List<StockTemperature> temperatures = getStockTemperatures(queryEnd);
        sb.append(queryEnd).append("===>【盘面实时运行情况1】:<br>").append(temperatures);
        sb.append(queryEnd).append("===>【盘面实时运行情况2】:<br>").append(scs);
        switch(c){
            case 1 :
                List<StockInfo> news = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_NEW.getCode());
                List<StockInfo> nearlys = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_NEARLY.getCode());
                sb.append(queryEnd).append(week).append("===>【新股】:<br>").append(news).
                        append(queryEnd).append(week).append("===>【次新股】:<br>").append(nearlys);
                break;
            case 2 :
                List<StockInfo> highCurrents = stockInfoService.fiveHeightSpace(start, queryEnd);
                List<StockInfo> kpls = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
                sb.append(queryEnd).append(week).
                        append(queryEnd).append(week).append("===>【灵魂股】:<br>").append(kpls).append("===>【空间股】:<br>").append(highCurrents);
                break; //可选
            case 3 :
                List<StockInfo> fiveOpens = stockInfoService.findStockDaysByDayFormatOpen(queryEnd, NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
                System.out.println("=============queryEnd = [" + queryEnd + "]"+"queryYesterday"+queryYesterday);
                List<StockInfo> yesterdayOpensFive = stockInfoService.findStockFivesTomorrowOpenYield(queryYesterday);
                List<StockInfo> yesterdayOpensResultFive = new ArrayList<>();
                for(StockInfo stockInfo :yesterdayOpensFive){
                    yesterdayOpensResultFive.add(stockInfo);
                    StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
                    if(stockInfoOpen!=null){
                        yesterdayOpensResultFive.add(stockInfoOpen);
                    }
                }
                List<StockInfo> yesterdayCloses = stockInfoService.findStockFivesTomorrowCloseYield(queryYesterday);
                List<StockInfo> yesterdayClosesResult = new ArrayList<>();
                for(StockInfo stockInfo :yesterdayCloses){
                    yesterdayClosesResult.add(stockInfo);
                    StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
                    if(stockInfoOpen!=null){
                        yesterdayClosesResult.add(stockInfoOpen);
                    }
                }
                sb.append(queryEnd).append(week).append("===>【早盘五日最强竞价】:<br>").append(fiveOpens)
                        .append(queryEnd).append(week).append("===>【早盘五日冲击情况】:<br>").append(yesterdayOpensResultFive)
                        .append(queryEnd).append(week).append("===>【尾盘五日冲击情况】:<br>").append(yesterdayClosesResult);
                break; //可选
            case 4 :
                List<StockInfo> dayOpens = stockInfoService.findStockDaysByDayFormatOpen(queryEnd,NumberEnum.StockType.STOCK_DAY.getCode());
                List<StockInfo> yesterdayOpens = stockInfoService.findStockDaysByDayFormatTomorrowOpenYield(queryYesterday);
                List<StockInfo> yesterdayOpensResult = new ArrayList<>();
                for(StockInfo stockInfo :yesterdayOpens){
                    yesterdayOpensResult.add(stockInfo);
                    StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
                    if(stockInfoOpen!=null){
                        yesterdayOpensResult.add(stockInfoOpen);
                    }
                }

                List<StockInfo> yesterdayDayCloses = stockInfoService.findStockDaysByDayFormatTomorrowCloseYield(queryYesterday);
                List<StockInfo> yesterdayDayClosesResult = new ArrayList<>();
                for(StockInfo stockInfo :yesterdayDayCloses){
                    yesterdayDayClosesResult.add(stockInfo);
                    StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
                    if(stockInfoOpen!=null){
                        yesterdayDayClosesResult.add(stockInfoOpen);
                    }
                }
                List<StockInfo> dayHot =stockInfoService.find2DayHot(queryEnd);
                sb.append(queryEnd).append(week).append("===>【最热议】:<br>").append(dayHot).
                append(queryEnd).append(week).append("===>【早盘当日最强竞价】:<br>").append(dayOpens).
                append(queryEnd).append(week).append("===>【早盘当日冲击情况】:<br>").append(yesterdayOpensResult)
                .append(queryEnd).append(week).append("===>【尾盘五日冲击情况】:<br>").append(yesterdayDayClosesResult);
                break; //可选
            //你可以有任意数量的case语句
            default : //可选
                //语句
        }



        /*List<StockTemperature> temperatures = getStockTemperatures(queryEnd);
        sb.append(queryEnd).append("===>【盘面实时运行情况】:<br>").append(temperatures);*/
        return sb.toString();
    }
    private List<StockTemperature> getStockTemperatures(String queryEnd) {
        Date endDate;
        List<StockTemperature> dayTemperatures = stockTemperatureRepository.findByDayFormat(queryEnd);
        endDate = MyUtils.getFormatDate(queryEnd);
        String preDate = MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        List<StockTemperature> preTemperatures = stockTemperatureRepository.findByDayFormat(preDate);
        HashMap<String,StockTemperature> map = new HashMap();
        for (StockTemperature pre : preTemperatures){
            map.put(pre.time(),pre);
        }
        List<StockTemperature> temperatures = new ArrayList<>();
        for (StockTemperature st : dayTemperatures) {
            temperatures.add(map.get(st.time()));
            temperatures.add(st);
        }
        return temperatures;
    }

    private String getQueryDate(@PathVariable("end") String end) {
        String queryEnd = end;
        if ("1".equals(end)) {
            if (MyChineseWorkDay.isWorkday()) {
                queryEnd = MyUtils.getDayFormat();
            } else {
                queryEnd = MyUtils.getYesterdayDayFormat();
            }
        } else if ("2".equals(end)) {
            Date endDate = MyUtils.getFormatDate(PRE_END);
            queryEnd = MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        } else if ("3".equals(end)) {
            Date endDate = MyUtils.getFormatDate(PRE_END);
            queryEnd = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        PRE_END = queryEnd;
        return queryEnd;
    }

}
