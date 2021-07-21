package com.example.demo.service.dfcf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.dao.StockYybInfoRepository;
import com.example.demo.dao.StockYybRepository;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.domain.table.StockYyb;
import com.example.demo.domain.table.StockYybInfo;
import com.example.demo.enums.YybEnum;
import com.example.demo.service.base.BaseService;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class DfcfPankService extends BaseService {
    private static String current_Continue="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0816&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_Yesterday="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0815&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_yyb="https://datainterface3.eastmoney.com/EM_DataCenter_V3/api/YYBJXMX/GetYYBJXMX?js=jQuery112307119371614566392_1625561877508&sortfield=&sortdirec=-1&pageSize=50&tkn=eastmoney&tdir=&dayNum=&startDateTime=2020-07-06&endDateTime=2021-07-06&cfg=yybjymx&salesCode=";

    private static String five_day_url="https://datainterface3.eastmoney.com/EM_DataCenter_V3/api/YYBJXMX/GetYYBJXMX?js=jQuery112308524302760036775_1625625265638&sortfield=&sortdirec=-1&pageSize=50&pageNum=1&tkn=eastmoney&tdir=&dayNum=&cfg=yybjymx&startDateTime=";

    String url ="https://45.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112402047143833512457_1626774202347&pn=1&pz=10&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f6&fs=m:0+t:6,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1626774202624";

    @Autowired
    QtService qtService;
    @Autowired
    StockYybRepository stockYybRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    StockYybInfoRepository stockYybInfoRepository;
    @Autowired
    StockTradeValInfoJobRepository stockTradeValInfoJobRepository;


    public void dealRank() {
        Object result = getRequest(url);
        String str=result.toString();
        if(str.length()<700){
            System.out.println("result =================================== [" + str + "]");
            return;
        }
        int index = str.indexOf("(");
        str = str.substring(index+1,str.length()-2);
        //System.out.printf(str);
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONObject jsonObject2 =jsonObject.getJSONObject("data");
        JSONArray jsonArray=jsonObject2.getJSONArray("diff");
        int i=0;
        for(Object data :jsonArray){
            i++;
            String dataStr = data.toString();
            JSONObject jsonObject1 =JSONObject.parseObject(dataStr);

            StockTradeValInfoJob stockTradeValInfoJob = new StockTradeValInfoJob();
            stockTradeValInfoJob.setDayFormat(MyUtils.getDayFormat());
            stockTradeValInfoJob.setCode(jsonObject1.getString("f12"));
            stockTradeValInfoJob.setName(jsonObject1.getString("f14"));
            stockTradeValInfoJob.setYesterdayClosePrice(MyUtils.getCentByYuanStr(jsonObject1.getString("f2")));
            String yesterdayTurnoverStr =jsonObject1.getString("f6");
            stockTradeValInfoJob.setYesterdayTurnover(Integer.parseInt(yesterdayTurnoverStr.substring(0,yesterdayTurnoverStr.length()-10)));
            stockTradeValInfoJob.setYesterdayGains(MyUtils.getCentByYuanStr(jsonObject1.getString("f3")));
            stockTradeValInfoJob.setRank(i);
            stockTradeValInfoJob.setPlateName(getPlateName(stockTradeValInfoJob.getCode()));
            stockTradeValInfoJob.setYn(1);
            stockTradeValInfoJob.setRankType(1);
            stockTradeValInfoJobRepository.save(stockTradeValInfoJob);
        }
    }

    private String getPlateName(String code){
        if (code.indexOf("6") == 0) {
            code = "sh" + code;
        } else {
            code = "sz" + code;
        }
        String plateName="";
        StockLimitUp  xgbStock =stockLimitUpRepository.findTop1ByCodeAndPlateNameIsNotNullOrderByIdDesc(code);
        if(xgbStock!=null ) {
            plateName = xgbStock.getPlateName();
        }
        return plateName;
    }
}
