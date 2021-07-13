package com.example.demo.dao;

import com.example.demo.domain.StockYybInfoRecord;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockYybInfoRecordRepository extends JpaRepository<StockYybInfoRecord,Long> {
    StockYybInfoRecord findTop1ByYybIdAndYzTypeAndCodeAndDayFormat(Integer ybId, Integer yzType, String code, String dayFormat);



}
