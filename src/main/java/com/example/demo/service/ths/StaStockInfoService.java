package com.example.demo.service.ths;

import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.dao.StockTradeValInfoJobSisRepository;
import com.example.demo.domain.MyTradeSisStock;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.StockTradeValInfoJobSis;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class StaStockInfoService {
    @Autowired
    private StockTradeValInfoJobRepository stockTradeValInfoJobRepository;
    @Autowired
    private StockTradeValInfoJobSisRepository stockTradeValInfoJobSisRepository;

    public void staInfo(String dayFormat,Date tomorrowDate) {
        List<MyTradeSisStock> list =stockTradeValInfoJobRepository.statisticDay(dayFormat);
        for(MyTradeSisStock stock:list){
            StockTradeValInfoJobSis sis = new StockTradeValInfoJobSis();
            ChineseWorkDay threeWorkDay = new ChineseWorkDay(tomorrowDate);
            Date threeDate = threeWorkDay.nextWorkDay();
            sis.setDayFormat(MyUtils.getDayFormat(threeDate));
            sis.setCode(stock.getCode());
            sis.setName(stock.getName());
            sis.setRank(stock.getRank());
            System.out.printf(sis.getName()+"---------"+sis.getDayFormat());
            stockTradeValInfoJobSisRepository.save(sis);
        }
    }

    public void jobDo() {
        String end ="20210806";
        String start = "20201213";
        while (!end.equals(start)){
            Date now = MyUtils.getFormatDate(start);
            ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(now);
            Date tomorrowDate = tomorrowWorkDay.nextWorkDay();
            staInfo(start,tomorrowDate);
            start=MyUtils.getDayFormat(tomorrowDate);
        }
    }
}
