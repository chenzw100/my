package com.example.demo.service;

import com.example.demo.dao.StockZyRepository;
import com.example.demo.domain.table.StockZy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
public class StockZyService {

    @Autowired
    StockZyRepository stockZyRepository;
    public Page<StockZy> findALl(Integer pageNumber,Integer pageSize,StockZy stockZy){
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
        Example<StockZy> example = Example.of(stockZy/*,matcher*/);
        Page<StockZy> all = stockZyRepository.findAll(example,pageable);
        return all;
    }
    public StockZy saveOrUpdate(StockZy stockZy){
        return stockZyRepository.save(stockZy);
    }
    public StockZy getById(Long id){
        return stockZyRepository.getOne(id);
    }

}
