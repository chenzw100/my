package com.example.demo.service.sina;

import com.example.demo.domain.SinaTinyInfoStock;
import com.example.demo.service.base.BaseService;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/4.
 * 0：”大秦铁路”，股票名字；
 1：”27.55″，今日开盘价；
 2：”27.25″，昨日收盘价；
 3：”26.91″，当前价格；
 4：”27.55″，今日最高价；
 5：”26.20″，今日最低价；
 6：”26.91″，竞买价，即“买一”报价；
 7：”26.92″，竞卖价，即“卖一”报价；
 8：”22114263″，成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百；
 9：”589824680″，成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万；
 10：”4695″，“买一”申请4695股，即47手；
 11：”26.91″，“买一”报价；
 12：”57590″，“买二”
 13：”26.90″，“买二”
 14：”14700″，“买三”
 15：”26.89″，“买三”
 16：”14300″，“买四”
 17：”26.88″，“买四”
 18：”15100″，“买五”
 19：”26.87″，“买五”
 20：”3100″，“卖一”申报3100股，即31手；
 21：”26.92″，“卖一”报价
 (22, 23), (24, 25), (26,27), (28, 29)分别为“卖二”至“卖四的情况”
 30：”2008-01-11″，日期；
 31：”15:05:32″，时间；

 */
@Component
public class SinaService extends BaseService {
    static String url = "https://hq.sinajs.cn/list=";

    private String[] getStock(String code) {
        Object response = getRequest(url+code);
        if(response==null){
            log.error("--error code:"+code);
            return null;
        }
        String str = response.toString();
        String[] stockObj = str.split(",");
        return stockObj;
    }

    public SinaTinyInfoStock getTiny(String code){
        SinaTinyInfoStock tiny = SINA_CACHE.get(code);

        if(tiny==null){
            tiny= new SinaTinyInfoStock();
            tiny.setCode(code);
            String[] stockObj = getStock(code);
            log.info(code+"-SINA_CACHE-"+tiny.toString());
            if(stockObj.length<3){
                return tiny;
            }
            tiny.setName(stockObj[0].split("=\"")[1]);
            tiny.setOpenPrice(MyUtils.getCentBySinaPriceStr(stockObj[1]));
            tiny.setYesterdayPrice(MyUtils.getCentBySinaPriceStr(stockObj[2]));
            tiny.setCurrentPrice(MyUtils.getCentBySinaPriceStr(stockObj[3]));
            tiny.setHighPrice(MyUtils.getCentBySinaPriceStr(stockObj[4]));
            tiny.setLowPrice(MyUtils.getCentBySinaPriceStr(stockObj[5]));
            SINA_CACHE.put(code,tiny);

        }else {
            log.info(code+"-SINA_no_CACHE-"+tiny.toString());
        }
        return tiny;
    }
    public Integer getIntCurrentPrice(String code){
        SinaTinyInfoStock stockObj = getTiny(code);
        return stockObj.getCurrentPrice();
    }
}
