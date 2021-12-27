package com.example.demo.service;

import com.example.demo.dao.StockOptRepository;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockOpt;
import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockOptService {
    public Log log = LogFactory.getLog(StockOptService.class);
    @Autowired
    StockOptRepository stockOptRepository;
    @Autowired
    StockInfoService stockInfoService;

    public void staStock(){
        List<StockInfo> staStockPlatesWeek = stockInfoService.weekStatistic();
        log.info("StockOpt Week-ready data"+staStockPlatesWeek.size());
        if(staStockPlatesWeek.size()>0){
            for(StockInfo s: staStockPlatesWeek){
                StockOpt stockPlateSta = new StockOpt();
                stockPlateSta.setDayFormat(MyUtils.getDayFormat());
                stockPlateSta.setHotType(NumberEnum.PlateType.WEEK.getCode());
                stockPlateSta.setCode(s.getCode());
                stockPlateSta.setName(s.getName());
                stockPlateSta.setHotValue(s.getHotValue());
                stockOptRepository.save(stockPlateSta);
            }
        }
        List<StockInfo> staStockPlatesWeek2 = stockInfoService.week2Statistic();
        log.info("StockOpt Week2-ready data"+staStockPlatesWeek2.size());
        if(staStockPlatesWeek.size()>0){
            for(StockInfo s: staStockPlatesWeek2){
                StockOpt stockPlateSta = new StockOpt();
                stockPlateSta.setDayFormat(MyUtils.getDayFormat());
                stockPlateSta.setHotType(NumberEnum.PlateType.TWO_WEEK.getCode());
                stockPlateSta.setCode(s.getCode());
                stockPlateSta.setName(s.getName());
                stockPlateSta.setHotValue(s.getHotValue());
                stockOptRepository.save(stockPlateSta);
            }
        }
        List<StockInfo> staStockPlatesMonth = stockInfoService.monthStatistic();
        log.info("StockOpt Month-ready data"+staStockPlatesMonth.size());
        if(staStockPlatesWeek.size()>0){
            for(StockInfo s: staStockPlatesMonth){
                StockOpt stockPlateSta = new StockOpt();
                stockPlateSta.setDayFormat(MyUtils.getDayFormat());
                stockPlateSta.setHotType(NumberEnum.PlateType.MONTH.getCode());
                stockPlateSta.setCode(s.getCode());
                stockPlateSta.setName(s.getName());
                stockPlateSta.setHotValue(s.getHotValue());
                stockOptRepository.save(stockPlateSta);
            }
        }
    }
}
