package com.example.demo.dao;

import com.example.demo.domain.MyTradeSisStock;
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
    public List<StockTradeValInfoJob> findByDayFormatAndRankType(String start,Integer rankType);
    public List<StockTradeValInfoJob> findByOneNextCloseIncomeRateIsNull();
    public List<StockTradeValInfoJob> findByOneOpenIncomeRateIsNullAndTodayClosePriceIsNotNull();
    public List<StockTradeValInfoJob> findByOneOpenRateIsNull();
    public List<StockTradeValInfoJob> findByOneNextOpenIncomeRateIsNullAndTomorrowClosePriceIsNotNull();
    Page<StockTradeValInfoJob> findTop200ByRankTypeAndPlateNameContainingOrderByDayFormatDesc(Integer rankType,String plateName, Pageable pageable);
    Page<StockTradeValInfoJob> findTop200ByRankType(Integer rankType, Pageable pageable);
    Page<StockTradeValInfoJob> findTop200ByRankTypeAndRankLessThanEqual(Integer rankType, Integer rank, Pageable pageable);


    @Query(value="SELECT sz.day_format 'dayFormat',sum(sz.one_open_rate) 'oneOpenRate',sum(sz.one_close_rate) 'oneCloseRate',sum(sz.one_open_income_rate) 'oneOpenIncomeRate',sum(sz.one_close_income_rate) 'oneCloseIncomeRate',sum(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',sum(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',sum(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_job sz WHERE sz.yn=1  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics(String start, String end,Integer rankType,Integer yesterdayTurnover);
    @Query(value="SELECT sz.day_format 'dayFormat',sum(sz.one_open_rate) 'oneOpenRate',sum(sz.one_close_rate) 'oneCloseRate',sum(sz.one_open_income_rate) 'oneOpenIncomeRate',sum(sz.one_close_income_rate) 'oneCloseIncomeRate',sum(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',sum(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',sum(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_job sz WHERE sz.yn=1 and sz.rank<10  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics2(String start, String end,Integer rankType,Integer yesterdayTurnover);
    @Query(value="SELECT sz.day_format 'dayFormat',sum(sz.one_open_rate) 'oneOpenRate',sum(sz.one_close_rate) 'oneCloseRate',sum(sz.one_open_income_rate) 'oneOpenIncomeRate',sum(sz.one_close_income_rate) 'oneCloseIncomeRate',sum(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',sum(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',sum(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_job sz WHERE sz.yn=1 and sz.rank<6  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics3(String start, String end,Integer rankType,Integer yesterdayTurnover);

    @Query(value="SELECT a.num rank,a.day_format dayFormat,a.`name`,a.`code` FROM (SELECT count(s.`code`) num,max(s.day_format)day_format,s.`code`,s.`name` FROM stock_trade_val_job_copy1 s WHERE s.day_format<= ?1 and s.rank_type =50 and  s.one_close_income_rate>1500 GROUP BY s.`code` ) a WHERE a.num>1 and a.day_format=?1 ORDER BY a.day_format", nativeQuery = true)
    public List<MyTradeSisStock> statisticDay(String end);

    @Query(value="select * from (select tablename_tmp.*,@rownum\\:=@rownum+1,if(@pyear=tablename_tmp.day_format,@rank\\:=@rank+1,@rank\\:=1) as rank_tmp,@pyear\\:=tablename_tmp.day_format from (select * from stock_trade_val_job WHERE rank_type=50 and yesterday_close_price <5000  order by day_format desc,rank asc)tablename_tmp ,(select @rownum\\:=0 , @pyear\\:=null ,@rank\\:=0) a)result where rank_tmp <=?1", nativeQuery = true)
    public List<StockTradeValInfoJob> statisticsList(Integer sum);

    @Query(value="SELECT sz.day_format 'dayFormat',sum(sz.one_open_rate) 'oneOpenRate',sum(sz.one_close_rate) 'oneCloseRate',sum(sz.one_open_income_rate) 'oneOpenIncomeRate',sum(sz.one_close_income_rate) 'oneCloseIncomeRate',sum(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',sum(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',sum(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM (select * from (select tablename_tmp.*,@rownum\\:=@rownum+1,if(@pyear=tablename_tmp.day_format,@rank\\:=@rank+1,@rank\\:=1) as rank_tmp,@pyear\\:=tablename_tmp.day_format from (select * from stock_trade_val_job WHERE rank_type=50 and yesterday_close_price <5000  order by day_format desc,rank asc)tablename_tmp ,(select @rownum\\:=0 , @pyear\\:=null ,@rank\\:=0) a)result where rank_tmp <=?1) sz WHERE sz.yn=1 and sz.day_format BETWEEN  ?2 AND ?3 and sz.rank_type=?4 and sz.yesterday_turnover>?5 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics4(Integer sum,String start, String end,Integer rankType,Integer yesterdayTurnover);


}
