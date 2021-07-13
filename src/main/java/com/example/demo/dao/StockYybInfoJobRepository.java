package com.example.demo.dao;

import com.example.demo.domain.StockYybInfoJob;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockYybInfoJobRepository extends JpaRepository<StockYybInfoJob,Long> {
    StockYybInfoJob findTop1ByYybIdAndYzTypeAndCodeAndDayFormat(Integer ybId, Integer yzType, String code, String dayFormat);



}
