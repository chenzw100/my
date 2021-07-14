package com.example.demo.dao;

import com.example.demo.domain.StockYybInfoRecordDeal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockYybInfoRecordDealRepository extends JpaRepository<StockYybInfoRecordDeal,Long> {
    List<StockYybInfoRecordDeal> findByDayFormatBetweenAndYybIdAndCode(String startDay, String endDay, Integer yybId, String code);

}
