package com.example.demo.dao;

import com.example.demo.domain.table.StockYyb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockYybRepository extends JpaRepository<StockYyb,Long> {
    List<StockYyb> findByYybIdAndCodeAndDayFormat(Integer ybId,String code,String dayFormat);



}
