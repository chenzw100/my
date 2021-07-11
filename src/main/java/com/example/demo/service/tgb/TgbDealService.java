package com.example.demo.service.tgb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockInfoRecordRepository;
import com.example.demo.dao.StockInfoRepository;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockInfoRecord;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 * 此乃题材挖掘利器
 */
@Component
public class TgbDealService extends QtService {
    private static String history_url ="https://q.stock.sohu.com/hisHq?code=cn_";
    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockInfoRecordRepository stockInfoRecordRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    //获取24小时的热搜数据
    public void dayDate(){
        List<StockInfo> infos = stockInfoRepository.findByStockTypeOrderByDayFormatDesc(NumberEnum.StockType.STOCK_DAY.getCode());
        for (StockInfo s:infos){
            StockInfoRecord stockInfoRecord = new StockInfoRecord();
            stockInfoRecord.setDayFormat(s.getDayFormat());
            if (StringUtils.isBlank(s.getPlateName())) {
                stockInfoRecord.setPlateName(getPlateName(stockInfoRecord.getCode()));
            }else {
                stockInfoRecord.setPlateName(s.getPlateName());
            }
            stockInfoRecord.setYesterdayClosePrice(s.getYesterdayClosePrice());
            stockInfoRecord.setCode(s.getCode().substring(2,8));


            StockInfoRecord stockYyb1 = stockInfoRecordRepository.findTop1ByCodeAndDayFormat(stockInfoRecord.getCode(), stockInfoRecord.getDayFormat());
            if (stockYyb1 != null ) {
                stockInfoRecord = stockYyb1;
            }
            if (stockInfoRecord.getCode().indexOf("688") == 0) {
                stockInfoRecord.setYn(-1);
            } else if (stockInfoRecord.getCode().indexOf("900") == 0) {
                stockInfoRecord.setYn(-2);
            } else {
                stockInfoRecord.setYn(1);
            }
            stockInfoRecord.setName(s.getName());

            //System.out.println(stockInfoRecord.toString());

            /*Date yesterdayDate = MyUtils.getFormatDate(stockInfoRecord.getDayFormat());
            ChineseWorkDay nowWorkDay = new ChineseWorkDay(new Date());
            Date now = nowWorkDay.nextWorkDay(yesterdayDate);
            ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(new Date());
            Date tomorrowDate = tomorrowWorkDay.nextWorkDay(now);
            ChineseWorkDay threeWorkDay = new ChineseWorkDay(new Date());
            Date threeDate = threeWorkDay.nextWorkDay(tomorrowDate);*/

            Date now = MyUtils.getFormatDate(stockInfoRecord.getDayFormat());
            ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(new Date());
            Date tomorrowDate = tomorrowWorkDay.nextWorkDay(now);
            ChineseWorkDay threeWorkDay = new ChineseWorkDay(new Date());
            Date threeDate = threeWorkDay.nextWorkDay(tomorrowDate);


            String start = stockInfoRecord.getDayFormat();
            String end = MyUtils.getDayFormat(threeDate);


            HashMap<String, JSONArray> map = getHistory(stockInfoRecord.getCode(),start,end);
           /* JSONArray yesterday = map.get(stockInfoRecord.getDayFormat());*/
            JSONArray today = map.get(start);
            JSONArray tomorrow =map.get(MyUtils.getDayFormat(tomorrowDate));
            JSONArray three = map.get(MyUtils.getDayFormat(threeDate));
            try {
                /*Integer yesterdayClosePrice =MyUtils.getCentByYuanStr(yesterday.getString(2));
                stockInfoRecord.setYesterdayClosePrice(yesterdayClosePrice);*/

                Integer todayOpenPrice =MyUtils.getCentByYuanStr(today.getString(1));
                stockInfoRecord.setTodayOpenPrice(todayOpenPrice);
                Integer todayClosePrice=MyUtils.getCentByYuanStr(today.getString(2));
                stockInfoRecord.setTodayClosePrice(todayClosePrice);
                Integer todayLowePrice=MyUtils.getCentByYuanStr(today.getString(5));
                stockInfoRecord.setTodayLowPrice(todayLowePrice);
                Integer todayHighPrice =MyUtils.getCentByYuanStr(today.getString(6));
                stockInfoRecord.setTodayHighPrice(todayHighPrice);

                Integer tomorrowOpenPrice=MyUtils.getCentByYuanStr(tomorrow.getString(1));
                stockInfoRecord.setTomorrowOpenPrice(tomorrowOpenPrice);
                Integer tomorrowClosePrice=MyUtils.getCentByYuanStr(tomorrow.getString(2));
                stockInfoRecord.setTomorrowClosePrice(tomorrowClosePrice);
                Integer tomorrowLowePrice=MyUtils.getCentByYuanStr(tomorrow.getString(5));
                stockInfoRecord.setTomorrowLowPrice(tomorrowLowePrice);
                Integer tomorrowHighPrice =MyUtils.getCentByYuanStr(tomorrow.getString(6));
                stockInfoRecord.setTomorrowHighPrice(tomorrowHighPrice);

                Integer threeOpenPrice=MyUtils.getCentByYuanStr(three.getString(1));
                stockInfoRecord.setThreeOpenPrice(threeOpenPrice);
                Integer threeClosePrice=MyUtils.getCentByYuanStr(three.getString(2));
                stockInfoRecord.setThreeClosePrice(threeClosePrice);
                Integer threeLowePrice=MyUtils.getCentByYuanStr(three.getString(5));
                stockInfoRecord.setThreeLowPrice(threeLowePrice);
                Integer threeHighPrice=MyUtils.getCentByYuanStr(three.getString(6));
                stockInfoRecord.setThreeHighPrice(threeHighPrice);

                stockInfoRecord.toString();
                stockInfoRecordRepository.save(stockInfoRecord);
                System.out.println("result = [" + stockInfoRecord.toString() + "]");
            } catch (Exception e) {
                System.out.println("error -------------"+stockInfoRecord.getDayFormat()+",code="+stockInfoRecord.getCode());
                e.getMessage();
            }
        }
    }
    public HashMap<String,JSONArray> getHistory(String code,String start,String end){
        String url = history_url+code+"&start="+start+"&end="+end;
        System.out.println("history_url = [" + url + "]");
        Object result = getRequest(url);
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

    private String getPlateName(String code){
        String plateName="";
        StockLimitUp xgbStock =stockLimitUpRepository.findTop1ByCodeAndPlateNameIsNotNullOrderByIdDesc(code);
        if(xgbStock!=null ) {
            plateName = xgbStock.getPlateName();
        }
        return plateName;
    }
}
