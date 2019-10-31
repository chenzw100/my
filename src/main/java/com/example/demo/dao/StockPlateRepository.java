package com.example.demo.dao;

import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.StaStockPlate;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockPlate;
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
public interface StockPlateRepository extends JpaRepository<StockPlate,Long> {
    StockPlate findByPlateCodeAndDayFormat(String code, String dayFormat);
    StockPlate save(StockPlate stockPlate);
    @Query(value="SELECT * from stock_info WHERE day_format BETWEEN ?1 AND ?2", nativeQuery = true)
    public List<StockPlate> statistic(String start, String end);
    @Query(value="SELECT * FROM ( SELECT description,plate_name as plateName,sum(continuous_count) as continuousCount,sum(hot_sort) as hotSort, COUNT(id) as totalCount from stock_plate WHERE day_format BETWEEN ?1 AND ?2  GROUP BY plateName) as temp  ORDER BY totalCount DESC  LIMIT 3", nativeQuery = true)
    public List<StaStockPlate> limit3(String start, String end);


}
