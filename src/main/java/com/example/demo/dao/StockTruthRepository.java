package com.example.demo.dao;

import com.example.demo.domain.table.StockTruth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockTruthRepository extends JpaRepository<StockTruth,Long> {
    List<StockTruth> findByDayFormat(String dayFormat);
    StockTruth save(StockTruth tgbStock);



}
