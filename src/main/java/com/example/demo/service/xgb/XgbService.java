package com.example.demo.service.xgb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockPlateRepository;
import com.example.demo.dao.StockPlateStaRepository;
import com.example.demo.dao.StockTemperatureRepository;
import com.example.demo.domain.StaStockPlate;
import com.example.demo.domain.StaStockPlateImpl;
import com.example.demo.domain.table.*;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockPlateService;
import com.example.demo.service.dfcf.DfcfService;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 */

@Component
public class XgbService extends QtService {
    private static String plates_url = "https://flash-api.xuangubao.cn/api/surge_stock/plates";
    private static String limit_up="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_up";
    private static String limit_up_broken ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_up_broken";
    private static String super_stock ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=super_stock";
    private static String market_temperature="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=market_temperature,limit_up_broken_count,limit_up_broken_ratio,rise_count,fall_count,limit_down_count,limit_up_count,yesterday_limit_up_avg_pcp";

   // private static String limit_down="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_down";
    //private static String yesterday_limit_up="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=yesterday_limit_up";yesterday_limit_up_avg_pcp
    private static String market_url="https://flash-api.xuangubao.cn/api/market_indicator/pcp_distribution";
    //private static String broken_url="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=limit_up_broken_count,limit_up_broken_ratio";

    @Autowired
    DfcfService dfcfService;
    @Autowired
    StockTemperatureRepository stockTemperatureRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockPlateService stockPlateService;
    @Autowired
    StockPlateStaRepository stockPlateStaRepository;
    public void closePan(){
        log.info("XgbService xgb==>start closePan");
        limitUp();
        //superStockBefore();
        //limitUpBrokenAfter();
        //temperature(NumberEnum.TemperatureType.CLOSE.getCode());
        //platesClose();
        staPlates();
        log.info("xgb===>end closePan");
    }
    public void staPlates(){
        List<StaStockPlate> staStockPlatesWeek = stockPlateService.weekStatistic();
        log.info("staStockPlatesWeek-ready data"+staStockPlatesWeek.size());
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesWeek){
                StockPlateSta stockPlateSta = new StockPlateSta();
                stockPlateSta.setDayFormat(MyUtils.getDayFormat());
                stockPlateSta.setContinuousCount(s.getContinuousCount());
                stockPlateSta.setDescription(s.getDescription());
                stockPlateSta.setHotSort(s.getHotSort());
                stockPlateSta.setPlateName(s.getPlateName());
                stockPlateSta.setPlateType(NumberEnum.PlateType.WEEK.getCode());
                stockPlateSta.setTotalCount(s.getTotalCount());
                stockPlateStaRepository.save(stockPlateSta);
            }
        }
        List<StaStockPlate> staStockPlatesWeek2 = stockPlateService.week2Statistic();
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesWeek2){
                StockPlateSta stockPlateSta = new StockPlateSta();
                stockPlateSta.setDayFormat(MyUtils.getDayFormat());
                stockPlateSta.setContinuousCount(s.getContinuousCount());
                stockPlateSta.setDescription(s.getDescription());
                stockPlateSta.setHotSort(s.getHotSort());
                stockPlateSta.setPlateName(s.getPlateName());
                stockPlateSta.setTotalCount(s.getTotalCount());
                stockPlateSta.setPlateType(NumberEnum.PlateType.TWO_WEEK.getCode());
                stockPlateStaRepository.save(stockPlateSta);
            }
        }
        List<StaStockPlate> staStockPlatesMonth = stockPlateService.monthStatistic();
        log.info("staStockPlatesMonth-ready data"+staStockPlatesMonth.size());
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesMonth){
                StockPlateSta stockPlateSta = new StockPlateSta();
                stockPlateSta.setDayFormat(MyUtils.getDayFormat());
                stockPlateSta.setContinuousCount(s.getContinuousCount());
                stockPlateSta.setDescription(s.getDescription());
                stockPlateSta.setHotSort(s.getHotSort());
                stockPlateSta.setPlateName(s.getPlateName());
                stockPlateSta.setTotalCount(s.getTotalCount());
                stockPlateSta.setPlateType(NumberEnum.PlateType.MONTH.getCode());
                stockPlateStaRepository.save(stockPlateSta);
            }
        }
    }

    public void temperature(int type)  {
        StockTemperature temperature = new StockTemperature(type);
        DecimalFormat decimalFormat=new DecimalFormat("0.00");
        Object response = getRequest(market_temperature);
        if(response!=null){
            JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");
            JSONObject jsonDataLast = array.getJSONObject(array.size() - 1);

            int limitDownCount = jsonDataLast.getInteger("limit_down_count");
            int limitUpCount = jsonDataLast.getInteger("limit_up_count");
            temperature.setLimitDown(limitDownCount);
            temperature.setDownUp(limitDownCount);
            temperature.setLimitUp(limitUpCount);
            temperature.setRaiseUp(limitUpCount);
            temperature.setDown(jsonDataLast.getInteger("fall_count"));
            temperature.setRaise(jsonDataLast.getInteger("rise_count"));

            Double limitUpBrokenCount = jsonDataLast.getDouble("limit_up_broken_ratio")*100;
            temperature.setBrokenRatio(MyUtils.getCentBySinaPriceStr(decimalFormat.format(limitUpBrokenCount)));
            temperature.setOpen(jsonDataLast.getInteger("limit_up_broken_count"));

            Double yesterday_limit_up_avg_pcp = jsonDataLast.getDouble("yesterday_limit_up_avg_pcp")*100;
            temperature.setYesterdayShow(MyUtils.getCentBySinaPriceStr(decimalFormat.format(yesterday_limit_up_avg_pcp)));

            array = JSONObject.parseObject(response.toString()).getJSONArray("data");
            jsonDataLast = array.getJSONObject(array.size() - 1);
            String temperatureNum = jsonDataLast.getString("market_temperature");
            temperature.setNowTemperature(MyUtils.getCentBySinaPriceStr(temperatureNum));
        }
        response = getRequest(market_url);
        if(response!=null){
            JSONObject jsonObject =  JSONObject.parseObject(response.toString()).getJSONObject("data");
            int limitDownCount = jsonObject.getInteger("st_limit_down_count");
            int limitUpCount = jsonObject.getInteger("st_limit_up_count");
            temperature.setLimitDown(temperature.getDownUp()-limitDownCount);
            temperature.setLimitUp(temperature.getRaiseUp()-limitUpCount);
        }

        if(type==NumberEnum.TemperatureType.OPEN.getCode()){
            temperature.setContinueVal("0");
            //temperature.setYesterdayShow(0);
        }else {
            temperature.setContinueVal(dfcfService.currentContinueVal());
            //temperature.setYesterdayShow(MyUtils.getCentByYuanStr(dfcfService.currentYesterdayVal()));
        }
        temperature.setTradeVal(currentTradeVal());

        if(type==NumberEnum.TemperatureType.CLOSE.getCode()){
            List<StockInfo> downStocks =stockInfoService.findStockDownsByTomorrowFormat();
            temperature.setStrongDowns(downStocks.size());
            List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByDayFormatAndContinueBoardCountGreaterThan(MyUtils.getDayFormat(),1);
            if(xgbStocks!=null){
                temperature.setContinueCount(xgbStocks.size());
            }else {
                temperature.setContinueCount(0);
            }
        }else {
            temperature.setStrongDowns(0);
        }

        stockTemperatureRepository.save(temperature);
    }

    public void limitUp(){
        StockLimitUp spaceHeightStock=null;
        int spaceHeight = 1;
        Object response = getRequest(limit_up);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        log.info("XgbService zt==================>"+array.size());
        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String name = jsonStock.getString("stock_chi_name");
            if(!name.contains("S")) {
                String codeStr = jsonStock.getString("symbol");
                String code = codeStr.substring(0, 6);
                StockLimitUp xgbStock = new StockLimitUp(code,name,MyUtils.getCurrentDate());
                if (codeStr.contains("Z")) {
                    xgbStock.setCode("sz" + code);
                } else {
                    xgbStock.setCode("sh" + code);
                }
                //log.info(i+":zt==================>"+xgbStock.getCode());
                xgbStock.setOpenCount(jsonStock.getInteger("break_limit_up_times"));
                xgbStock.setContinueBoardCount(jsonStock.getInteger("limit_up_days"));
                xgbStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(jsonStock.getString("price")));

                JSONObject jsonReason = jsonStock.getJSONObject("surge_reason");
                String plateName="";
                if(jsonReason !=  null){
                    JSONArray jsonPlateArray = jsonReason.getJSONArray("related_plates");
                    int length = jsonPlateArray.size();
                    for (int j = 0; j < length; j++) {
                        JSONObject jsonPalte = (JSONObject) jsonPlateArray.get(j);
                        plateName+=jsonPalte.getString("plate_name");
                    }
                   // xgbStock.setPlateName(jsonReason.getString("stock_reason"));
                }
                xgbStock.setPlateName(plateName);
                stockLimitUpRepository.save(xgbStock);
                log.info(i+"XgbService zt==================save>"+xgbStock.getCode());
                if (xgbStock.getContinueBoardCount() > spaceHeight) {
                    spaceHeightStock = xgbStock;
                    spaceHeight = xgbStock.getContinueBoardCount();
                }
                if(xgbStock.getContinueBoardCount()>4){
                    log.info("five up--> code:"+xgbStock.getCode()+",height:"+ xgbStock.getContinueBoardCount());
                    stockInfoService.save(xgbStock.toStockLimitUpFive());
                }
            }
        }
        if(spaceHeightStock!=null){
            List<StockLimitUp> limitUps = stockLimitUpRepository.findByDayFormatAndContinueBoardCount(spaceHeightStock.getDayFormat(),spaceHeightStock.getContinueBoardCount());
            for (StockLimitUp height:limitUps){
                spaceHeight(height);
            }
        }
    }
    void spaceHeight(StockLimitUp hstock) {
        log.info("XgbService space_height code:"+hstock.getCode()+",height:"+ hstock.getContinueBoardCount());
        StockInfo spaceHeight = new StockInfo(hstock.getCode(),hstock.getName(), NumberEnum.StockType.STOCK_SPACE_HEIGHTS.getCode());
        spaceHeight.setOneFlag(hstock.getOpenCount());
        spaceHeight.setContinuous(hstock.getContinueBoardCount());
        spaceHeight.setPlateName(hstock.getPlateName());
        spaceHeight.setYesterdayClosePrice(hstock.getYesterdayClosePrice());
        StockInfo stockTemp =stockInfoService.findStockSpaceHeightByCodeAndYesterdayFormat(spaceHeight.getCode());
        if(stockTemp!=null){
            log.info("XgbService yesterday_space_height:"+spaceHeight.toString());
            spaceHeight.setShowCount(stockTemp.getShowCount() + 1);
        }else {
            spaceHeight.setShowCount(1);
        }
        spaceHeight.setHotValue(100);
        spaceHeight.setHotSeven(100);
        stockInfoService.save(spaceHeight);
    }
    public void limitUpBrokenAfter(){
        // 先强势，后炸板
        Object response = getRequest(limit_up_broken);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        log.info("XgbService -broken---->" + array.size());
        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String name = jsonStock.getString("stock_chi_name");
            String changePercent = jsonStock.getString("change_percent");
            int cp =MyUtils.getCentByYuanStr(changePercent);
            //log.info(name+"-broken---->"+cp);
            if(!name.contains("S") && cp<0) {
                String codeStr = jsonStock.getString("symbol");
                String code = codeStr.substring(0, 6);
                if (codeStr.contains("Z")) {
                    code="sz" + code;
                } else {
                    code="sh" + code;
                }
                StockInfo downStock =stockInfoService.findStockDownsByCodeTodayFormat(code);
                if(downStock==null){
                    downStock = new StockInfo(code,name, NumberEnum.StockType.STOCK_DOWN.getCode());
                }
                downStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(jsonStock.getString("price")));
                log.info("open limit up rate:"+cp);
                List<StockLimitUp> xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(downStock.getCode());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    downStock.setPlateName(xgbStocks.get(0).getPlateName());
                }else {
                    downStock.setPlateName("");
                }
                downStock.setOneFlag(1);
                downStock.setContinuous(0);
                downStock.setLimitUp(0);
                stockInfoService.save(downStock);

            }

        }
    }
    public void superStockBefore(){
        Object response = getRequest(super_stock);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        log.info("-->super"+array.size());
        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String name = jsonStock.getString("stock_chi_name");
            String changePercent = jsonStock.getString("change_percent");
            int cp =MyUtils.getCentByYuanStr(changePercent);
            //log.info(name+"-->super"+cp);
            if(!name.contains("S") && cp<-8) {
                String codeStr = jsonStock.getString("symbol");
                String code = codeStr.substring(0, 6);
                if (codeStr.contains("Z")) {
                    code="sz" + code;
                } else {
                    code="sh" + code;
                }
                StockInfo downStock = new StockInfo(code,name, NumberEnum.StockType.STOCK_DOWN.getCode());
                downStock.setYesterdayClosePrice(MyUtils.getCentByYuanStr(jsonStock.getString("price")));
                log.info("super down rate:"+cp);
                List<StockLimitUp> xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(downStock.getCode());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    downStock.setPlateName(xgbStocks.get(0).getPlateName());
                }else {
                    downStock.setPlateName("");
                }
                downStock.setOneFlag(1);
                downStock.setContinuous(0);
                downStock.setLimitUp(0);
                stockInfoService.save(downStock);

            }

        }
    }
    public void platesClose(){
        Object response = getRequest(plates_url);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONObject("data").getJSONArray("items");
        log.info("-->plates："+array.size());
        int length = 5;
        if(array.size()<5){
            length = array.size();
        }
        for(int i=0;i<length;i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String code = jsonStock.getString("id");
            StockPlate stockPlate = stockPlateService.findByPlateCodeByYesterday(code);
            String desc = jsonStock.getString("description");
            if(stockPlate == null){
                stockPlate = new StockPlate();
                String name = jsonStock.getString("name");
                stockPlate.setPlateName(name);
                stockPlate.setPlateCode(code);
                stockPlate.setContinuousCount(1);
            }else {
                stockPlate.setId(null);
                stockPlate.setContinuousCount(stockPlate.getContinuousCount()+1);
            }
            if(desc==null){
                desc="";
            }
            stockPlate.setDescription(desc);
            stockPlate.setDayFormat(MyUtils.getDayFormat());
            stockPlate.setHotSort(i+1);
            stockPlateService.save(stockPlate);
            log.info("-->plate："+stockPlate.toString());
        }
    }

}
