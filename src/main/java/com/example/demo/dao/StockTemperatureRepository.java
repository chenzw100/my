package com.example.demo.dao;

import com.example.demo.domain.table.StockTemperature;
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
 SELECT * FROM temperature WHERE type=1 ORDER BY id DESC LIMIT 5
 SELECT * FROM temperature WHERE type=3 ORDER BY id DESC LIMIT 5
 WHERE day_format BETWEEN ?1 AND ?2
 SELECT * FROM temperature WHERE type=1 and day_format BETWEEN '20190321' AND '20190328' ORDER BY id DESC;
 SELECT continue_val,yesterday_show/100,now_temperature FROM temperature WHERE day_format='20190402'
 SELECT continue_val,yesterday_show/100,now_temperature FROM temperature WHERE day_format BETWEEN '20190301' AND '20190402'  and type=2;
 */
public interface StockTemperatureRepository extends JpaRepository<StockTemperature,Long> {
    List<StockTemperature> findAll();
    List<StockTemperature> findByDayFormat(String dayFormat);
    StockTemperature save(StockTemperature temperature);
    @Query(value=" SELECT * FROM stock_temperature WHERE type=1 and day_format BETWEEN ?1 AND ?2 ORDER BY id ", nativeQuery = true)
    public List<StockTemperature> open(String start, String end);
    @Query(value=" SELECT * FROM stock_temperature WHERE type=2 and day_format BETWEEN ?1 AND ?2 ORDER BY id ", nativeQuery = true)
    public List<StockTemperature> close(String start, String end);
    @Query(value=" SELECT * FROM stock_temperature WHERE type=3 and day_format BETWEEN ?1 AND ?2 ORDER BY id ", nativeQuery = true)
    public List<StockTemperature> normal(String start, String end);
    @Query(value=" SELECT * FROM stock_temperature WHERE day_format BETWEEN ?1 AND ?2 ORDER BY id ", nativeQuery = true)
    public List<StockTemperature> current(String start, String end);


    @Query(value="SELECT * FROM stock_temperature s WHERE s.type=?1 and (s.continue_val<0 and s.raise<1000) ORDER BY s.created DESC LIMIT 6", nativeQuery = true)
    public List<StockTemperature> buy1(Integer type);

    @Query(value="SELECT * FROM stock_temperature s WHERE s.type=?1 and s.limit_down<5 and s.strong_downs<3 ORDER BY s.created DESC LIMIT 6", nativeQuery = true)
    public List<StockTemperature> buy2(Integer type);

    @Query(value="SELECT * FROM stock_temperature s WHERE s.type=?1 and s.raise<1000 ORDER BY s.created DESC LIMIT 10", nativeQuery = true)
    public List<StockTemperature> ice(Integer type);
    @Query(value="SELECT * FROM stock_temperature s WHERE s.type=?1 and s.raise>3000 ORDER BY s.created DESC LIMIT 10", nativeQuery = true)
    public List<StockTemperature> hot(Integer type);

    @Query(value="SELECT * FROM stock_temperature WHERE type = 2 AND ( continue_val > 5 OR raise > 3700 OR yesterday_show > 500 OR broken_ratio < 2001 ) ORDER BY day_format DESC", nativeQuery = true)
    public List<StockTemperature> hotDay();
    @Query(value="SELECT * FROM stock_temperature WHERE type=2 and strong_downs>9 and continue_val>-0.5 ORDER BY day_format desc", nativeQuery = true)
    public List<StockTemperature> iceDay();
}
