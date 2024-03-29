package com.example.demo.service.dfcf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockYZRecordRepository;
import com.example.demo.dao.StockYybInfoRepository;
import com.example.demo.dao.StockYybRepository;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.domain.table.StockYZRecord;
import com.example.demo.domain.table.StockYyb;
import com.example.demo.domain.table.StockYybInfo;
import com.example.demo.enums.YybEnum;
import com.example.demo.service.base.BaseService;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import com.mysql.cj.xdevapi.JsonArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class DfcfRecordService extends BaseService {
    private static String current_Continue="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0816&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_Yesterday="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0815&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_yyb="https://datainterface3.eastmoney.com/EM_DataCenter_V3/api/YYBJXMX/GetYYBJXMX?js=jQuery112307119371614566392_1625561877508&sortfield=&sortdirec=-1&pageSize=50&tkn=eastmoney&tdir=&dayNum=&startDateTime=2020-01-06&endDateTime=2021-07-09&cfg=yybjymx&salesCode=";

    private static String five_day_url="https://datainterface3.eastmoney.com/EM_DataCenter_V3/api/YYBJXMX/GetYYBJXMX?js=jQuery112308524302760036775_1625625265638&sortfield=&sortdirec=-1&pageSize=50&pageNum=1&tkn=eastmoney&tdir=&dayNum=&cfg=yybjymx&startDateTime=";
    private static String history_url ="https://q.stock.sohu.com/hisHq?code=cn_";

    @Autowired
    QtService qtService;
    @Autowired
    StockYZRecordRepository stockYZRecordRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    StockYybInfoRepository stockYybInfoRepository;

    public HashMap<String,JSONArray> getHistory(String code,String start,String end){
        String url = history_url+code+"&start="+start+"&end="+end;
        Object result = getRequest(url);
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

    public boolean currentYyb(Integer ybbId,Integer page) {
        Object result = getRequest(current_yyb+ybbId+"&pageNum="+page);
        return dealInfo(result);
    }
    public void yyb(Integer yybId){
        boolean f=true;

        int i=1;
        while (f){
            f=currentYyb(yybId,i);
            i++;
            System.out.println("=============="+i);
        }
    }
    public void yybJob2(){

        for (YybEnum.YzName yzName:YybEnum.YzName.values()){
            int i=1;
            boolean f=true;
            System.out.println(f+yzName.getDesc()+" start yzName end=============="+i);
            while (f){
                f=currentYyb(yzName.getCode(),i);
                i++;
                System.out.println(yzName.getDesc()+" yzName =============="+i);
            }
            System.out.println(yzName.getDesc()+" yzName end=============="+i);
        }
    }
    public void yybJob(){
        YybEnum.YzName.values();
        for (YybEnum.YzName yzName:YybEnum.YzName.values()){
            currentYybJob(yzName.getCode());
            System.out.println(yzName.getCode()+"========+++======"+yzName.name());
        }
    }

    public boolean currentYybJob(Integer ybbId) {
        String endDay = MyUtils.getDayFormat2(new Date());
        String startDay =MyUtils.getPreTwoMonthDayFormat();
        String url = startDay+"&endDateTime="+endDay+"&salesCode="+ybbId;
        System.out.println("url = [" + url + "]");
        Object result = getRequest(five_day_url+url);
       return dealInfo(result);

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

    public boolean dealInfo(Object result) {
        String str = result.toString();
        if (str.length() < 2000) {
            System.out.println("result 111=================================== [" + str + "]");
            return false;
        }
        int index = str.indexOf("(");
        str = str.substring(index + 1, str.length() - 1);
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONArray jsonArray = jsonObject.getJSONArray("Data");
        jsonObject = (JSONObject) jsonArray.get(0);
        jsonArray = jsonObject.getJSONArray("Data");
        for (Object data : jsonArray) {
            String dataStr = data.toString();
            String d[] = dataStr.split("\\|");
            try {
                System.out.println(d[29] + d[5] + ",买入" + d[0] + ",净额" + d[17] + ",code" + d[10] + "日期" + d[22] + d[8] + ",一" + d[30] + ",二" + d[13] + "三" + d[6] + "五" + d[11] + "十" + d[4] + "二十" + d[2] + "三十" + d[18]);
            } catch (Exception e) {
                e.getMessage();
                continue;
            }

            StockYZRecord stockYyb = new StockYZRecord();
            stockYyb.setDayFormat(MyUtils.get2DayFormat(d[22]));
            stockYyb.setCode(d[10]);
            stockYyb.setYybId(Integer.parseInt(d[29]));
            stockYyb.setYzName(YybEnum.YzName.getYzName(stockYyb.getYybId()));

            List<StockYZRecord> stockYybList = stockYZRecordRepository.findByYybIdAndCodeAndDayFormat(stockYyb.getYybId(), stockYyb.getCode(), stockYyb.getDayFormat());
            if (stockYybList != null && stockYybList.size() > 0) {
                stockYyb = stockYybList.get(0);
            }
            if (stockYyb.getCode().indexOf("688") == 0) {
                stockYyb.setYn(-1);
            } else if (stockYyb.getCode().indexOf("900") == 0) {
                stockYyb.setYn(-2);
            } else {
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
            //System.out.println(stockYyb.toString());
            if (StringUtils.isBlank(stockYyb.getPlateName())) {
                stockYyb.setPlateName(getPlateName(stockYyb.getCode()));
            }
            Date yesterdayDate = MyUtils.getFormatDate(stockYyb.getDayFormat());
            ChineseWorkDay nowWorkDay = new ChineseWorkDay(new Date());
            Date now = nowWorkDay.nextWorkDay();
            ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(new Date());
            Date tomorrowDate = tomorrowWorkDay.nextWorkDay();
            ChineseWorkDay threeWorkDay = new ChineseWorkDay(new Date());
            Date threeDate = threeWorkDay.nextWorkDay();


            String start = stockYyb.getDayFormat();
            String end = MyUtils.getDayFormat(threeDate);


            HashMap<String, JSONArray> map = getHistory(stockYyb.getCode(),start,end);
            JSONArray yesterday = map.get(stockYyb.getDayFormat());
            JSONArray today = map.get(MyUtils.getDayFormat(now));
            JSONArray tomorrow =map.get(MyUtils.getDayFormat(tomorrowDate));
            JSONArray three = map.get(MyUtils.getDayFormat(threeDate));
            try {
                Integer yesterdayClosePrice =MyUtils.getCentByYuanStr(yesterday.getString(2));
                stockYyb.setYesterdayClosePrice(yesterdayClosePrice);

                Integer todayOpenPrice =MyUtils.getCentByYuanStr(today.getString(1));
                stockYyb.setTodayOpenPrice(todayOpenPrice);
                Integer todayClosePrice=MyUtils.getCentByYuanStr(today.getString(2));
                stockYyb.setTodayClosePrice(todayClosePrice);
                Integer todayLowePrice=MyUtils.getCentByYuanStr(today.getString(5));
                stockYyb.setTodayLowPrice(todayLowePrice);
                Integer todayHighPrice =MyUtils.getCentByYuanStr(today.getString(6));
                stockYyb.setTodayHighPrice(todayHighPrice);

                Integer tomorrowOpenPrice=MyUtils.getCentByYuanStr(tomorrow.getString(1));
                stockYyb.setTomorrowOpenPrice(tomorrowOpenPrice);
                Integer tomorrowClosePrice=MyUtils.getCentByYuanStr(tomorrow.getString(2));
                stockYyb.setTomorrowClosePrice(tomorrowClosePrice);
                Integer tomorrowLowePrice=MyUtils.getCentByYuanStr(tomorrow.getString(5));
                stockYyb.setTomorrowLowPrice(tomorrowLowePrice);
                Integer tomorrowHighPrice =MyUtils.getCentByYuanStr(tomorrow.getString(6));
                stockYyb.setTomorrowHighPrice(tomorrowHighPrice);

                Integer threeOpenPrice=MyUtils.getCentByYuanStr(three.getString(1));
                stockYyb.setThreeOpenPrice(threeOpenPrice);
                Integer threeClosePrice=MyUtils.getCentByYuanStr(three.getString(2));
                stockYyb.setThreeClosePrice(threeClosePrice);
                Integer threeLowePrice=MyUtils.getCentByYuanStr(three.getString(5));
                stockYyb.setThreeLowPrice(threeLowePrice);
                Integer threeHighPrice=MyUtils.getCentByYuanStr(three.getString(6));
                stockYyb.setThreeHighPrice(threeHighPrice);

                stockYyb.toString();
                stockYZRecordRepository.save(stockYyb);
                System.out.println("result = [" + stockYyb.toString() + "]");
            } catch (Exception e) {
                System.out.println("error -------------"+stockYyb.getDayFormat()+",code="+stockYyb.getCode());
                e.getMessage();
            }

        }
        return true;
    }
}
