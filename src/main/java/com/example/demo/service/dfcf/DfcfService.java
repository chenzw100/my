package com.example.demo.service.dfcf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockYybRepository;
import com.example.demo.domain.table.StockYyb;
import com.example.demo.enums.YybEnum;
import com.example.demo.service.base.BaseService;
import com.example.demo.utils.HttpClientUtil;
import com.example.demo.utils.MyUtils;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class DfcfService extends BaseService {
    private static String current_Continue="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0816&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_Yesterday="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0815&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_yyb="https://datainterface3.eastmoney.com/EM_DataCenter_V3/api/YYBJXMX/GetYYBJXMX?js=jQuery112307119371614566392_1625561877508&sortfield=&sortdirec=-1&pageSize=50&tkn=eastmoney&tdir=&dayNum=&startDateTime=2020-07-06&endDateTime=2021-07-06&cfg=yybjymx&salesCode=";

    private static String five_day_url="https://datainterface3.eastmoney.com/EM_DataCenter_V3/api/YYBJXMX/GetYYBJXMX?js=jQuery112308524302760036775_1625625265638&sortfield=&sortdirec=-1&pageSize=50&pageNum=1&tkn=eastmoney&tdir=&dayNum=&cfg=yybjymx&startDateTime=";
    @Autowired
    StockYybRepository stockYybRepository;
    public String currentContinueVal() {
        Object str = getRequest(current_Continue);
        JSONObject jsonObject = JSONObject.parseObject(str.toString());
        int temp = jsonObject.getJSONObject("data").getInteger("f170");
        MyUtils.getYuanByCent(temp);
        return MyUtils.getYuanByCent(temp);
    }
    public String currentYesterdayVal() {
        Object str = getRequest(current_Yesterday);
        JSONObject jsonObject = JSONObject.parseObject(str.toString());
        int temp = jsonObject.getJSONObject("data").getInteger("f170");
        MyUtils.getYuanByCent(temp);
        return MyUtils.getYuanByCent(temp);
    }
    public void currentYyb(Integer ybbId,Integer page) {
        Object result = getRequest(current_yyb+ybbId+"&pageNum="+page);
        String str=result.toString();
        if(str.length()<2000){
            System.out.println("result =================================== [" + str + "]");
            return;
        }
        int index = str.indexOf("(");
        str = str.substring(index+1,str.length()-1);
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray jsonArray=jsonObject.getJSONArray("Data");
        jsonObject = (JSONObject) jsonArray.get(0);
        jsonArray=jsonObject.getJSONArray("Data");
        for(Object data :jsonArray){
            String dataStr = data.toString();
            String d[]=dataStr.split("\\|");
            try {
                System.out.println(d[29]+d[5]+",买入"+d[0]+",净额"+d[17]+",code"+d[10]+"日期"+d[22]+d[8]+",一"+d[30]+",二"+d[13]+"三"+d[6]+"五"+d[11]+"十"+d[4]+"二十"+d[2]+"三十"+d[18]);
            }catch (Exception e){
                e.getMessage();
                return;
            }

            StockYyb stockYyb = new StockYyb();
            stockYyb.setDayFormat(d[22]);
            stockYyb.setCode(d[10]);
            stockYyb.setYybId(Integer.parseInt(d[29]));
            stockYyb.setYzName(YybEnum.YzName.getYzName(stockYyb.getYybId()));

            List<StockYyb> stockYybList =stockYybRepository.findByYybIdAndCodeAndDayFormat(stockYyb.getYybId(),stockYyb.getCode(),stockYyb.getDayFormat());
            if(stockYybList!=null &&stockYybList.size()>0){
                stockYyb= stockYybList.get(0);
            }
            if(stockYyb.getCode().indexOf("688")==0){
                stockYyb.setYn(-1);
            }else {
                stockYyb.setYn(1);
            }

            stockYyb.setName(d[8]);
            stockYyb.setYybName(d[5]);
            stockYyb.setTradeAmount(MyUtils.getYuanPriceStr(d[0]));
            stockYyb.setSumAmount(MyUtils.getYuanPriceStr(d[17]));
            stockYyb.setOneDay(MyUtils.getYuanPriceStr(d[30]));
            stockYyb.setTwoDay(MyUtils.getYuanPriceStr(d[13]));
            stockYyb.setThreeDay(MyUtils.getYuanPriceStr(d[6]));
            stockYyb.setFiveDay(MyUtils.getYuanPriceStr(d[11]));
            stockYyb.setTenDay(MyUtils.getYuanPriceStr(d[4]));
            stockYyb.setTwentyDay(MyUtils.getYuanPriceStr(d[2]));
            stockYyb.setThirtyDay(MyUtils.getYuanPriceStr(d[18]));
            System.out.println(stockYyb.toString());
            try {
                stockYybRepository.save(stockYyb);
            }catch (Exception e){
                e.getMessage();
            }
        }
    }
    public void yyb(Integer yybId){
        for (int i=1;i<21;i++){
            currentYyb(yybId,i);
            System.out.println("=============="+i);
        }
    }
    public void yybJob(){
        YybEnum.YzName.values();
        for (YybEnum.YzName yzName:YybEnum.YzName.values()){
            currentYybJob(yzName.getCode());
            System.out.println(yzName.getCode()+"========+++======"+yzName.name());
        }
    }

    public void currentYybJob(Integer ybbId) {
        String endDay = MyUtils.getDayFormat2(new Date());
        String startDay =MyUtils.getPreTwoMonthDayFormat();
        String url = startDay+"&endDateTime="+endDay+"&salesCode="+ybbId;
        System.out.println("url = [" + url + "]");
        Object result = getRequest(five_day_url+url);
        String str=result.toString();
        if(str.length()<2000){
            System.out.println("result =================================== [" + str + "]");
            return;
        }
        int index = str.indexOf("(");
        str = str.substring(index+1,str.length()-1);
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray jsonArray=jsonObject.getJSONArray("Data");
        jsonObject = (JSONObject) jsonArray.get(0);
        jsonArray=jsonObject.getJSONArray("Data");
        for(Object data :jsonArray){
            String dataStr = data.toString();
            String d[]=dataStr.split("\\|");
            try {
                System.out.println(d[29]+d[5]+",买入"+d[0]+",净额"+d[17]+",code"+d[10]+"日期"+d[22]+d[8]+",一"+d[30]+",二"+d[13]+"三"+d[6]+"五"+d[11]+"十"+d[4]+"二十"+d[2]+"三十"+d[18]);
            }catch (Exception e){
                e.getMessage();
                return;
            }

            StockYyb stockYyb = new StockYyb();
            stockYyb.setDayFormat(d[22]);
            stockYyb.setCode(d[10]);
            stockYyb.setYybId(Integer.parseInt(d[29]));
            stockYyb.setYzName(YybEnum.YzName.getYzName(stockYyb.getYybId()));

            List<StockYyb> stockYybList =stockYybRepository.findByYybIdAndCodeAndDayFormat(stockYyb.getYybId(),stockYyb.getCode(),stockYyb.getDayFormat());
            if(stockYybList!=null &&stockYybList.size()>0){
                stockYyb= stockYybList.get(0);
            }
            if(stockYyb.getCode().indexOf("688")==0){
                stockYyb.setYn(-1);
            }else {
                stockYyb.setYn(1);
            }

            stockYyb.setName(d[8]);
            stockYyb.setYybName(d[5]);
            stockYyb.setTradeAmount(MyUtils.getYuanPriceStr(d[0]));
            stockYyb.setSumAmount(MyUtils.getYuanPriceStr(d[17]));
            stockYyb.setOneDay(MyUtils.getYuanPriceStr(d[30]));
            stockYyb.setTwoDay(MyUtils.getYuanPriceStr(d[13]));
            stockYyb.setThreeDay(MyUtils.getYuanPriceStr(d[6]));
            stockYyb.setFiveDay(MyUtils.getYuanPriceStr(d[11]));
            stockYyb.setTenDay(MyUtils.getYuanPriceStr(d[4]));
            stockYyb.setTwentyDay(MyUtils.getYuanPriceStr(d[2]));
            stockYyb.setThirtyDay(MyUtils.getYuanPriceStr(d[18]));
            System.out.println(stockYyb.toString());
            try {
                stockYybRepository.save(stockYyb);
            }catch (Exception e){
                e.getMessage();
            }
        }
    }
}
