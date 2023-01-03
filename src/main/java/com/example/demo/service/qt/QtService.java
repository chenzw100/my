package com.example.demo.service.qt;

import cn.hutool.core.date.DateTime;
import com.example.demo.domain.QtStock;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockRank;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.base.BaseService;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by laikui on 2019/9/2.
 *  * v_s_sh600519="1~贵州茅台~600519~358.74~-2.55~-0.71~27705~99411~~4506.49";
 1  0: 未知
 2  1: 股票名称
 3  2: 股票代码
 4  3: 当前价格
 5  4: 涨跌
 6  5: 涨跌%
 7  6: 成交量（手）
 8  7: 成交额（万）
 9  8:
 10  9: 总市值
 */
@Component
public class QtService extends BaseService{
    //http://qt.gtimg.cn/q=sz000858
    static String url_info ="http://qt.gtimg.cn/q=";
    static String url ="http://qt.gtimg.cn/q=s_";
    public String getCurrentPrice(String code){
        DateTime dateTime = new DateTime();
        int hour =dateTime.hour(true);
        String price=null;
        if(hour>14){
            price = PRICE_CACHE.get(code);
        }
        if(hour==9){
            int minute =dateTime.minute();
            if(minute>24&&minute<30){
                price = PRICE_CACHE.get(code);
            }
        }
        if(StringUtils.isBlank(price)){
            String[] stockObj = getStock(code);
            if(stockObj.length<3){
                return "0";
            }
            price = stockObj[3];
            PRICE_CACHE.put(code,price);
            log.info(code+"-put PRICE_CACHE-"+price);
        }else {
            log.info(code+"-get PRICE_CACHE-"+price);
        }
        return price;
    }
    public Integer getIntCurrentPrice(String code){
        String stockObj = getCurrentPrice(code);
        return MyUtils.getCentByYuanStr(stockObj);
    }
    public Integer getIntCurrentPriceNotSys(String code){
        if (code.indexOf("6") == 0) {
            code = "sh" + code;
        } else {
            code = "sz" + code;
        }
        String stockObj = getCurrentPrice(code);
        return MyUtils.getCentByYuanStr(stockObj);
    }
    public Integer currentTradeVal() {
        String[] stockObj = getStock("sh000001");
        if(stockObj.length<7){
            log.error( ":err=qt");
            return 0;
        }
        String str =stockObj[7];
        str = str.substring(0,str.length()-4);
        return Integer.parseInt(str);
    }
    public Integer currentCYBZTradeVal() {
        String[] stockObj = getStock("sz399006");
        if(stockObj.length<7){
            log.error( ":err=qt");
            return 0;
        }
        String str =stockObj[7];
        str = str.substring(0,str.length()-4);
        return Integer.parseInt(str);
    }
    public Integer currentCYBTradeVal() {
        String[] stockObj = getStock("sz399102");
        if(stockObj.length<7){
            log.error( ":err=qt");
            return 0;
        }
        String str =stockObj[7];
        str = str.substring(0,str.length()-4);
        return Integer.parseInt(str);
    }
    private String[] getStock(String code) {
        Object response = getRequest(url+code);
        String str = response.toString();
        String[] stockObj = str.split("~");
        return stockObj;
    }
    private String[] getStockInfo(String code) {
        if (code.indexOf("6") == 0) {
            code = "sh" + code;
        }
        if (code.indexOf("00") == 0)  {
            code = "sz" + code;
        }
        if (code.indexOf("300") == 0)  {
            code = "sz" + code;
        }
        Object response = getRequest(url_info+code);
        String str = response.toString();
        String[] stockObj = str.split("~");
        return stockObj;
    }
    public StockInfo getInfo(String code){
        String[] stockObj = getStock(code);
        if(stockObj.length<3){
            return null;
        }
        StockInfo info = new StockInfo(code, stockObj[1], NumberEnum.StockType.STOCK_KPL.getCode());
        String price = stockObj[3];
        info.setYesterdayClosePrice(MyUtils.getCentByYuanStr(price));
        return info;
    }
    public StockRank getRankInfo(String code){
        String[] stockObj = getStock(code);
        if(stockObj.length<3){
            return null;
        }
        StockRank info = new StockRank();
        info.setCode(code);
        info.setName(stockObj[1]);
        String price = stockObj[3];
        info.setYesterdayClosePrice(MyUtils.getCentByYuanStr(price));
        return info;
    }

    /**
     *     //http://qt.gtimg.cn/q=sz000858
     *  1: 名字
     *  2: 代码
     *  3: 当前价格
     *  4: 昨收
     *  5: 今开
     *  47: 涨停价
     * 48: 跌停价
     */
    public QtStock getQtInfo(String code){
        String[] stockObj = getStockInfo(code);
        if(stockObj.length<3){
            return null;
        }
        QtStock info = new QtStock();
        info.setName(stockObj[1]);
        info.setCode(stockObj[2]);
        info.setCurrentPrice(stockObj[3]);
        info.setYesterdayClosePrice(stockObj[4]);
        info.setTodayOpenPrice(stockObj[5]);
        info.setUpPrice(stockObj[47]);
        info.setDownPrice(stockObj[48]);
        return info;
    }
}
