package com.example.demo.dao;

import com.example.demo.domain.StockTradeValCurrent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface StockTradeValCurrentRepository extends JpaRepository<StockTradeValCurrent,Long> {

        List<StockTradeValCurrent> findByDayFormat(String dayFormat);

}
