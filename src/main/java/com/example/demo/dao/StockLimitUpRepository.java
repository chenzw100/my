package com.example.demo.dao;

import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockLimitUp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
public interface StockLimitUpRepository extends JpaRepository<StockLimitUp,Long> {
    List<StockLimitUp> findAll();
    List<StockLimitUp> findByCodeAndDayFormat(String code, String dayFormat);
    List<StockLimitUp> findByDayFormatAndContinueBoardCountGreaterThan(String dayFormat, int min);
    List<StockLimitUp> findByDayFormatAndContinueBoardCountGreaterThanOrderByContinueBoardCountDesc(String dayFormat, int min);
    StockLimitUp save(StockLimitUp xgbStock);
    List<StockLimitUp> findByCodeAndPlateNameIsNotNullOrderByIdDesc(String code);
    StockLimitUp findTop1ByCodeAndPlateNameIsNotNullOrderByIdDesc(String code);
    Page<StockLimitUp> findByDayFormatAndContinueBoardCountGreaterThan(String dayFormat, int min, Pageable pageable);
    List<StockLimitUp> findByDayFormatAndContinueBoardCount(String dayFormat, int continues);

}
