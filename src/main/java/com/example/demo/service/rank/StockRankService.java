package com.example.demo.service.rank;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockInfoRepository;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockRankRepository;
import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.domain.MyDoTradeStock;
import com.example.demo.domain.MyTrendStock;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.domain.table.StockRank;
import com.example.demo.enums.NumberEnum;
import com.example.demo.enums.RankTypeEnum;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class StockRankService extends QtService{
    private static String history_url ="https://q.stock.sohu.com/hisHq?code=cn_";
    @Autowired
    StockRankRepository stockRankRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockTradeValInfoJobRepository stockTradeValInfoJobRepository;
    public StockRank save(StockRank stockInfo){
        return stockRankRepository.save(stockInfo);
    }
    public List<StockRank> findByDayFormatOrderByStockType(String dayFormat){
        return stockRankRepository.findByDayFormatOrderByRankType(dayFormat);
    }
    public List<StockRank> findByDayFormatOrderAndStockType(String dayFormat,Integer stockType){
        return stockRankRepository.findByDayFormatAndRankType(dayFormat,stockType);
    }

    public void dealRank(){
        List<StockInfo> tgbs = stockInfoRepository.findByDayFormatAndStockTypeOrderByHotSortAsc(MyUtils.getDayFormat(), NumberEnum.StockType.STOCK_DAY.getCode());
        for (StockInfo info : tgbs){
            stockRankRepository.save(getRank(info));
        }
        List<StockTradeValInfoJob> trades = stockTradeValInfoJobRepository.findByDayFormatAndRankType(MyUtils.getYesterdayDayFormat(), NumberEnum.StockTradeType.TRADE.getCode());
        for (StockTradeValInfoJob info : trades){
            stockRankRepository.save(getThsRank(info));
        }
    }
    public void dealThsRank(){
        List<StockTradeValInfoJob> ths = stockTradeValInfoJobRepository.findByDayFormatAndRankType(MyUtils.getYesterdayDayFormat(), NumberEnum.StockTradeType.THS.getCode());
        for (StockTradeValInfoJob info : ths){
            stockRankRepository.save(getThsRank(info));
        }
    }
    public void dealThsTradesRank(){
        List<StockTradeValInfoJob> trades = stockTradeValInfoJobRepository.findByDayFormatAndRankType(MyUtils.getYesterdayDayFormat(), NumberEnum.StockTradeType.TRADE.getCode());
        for (StockTradeValInfoJob info : trades){
            stockRankRepository.save(getThsRank(info));
        }
    }

    public StockRank getRank(StockInfo info){
        String code =info.getCode().substring(2,8);
        StockRank myStock = new StockRank();
        myStock.setCode(code);
        myStock.setDayFormat(MyUtils.getDayFormat());
        myStock.setRankType(RankTypeEnum.TGB.getCode());
        myStock.setHotSort(info.getHotSort());
        myStock.setHotValue(info.getHotValue());
        myStock.setName(info.getName());
        myStock.setYesterdayClosePrice(info.getYesterdayClosePrice());
        myStock.setPlateName(info.getPlateName());
        myStock.setContinuous(info.getContinuous());
        myStock.setShowCount(info.getShowCount());
        myStock.setYn(1);
        return myStock;

    }

    public StockRank getThsRank(StockTradeValInfoJob info){
        StockRank myStock = new StockRank();
        myStock.setDayFormat(MyUtils.getDayFormat());
        myStock.setHotSort(info.getRank());
        String code = info.getCode();
        if (code.indexOf("6") == 0) {
            code = "sh" + code;
        } else if (code.indexOf("0") == 0){
            code = "sz" + code;
        }else if (code.indexOf("3") == 0){
            code = "sz" + code;
        }
        myStock.setCode(info.getCode());
        myStock.setName(info.getName());
        myStock.setRankType(info.getRankType());
        myStock.setHotValue(info.getHot());

        myStock.setYesterdayClosePrice(info.getYesterdayClosePrice());

        List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myStock.getCode(),MyUtils.getYesterdayDayFormat());
        if(xgbStocks!=null && xgbStocks.size()>0){
            StockLimitUp xgbStock =xgbStocks.get(0);
            myStock.setPlateName(xgbStock.getPlateName());
            myStock.setContinuous(xgbStock.getContinueBoardCount());
        }else {
            xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(code);
            if(xgbStocks!=null && xgbStocks.size()>0){
                myStock.setPlateName(xgbStocks.get(0).getPlateName());
            }else {
                myStock.setPlateName("");
            }
            myStock.setContinuous(0);
        }
        myStock.setYn(1);
        StockRank myStockY =stockRankRepository.findByDayFormatAndRankTypeAndCode(MyUtils.getYesterdayDayFormat(),myStock.getRankType(),myStock.getCode());
        if(myStockY!=null){
            myStock.setShowCount(myStockY.getShowCount()+1);
        }else {
            myStock.setShowCount(1);
        }
        return myStock;
    }
    
    public void dealSis(){
        log.info("开始处理趋势数据");
        List<StockRank> list = stockRankRepository.findByThreeClosePriceIsNull();
        for(StockRank s:list){
            Date now = MyUtils.getFormatDate(s.getDayFormat());
            ChineseWorkDay todayWorkDay = new ChineseWorkDay(now);
            if(todayWorkDay.isHoliday()){
                System.out.println("isHoliday = [" + s.getDayFormat() + "]");
                continue;
            }
            ChineseWorkDay yesterdayWorkDay = new ChineseWorkDay(now);
            Date yesterdayDate = yesterdayWorkDay.preWorkDay();

            ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(now);
            Date tomorrowDate = tomorrowWorkDay.nextWorkDay();

            ChineseWorkDay threeWorkDay = new ChineseWorkDay(tomorrowDate);
            Date threeDate = threeWorkDay.nextWorkDay();


            String start = MyUtils.getDayFormat(yesterdayDate);
            String end = MyUtils.getDayFormat(threeDate);


            HashMap<String, JSONArray> map = getHistory(s.getCode(),start,end);
            if(map==null){
                continue;
            }
            JSONArray yesterday = map.get(MyUtils.getDayFormat(yesterdayDate));
            JSONArray today = map.get(MyUtils.getDayFormat(now));
            JSONArray tomorrow =map.get(MyUtils.getDayFormat(tomorrowDate));
            JSONArray three = map.get(MyUtils.getDayFormat(threeDate));
            try {
                Integer yesterdayClosePrice =MyUtils.getCentByYuanStr(yesterday.getString(2));
                s.setYesterdayClosePrice(yesterdayClosePrice);

                if(today==null){
                    String url = history_url+s.getCode()+"&start="+start+"&end="+end;
                    log.info(s.toInfo()+"没有今日数据,昨日收盘价"+s.getYesterdayClosePrice()+",today url="+url);
                    stockRankRepository.save(s);
                    continue;
                }
                Integer todayOpenPrice =MyUtils.getCentByYuanStr(today.getString(1));
                s.setTodayOpenPrice(todayOpenPrice);
                Integer todayClosePrice=MyUtils.getCentByYuanStr(today.getString(2));
                s.setTodayClosePrice(todayClosePrice);

                if(tomorrow == null){
                    s.toOneDay();
                    String url = history_url+s.getCode()+"&start="+start+"&end="+end;
                    log.info(s.toInfo()+"没有明日数据,tomorrow url="+url);
                    stockRankRepository.save(s);
                    continue;
                }
                Integer tomorrowOpenPrice=MyUtils.getCentByYuanStr(tomorrow.getString(1));
                s.setTomorrowOpenPrice(tomorrowOpenPrice);
                Integer tomorrowClosePrice=MyUtils.getCentByYuanStr(tomorrow.getString(2));
                s.setTomorrowClosePrice(tomorrowClosePrice);

                if(three==null){
                    s.toOneDay();
                    s.toOneIncome();
                    String url = history_url+s.getCode()+"&start="+start+"&end="+end;
                    log.info(s.toInfo()+"没有第三日数据,,three url"+url);
                    stockRankRepository.save(s);
                    continue;
                }
                Integer threeOpenPrice=MyUtils.getCentByYuanStr(three.getString(1));
                s.setThreeOpenPrice(threeOpenPrice);
                Integer threeClosePrice=MyUtils.getCentByYuanStr(three.getString(2));
                s.setThreeClosePrice(threeClosePrice);


                s.toString();
                stockRankRepository.save(s);
                System.out.println("result = [" + s.toString() + "]");
            } catch (Exception e) {
                log.info(s.toInfo()+"没有第三日数据,,three url"+s.getDayFormat()+",code="+s.getCode()+e.getMessage(),e);
                e.getMessage();
            }
        }
        log.info("处理趋势数据完成");
    }
    public HashMap<String,JSONArray> getHistory(String code,String start,String end){
        String url = history_url+code+"&start="+start+"&end="+end;
        System.out.println("history_url = [" + url + "]");
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

    public List<MyTrendStock> doStaFlow(Integer rankType, String start, String end){
        List<MyTrendStock> details =stockRankRepository.doStaFlow(rankType,start,end);
        return details;
    }
}
