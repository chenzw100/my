package com.example.demo.dao;

import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.StockYybInfoJob;
import com.example.demo.domain.table.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockTradeValInfoJobRepository extends JpaRepository<StockTradeValInfoJob,Long> {
    @Query(value="SELECT * from stock_trade_val_info_job WHERE day_format BETWEEN ?1 AND ?2", nativeQuery = true)
    public List<StockTradeValInfoJob> threeStatistic(String start, String end);
    public List<StockTradeValInfoJob> findByOneNextCloseIncomeRateIsNull();
    public List<StockTradeValInfoJob> findByOneOpenIncomeRateIsNull();

}
