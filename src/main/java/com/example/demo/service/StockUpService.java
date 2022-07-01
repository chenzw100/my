package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 * 此乃题材挖掘利器
 */
@Component
public class StockUpService extends QtService {
    private static String history_url ="https://q.stock.sohu.com/hisHq?code=cn_";
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;



    public Page<StockLimitUp> findALl(Integer pageNumber, Integer pageSize, StockLimitUp obj){
        if(pageNumber==null){
            pageNumber=0;
            pageSize=10;
        }
        pageNumber--;

        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"continueBoardCount");
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC,"id");
        //如果有多个排序条件 建议使用此种方式 使用 Sort.by 替换之前的  new Sort();
        Sort sort = Sort.by(order,order1);
        //使用 PageRequest.of 替代之前的 new PageRequest();
        /**
         * page：0 开始
         * size:每页显示的数量
         * 排序的规则
         */
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
       /* ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("customerWx", match -> match.startsWith())
                .withMatcher("customerYx", match -> match.startsWith());*/
        Example<StockLimitUp> example = Example.of(obj/*,matcher*/);
        Page<StockLimitUp> all =null;
        all=stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(obj.getDayFormat(),2,pageable);
        if(all.getTotalElements()==0){
            all=stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(obj.getDayFormat(),1,pageable);
        }

        return all;
    }


    public void doLimitUp(){
        String start = "20220601";
        String end = "20220627";
        boolean f=true;
        while (f){
            if(start.equals(end)){
                f= false;
            }
            Date now = MyUtils.getFormatDate(start);
            ChineseWorkDay nowWorkDay = new ChineseWorkDay(now);
            Date nextDay = nowWorkDay.nextWorkDay();
            List<StockLimitUp> limitUps = stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(start,1);
            for (StockLimitUp up:limitUps){
                dealLimitUp(up);
            }
            start=MyUtils.getDayFormat(nextDay);

        }


    }
    public void dealLimitUp(StockLimitUp stockInfoRecord){
        Date yesterdayDate = MyUtils.getFormatDate(stockInfoRecord.getDayFormat());
        ChineseWorkDay yesterdayWorkDay = new ChineseWorkDay(yesterdayDate);
        if(yesterdayWorkDay.isHoliday()){
            System.out.println("放假 isHoliday = [" + stockInfoRecord.getDayFormat() + "]");
            return;
        }

        ChineseWorkDay todayWorkDay = new ChineseWorkDay(yesterdayDate);
        Date now = todayWorkDay.nextWorkDay();

        ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(now);
        Date tomorrowDate = tomorrowWorkDay.nextWorkDay();

        String start = MyUtils.getDayFormat(yesterdayDate);
        String end = MyUtils.getDayFormat(tomorrowDate);
        stockInfoRecord.setCode(stockInfoRecord.getCode().substring(2,8));
        HashMap<String, JSONArray> map = getHistory(stockInfoRecord.getCode(),start,end);
        if(map==null){
            System.out.println("null = [" + stockInfoRecord.getDayFormat() + "]");
            return;
        }
        JSONArray yesterday = map.get(MyUtils.getDayFormat(yesterdayDate));
        JSONArray today = map.get(MyUtils.getDayFormat(now));
        JSONArray tomorrow =map.get(MyUtils.getDayFormat(tomorrowDate));

        Integer yesterdayClosePrice =MyUtils.getCentByYuanStr(yesterday.getString(2));
        stockInfoRecord.setYesterdayClosePrice(yesterdayClosePrice);


        Integer todayOpenPrice =MyUtils.getCentByYuanStr(today.getString(1));
        stockInfoRecord.setTodayOpenPrice(todayOpenPrice);
        Integer todayClosePrice=MyUtils.getCentByYuanStr(today.getString(2));
        stockInfoRecord.setTodayClosePrice(todayClosePrice);

        Integer tomorrowOpenPrice=MyUtils.getCentByYuanStr(tomorrow.getString(1));
        stockInfoRecord.setTomorrowOpenPrice(tomorrowOpenPrice);
        Integer tomorrowClosePrice=MyUtils.getCentByYuanStr(tomorrow.getString(2));
        stockInfoRecord.setTomorrowClosePrice(tomorrowClosePrice);

        stockLimitUpRepository.save(stockInfoRecord);
        System.out.println("result = [" + stockInfoRecord.toString() + "]");

    }
        public HashMap<String,JSONArray> getHistory(String code,String start,String end){
            String url = history_url+code+"&start="+start+"&end="+end;
            System.out.println("lim up history_url = [" + url + "]");
            Object result = getRequest(url);
            if(result.toString().length()<100){
                return null;
            }
            JSONObject jsonObject = (JSONObject) JSONArray.parseArray(result.toString()).get(0);
            JSONArray jsonArray = jsonObject.getJSONArray("hq");
            HashMap<String,JSONArray> map =new HashMap<>();
            for(Object object :jsonArray){
                JSONArray oa =JSONArray.parseArray(object.toString());
                String day = oa.get(0).toString();
                day=MyUtils.get2DayFormat(day);
                System.out.println("code = " + day );
                map.put(day,oa);
            }
            return map;
        }

}
