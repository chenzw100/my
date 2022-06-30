package com.example.demo.service;

import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.domain.table.StockLimitUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/2.
 * 此乃题材挖掘利器
 */
@Component
public class StockUpService {

    @Autowired
    StockLimitUpRepository stockLimitUpRepository;



    public Page<StockLimitUp> findALl(Integer pageNumber, Integer pageSize, StockLimitUp obj){
        if(pageNumber==null){
            pageNumber=0;
            pageSize=10;
        }
        pageNumber--;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"continueBoardCount");
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
        Example<StockLimitUp> example = Example.of(obj/*,matcher*/);
        Page<StockLimitUp> all =null;
        all=stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(obj.getDayFormat(),2,pageable);
        if(all.getTotalElements()==0){
            all=stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(obj.getDayFormat(),1,pageable);
        }

        return all;
    }

}
