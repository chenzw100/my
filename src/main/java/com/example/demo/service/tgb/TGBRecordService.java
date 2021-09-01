package com.example.demo.service.tgb;

import com.example.demo.dao.StockCurrentRepository;
import com.example.demo.dao.StockInfoRepository;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.MyDayStock;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 * 此乃题材挖掘利器
 */
@Component
public class TGBRecordService{
    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockCurrentRepository stockCurrentRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    public void deal(){
        String start = "20210722";
        String end = "20210901";
        boolean f=true;
        while (f){
            if(start.equals(end)){
                f= false;
            }
            Date now = MyUtils.getFormatDate(start);
            ChineseWorkDay nowWorkDay = new ChineseWorkDay(now);
            Date nextDay = nowWorkDay.nextWorkDay();
            dealMsg(start);
            start=MyUtils.getDayFormat(nextDay);

        }


    }
    public void dealMsg(String start){
        List<MyDayStock> list =stockInfoRepository.dayInfoInFive(start);
        for (MyDayStock m :list){
            StockTradeValInfoJob job =new StockTradeValInfoJob();
            job.setDayFormat(m.getDayFormat());
        }

    }


    public Page<StockInfo> findALl(Integer pageNumber, Integer pageSize, StockInfo obj){
        if(pageNumber==null){
            pageNumber=0;
            pageSize=10;
        }
        pageNumber--;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"hotSort");
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC,"id");
        //如果有多个排序条件 建议使用此种方式 使用 Sort.by 替换之前的  new Sort();
        Sort sort = Sort.by(order,order1);
        //使用 PageRequest.of 替代之前的 new PageRequest();
        /**
         * page：0 开始
         * size:每页显示的数量
         * 排序的规则
         */
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
       /* ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("customerWx", match -> match.startsWith())
                .withMatcher("customerYx", match -> match.startsWith());*/
        Example<StockInfo> example = Example.of(obj/*,matcher*/);
        Page<StockInfo> all =null;
        if(StringUtils.isNotBlank(obj.getCode())){
            obj.setDayFormat(null);
            all=stockInfoRepository.findByStockTypeAndCodeOrderByDayFormatDesc(obj.getStockType(),obj.getCode(),pageable);
        }else {
            all=stockInfoRepository.findByStockTypeAndDayFormatOrderByHotSort(obj.getStockType(),obj.getDayFormat(),pageable);
        }
        return all;
    }

}
