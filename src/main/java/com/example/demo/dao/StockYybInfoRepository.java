package com.example.demo.dao;

import com.example.demo.domain.table.StockYybInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockYybInfoRepository extends JpaRepository<StockYybInfo,Long> {
    StockYybInfo findTop1ByStockYybId(Long stockYybId);
    List<StockYybInfo>  findByDayFormat(String dayFormat);



}
