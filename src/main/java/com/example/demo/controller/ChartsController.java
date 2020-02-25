package com.example.demo.controller;

import com.example.demo.dao.StockTemperatureRepository;

import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockTemperature;
import com.example.demo.service.StockInfoService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.example.demo.utils.MyChineseWorkDay.isWorkday;

@RestController
public class ChartsController {
    @Autowired
    StockTemperatureRepository temperatureRepository;
    @Autowired
    StockInfoService stockInfoService;
    private static String PRE_END="";
    @RequestMapping(value = "/risk/{end}", method = RequestMethod.GET)
     public Map risk(@PathVariable("end")String end){
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd=MyUtils.getDayFormat();
            }else {
                queryEnd= MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(12, endDate));
        List<StockTemperature> temperaturesClose=temperatureRepository.close(start, queryEnd);

        TreeMap continueValMap = new TreeMap<>();
        TreeMap nowTemperatureMap = new TreeMap<>();
        TreeMap firstContinue = new TreeMap<>();

        TreeMap continueCountMap = new TreeMap<>();
        TreeMap downCountMap = new TreeMap<>();

        TreeMap limitUpMap = new TreeMap<>();
        TreeMap limitDownMap = new TreeMap<>();

        for (StockTemperature t:temperaturesClose){
            continueValMap.put(t.getDayFormat(), t.getContinueVal());
            nowTemperatureMap.put(t.getDayFormat(), t.getNowTemperature());
            continueCountMap.put(t.getDayFormat(), t.getContinueCount());
            downCountMap.put(t.getDayFormat(), t.getStrongDowns());
            limitUpMap.put(t.getDayFormat(), t.getLimitUp());
            limitDownMap.put(t.getDayFormat(), t.getLimitDown());
        }

        HashMap resultMap =new HashMap();
        resultMap.put("x", continueValMap.keySet());

        resultMap.put("yContinueVal", continueValMap.values());
        resultMap.put("yNowTemperature", nowTemperatureMap.values());

        resultMap.put("yContinueCount", continueCountMap.values());
        resultMap.put("yLimitUp", limitUpMap.values());

        resultMap.put("yDownCount", downCountMap.values());
        resultMap.put("yLimitDown", limitDownMap.values());


        List<StockInfo> highCurrents = stockInfoService.fiveHeightSpace(start, queryEnd);
        for(StockInfo sh:highCurrents){
            firstContinue.put(sh.getDayFormat(),sh.getContinuous());
        }
        resultMap.put("firstContinue", firstContinue.values());

        return resultMap;
    }

    public boolean isWorkday(){
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
