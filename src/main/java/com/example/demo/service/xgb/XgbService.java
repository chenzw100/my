package com.example.demo.service.xgb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockTemperatureRepository;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.domain.table.StockPlate;
import com.example.demo.domain.table.StockTemperature;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockPlateService;
import com.example.demo.service.dfcf.DfcfService;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
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
    //private static String market_url="https://flash-api.xuangubao.cn/api/market_indicator/pcp_distribution";
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

                JSONObject jsonReason = jsonStock.getJSONObject("surge_reason");
                JSONArray jsonPlateArray = jsonReason.getJSONArray("related_plates");
                int length = jsonPlateArray.size();
                String plateName="";
                for (int j = 0; j < length; j++) {
                    JSONObject jsonPalte = (JSONObject) jsonPlateArray.get(j);
                    plateName+=jsonPalte.getString("plate_name");
                }
               // xgbStock.setPlateName(jsonReason.getString("stock_reason"));
                xgbStock.setPlateName(plateName);
                stockLimitUpRepository.save(xgbStock);
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
            spaceHeight(spaceHeightStock);
        }
    }
    void spaceHeight(StockLimitUp hstock) {
        log.info("space_height code:"+hstock.getCode()+",height:"+ hstock.getContinueBoardCount());
        StockInfo spaceHeight = new StockInfo(hstock.getCode(),hstock.getName(), NumberEnum.StockType.STOCK_SPACE_HEIGHT.getCode());
        spaceHeight.setOneFlag(hstock.getOpenCount());
        spaceHeight.setContinuous(hstock.getContinueBoardCount());
        spaceHeight.setPlateName(hstock.getPlateName());
        spaceHeight.setYesterdayClosePrice(hstock.getYesterdayClosePrice());
        stockInfoService.save(spaceHeight);
    }
    public void limitUpBrokenAfter(){
        // 先强势，后炸板
        Object response = getRequest(limit_up_broken);
        JSONArray array = JSONObject.parseObject(response.toString()).getJSONArray("data");
        log.info("-broken---->" + array.size());
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
                //downStock.setDownRate(cp*100);
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
        }
    }

}
