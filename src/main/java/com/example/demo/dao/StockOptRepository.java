package com.example.demo.dao;

import com.example.demo.domain.table.StockOpt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



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
public interface StockOptRepository extends JpaRepository<StockOpt,Long> {
    StockOpt save(StockOpt stockPlate);
    Page<StockOpt> findByDayFormatOrderByHotType(String dayFormat, Pageable pageable);



}
