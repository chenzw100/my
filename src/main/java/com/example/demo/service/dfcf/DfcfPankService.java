package com.example.demo.service.dfcf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.dao.StockYybInfoRepository;
import com.example.demo.dao.StockYybRepository;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class DfcfPankService extends QtService {
    public Log log = LogFactory.getLog(DfcfPankService.class);
    private static String current_Continue="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0816&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_Yesterday="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0815&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_yyb="https://datainterface3.eastmoney.com/EM_DataCenter_V3/api/YYBJXMX/GetYYBJXMX?js=jQuery112307119371614566392_1625561877508&sortfield=&sortdirec=-1&pageSize=50&tkn=eastmoney&tdir=&dayNum=&startDateTime=2020-07-06&endDateTime=2021-07-06&cfg=yybjymx&salesCode=";

    private static String five_day_url="https://datainterface3.eastmoney.com/EM_DataCenter_V3/api/YYBJXMX/GetYYBJXMX?js=jQuery112308524302760036775_1625625265638&sortfield=&sortdirec=-1&pageSize=50&pageNum=1&tkn=eastmoney&tdir=&dayNum=&cfg=yybjymx&startDateTime=";
    private static String history_url ="https://q.stock.sohu.com/hisHq?code=cn_";
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
    public void getAllList(){
        List<StockTradeValInfoJob> list =stockTradeValInfoJobRepository.findByOneNextCloseIncomeRateIsNull();
        //List<StockTradeValInfoJob> list =stockTradeValInfoJobRepository.findByOrderByDayFormatDesc();
        for(StockTradeValInfoJob s:list){
            dealJob(s);
        }
    }
    public void getList(){
        Date now = new Date();
        ChineseWorkDay nowWorkDay = new ChineseWorkDay(now);
        Date yesterdayDate2 =nowWorkDay.preDaysWorkDay(2,now);
        List<StockTradeValInfoJob> list =stockTradeValInfoJobRepository.threeStatistic(MyUtils.getDayFormat(yesterdayDate2),MyUtils.getDayFormat(now));
        for(StockTradeValInfoJob s:list){
            dealJob(s);
        }
    }
    public void dealJob(StockTradeValInfoJob stockYyb ){
        Date yesterdayDate = MyUtils.getFormatDate(stockYyb.getDayFormat());
        ChineseWorkDay nowWorkDay = new ChineseWorkDay(yesterdayDate);
        Date now = nowWorkDay.nextWorkDay();
        ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(now);
        Date tomorrowDate = tomorrowWorkDay.nextWorkDay();
        ChineseWorkDay threeWorkDay = new ChineseWorkDay(tomorrowDate);
        Date threeDate = threeWorkDay.nextWorkDay();


       /* Date now = MyUtils.getFormatDate(stockYyb.getDayFormat());
        ChineseWorkDay yesterdayWorkDay = new ChineseWorkDay(now);
        if(yesterdayWorkDay.isHoliday()){
                log.info("------------ h 休息日"+stockYyb.getDayFormat());
                return;
        }
        Date yesterdayDate = yesterdayWorkDay.preWorkDay();
        ChineseWorkDay tomorrowWorkDay = new ChineseWorkDay(now);
        Date tomorrowDate = tomorrowWorkDay.nextWorkDay();
        ChineseWorkDay threeWorkDay = new ChineseWorkDay(tomorrowDate);
        Date threeDate = threeWorkDay.nextWorkDay();*/


        String start = MyUtils.getDayFormat(yesterdayDate);
        String end = MyUtils.getDayFormat(threeDate);


        HashMap<String, JSONArray> map = getHistory(stockYyb.getCode(),start,end);
        if(map==null){
            return;
        }
        JSONArray three = map.get(MyUtils.getDayFormat(threeDate));
        JSONArray yesterday = map.get(MyUtils.getDayFormat(yesterdayDate));
        JSONArray today = map.get(MyUtils.getDayFormat(now));
        JSONArray tomorrow =map.get(MyUtils.getDayFormat(tomorrowDate));
        try {
            Integer yesterdayClosePrice =MyUtils.getCentByYuanStr(yesterday.getString(2));
            stockYyb.setYesterdayClosePrice(yesterdayClosePrice);
            String gains = yesterday.getString(4);
            stockYyb.setYesterdayGains(MyUtils.getCentByYuanStr(gains.substring(0,gains.length()-1)));
            String turnoverRate=yesterday.getString(9);
            stockYyb.setYesterdayTurnoverRate(MyUtils.getCentByYuanStr(turnoverRate.substring(0,turnoverRate.length()-1)));

            String yesterdayTurnoverStr =yesterday.getString(8);
            yesterdayTurnoverStr =yesterdayTurnoverStr.substring(0,yesterdayTurnoverStr.length()-7);
            stockYyb.setYesterdayTurnover(Integer.parseInt(yesterdayTurnoverStr));

            //stockYyb.setYesterdayTurnover(MyUtils.getCentByYuanStr(yesterday.getString(8)));
            stockYyb.setYesterdayVolume(yesterday.getInteger(7));

            if(today==null){
                log.info(stockYyb.toInfo()+"没有今日数据,"+stockYyb.getYesterdayClosePrice());
                stockTradeValInfoJobRepository.save(stockYyb);
                return;
            }
            Integer todayOpenPrice =MyUtils.getCentByYuanStr(today.getString(1));
            stockYyb.setTodayOpenPrice(todayOpenPrice);
            Integer todayClosePrice=MyUtils.getCentByYuanStr(today.getString(2));
            stockYyb.setTodayClosePrice(todayClosePrice);
            Integer todayLowePrice=MyUtils.getCentByYuanStr(today.getString(5));
            stockYyb.setTodayLowPrice(todayLowePrice);
            Integer todayHighPrice =MyUtils.getCentByYuanStr(today.getString(6));
            stockYyb.setTodayHighPrice(todayHighPrice);
            String todayGains = today.getString(4);
            stockYyb.setTodayGains(MyUtils.getCentByYuanStr(todayGains.substring(0,todayGains.length()-1)));
            String todayTurnoverRate=today.getString(9);
            stockYyb.setTodayTurnoverRate(MyUtils.getCentByYuanStr(todayTurnoverRate.substring(0,todayTurnoverRate.length()-1)));
            stockYyb.setTodayTurnover(MyUtils.getCentByYuanStr(today.getString(8)));
            stockYyb.setTodayVolume(today.getInteger(7));

            if(tomorrow == null){
                stockYyb.toOneDay();
                log.info(stockYyb.toInfo()+"没有明日数据,"+stockYyb.getOneOpenRate());
                stockTradeValInfoJobRepository.save(stockYyb);
                return;
            }
            Integer tomorrowOpenPrice=MyUtils.getCentByYuanStr(tomorrow.getString(1));
            stockYyb.setTomorrowOpenPrice(tomorrowOpenPrice);
            Integer tomorrowClosePrice=MyUtils.getCentByYuanStr(tomorrow.getString(2));
            stockYyb.setTomorrowClosePrice(tomorrowClosePrice);
            Integer tomorrowLowePrice=MyUtils.getCentByYuanStr(tomorrow.getString(5));
            stockYyb.setTomorrowLowPrice(tomorrowLowePrice);
            Integer tomorrowHighPrice =MyUtils.getCentByYuanStr(tomorrow.getString(6));
            stockYyb.setTomorrowHighPrice(tomorrowHighPrice);
            String tomorrowGains = tomorrow.getString(4);
            stockYyb.setTomorrowGains(MyUtils.getCentByYuanStr(tomorrowGains.substring(0,tomorrowGains.length()-1)));
            String tomorrowTurnoverRate=tomorrow.getString(9);
            stockYyb.setTomorrowTurnoverRate(MyUtils.getCentByYuanStr(tomorrowTurnoverRate.substring(0,tomorrowTurnoverRate.length()-1)));
            stockYyb.setTomorrowTurnover(MyUtils.getCentByYuanStr(tomorrow.getString(8)));
            stockYyb.setTomorrowVolume(tomorrow.getInteger(7));

            if(three==null){
                stockYyb.toOneDay();
                stockYyb.toOneIncome();
                log.info(stockYyb.toInfo()+"更新明日竞价数据,"+stockYyb.getTwoOpenRate());
                stockTradeValInfoJobRepository.save(stockYyb);
                return;
            }
            Integer threeOpenPrice=MyUtils.getCentByYuanStr(three.getString(1));
            stockYyb.setThreeOpenPrice(threeOpenPrice);
            Integer threeClosePrice=MyUtils.getCentByYuanStr(three.getString(2));
            stockYyb.setThreeClosePrice(threeClosePrice);
            Integer threeLowePrice=MyUtils.getCentByYuanStr(three.getString(5));
            stockYyb.setThreeLowPrice(threeLowePrice);
            Integer threeHighPrice=MyUtils.getCentByYuanStr(three.getString(6));
            stockYyb.setThreeHighPrice(threeHighPrice);
            String threeGains = three.getString(4);
            stockYyb.setThreeGains(MyUtils.getCentByYuanStr(threeGains.substring(0,threeGains.length()-1)));
            String threeTurnoverRate=three.getString(9);
            stockYyb.setThreeTurnoverRate(MyUtils.getCentByYuanStr(threeTurnoverRate.substring(0,threeTurnoverRate.length()-1)));
            stockYyb.setThreeTurnover(MyUtils.getCentByYuanStr(three.getString(8)));
            stockYyb.setThreeVolume(three.getInteger(7));

            stockYyb.toString();
            stockTradeValInfoJobRepository.save(stockYyb);
            log.info(stockYyb.getDayFormat()+"  总更新竞价数据,"+stockYyb.getTwoOpenRate());
        } catch (Exception e) {
            System.out.println("总更新竞价数据 error -------------"+stockYyb.getDayFormat()+",code="+stockYyb.getCode());
            e.getMessage();
        }
    }

    public HashMap<String,JSONArray> getHistory(String code,String start,String end){
        String url = history_url+code+"&start="+start+"&end="+end;
        Object result = getRequest(url);
        if(result.toString().length()<5){
            log.error("失败1："+url);
            return null;
        }
        JSONObject jsonObject = (JSONObject) JSONArray.parseArray(result.toString()).get(0);
        JSONArray jsonArray = jsonObject.getJSONArray("hq");
        if(jsonArray==null){
            log.error("失败2："+url);
            return null;
        }
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

    public void oneOpenIncomeRate(){
        List<StockTradeValInfoJob> list = stockTradeValInfoJobRepository.findByOneOpenRateIsNull();
        log.info("需要处理的早盘数据"+list.size());
        for(StockTradeValInfoJob s: list){
            String code =s.getCode();
            if (code.indexOf("6") == 0) {
                code = "sh" + code;
            } else {
                code = "sz" + code;
            }
            s.setTodayOpenPrice(getIntCurrentPrice(code));
            try {
                s.toOneOpen();
                stockTradeValInfoJobRepository.save(s);
            }catch (Exception e){
                log.error("code"+code+"=开盘价"+s.getTodayOpenPrice()+e.getMessage(),e);
            }
        }
        list = stockTradeValInfoJobRepository.findByOneOpenIncomeRateIsNullAndTodayClosePriceIsNotNull();
        log.info("需要第二处理的早盘数据"+list.size());
        for(StockTradeValInfoJob s: list){
            String code =s.getCode();
            if (code.indexOf("6") == 0) {
                code = "sh" + code;
            } else {
                code = "sz" + code;
            }
            s.setTomorrowOpenPrice(getIntCurrentPrice(code));
            try {
                s.toOneIncomeOpen();
                log.info("OneIncomeOpen"+s.getOneOpenIncomeRate()+",TomorrowOpenPrice="+s.getTomorrowOpenPrice()+",todayOpenPrice="+s.getTodayOpenPrice());
                stockTradeValInfoJobRepository.save(s);
            }catch (Exception e){
                log.error("code"+code+"=明日开盘价"+s.getTomorrowOpenPrice()+e.getMessage(),e);
            }
        }
        list = stockTradeValInfoJobRepository.findByOneNextOpenIncomeRateIsNullAndTomorrowClosePriceIsNotNull();
        log.info("需要第三处理的早盘数据"+list.size());
        for(StockTradeValInfoJob s: list){
            String code =s.getCode();
            if (code.indexOf("6") == 0) {
                code = "sh" + code;
            } else {
                code = "sz" + code;
            }
            s.setThreeOpenPrice(getIntCurrentPrice(code));
            try {
                s.toOneNextOpenIncomeRateOpen();
                log.info("OneNextOpenIncomeRate"+s.getOneNextCloseIncomeRate()+",ThreeOpenPrice="+s.getThreeOpenPrice()+",todayOpenPrice="+s.getTodayOpenPrice());
                stockTradeValInfoJobRepository.save(s);
            }catch (Exception e){
                log.error("code"+code+"=再日开盘价"+s.getTomorrowOpenPrice()+e.getMessage(),e);
            }
        }
    }

    public void doMe(){
        //String start = "20210809";20220808
        String start = "20220801";
        while (true){
            if(start.equals("20220808")){
                return;
            }
            if(start.equals("20220808")){
                return;
            }
            List<StockTradeValInfoJob> job6s = stockTradeValInfoJobRepository.doMe(start,6);
            for(StockTradeValInfoJob job :job6s){
                StockTradeValInfoJob job6 = new StockTradeValInfoJob();
                BeanUtils.copyProperties(job,job6);
                job6.setId(null);
                job6.setRankType(16);
                stockTradeValInfoJobRepository.save(job6);
            }

            List<StockTradeValInfoJob> job8s = stockTradeValInfoJobRepository.doMe(start,8);
            for(StockTradeValInfoJob job :job8s){
                StockTradeValInfoJob job8 = new StockTradeValInfoJob();
                BeanUtils.copyProperties(job,job8);
                job8.setId(null);
                job8.setRankType(18);
                stockTradeValInfoJobRepository.save(job8);
            }
            Date yesterdayDate = MyUtils.getFormatDate(start);
            ChineseWorkDay nowWorkDay = new ChineseWorkDay(yesterdayDate);
            Date now = nowWorkDay.nextWorkDay();
            start = MyUtils.getDayFormat(now);
            log.info("start"+start);
        }
    }
}
