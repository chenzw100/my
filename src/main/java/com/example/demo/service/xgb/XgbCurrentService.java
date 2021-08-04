package com.example.demo.service.xgb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockTemperatureRepository;
import com.example.demo.domain.SinaTinyInfoStock;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.domain.table.StockPlate;
import com.example.demo.domain.table.StockTemperature;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockPlateService;
import com.example.demo.service.dfcf.DfcfService;
import com.example.demo.service.qt.QtService;
import com.example.demo.service.sina.SinaService;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 */

@Component
public class XgbCurrentService extends QtService {
    private static String plates_url = "https://flash-api.xuangubao.cn/api/surge_stock/plates";
    private static String limit_up="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_up";
    private static String limit_up_broken ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_up_broken";
    private static String super_stock ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=super_stock";
    private static String new_stock ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=new_stock";
    private static String nearly_stock ="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=nearly_stock";
    private static String market_temperature="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=market_temperature,limit_up_broken_count,limit_up_broken_ratio,rise_count,fall_count,limit_down_count,limit_up_count,yesterday_limit_up_avg_pcp";

   // private static String limit_down="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=limit_down";
    //private static String yesterday_limit_up="https://flash-api.xuangubao.cn/api/pool/detail?pool_name=yesterday_limit_up";yesterday_limit_up_avg_pcp
    private static String market_url="https://flash-api.xuangubao.cn/api/market_indicator/pcp_distribution";
    //private static String broken_url="https://flash-api.xuangubao.cn/api/market_indicator/line?fields=limit_up_broken_count,limit_up_broken_ratio";

    @Autowired
    DfcfService dfcfService;
    @Autowired
    SinaService sinaService;
    @Autowired
    StockTemperatureRepository stockTemperatureRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockPlateService stockPlateService;

    private static int downCount =0,downCountCYB=0;
    private static int downNewCount =0,superNewCount=0;
    private static int downNearlyCount =0,superNearlyCount=0;
    private static int  upCount = 0,upCountCYB=0;
    private static int superCount=0,superCountCYB=0;
    private static int superUpCount=0,superUpCountCYB=0;
    public void currentPan(){
        log.info("xgb==>start current");
        limitUp();
        superStockBefore();
        limitUpBrokenAfter();
        temperature(NumberEnum.TemperatureType.NORMAL.getCode());
        log.info("xgb===>end current");
    }
    public void closePan(){
        log.info("xgb==>start closePan");
        limitUp();
        superStockBefore();
        limitUpBrokenAfter();
        temperature(NumberEnum.TemperatureType.CLOSE.getCode());
        platesClose();
        log.info("xgb===>end closePan");
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
            String continueVal = dfcfService.currentContinueVal();
            log.info("-----------lian :"+continueVal);
            temperature.setContinueVal(continueVal);
            //temperature.setYesterdayShow(MyUtils.getCentByYuanStr(dfcfService.currentYesterdayVal()));
        }
        temperature.setTradeVal(currentTradeVal());
        temperature.setTradeCYBVal(currentCYBTradeVal());
        temperature.setContinueCount(upCount);
        temperature.setContinueCountCYB(upCountCYB);
        temperature.setStrongDowns(downCount);
        temperature.setStrongDownsCYB(downCountCYB);
        temperature.setSuperCount(superCount);
        temperature.setSuperCountCYB(superCountCYB);
        temperature.setSuperUpCount(superUpCount);
        temperature.setSuperUpCountCYB(superUpCountCYB);
        stockTemperatureRepository.save(temperature);
        upCount=0;upCountCYB=0;
        downCount=0;downCountCYB=0;
        superCount=0;superCountCYB=0;
        superUpCount=0;superUpCountCYB=0;
    }

    public void limitUp(){
        int spaceHeight = 1;
        Object response = getRequest(limit_up);
        JSONArray array =null;
        try {
           array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        }catch (Exception e){
            log.error(":limitUp==================>"+e.getMessage(),e);
            return;
        }
        log.info(":zt==================>"+array.size());
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

                if (xgbStock.getContinueBoardCount() > spaceHeight) {
                    upCount++;
                    if(codeStr.indexOf("300")==0){
                        upCountCYB++;
                    }
                }
            }
        }

    }

    public void limitUpBrokenAfter(){
        // 先强势，后炸板
        Object response = getRequest(limit_up_broken);
        JSONArray array =null;
        try {
            array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        }catch (Exception e){
            log.error("-broken---->" + e.getMessage(),e);
            return;
        }
        log.info("-broken---->" + array.size());
        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String name = jsonStock.getString("stock_chi_name");
            String symbol = jsonStock.getString("symbol");
            String changePercent = jsonStock.getString("change_percent");
            int cp =MyUtils.getCentByYuanStr(changePercent);
            //log.info(name+"-broken---->"+cp);
            if(!name.contains("S") && cp<0) {
                downCount++;
                if(symbol.indexOf("300")==0){
                    downCountCYB++;
                }
            }

        }
    }
    public void superStockBefore(){
        Object response = getRequest(super_stock);
        JSONArray array =null;
        try {
            array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        }catch (Exception e){
            log.error("-->super"+e.getMessage(),e);
            return;
        }
        log.info("-->super"+array.size());
        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String name = jsonStock.getString("stock_chi_name");
            String changePercent = jsonStock.getString("change_percent");
            Integer superUp = jsonStock.getInteger("limit_up_days");
            String symbol = jsonStock.getString("symbol");
            int cp =MyUtils.getCentByYuanStr(changePercent);
            //log.info(name+"-->super"+cp);
            if(!name.contains("S")) {
                if(superUp==0){
                    if(cp<-8){
                        downCount++;
                        if(symbol.indexOf("300")==0){
                            downCountCYB++;
                        }
                    }else if(cp>6) {
                        superCount++;
                        if(symbol.indexOf("300")==0){
                            superCountCYB++;
                        }
                    }
                }else {
                    superUpCount++;
                    if(symbol.indexOf("300")==0){
                        superUpCountCYB++;
                    }
                }

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
                //stockPlate.setId(null);
                stockPlate.setContinuousCount(stockPlate.getContinuousCount()+1);
            }
            if(desc==null){
                desc="";
            }
            stockPlate.setDescription(desc);
            stockPlate.setDayFormat(MyUtils.getDayFormat());
            stockPlate.setHotSort(i+1);
            log.info("-->plate："+stockPlate.toString());
            stockPlateService.save(stockPlate);
        }
    }
    public void closeNewAndNearly(){
        nearlyStockBefore();
        newStockBefore();
    }
    public void newStockBefore(){
        Object response = getRequest(new_stock);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        log.info("-->super"+array.size());
        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String changePercent = jsonStock.getString("change_percent");
            int cp =MyUtils.getCentByYuanStr(changePercent);
            //log.info(name+"-->super"+cp);
            if(cp<-8){
                downNewCount++;
                String codeStr = jsonStock.getString("symbol");
                add(codeStr,NumberEnum.StockType.STOCK_NEW.getCode());

            }else if(cp>10) {
                superNewCount++;
                String codeStr = jsonStock.getString("symbol");
                add(codeStr,NumberEnum.StockType.STOCK_NEW.getCode());
            }


        }
    }

    public void nearlyStockBefore(){
        Object response = getRequest(nearly_stock);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        log.info("-->super"+array.size());
        for(int i=0;i<array.size();i++){
            JSONObject jsonStock =  array.getJSONObject(i);
            String changePercent = jsonStock.getString("change_percent");
            int cp =MyUtils.getCentByYuanStr(changePercent);
            //log.info(name+"-->super"+cp);
            if(cp<-8){
                downNearlyCount++;
                String codeStr = jsonStock.getString("symbol");
                add(codeStr,NumberEnum.StockType.STOCK_NEARLY.getCode());

            }else if(cp>10) {
                superNearlyCount++;
                String codeStr = jsonStock.getString("symbol");
                add(codeStr,NumberEnum.StockType.STOCK_NEARLY.getCode());
            }


        }
    }
    public void add(String codeStr,Integer type) {
        String code = codeStr.substring(0, 6);
        if (codeStr.contains("Z")) {
            code = "sz" + code;
        } else {
            code = "sh" + code;
        }
        SinaTinyInfoStock sinaStock = sinaService.getTiny(code);
        if (sinaStock == null) {
            return ;
        }
        StockInfo myStock = new StockInfo(code, sinaStock.getName(), type);
        myStock.setYesterdayClosePrice(sinaStock.getYesterdayPrice());
        myStock.setTodayOpenPrice(sinaStock.getOpenPrice());
        myStock.setTodayClosePrice(sinaStock.getCurrentPrice());
        myStock.getTodayCloseYield();
        myStock.setContinuous(1);
        myStock.setOpenCount(-1);
        myStock.setHotSort(-1);
        myStock.setOneFlag(-1);
        StockInfo fiveTgbStockTemp =stockInfoService.findStockKplByCodeAndYesterdayFormat(myStock.getCode());
        if(fiveTgbStockTemp!=null){
            myStock.setShowCount(fiveTgbStockTemp.getShowCount() + 1);
        }else {
            myStock.setShowCount(1);
        }
        List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myStock.getCode(),MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
        if(xgbStocks!=null && xgbStocks.size()>0){
            StockLimitUp xgbStock =xgbStocks.get(0);
            myStock.setPlateName(xgbStock.getPlateName());
            myStock.setOneFlag(xgbStock.getOpenCount());
            myStock.setContinuous(xgbStock.getContinueBoardCount());
            myStock.setLimitUp(1);
        }else {
            xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(myStock.getCode());
            if(xgbStocks!=null && xgbStocks.size()>0){
                myStock.setPlateName(xgbStocks.get(0).getPlateName());
            }else {
                myStock.setPlateName("");
            }
            myStock.setOneFlag(1);
            myStock.setContinuous(0);
            myStock.setLimitUp(0);
        }
        stockInfoService.save(myStock);
    }
}
