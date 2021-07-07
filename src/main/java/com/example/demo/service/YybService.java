package com.example.demo.service;

import com.example.demo.dao.StockYybRepository;
import com.example.demo.domain.table.StockYyb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Component
public class YybService {
    @Autowired
    StockYybRepository stockYybRepository;
    public Page<StockYyb> findALl(Integer pageNumber, Integer pageSize, StockYyb stockYyb){
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
       /* ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("customerWx", match -> match.startsWith())
                .withMatcher("customerYx", match -> match.startsWith());*/
        Example<StockYyb> example = Example.of(stockYyb/*,matcher*/);
        Page<StockYyb> all = stockYybRepository.findAll(example,pageable);
        return all;
    }
}
