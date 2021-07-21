package com.example.demo.dao;

import com.example.demo.domain.StockDPInfo;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockDPInfoRepository extends JpaRepository<StockDPInfo,Long> {
    StockDPInfo findTop1ByCodeAndDayFormat(String code, String dayFormat);



}
