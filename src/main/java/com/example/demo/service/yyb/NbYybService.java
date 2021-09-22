package com.example.demo.service.yyb;

import com.example.demo.dao.StockYybInfoJobRepository;
import com.example.demo.domain.StockYybInfoJob;
import com.example.demo.enums.YybEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Component
public class NbYybService {
    @Autowired
    StockYybInfoJobRepository stockYybInfoJobRepository;
    public Page<StockYybInfoJob> findALl(Integer pageNumber, Integer pageSize, StockYybInfoJob obj){
        if(pageNumber==null){
            pageNumber=0;
            pageSize=10;
        }
        pageNumber--;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"dayFormat");
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
        Example<StockYybInfoJob> example = Example.of(obj/*,matcher*/);
        Page<StockYybInfoJob> all =null;
        if(YybEnum.YzName.xhgm.getCode()==obj.getYybId()){
            all=stockYybInfoJobRepository.findTop20ByYybIdAndTradeAmountBetweenAndSaleAmountLessThanOrderByDayFormatDesc(obj.getYybId(),80000000,500000000,3000000,pageable);
        }else if(YybEnum.YzName.zsyx.getCode()==obj.getYybId()) {
            all=stockYybInfoJobRepository.findTop20ByYybIdAndTradeAmountBetweenAndYesterdayGainsBetweenAndYesterdayTurnoverRateBetweenOrderByDayFormatDesc(obj.getYybId(),80000000,500000000,100,1100,200,7000,pageable);
        }else {
            all=stockYybInfoJobRepository.findTop10ByTradeAmountGreaterThanOrderByDayFormatDesc(10,pageable);
        }
        return all;
    }
}
