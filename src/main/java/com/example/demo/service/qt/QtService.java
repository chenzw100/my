package com.example.demo.service.qt;

import com.example.demo.service.base.BaseService;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

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

    static String url ="http://qt.gtimg.cn/q=s_";
    public String getCurrentPrice(String code){
        String price = PRICE_CACHE.get(code);
        if(StringUtils.isEmpty(price)){
            String[] stockObj = getStock(code);
            if(stockObj.length<3){
                return "-1";
            }
            price = stockObj[3];
            PRICE_CACHE.put(code,price);
        }
        return price;
    }
    public Integer getIntCurrentPrice(String code){
        String stockObj = getCurrentPrice(code);
        return MyUtils.getCentByYuanStr(stockObj);
    }
    public Integer currentTradeVal() {
        String[] stockObj = getStock("sh000001");
        if(stockObj.length<7){
            log.error( ":err=qt");
            return -1;
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
}
