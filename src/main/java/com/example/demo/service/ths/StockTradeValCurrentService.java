package com.example.demo.service.ths;

import com.example.demo.dao.StockTradeValCurrentRepository;
import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.dao.StockTradeValInfoJobSisRepository;
import com.example.demo.domain.MyTradeSisStock;
import com.example.demo.domain.StockTradeValCurrent;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.StockTradeValInfoJobSis;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.base.BaseService;
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
public class StockTradeValCurrentService {
    public Log log = LogFactory.getLog(StockTradeValCurrentService.class);
    @Autowired
    private StockTradeValInfoJobRepository stockTradeValInfoJobRepository;

    @Autowired
    private StockTradeValCurrentRepository stockTradeValCurrentRepository;
    @Autowired
    QtService qtService;
    public List<StockTradeValCurrent> findByDayFormatAndRankType(String dayFormat,Integer rankType){
        log.info("query findByDayFormatAndRankType :"+dayFormat);
        List<StockTradeValCurrent>  currents = stockTradeValCurrentRepository.findByDayFormatAndRankType(dayFormat,rankType);
        return currents;
    }

    public List<StockTradeValCurrent> findByDayFormat(String dayFormat){
        log.info("query day:"+dayFormat);
        List<StockTradeValCurrent>  currents = stockTradeValCurrentRepository.findByDayFormatOrderByRankType(dayFormat);
        return currents;
    }
    public void jobDo() {
        jobDoType(NumberEnum.StockTradeType.FIFTY.getCode());
        jobDoType(NumberEnum.StockTradeType.RISE.getCode());
        jobDoType(NumberEnum.StockTradeType.FALL.getCode());

    }
    public void jobDoType(Integer rankType) {
        Integer oneOpenRate =0; // MyUtils.getIncreaseRateCent(this.todayOpenPrice,this.yesterdayClosePrice).intValue();
        Integer oneCloseRate =0; //  MyUtils.getIncreaseRateCent(this.todayClosePrice,this.todayOpenPrice).intValue();
        Integer oneOpenIncomeRate =0; // MyUtils.getIncreaseRateCent(this.tomorrowOpenPrice,this.todayOpenPrice).intValue();
        Integer oneCloseIncomeRate =0; // MyUtils.getIncreaseRateCent(this.tomorrowClosePrice,this.todayOpenPrice).intValue();
        Integer oneNextOpenIncomeRate = 0; //MyUtils.getIncreaseRateCent(this.threeOpenPrice,this.todayOpenPrice).intValue();
        Integer oneNextCloseIncomeRate = 0; //MyUtils.getIncreaseRateCent(this.threeClosePrice,this.todayOpenPrice).intValue();
        Date now = new Date();
        ChineseWorkDay nowWorkDay = new ChineseWorkDay(now);
        Date yesterdayDate =nowWorkDay.preWorkDay();
        Date yesterdayDate2 =nowWorkDay.preDaysWorkDay(2,now);
        Date yesterdayDate3 =nowWorkDay.preDaysWorkDay(3,now);
        StockTradeValCurrent current = new StockTradeValCurrent();

        List<StockTradeValInfoJob> list =stockTradeValInfoJobRepository.findByDayFormatAndRankType(MyUtils.getDayFormat(yesterdayDate), rankType);
        for(StockTradeValInfoJob s:list){
            oneOpenRate=oneOpenRate+s.getOneOpenRate();
            oneCloseRate =oneCloseRate+MyUtils.getIncreaseRateCent(qtService.getIntCurrentPriceNotSys(s.getCode()),s.getTodayOpenPrice()).intValue();
        }
        list =stockTradeValInfoJobRepository.findByDayFormatAndRankType(MyUtils.getDayFormat(yesterdayDate2),rankType);
        for(StockTradeValInfoJob s:list){
            oneOpenIncomeRate=oneOpenIncomeRate+s.getOneOpenIncomeRate();
            oneCloseIncomeRate =oneCloseIncomeRate+MyUtils.getIncreaseRateCent(qtService.getIntCurrentPriceNotSys(s.getCode()),s.getTodayOpenPrice()).intValue();
        }
        list =stockTradeValInfoJobRepository.findByDayFormatAndRankType(MyUtils.getDayFormat(yesterdayDate3),rankType);
        for(StockTradeValInfoJob s:list){
            oneNextOpenIncomeRate =oneNextOpenIncomeRate+s.getOneNextOpenIncomeRate();
            oneNextCloseIncomeRate=oneNextCloseIncomeRate+MyUtils.getIncreaseRateCent(qtService.getIntCurrentPriceNotSys(s.getCode()),s.getTodayOpenPrice()).intValue();
        }
        current.setOneOpenRate(oneOpenRate);
        current.setOneCloseRate(oneCloseRate);
        current.setOneOpenIncomeRate(oneOpenIncomeRate);
        current.setOneCloseIncomeRate(oneCloseIncomeRate);
        current.setOneNextOpenIncomeRate(oneNextOpenIncomeRate);
        current.setOneNextCloseIncomeRate(oneNextCloseIncomeRate);
        current.setDayFormat(MyUtils.getDayFormat());
        current.setCreated(new Date());
        current.setRankType(rankType);
        current.setYn(1);
        stockTradeValCurrentRepository.save(current);

        log.info("情绪流水记录成功");

    }
}
