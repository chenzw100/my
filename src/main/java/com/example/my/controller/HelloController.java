package com.example.my.controller;

import com.example.my.dao.*;
import com.example.my.domain.*;
import com.example.my.enums.NumberEnum;
import com.example.my.utils.MyChineseWorkDay;
import com.example.my.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/1/31.
 */
@RestController
public class HelloController {

    @Autowired
    TemperatureRepository temperatureRepository;
    @Autowired
    DownStockRepository downStockRepository;
    @Autowired
    TgbStockRepository tgbStockRepository;
    @Autowired
    MyTgbStockRepository myTgbStockRepository;
    @Autowired
    FiveTgbStockRepository fiveTgbStockRepository;
    @Autowired
    MyFiveTgbStockRepository myFiveTgbStockRepository;
    @Autowired
    CurrentStockRepository currentStockRepository;


    @RequestMapping("/t/{end}")
    String t(@PathVariable("end")String end){
        System.out.println(end);
        return "success12<br>"+end;
    }
    @RequestMapping("/m/{end}")
    String m(@PathVariable("end")String end) {
        String desc ="查询20190124之后的数据，坚持模式！！！<br>【[跌停数][炸板][焦点股]不涨停==》继续计提】<br>查询日期";
        Date endDate =  MyUtils.getFormatDate(end);
        String yesterday = MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(1, endDate));
        List<Temperature> yesterdays = temperatureRepository.findByDayFormatAndType(yesterday, NumberEnum.TemperatureType.CLOSE.getCode());
        List<DownStock> downStocks =downStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<FiveTgbStock> hotSortFive = fiveTgbStockRepository.findByDayFormatOrderByOpenBidRate(end);
        List<MyFiveTgbStock> myTgbStockFive = myFiveTgbStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<TgbStock> tgbHots = tgbStockRepository.findByDayFormatOrderByOpenBidRate(end);

        List<Temperature> temperatures = temperatureRepository.findByDayFormatOrderByIdDesc(end);
        List<DownStock> downBeforeStocks =downStockRepository.findByPreFormatOrderByOpenBidRateDesc(end);

        return desc+end+"昨日情况:<br>"+downStocks+"<br>"+yesterdays+"<br>股吧竞价:<br>"+hotSortFive+"end"+end+"<br>我的竞价:<br>"+myTgbStockFive+"<br>股吧热门:<br>"+tgbHots+":<br>"+temperatures+end+"当日:<br>"+downBeforeStocks;
    }
}
