package com.example.demo.service.ths;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockTradeValInfoTestRepository;
import com.example.demo.domain.StockTradeValInfoTest;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class ThsTestService extends QtService {
    public Log log = LogFactory.getLog(ThsTestService.class);
    private static String history_url ="https://q.stock.sohu.com/hisHq?code=cn_";

    @Autowired
    QtService qtService;

    @Autowired
    StockLimitUpRepository stockLimitUpRepository;

    @Autowired
    StockTradeValInfoTestRepository stockTradeValInfoTestRepository;





    public void getAllList(){
        List<StockTradeValInfoTest> list =stockTradeValInfoTestRepository.findByThreeClosePriceIsNull();
        //List<StockTradeValInfoTest> list =stockTradeValInfoTestRepository.findByOrderByDayFormatDesc();
        for(StockTradeValInfoTest s:list){
            dealJob(s);
        }
    }

    public void dealJob(StockTradeValInfoTest stockYyb ){
        Date yesterdayDate = MyUtils.getFormatDate(stockYyb.getDayFormat());
        ChineseWorkDay nowWorkDay = new ChineseWorkDay(yesterdayDate);
        Date now = nowWorkDay.nextWorkDay();
        ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(now);
        Date tomorrowDate = tomorrowWorkDay.nextWorkDay();
        ChineseWorkDay threeWorkDay = new ChineseWorkDay(tomorrowDate);
        Date threeDate = threeWorkDay.nextWorkDay();

        String start = MyUtils.getDayFormat(yesterdayDate);
        String end = MyUtils.getDayFormat(threeDate);

        HashMap<String, JSONArray> map = getHistory(stockYyb.getCode(),start,end);
        if(map==null){
            return;
        }

        JSONArray three = map.get(MyUtils.getDayFormat(threeDate));
        JSONArray yesterday = map.get(MyUtils.getDayFormat(yesterdayDate));
        JSONArray today = map.get(MyUtils.getDayFormat(now));
        JSONArray tomorrow =map.get(MyUtils.getDayFormat(tomorrowDate));
        try {
            Integer yesterdayClosePrice =MyUtils.getCentByYuanStr(yesterday.getString(2));
            stockYyb.setYesterdayClosePrice(yesterdayClosePrice);
            String gains = yesterday.getString(4);
            stockYyb.setYesterdayGains(MyUtils.getCentByYuanStr(gains.substring(0,gains.length()-1)));
            String turnoverRate=yesterday.getString(9);
            stockYyb.setYesterdayTurnoverRate(MyUtils.getCentByYuanStr(turnoverRate.substring(0,turnoverRate.length()-1)));

            String yesterdayTurnoverStr =yesterday.getString(8);
            yesterdayTurnoverStr =yesterdayTurnoverStr.substring(0,yesterdayTurnoverStr.length()-7);
            stockYyb.setYesterdayTurnover(Integer.parseInt(yesterdayTurnoverStr));

            //stockYyb.setYesterdayTurnover(MyUtils.getCentByYuanStr(yesterday.getString(8)));
            stockYyb.setYesterdayVolume(yesterday.getInteger(7));

            if(today==null){
                String url = history_url+stockYyb.getCode()+"&start="+start+"&end="+end;
                log.info(stockYyb.toInfo()+"没有今日数据,昨日收盘价"+stockYyb.getYesterdayClosePrice()+",today url="+url);
                stockTradeValInfoTestRepository.save(stockYyb);
                return;
            }
            Integer todayOpenPrice =MyUtils.getCentByYuanStr(today.getString(1));
            stockYyb.setTodayOpenPrice(todayOpenPrice);
            Integer todayClosePrice=MyUtils.getCentByYuanStr(today.getString(2));
            stockYyb.setTodayClosePrice(todayClosePrice);
            Integer todayLowePrice=MyUtils.getCentByYuanStr(today.getString(5));
            stockYyb.setTodayLowPrice(todayLowePrice);
            Integer todayHighPrice =MyUtils.getCentByYuanStr(today.getString(6));
            stockYyb.setTodayHighPrice(todayHighPrice);
            String todayGains = today.getString(4);
            stockYyb.setTodayGains(MyUtils.getCentByYuanStr(todayGains.substring(0,todayGains.length()-1)));
            String todayTurnoverRate=today.getString(9);
            stockYyb.setTodayTurnoverRate(MyUtils.getCentByYuanStr(todayTurnoverRate.substring(0,todayTurnoverRate.length()-1)));
            stockYyb.setTodayTurnover(MyUtils.getCentByYuanStr(today.getString(8)));
            stockYyb.setTodayVolume(today.getInteger(7));

            if(tomorrow == null){
                stockYyb.toOneDay();
                String url = history_url+stockYyb.getCode()+"&start="+start+"&end="+end;
                log.info(stockYyb.toInfo()+"没有明日数据,OneOpenRate:"+stockYyb.getOneOpenRate()+",tomorrow url="+url);
                stockTradeValInfoTestRepository.save(stockYyb);
                return;
            }
            Integer tomorrowOpenPrice=MyUtils.getCentByYuanStr(tomorrow.getString(1));
            stockYyb.setTomorrowOpenPrice(tomorrowOpenPrice);
            Integer tomorrowClosePrice=MyUtils.getCentByYuanStr(tomorrow.getString(2));
            stockYyb.setTomorrowClosePrice(tomorrowClosePrice);
            Integer tomorrowLowePrice=MyUtils.getCentByYuanStr(tomorrow.getString(5));
            stockYyb.setTomorrowLowPrice(tomorrowLowePrice);
            Integer tomorrowHighPrice =MyUtils.getCentByYuanStr(tomorrow.getString(6));
            stockYyb.setTomorrowHighPrice(tomorrowHighPrice);
            String tomorrowGains = tomorrow.getString(4);
            stockYyb.setTomorrowGains(MyUtils.getCentByYuanStr(tomorrowGains.substring(0,tomorrowGains.length()-1)));
            String tomorrowTurnoverRate=tomorrow.getString(9);
            stockYyb.setTomorrowTurnoverRate(MyUtils.getCentByYuanStr(tomorrowTurnoverRate.substring(0,tomorrowTurnoverRate.length()-1)));
            stockYyb.setTomorrowTurnover(MyUtils.getCentByYuanStr(tomorrow.getString(8)));
            stockYyb.setTomorrowVolume(tomorrow.getInteger(7));

            if(three==null){
                stockYyb.toOneDay();
                stockYyb.toOneIncome();
                String url = history_url+stockYyb.getCode()+"&start="+start+"&end="+end;
                log.info(stockYyb.toInfo()+"更新明日竞价数据,"+stockYyb.getTwoOpenRate()+",three url"+url);
                stockTradeValInfoTestRepository.save(stockYyb);
                return;
            }
            Integer threeOpenPrice=MyUtils.getCentByYuanStr(three.getString(1));
            stockYyb.setThreeOpenPrice(threeOpenPrice);
            Integer threeClosePrice=MyUtils.getCentByYuanStr(three.getString(2));
            stockYyb.setThreeClosePrice(threeClosePrice);
            Integer threeLowePrice=MyUtils.getCentByYuanStr(three.getString(5));
            stockYyb.setThreeLowPrice(threeLowePrice);
            Integer threeHighPrice=MyUtils.getCentByYuanStr(three.getString(6));
            stockYyb.setThreeHighPrice(threeHighPrice);
            String threeGains = three.getString(4);
            stockYyb.setThreeGains(MyUtils.getCentByYuanStr(threeGains.substring(0,threeGains.length()-1)));
            String threeTurnoverRate=three.getString(9);
            stockYyb.setThreeTurnoverRate(MyUtils.getCentByYuanStr(threeTurnoverRate.substring(0,threeTurnoverRate.length()-1)));
            stockYyb.setThreeTurnover(MyUtils.getCentByYuanStr(three.getString(8)));
            stockYyb.setThreeVolume(three.getInteger(7));

            stockYyb.toString();
            stockTradeValInfoTestRepository.save(stockYyb);
            log.info(stockYyb.getDayFormat()+"  总更新竞价数据,"+stockYyb.getTwoOpenRate());
        } catch (Exception e) {
            System.out.println("总更新竞价数据 error -------------"+stockYyb.getDayFormat()+",code="+stockYyb.getCode());
            e.getMessage();
        }
    }

    public HashMap<String,JSONArray> getHistory(String code,String start,String end){
        String url = history_url+code+"&start="+start+"&end="+end;
        //log.error("url记录："+url);
        Object result = getRequest(url);
        if(result.toString().length()<5){
            log.error("失败1："+url);
            return null;
        }
        JSONObject jsonObject = (JSONObject) JSONArray.parseArray(result.toString()).get(0);
        JSONArray jsonArray = jsonObject.getJSONArray("hq");
        if(jsonArray==null){
            log.error("失败2："+url);
            return null;
        }
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
