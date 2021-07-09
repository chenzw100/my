package com.example.demo.dao;

import com.example.demo.domain.table.StockYybRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockYybRecordRepository extends JpaRepository<StockYybRecord,Long> {
    List<StockYybRecord> findTop1ByYybIdAndCodeAndDayFormat(Integer ybId, String code, String dayFormat);

    @Query(value="SELECT syi.id,syi.day_format,y.yyb_id,y.yz_name,y.`code`,y.`name`,y.plate_name,y.sum_amount,y.one_day,syi.yesterday_close_price,syi.today_open_price,syi.today_close_price,syi.tomorrow_open_price,syi.tomorrow_close_price,syi.three_open_price,syi.three_close_price FROM stock_yyb y LEFT JOIN stock_yyb_info syi  on y.id = syi.stock_yyb_id where  syi.day_format  BETWEEN ?1 AND ?2 ", nativeQuery = true)
    public List<StockYybRecord> findRecord(String start, String end);



}
