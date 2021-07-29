package com.example.demo.dao;

import com.example.demo.domain.MyTradeStock;
import com.example.demo.domain.StockTradeValInfoJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<StockTradeValInfoJob> findByOneOpenRateIsNull();
    public List<StockTradeValInfoJob> findByOneNextOpenIncomeRateIsNull();
    Page<StockTradeValInfoJob> findTop50ByRankTypeAndPlateNameContainingOrderByDayFormatDesc(Integer rankType,String plateName, Pageable pageable);
    Page<StockTradeValInfoJob> findTop50ByRankType(Integer rankType, Pageable pageable);

    @Query(value="SELECT sz.day_format 'dayFormat',sum(sz.one_open_rate) 'oneOpenRate',sum(sz.one_close_rate) 'oneCloseRate',sum(sz.one_open_income_rate) 'oneOpenIncomeRate',sum(sz.one_close_income_rate) 'oneCloseIncomeRate',sum(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',sum(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',count(sz.day_format)'countNum' FROM stock_trade_val_job sz WHERE sz.yn=1 and sz.rank_type=1 and sz.day_format BETWEEN  ?1 AND ?2  GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics(String start, String end);
}
