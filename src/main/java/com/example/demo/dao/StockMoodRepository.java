package com.example.demo.dao;

import com.example.demo.domain.table.StockMood;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockMoodRepository extends JpaRepository<StockMood,Long> {
    StockMood findByDayFormat(String dayFormat);
    StockMood save(StockMood tgbStock);



}
