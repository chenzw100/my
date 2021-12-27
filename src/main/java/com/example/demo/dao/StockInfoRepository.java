package com.example.demo.dao;

import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.table.MyDayStock;
import com.example.demo.domain.table.StockInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<StockInfo> findByStockTypeAndDayFormatOrderByHotSort(Integer stockType,String dayFormat, Pageable pageable);
    Page<StockInfo> findByStockTypeAndCodeOrderByDayFormatDesc(Integer stockType,String code, Pageable pageable);
    Page<StockInfo> findFirst10ByStockTypeAndHotSortOrderByDayFormatDesc(Integer stockType,Integer hotSort, Pageable pageable);
    List<StockInfo> findByStockTypeOrderByDayFormatDesc(Integer stockType);
    List<StockInfo> findByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    StockInfo findByCodeAndDayFormatAndStockType(String code, String dayFormat,Integer stockType);
    StockInfo findFirst1ByCodeAndDayFormat(String code, String dayFormat);
    List<StockInfo> findByDayFormatAndStockTypeOrderByOpenBidRate(String dayFormat,Integer stockType);
    List<StockInfo> findByDayFormatAndStockTypeOrderByPlateName(String dayFormat,Integer stockType);


    //竞价前3
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByOpenBidRateDesc(String dayFormat,Integer stockType);
    //竞价前3
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByOpenBidRate(String dayFormat,Integer stockType);
    //收盘前3
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByTodayCloseYieldDesc(String dayFormat,Integer stockType);
    //收盘前3
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByTodayCloseYield(String dayFormat,Integer stockType);
    //明日开盘前3
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(String dayFormat,Integer stockType);
    //明日开盘前3
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByTomorrowOpenYield(String dayFormat,Integer stockType);
    //明日收盘前3
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByTomorrowCloseYieldDesc(String dayFormat,Integer stockType);
    //明日收盘前3
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByTomorrowCloseYield(String dayFormat,Integer stockType);

    List<StockInfo> findFirst3ByDayFormatOrderByFiveHighYieldDesc(String dayFormat);


    List<StockInfo> findByDayFormatAndStockTypeOrderByTodayCloseYieldDesc(String dayFormat,Integer stockType);
    List<StockInfo> findByDayFormatAndStockTypeOrderByTomorrowOpenYieldDesc(String dayFormat,Integer stockType);
    List<StockInfo> findByDayFormatAndOpenBidRateLessThanOrderByOpenBidRateDesc(String dayFormat,int max);
    List<StockInfo> findByDayFormatOrderByStockTypeDesc(String dayFormat);
    StockInfo save(StockInfo tgbStock);
    @Query(value="SELECT * from stock_info WHERE day_format BETWEEN ?1 AND ?2", nativeQuery = true)
    public List<StockInfo> fiveStatistic(String start, String end);
    @Query(value="SELECT * FROM ( SELECT code, name,sum(hot_seven) as hotSeven,sum(hot_value)as hotValue, COUNT(id) as totalCount from stock_info WHERE stock_type =10 and day_format BETWEEN ?1 AND ?2  GROUP BY code) as temp WHERE temp.totalCount>2 ORDER BY totalCount DESC ", nativeQuery = true)
    public List<MyTotalStock> fiveDayInfo(String start, String end);
    @Query(value="SELECT * FROM ( SELECT code, name,sum(hot_seven) as hotSeven,sum(hot_value)as hotValue, COUNT(id) as totalCount from stock_info WHERE stock_type =10 and day_format BETWEEN ?1 AND ?2  GROUP BY code) as temp WHERE temp.totalCount>1 ORDER BY totalCount DESC ", nativeQuery = true)
    public List<MyTotalStock> threeDayInfo(String start, String end);
    @Query(value="SELECT * from stock_info WHERE stock_type=40 and day_format BETWEEN ?1 AND ?2", nativeQuery = true)
    public List<StockInfo> fiveHeightSpace(String start, String end);
    @Query(value="SELECT COUNT(s.code) hotValue,s.`code`,s.`name` FROM stock_info s WHERE s.stock_type=10 and s.day_format BETWEEN  ?1 AND ?2  GROUP BY s.`code` ORDER BY hotValue desc LIMIT 0,5", nativeQuery = true)
    public List<StockInfo> hotCode(String start, String end);
    //竞价前3
    List<StockInfo> findFirst3DistinctStockInfoByDayFormatOrderByOpenBidRateDesc(String dayFormat);
    //竞价末3
    List<StockInfo> findFirst3CodeDistinctByDayFormatOrderByOpenBidRate(String dayFormat);

    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByHotSevenDesc(String dayFormat,Integer stockType);
    List<StockInfo> findFirst2ByDayFormatAndStockTypeOrderByHotSort(String dayFormat,Integer stockType);

    @Query(value="SELECT sd.day_format as dayFormat,sd.`name`,sd.`code`,sd.hot_sort as hotSort FROM stock_info sd WHERE sd.stock_type=10 and sd.day_format=?1 and sd.`code` in (SELECT s.`code` FROM stock_info s WHERE s.stock_type=70 and s.day_format=?1)", nativeQuery = true)
    public List<MyDayStock> dayInfoInFive(String start);

}
