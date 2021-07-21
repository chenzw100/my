package com.example.demo.service.sohu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockDPInfoRepository;
import com.example.demo.domain.StockDPInfo;
import com.example.demo.service.base.BaseService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
@Component
public class StockHistoryService extends BaseService {
    private static String history_url ="https://q.stock.sohu.com/hisHq?code=";
    private static String sz_url="https://q.stock.sohu.com/hisHq?code=zs_000001&start=20200101&end=20210716";
    @Autowired
    StockDPInfoRepository stockDPInfoRepository;

    public HashMap<String,JSONArray> getHistory(String code, String start, String end){
        String url = history_url+code+"&start="+start+"&end="+end;
        HashMap<String,JSONArray> map =getHistory(url);
        return map;
    }
    public HashMap<String,JSONArray> getHistory(String url){
        Object result = getRequest(url);
        JSONObject jsonObject = (JSONObject) JSONArray.parseArray(result.toString()).get(0);
        JSONArray jsonArray = jsonObject.getJSONArray("hq");
        HashMap<String,JSONArray> map =new HashMap<>();
        for(Object object :jsonArray){
            JSONArray oa =JSONArray.parseArray(object.toString());
            String day = oa.get(0).toString();
            day= MyUtils.get2DayFormat(day);
            System.out.println("code = " + day );
            map.put(day,oa);
        }
        return map;
    }
    public String getSzHistory(String dayFormat,HashMap<String,JSONArray> map) {
        StockDPInfo stockYyb = new StockDPInfo();
        stockYyb.setCode("000001");
        stockYyb.setName("上证");
        stockYyb.setDayFormat(dayFormat);
        Date yesterdayDate = MyUtils.getFormatDate(stockYyb.getDayFormat());
        ChineseWorkDay nowWorkDay = new ChineseWorkDay(new Date());
        Date now = nowWorkDay.nextWorkDay(yesterdayDate);
        String resultNowDay=MyUtils.getDayFormat(now);
        ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(new Date());
        Date tomorrowDate = tomorrowWorkDay.nextWorkDay(now);
        ChineseWorkDay threeWorkDay = new ChineseWorkDay(new Date());
        Date threeDate = threeWorkDay.nextWorkDay(tomorrowDate);

        JSONArray three = map.get(MyUtils.getDayFormat(threeDate));
        JSONArray yesterday = map.get(stockYyb.getDayFormat());
        JSONArray today = map.get(resultNowDay);
        JSONArray tomorrow = map.get(MyUtils.getDayFormat(tomorrowDate));
        try {
            Integer yesterdayClosePrice = MyUtils.getCentByYuanStr(yesterday.getString(2));
            stockYyb.setYesterdayClosePrice(yesterdayClosePrice);
            String gains = yesterday.getString(4);
            stockYyb.setYesterdayGains(MyUtils.getCentByYuanStr(gains.substring(0, gains.length() - 1)));
            stockYyb.setYesterdayTurnover(MyUtils.getCentByYuanStr(yesterday.getString(8)));
            stockYyb.setYesterdayVolume(yesterday.getInteger(7));

            if (today == null) {
                log.info(stockYyb.toInfo() + "指数 新数据创建," + stockYyb.getYesterdayClosePrice());
                stockDPInfoRepository.save(stockYyb);

                return resultNowDay;
            }
            Integer todayOpenPrice = MyUtils.getCentByYuanStr(today.getString(1));
            stockYyb.setTodayOpenPrice(todayOpenPrice);
            Integer todayClosePrice = MyUtils.getCentByYuanStr(today.getString(2));
            stockYyb.setTodayClosePrice(todayClosePrice);
            Integer todayLowePrice = MyUtils.getCentByYuanStr(today.getString(5));
            stockYyb.setTodayLowPrice(todayLowePrice);
            Integer todayHighPrice = MyUtils.getCentByYuanStr(today.getString(6));
            stockYyb.setTodayHighPrice(todayHighPrice);
            String todayGains = today.getString(4);
            stockYyb.setTodayGains(MyUtils.getCentByYuanStr(todayGains.substring(0, todayGains.length() - 1)));
            stockYyb.setTodayTurnover(MyUtils.getCentByYuanStr(today.getString(8)));
            stockYyb.setTodayVolume(today.getInteger(7));

            if (tomorrow == null) {
                stockYyb.toOneDay();
                log.info(stockYyb.toInfo() + "指数 更新今日竞价数据," + stockYyb.getOneOpenRate());
                stockDPInfoRepository.save(stockYyb);
                return resultNowDay;
            }
            Integer tomorrowOpenPrice = MyUtils.getCentByYuanStr(tomorrow.getString(1));
            stockYyb.setTomorrowOpenPrice(tomorrowOpenPrice);
            Integer tomorrowClosePrice = MyUtils.getCentByYuanStr(tomorrow.getString(2));
            stockYyb.setTomorrowClosePrice(tomorrowClosePrice);
            Integer tomorrowLowePrice = MyUtils.getCentByYuanStr(tomorrow.getString(5));
            stockYyb.setTomorrowLowPrice(tomorrowLowePrice);
            Integer tomorrowHighPrice = MyUtils.getCentByYuanStr(tomorrow.getString(6));
            stockYyb.setTomorrowHighPrice(tomorrowHighPrice);
            String tomorrowGains = tomorrow.getString(4);
            stockYyb.setTomorrowGains(MyUtils.getCentByYuanStr(tomorrowGains.substring(0, tomorrowGains.length() - 1)));
            stockYyb.setTomorrowTurnover(MyUtils.getCentByYuanStr(tomorrow.getString(8)));
            stockYyb.setTomorrowVolume(tomorrow.getInteger(7));

            if (three == null) {
                stockYyb.toOneDay();
                stockYyb.toOneIncome();
                log.info(stockYyb.toInfo() + "指数 更新明日竞价数据," + stockYyb.getTwoOpenRate());
                stockDPInfoRepository.save(stockYyb);
                return resultNowDay;
            }
            Integer threeOpenPrice = MyUtils.getCentByYuanStr(three.getString(1));
            stockYyb.setThreeOpenPrice(threeOpenPrice);
            Integer threeClosePrice = MyUtils.getCentByYuanStr(three.getString(2));
            stockYyb.setThreeClosePrice(threeClosePrice);
            Integer threeLowePrice = MyUtils.getCentByYuanStr(three.getString(5));
            stockYyb.setThreeLowPrice(threeLowePrice);
            Integer threeHighPrice = MyUtils.getCentByYuanStr(three.getString(6));
            stockYyb.setThreeHighPrice(threeHighPrice);
            String threeGains = three.getString(4);
            stockYyb.setThreeGains(MyUtils.getCentByYuanStr(threeGains.substring(0, threeGains.length() - 1)));
            stockYyb.setThreeTurnover(MyUtils.getCentByYuanStr(three.getString(8)));
            stockYyb.setThreeVolume(three.getInteger(7));

            stockYyb.toString();
            stockDPInfoRepository.save(stockYyb);
            log.info("指数 总更新竞价数据," + stockYyb.getTwoOpenRate());
            return resultNowDay;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return resultNowDay;
    }

    public void dealInfo(){
        String endDay="20210716";
        HashMap<String, JSONArray> map =getHistory(sz_url);
        String resultDay ="20200102";
        while (!endDay.equals(resultDay)){
            log.info("指数 总更新进度," + resultDay);
            resultDay=getSzHistory(resultDay,map);
        }
    }
}
