package com.example.demo.dao;

import com.example.demo.domain.table.StockPlateSta;
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
public interface StockPlateStaRepository extends JpaRepository<StockPlateSta,Long> {
    List<StockPlateSta> findByDayFormatAndPlateType(String dayFormat,Integer plateType);
    List<StockPlateSta> findByDayFormatOrderByPlateType(String dayFormat);
    StockPlateSta save(StockPlateSta stockPlate);



}
