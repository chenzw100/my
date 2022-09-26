package com.example.demo.service;

import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.dao.StockYybRepository;
import com.example.demo.domain.MyDoTradeStock;
import com.example.demo.domain.MyTradeStock;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.StockYyb;
import com.example.demo.enums.NumberEnum;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TradeService {
    @Autowired
    StockTradeValInfoJobRepository stockTradeValInfoJobRepository;
    public Page<StockTradeValInfoJob> findLimitUP(Integer pageNumber, Integer pageSize, StockTradeValInfoJob obj){
        if(pageNumber==null){
            pageNumber=0;
            pageSize=10;
        }
        pageNumber--;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"dayFormat");
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC,"rank");
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
        Example<StockTradeValInfoJob> example = Example.of(obj/*,matcher*/);
        Page<StockTradeValInfoJob> all =null;
        if(obj.getOneOpenRate()==700){
            all = stockTradeValInfoJobRepository.findByRankTypeAndOneOpenRateGreaterThan(obj.getRankType(),obj.getOneOpenRate(),pageable);
        }else {
            all=stockTradeValInfoJobRepository.findByRankTypeAndOneOpenRateLessThan(obj.getRankType(),obj.getOneOpenRate(),pageable);
        }
        return all;
    }
    public Page<StockTradeValInfoJob> findALl(Integer pageNumber, Integer pageSize, StockTradeValInfoJob stockYyb){
        if(pageNumber==null){
            pageNumber=0;
            pageSize=10;
        }
        pageNumber--;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"dayFormat");
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC,"rank");
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
        Example<StockTradeValInfoJob> example = Example.of(stockYyb/*,matcher*/);
        Page<StockTradeValInfoJob> all =null;
        if(stockYyb.getRankType()==4){
            all=stockTradeValInfoJobRepository.findTop200ByRankTypeAndRankLessThanEqual(stockYyb.getRankType(),stockYyb.getRank(),pageable);
        }else {
            all = stockTradeValInfoJobRepository.findTop200ByRankType(stockYyb.getRankType(),pageable);
        }
        return all;
    }
    public Page<StockTradeValInfoJob> findList(Integer pageNumber, Integer pageSize, StockTradeValInfoJob stockYyb){

        if(StringUtils.isBlank(stockYyb.getPlateName())){
            return findALl(pageNumber,pageSize,stockYyb);
        }
        if(pageNumber==null){
            pageNumber=0;
            pageSize=10;
        }
        pageNumber--;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"id");
        Sort.Order order1 = new Sort.Order(Sort.Direction.DESC,"name");
        //如果有多个排序条件 建议使用此种方式 使用 Sort.by 替换之前的  new Sort();
        Sort sort = Sort.by(order,order1);
        //使用 PageRequest.of 替代之前的 new PageRequest();
        /**
         * page：0 开始
         * size:每页显示的数量
         * 排序的规则
         */
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<StockTradeValInfoJob> all = stockTradeValInfoJobRepository.findTop200ByRankTypeAndPlateNameContainingOrderByDayFormatDesc(stockYyb.getRankType(),stockYyb.getPlateName(),pageable);
        return all;
    }
    public List<StockTradeValInfoJob> statisticsList(Integer num){
        List<StockTradeValInfoJob> all = stockTradeValInfoJobRepository.statisticsList(num);
        return all;
    }
    public List<MyTradeStock> statistics(Integer rankType,Integer yesterdayTurnover,Integer sum){
        String start = MyUtils.getPreTwoMonthDayFormat();
        List<MyTradeStock> all =null;
        if(rankType==-1){
            all =stockTradeValInfoJobRepository.statistics2(start,MyUtils.getDayFormat(), NumberEnum.StockTradeType.FIFTY.getCode(),yesterdayTurnover);
        }else if(rankType==-2) {
            all =stockTradeValInfoJobRepository.statistics3(start,MyUtils.getDayFormat(),NumberEnum.StockTradeType.FIFTY.getCode(),yesterdayTurnover);
        }else if(rankType==-5) {
            all =stockTradeValInfoJobRepository.statistics4(sum,start,MyUtils.getDayFormat(),NumberEnum.StockTradeType.FIFTY.getCode(),yesterdayTurnover);
        }else {
            all =stockTradeValInfoJobRepository.statistics(start,MyUtils.getDayFormat(),rankType,yesterdayTurnover);
        }
        for(MyTradeStock m :all){
            System.out.println(m.getDayFormat());
        }
        return all;
    }

    public List<StockTradeValInfoJob> dododo(String day,Integer num){
        String start = MyUtils.getPreTwoMonthDayFormat();
        List<StockTradeValInfoJob> all =stockTradeValInfoJobRepository.doMe(day,num);
        return all;
    }
    public List<MyDoTradeStock> doMeSta(Integer rankType,String start,String end){

        List<MyDoTradeStock> details =stockTradeValInfoJobRepository.doMeStaDetail(rankType,start,end);
        List<MyDoTradeStock> all=stockTradeValInfoJobRepository.doMeSta(rankType,start,end);
        all.addAll(details);
        return all;
    }
}
