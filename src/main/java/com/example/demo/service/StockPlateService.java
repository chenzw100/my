package com.example.demo.service;

import com.example.demo.dao.StockInfoRepository;
import com.example.demo.dao.StockPlateRepository;
import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.StaStockPlate;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockPlate;
import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 STOCK_DAY(10,"当天"),
 STOCK_CURRENT(20,"当天实时"),
 STOCK_DOWN(30,"弱势"),
 STOCK_SPACE_HEIGHT(40,"空间"),
 STOCK_LIMIT_UP_FIVE(50,"五版"),
 STOCK_CURRENT_FIVE(60,"五日实时"),
 STOCK_DAY_FIVE(70,"五日当天"),
 */
@Component
public class StockPlateService {

    @Autowired
    StockPlateRepository stockPlateRepository;
    public StockPlate save(StockPlate stockPlate){
        return stockPlateRepository.save(stockPlate);
    }
    public StockPlate findByPlateCodeByYesterday(String code){
        return stockPlateRepository.findByPlateCodeAndDayFormat(code,MyUtils.getYesterdayDayFormat());
    }
    public List<StaStockPlate>  weekStatistic(){
        return stockPlateRepository.limit3(MyUtils.getDayFormat(),MyUtils.getPreFiveDayFormat());
    }
    public List<StaStockPlate>  week2Statistic(){
        return stockPlateRepository.limit3(MyUtils.getDayFormat(),MyUtils.getPre2WeekDayFormat());
    }
    public List<StaStockPlate>  monthStatistic(){
        return stockPlateRepository.limit3(MyUtils.getDayFormat(),MyUtils.getPreMonthDayFormat());
    }

}
