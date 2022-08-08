package com.example.demo.dao;

import com.example.demo.domain.table.StockStopTemperature;
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
 SELECT * FROM temperature WHERE type=1 ORDER BY id DESC LIMIT 5
 SELECT * FROM temperature WHERE type=3 ORDER BY id DESC LIMIT 5
 WHERE day_format BETWEEN ?1 AND ?2
 SELECT * FROM temperature WHERE type=1 and day_format BETWEEN '20190321' AND '20190328' ORDER BY id DESC;
 SELECT continue_val,yesterday_show/100,now_temperature FROM temperature WHERE day_format='20190402'
 SELECT continue_val,yesterday_show/100,now_temperature FROM temperature WHERE day_format BETWEEN '20190301' AND '20190402'  and type=2;
 */
public interface StockStopTemperatureRepository extends JpaRepository<StockStopTemperature,Long> {
    @Override
    List<StockStopTemperature> findAll();
    List<StockStopTemperature> findByDayFormat(String dayFormat);
    @Override
    StockStopTemperature save(StockStopTemperature temperature);

}
