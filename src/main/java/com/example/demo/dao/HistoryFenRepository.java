package com.example.demo.dao;

import com.example.demo.domain.StockTradeValInfoJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by czw on 2018/10/19.
 * JpaRepository default method
 */
public interface HistoryFenRepository extends JpaRepository<StockTradeValInfoJob,Long> {


}
