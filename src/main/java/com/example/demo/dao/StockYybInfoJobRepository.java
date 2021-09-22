package com.example.demo.dao;

import com.example.demo.domain.StockYybInfoJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockYybInfoJobRepository extends JpaRepository<StockYybInfoJob,Long> {
    StockYybInfoJob findTop1ByYybIdAndYzTypeAndCodeAndDayFormat(Integer ybId, Integer yzType, String code, String dayFormat);
    Page<StockYybInfoJob> findTop10ByTradeAmountGreaterThanOrderByDayFormatDesc(Integer greater,Pageable pageable);
    Page<StockYybInfoJob> findTop20ByYybIdAndTradeAmountBetweenAndSaleAmountLessThanOrderByDayFormatDesc(Integer yybId,Integer start, Integer end, Integer less, Pageable pageable);
    Page<StockYybInfoJob> findTop20ByYybIdAndTradeAmountBetweenAndYesterdayGainsBetweenAndYesterdayTurnoverRateBetweenOrderByDayFormatDesc(Integer yybId,Integer startAmount, Integer endAmount,Integer startGains, Integer endGains, Integer startRate, Integer endRate, Pageable pageable);


}
