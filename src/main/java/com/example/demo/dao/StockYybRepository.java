package com.example.demo.dao;

import com.example.demo.domain.table.StockYyb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockYybRepository extends JpaRepository<StockYyb,Long> {
    List<StockYyb> findByDayFormatAndCodeAndYybId(String dayFormat,String code,Integer ybId);



}
