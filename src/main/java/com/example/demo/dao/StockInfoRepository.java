package com.example.demo.dao;

import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.table.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 * User user=new User();
 userRepository.findAll();
 userRepository.findOne(1l);
 userRepository.save(user);
 userRepository.delete(user);
 userRepository.count();
 userRepository.exists(1l);
 */
public interface StockInfoRepository extends JpaRepository<StockInfo,Long> {
    List<StockInfo> findAll();
    List<StockInfo> findByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    StockInfo findByCodeAndDayFormatAndStockType(String code, String dayFormat,Integer stockType);
    List<StockInfo> findByDayFormatAndStockTypeOrderByOpenBidRate(String dayFormat,Integer stockType);


    //竞价前3
    List<StockInfo> findFirst3ByDayFormatAndStockTypeOrderByOpenBidRateDesc(String dayFormat,Integer stockType);
    //竞价前3
    List<StockInfo> findFirst3ByDayFormatAndStockTypeOrderByOpenBidRate(String dayFormat,Integer stockType);
    //收盘前3
    List<StockInfo> findFirst3ByDayFormatAndStockTypeOrderByTodayCloseYieldDesc(String dayFormat,Integer stockType);
    //收盘前3
    List<StockInfo> findFirst3ByDayFormatAndStockTypeOrderByTodayCloseYield(String dayFormat,Integer stockType);
    //明日开盘前3
    List<StockInfo> findFirst3ByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(String dayFormat,Integer stockType);
    //明日开盘前3
    List<StockInfo> findFirst3ByDayFormatAndStockTypeOrderByTomorrowOpenYield(String dayFormat,Integer stockType);
    //明日收盘前3
    List<StockInfo> findFirst3ByDayFormatAndStockTypeOrderByTomorrowCloseYieldDesc(String dayFormat,Integer stockType);
    //明日收盘前3
    List<StockInfo> findFirst3ByDayFormatAndStockTypeOrderByTomorrowCloseYield(String dayFormat,Integer stockType);

    List<StockInfo> findFirst3ByDayFormatOrderByFiveHighYieldDesc(String dayFormat);


    List<StockInfo> findByDayFormatAndStockTypeOrderByTodayCloseYieldDesc(String dayFormat,Integer stockType);
    List<StockInfo> findByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(String dayFormat,Integer stockType);
    List<StockInfo> findByDayFormatAndOpenBidRateLessThanOrderByOpenBidRateDesc(String dayFormat,int max);
    List<StockInfo> findByDayFormatOrderByStockTypeDesc(String dayFormat);
    StockInfo save(StockInfo tgbStock);
    @Query(value="SELECT * from stock_info WHERE day_format BETWEEN ?1 AND ?2", nativeQuery = true)
    public List<StockInfo> fiveStatistic(String start, String end);
    @Query(value="SELECT * FROM ( SELECT code, name,sum(hot_seven) as hot_seven,sum(hot_value)as hot_value, COUNT(id) as total_count from stock_info WHERE stock_type =10 and day_format BETWEEN ?1 AND ?2  GROUP BY code) as temp WHERE temp.total_count>2 ORDER BY total_count DESC ", nativeQuery = true)
    public List<MyTotalStock> fiveDayInfo(String start, String end);
    @Query(value="SELECT * from stock_info WHERE stock_type=40 and day_format BETWEEN ?1 AND ?2", nativeQuery = true)
    public List<StockInfo> fiveHeightSpace(String start, String end);
    //竞价前3
    List<StockInfo> findFirst3DistinctStockInfoByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    //竞价末3
    List<StockInfo> findFirst3CodeDistinctByDayFormatOrderByOpenBidRate(String dayFormat);


}
