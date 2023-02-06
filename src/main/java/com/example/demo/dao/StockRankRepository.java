package com.example.demo.dao;

import com.example.demo.domain.MyTrendStock;
import com.example.demo.domain.table.StockRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockRankRepository extends JpaRepository<StockRank,Long> {

    List<StockRank> findByDayFormatOrderByRankType(String dayFormat);
    List<StockRank> findByDayFormatAndRankType(String dayFormat,Integer rankType);
    StockRank findByDayFormatAndRankTypeAndCode(String dayFormat,Integer rankType,String code);
    List<StockRank> findByThreeClosePriceIsNull();
    List<StockRank> findByDayFormatAndShowCount(String dayFormat,Integer showCount);

    @Query(value="SELECT s.day_format'dayFormat',count(s.day_format)'countNum',sum(hot_value) 'hot',sum(first_open_rate) 'firstOpenRate',sum(first_close_rate)'firstCloseRate',sum(second_open_rate)'secondOpenRate',sum(second_close_rate)'secondCloseRate',sum(third_open_rate)'thirdOpenRate',sum(third_close_rate)'thirdCloseRate' FROM stock_rank s WHERE s.rank_type =?1  and s.day_format BETWEEN ?2 and ?3  GROUP BY s.day_format ORDER BY s.day_format desc ", nativeQuery = true)
    public List<MyTrendStock> doStaFlow(Integer rankType, String start, String end );
}
