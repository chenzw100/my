package com.example.demo.service;

import com.example.demo.dao.StockCurrentRepository;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.table.StockCurrent;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 * 此乃题材挖掘利器
 */
@Component
public class ThsService extends QtService {
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockCurrentRepository stockCurrentRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    String url ="http://45.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112402047143833512457_1626774202347&pn=2&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f6&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1626774202587";


    //获取24小时的热搜数据
    public void dayDate(){
        try {
            Document doc = Jsoup.connect("http://www.iwencai.com/unifiedwap/result?w=2021%E5%B9%B407%E6%9C%8820%E6%97%A5%E6%88%90%E4%BA%A4%E9%A2%9D%E4%BB%8E%E5%A4%A7%E5%88%B0%E5%B0%8F%E6%8E%92%E5%90%8D%E5%89%8D10&querytype=&issugs").get();
            System.out.printf("【"+doc.text()+"】");
            Elements elements = doc.getElementsByClass("iwc-table-content");//data-v-f59b8082
            for(int i=0;i<10;i++){
                Element element = elements.get(i);
            }
        } catch (IOException e) {
            log.error("==>WORKDAY fail "+e.getMessage());
            /*try {
                Thread.sleep(500000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            dayDate();
            log.info("==>重新执行");*/
        }
    }


}
