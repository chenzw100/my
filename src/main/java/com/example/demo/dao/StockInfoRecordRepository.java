package com.example.demo.dao;

import com.example.demo.domain.table.StockInfoRecord;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockInfoRecordRepository extends JpaRepository<StockInfoRecord,Long> {
    StockInfoRecord findTop1ByCodeAndDayFormat(String code, String dayFormat);





}
