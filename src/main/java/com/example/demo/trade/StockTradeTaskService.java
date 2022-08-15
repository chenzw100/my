package com.example.demo.trade;

import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class StockTradeTaskService {
    public Log log = LogFactory.getLog(StockTradeTaskService.class);

    @Autowired
    private StockTradeValInfoJobRepository stockTradeValInfoJobRepository;

    @Autowired
    QtService qtService;
    //20220811
    public void jobDoTen() {

        Date now = new Date();
        ChineseWorkDay nowWorkDay = new ChineseWorkDay(now);
        Date yesterdayDate =nowWorkDay.preWorkDay();
        ChineseWorkDay yesterdayWorkDay = new ChineseWorkDay(yesterdayDate);
        Date yesterdayDate2 =yesterdayWorkDay.preWorkDay();
        ChineseWorkDay yesterday3WorkDay = new ChineseWorkDay(yesterdayDate2);
        Date yesterdayDate3 =yesterday3WorkDay.preWorkDay();

        List<StockTradeValInfoJob> list =stockTradeValInfoJobRepository.findByDayFormat(MyUtils.getDayFormat(yesterdayDate2));
        for(StockTradeValInfoJob s:list){
            s.setTomorrowTenPrice(qtService.getIntCurrentPriceNotSys(s.getCode()));
            stockTradeValInfoJobRepository.save(s);
            log.info("ten1 size " +s.toInfoTen());

        }
        log.info("ten1 size " +list.size());
        list =stockTradeValInfoJobRepository.findByDayFormat(MyUtils.getDayFormat(yesterdayDate3));
        for(StockTradeValInfoJob s:list){
            s.setThreeTenPrice(qtService.getIntCurrentPriceNotSys(s.getCode()));
            stockTradeValInfoJobRepository.save(s);

        }
        log.info("ten2 size " +list.size());

    }

    public void jobDoTwo() {

        Date now = new Date();
        ChineseWorkDay nowWorkDay = new ChineseWorkDay(now);
        Date yesterdayDate =nowWorkDay.preWorkDay();
        ChineseWorkDay yesterdayWorkDay = new ChineseWorkDay(yesterdayDate);
        Date yesterdayDate2 =yesterdayWorkDay.preWorkDay();
        ChineseWorkDay yesterday3WorkDay = new ChineseWorkDay(yesterdayDate2);
        Date yesterdayDate3 =yesterday3WorkDay.preWorkDay();

        List<StockTradeValInfoJob> list =stockTradeValInfoJobRepository.findByDayFormat(MyUtils.getDayFormat(yesterdayDate2));
        for(StockTradeValInfoJob s:list){
            s.setTomorrowTwoPrice(qtService.getIntCurrentPriceNotSys(s.getCode()));
            stockTradeValInfoJobRepository.save(s);

        }
        log.info("two1 size " +list.size());
        list =stockTradeValInfoJobRepository.findByDayFormat(MyUtils.getDayFormat(yesterdayDate3));
        for(StockTradeValInfoJob s:list){
            s.setThreeTwoPrice(qtService.getIntCurrentPriceNotSys(s.getCode()));
            stockTradeValInfoJobRepository.save(s);
        }
        log.info("two2 size " +list.size());

    }
}
