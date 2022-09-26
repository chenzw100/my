package com.example.demo.dao;

import com.example.demo.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockTradeValInfoTestRepository extends JpaRepository<StockTradeValInfoTest,Long> {
    public List<StockTradeValInfoTest> findByDayFormat(String dayFormat);
    public List<StockTradeValInfoTest> findByRankType(Integer rankType);
    public List<StockTradeValInfoTest> findByOrderByDayFormatDesc();
    @Query(value="SELECT * from stock_trade_val_test WHERE day_format BETWEEN ?1 AND ?2", nativeQuery = true)
    public List<StockTradeValInfoTest> threeStatistic(String start, String end);
    public List<StockTradeValInfoTest> findByDayFormatAndRankType(String start, Integer rankType);
    public List<StockTradeValInfoTest> findByOneNextCloseIncomeRateIsNull();
    public List<StockTradeValInfoTest> findByThreeClosePriceIsNull();
    public List<StockTradeValInfoTest> findByOneOpenIncomeRateIsNullAndTodayClosePriceIsNotNull();
    public List<StockTradeValInfoTest> findByOneOpenRateIsNull();
    public List<StockTradeValInfoTest> findByOneNextOpenIncomeRateIsNullAndTomorrowClosePriceIsNotNull();
    Page<StockTradeValInfoTest> findTop200ByRankTypeAndPlateNameContainingOrderByDayFormatDesc(Integer rankType, String plateName, Pageable pageable);
    Page<StockTradeValInfoTest> findTop200ByRankType(Integer rankType, Pageable pageable);
    Page<StockTradeValInfoTest> findTop200ByRankTypeAndRankLessThanEqual(Integer rankType, Integer rank, Pageable pageable);
    Page<StockTradeValInfoTest> findByRankTypeAndOneOpenRateLessThan(Integer rankType, Integer oneOpenRate, Pageable pageable);
    Page<StockTradeValInfoTest> findByRankTypeAndOneOpenRateGreaterThan(Integer rankType, Integer oneOpenRate, Pageable pageable);


    @Query(value="SELECT sz.day_format 'dayFormat',AVG(sz.one_open_rate) 'oneOpenRate',AVG(sz.one_close_rate) 'oneCloseRate',AVG(sz.one_open_income_rate) 'oneOpenIncomeRate',AVG(sz.one_close_income_rate) 'oneCloseIncomeRate',AVG(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',AVG(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',AVG(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_test sz WHERE sz.yn=1  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics(String start, String end, Integer rankType, Integer yesterdayTurnover);
    @Query(value="SELECT sz.day_format 'dayFormat',AVG(sz.one_open_rate) 'oneOpenRate',AVG(sz.one_close_rate) 'oneCloseRate',AVG(sz.one_open_income_rate) 'oneOpenIncomeRate',AVG(sz.one_close_income_rate) 'oneCloseIncomeRate',AVG(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',AVG(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',AVG(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_test sz WHERE sz.yn=1 and sz.rank<10  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics2(String start, String end, Integer rankType, Integer yesterdayTurnover);
    @Query(value="SELECT sz.day_format 'dayFormat',AVG(sz.one_open_rate) 'oneOpenRate',AVG(sz.one_close_rate) 'oneCloseRate',AVG(sz.one_open_income_rate) 'oneOpenIncomeRate',AVG(sz.one_close_income_rate) 'oneCloseIncomeRate',AVG(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',AVG(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',AVG(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM stock_trade_val_test sz WHERE sz.yn=1 and sz.rank<6  and sz.day_format BETWEEN  ?1 AND ?2 and sz.rank_type=?3 and sz.yesterday_turnover>?4 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics3(String start, String end, Integer rankType, Integer yesterdayTurnover);

    @Query(value="SELECT a.num rank,a.day_format dayFormat,a.`name`,a.`code` FROM (SELECT count(s.`code`) num,max(s.day_format)day_format,s.`code`,s.`name` FROM stock_trade_val_job_copy1 s WHERE s.day_format<= ?1 and s.rank_type =50 and  s.one_close_income_rate>1500 GROUP BY s.`code` ) a WHERE a.num>1 and a.day_format=?1 ORDER BY a.day_format", nativeQuery = true)
    public List<MyTradeSisStock> statisticDay(String end);

    @Query(value="select * from (select tablename_tmp.*,@rownum\\:=@rownum+1,if(@pyear=tablename_tmp.day_format,@rank\\:=@rank+1,@rank\\:=1) as rank_tmp,@pyear\\:=tablename_tmp.day_format from (select * from stock_trade_val_test WHERE rank_type=50 and yesterday_close_price <5000  order by day_format desc,rank asc)tablename_tmp ,(select @rownum\\:=0 , @pyear\\:=null ,@rank\\:=0) a)result where rank_tmp <=?1", nativeQuery = true)
    public List<StockTradeValInfoTest> statisticsList(Integer sum);

    @Query(value="SELECT sz.day_format 'dayFormat',AVG(sz.one_open_rate) 'oneOpenRate',AVG(sz.one_close_rate) 'oneCloseRate',AVG(sz.one_open_income_rate) 'oneOpenIncomeRate',AVG(sz.one_close_income_rate) 'oneCloseIncomeRate',AVG(sz.one_next_open_income_rate) 'oneNextOpenIncomeRate',AVG(sz.one_next_close_income_rate) 'oneNextCloseIncomeRate',AVG(sz.yesterday_turnover) 'yesterdayTurnover',count(sz.day_format)'countNum' FROM (select * from (select tablename_tmp.*,@rownum\\:=@rownum+1,if(@pyear=tablename_tmp.day_format,@rank\\:=@rank+1,@rank\\:=1) as rank_tmp,@pyear\\:=tablename_tmp.day_format from (select * from stock_trade_val_test WHERE rank_type=50 and yesterday_close_price <5000  order by day_format desc,rank asc)tablename_tmp ,(select @rownum\\:=0 , @pyear\\:=null ,@rank\\:=0) a)result where rank_tmp <=?1) sz WHERE sz.yn=1 and sz.day_format BETWEEN  ?2 AND ?3 and sz.rank_type=?4 and sz.yesterday_turnover>?5 GROUP BY sz.day_format ORDER BY sz.day_format desc", nativeQuery = true)
    public List<MyTradeStock> statistics4(Integer sum, String start, String end, Integer rankType, Integer yesterdayTurnover);

    @Query(value="SELECT * FROM mystock_trade_val_job s WHERE s.yn=1 and s.rank_type=1 and s.yesterday_close_price<10000 and s.trade_amount<800 and s.yesterday_turnover<50 and s.day_format=?1 ORDER BY s.yesterday_turnover LIMIT ?2", nativeQuery = true)
    public List<StockTradeValInfoTest> doMe(String day, Integer num);

    @Query(value="SELECT s.day_format 'dayFormat',count(s.day_format)'countNum',sum(s.one_open_income_rate)'oneOpenIncomeRate',sum(s.one_close_income_rate)'oneCloseIncomeRate',sum(s.one_next_open_income_rate)'oneNextOpenIncomeRate',sum(s.one_next_close_income_rate)'oneNextCloseIncomeRate', sum(s.two_open_income_rate)'twoOpenIncomeRate',sum(s.two_close_income_rate)'twoCloseIncomeRate' FROM stock_trade_val_test s WHERE s.yn=1 and s.rank_type =?1 and s.one_open_rate < 960 and s.day_format BETWEEN ?2 and ?3 ", nativeQuery = true)
    public List<MyDoTradeStock> doMeSta(Integer rankType, String start, String end);

    @Query(value="SELECT * FROM stock_trade_val_test s WHERE s.yn=1 and s.yesterday_close_price >2000 and s.rank_type =1 and s.yesterday_turnover<110 and s.trade_amount BETWEEN 900 and 2000 and s.day_format=?1 ORDER BY s.yesterday_turnover", nativeQuery = true)
    public List<StockTradeValInfoTest> doBigMe(String day);
    @Query(value="SELECT * FROM stock_trade_val_test s WHERE s.yn=1 and s.rank_type =800 and s.today_gains <600 and s.one_close_rate BETWEEN -900 and 600 and s.day_format=?1 ORDER BY s.yesterday_turnover", nativeQuery = true)
    public List<StockTradeValInfoTest> doBig88Me(String day);


}
