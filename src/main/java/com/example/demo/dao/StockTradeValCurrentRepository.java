package com.example.demo.dao;

import com.example.demo.domain.MyTradeSisStock;
import com.example.demo.domain.MyTradeStock;
import com.example.demo.domain.StockTradeValCurrent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockTradeValCurrentRepository extends JpaRepository<StockTradeValCurrent,Long> {



    @Query(value="SELECT sz.day_format 'dayFormat',sum(sz.one_open_rate) 'oneOpenRate',sum(sz.one_close_rate) 'oneCloseRate',sum(sz.one_open_income_rate) 'oneOpenIncomeRate',sum(sz.one_close_income_rate) 'oneCloseIncomeRate',sum(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',sum(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',sum(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_job sz WHERE sz.yn=1  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics(String start, String end, Integer rankType, Integer yesterdayTurnover);
    @Query(value="SELECT sz.day_format 'dayFormat',sum(sz.one_open_rate) 'oneOpenRate',sum(sz.one_close_rate) 'oneCloseRate',sum(sz.one_open_income_rate) 'oneOpenIncomeRate',sum(sz.one_close_income_rate) 'oneCloseIncomeRate',sum(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',sum(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',sum(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_job sz WHERE sz.yn=1 and sz.rank<10  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics2(String start, String end, Integer rankType, Integer yesterdayTurnover);
    @Query(value="SELECT sz.day_format 'dayFormat',sum(sz.one_open_rate) 'oneOpenRate',sum(sz.one_close_rate) 'oneCloseRate',sum(sz.one_open_income_rate) 'oneOpenIncomeRate',sum(sz.one_close_income_rate) 'oneCloseIncomeRate',sum(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',sum(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',sum(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_job sz WHERE sz.yn=1 and sz.rank<6  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics3(String start, String end, Integer rankType, Integer yesterdayTurnover);

    @Query(value="SELECT a.num rank,a.day_format dayFormat,a.`name`,a.`code` FROM (SELECT count(s.`code`) num,max(s.day_format)day_format,s.`code`,s.`name` FROM stock_trade_val_job_copy1 s WHERE s.day_format<= ?1 and s.rank_type =4 and  s.one_close_income_rate>1500 GROUP BY s.`code` ) a WHERE a.num>1 and a.day_format=?1 ORDER BY a.day_format", nativeQuery = true)
    public List<MyTradeSisStock> statisticDay(String end);
}
