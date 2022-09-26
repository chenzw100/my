package com.example.demo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jodd.datetime.JDateTime;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
//import org.apache.commons.httpclient.*;

/**
 * Created by ShaoFan
 *
 * 非st 非退市 非科创  涨停后高开收阳 成交额10亿以上 成交额排序
 * 非st 非退市 非科创  涨停破板后高开收阳 成交额10亿以上 成交额排序
 */
public class ThsUtil {
    public Log log = LogFactory.getLog(ThsUtil.class);
    static String cookiesStr = "etrade_robot_session=sihMXw6k9d40veXAhE2EA72jKIMGQFymkX6BHLxf; v=A8HbhgCDRhz626oo1zdVLv6w0Abe7jXgX2LZ9CMWvUgnCu94az5FsO-y6cCw";

    static String stocks_url ="http://backtest.10jqka.com.cn/backtestonce/historydetail?sort_by=desc&id=20183f27f0950619e5fdb3f724337e1c&start_date=2021-01-01&end_date=2021-02-01&period=1";
    static String back_url="http://backtest.10jqka.com.cn/backtestonce/backtest";
    static String key_back="{\"query\":\"前2天涨跌幅小于-2%；跳空高开；振幅小于2%；缩量；今日收盘价大于前两日最高价\",\"period\":\"1\",\"start_date\":\"2021-01-01\",\"end_date\":\"2021-02-01\",\"benchmark\":\"399300\"}";
    static String url = "http://backtest.10jqka.com.cn/tradebacktest/mebacktest";
    static String keys88="{\"query\":\"非停牌 非st 非退市 昨日跌<-7% 今日锤子 成交额排序 \",\"period\":\"2\",\"start_date\":\"2019-01-01\",\"end_date\":\"2019-03-31\",\"stock_hold\":\"2\",\"day_buy_stock_num\":\"1\",\"upper_income\":\"9\",\"lower_income\":\"5\",\"fall_income\":\"1\",\"bear_market_upper_income\":\"9\",\"bear_market_lower_income\":\"5\",\"bear_market_fall_income\":\"1\",\"buy_position\":\"0\",\"menv\":\"macd1,macd2,macd3,kdj1,jx1,jx2,bbi1,bbi2,bbi3,asi1,asi2,asi3,asi4,dpo1,dpo2,dpo3,dpo4,dma1,dma2,dma3\",\"capital\":\"100000\"}";

    static String yield_url="http://backtest.10jqka.com.cn/tradebacktest/yieldbacktest";
    static String keys99="{\"query\":\"非st 非退市 非科创  涨停后高开收阳 成交额10亿以上 成交额排序\",\"start_date\":\"2022-01-01\",\"end_date\":\"2022-08-29\",\"period\":\"2\",\"stock_hold\":\"1\",\"upper_income\":\"9\",\"lower_income\":\"5\",\"fall_income\":\"1\",\"day_buy_stock_num\":\"1\",\"engine\":\"undefined\",\"capital\":\"100000\"}";

    static TreeMap<String,String> days = new TreeMap<>();
    public static TreeMap getDays() {
        /*days.put("2019-01-04","2019-03-26");
        days.put("2019-03-29","2019-04-23");
        days.put("2019-05-16","2019-05-20");
        days.put("2019-05-28","2019-06-05");
        days.put("2019-05-28","2019-06-05");
        days.put("2019-06-11","2019-07-08");
        days.put("2019-07-24","2019-08-01");
        days.put("2019-08-15","2019-09-18");
        days.put("2019-10-10","2019-10-18");
        days.put("2019-10-28","2019-10-30");
        days.put("2019-11-01","2019-11-11");
        days.put("2019-12-05","2020-01-17");*/

        /*days.put("2020-02-04","2020-02-28");
        days.put("2020-03-04","2020-03-12");
        days.put("2020-03-25","2020-04-27");
        days.put("2020-04-30","2020-05-18");
        days.put("2020-06-01","2020-06-12");
        days.put("2020-06-01","2020-06-12");
        days.put("2020-06-16","2020-07-16");
        days.put("2020-06-16","2020-07-16");
        days.put("2020-07-29","2020-08-13");
        days.put("2020-07-29","2020-08-13");
        days.put("2020-08-14","2020-08-26");
        days.put("2020-08-28","2020-09-04");
        days.put("2020-08-28","2020-09-04");
        days.put("2020-09-18","2020-09-23");
        days.put("2020-10-09","2020-10-23");
        days.put("2020-11-03","2020-12-09");
        days.put("2020-12-17","2021-01-28");*/

        days.put("2021-02-08","2021-02-25");
        days.put("2021-03-17","2021-03-24");
        days.put("2021-03-29","2021-04-12");
        days.put("2021-04-19","2021-04-27");
        days.put("2021-04-28","2021-05-06");
        days.put("2021-05-12","2021-06-09");
        days.put("2021-06-10","2021-06-15");
        days.put("2021-06-23","2021-07-05");
        days.put("2021-07-13","2021-07-19");
        days.put("2021-07-13","2021-07-19");
        days.put("2021-07-21","2021-07-26");
        days.put("2021-08-04","2021-08-18");
        days.put("2021-08-24","2021-09-16");
        days.put("2021-10-19","2021-10-27");
        days.put("2021-11-11","2021-12-20");
        days.put("2021-12-31","2022-01-05");
        return days;
    }
    private static JSONObject keys= new JSONObject();

    public static JSONObject getKeys() {
        keys = JSONObject.parseObject(keys99);
        return keys;
    }

    public static String doPostPayload(String url,String start,String end,String menv){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //存储cookies信息变量
        //CookieStore store = new BasicCookieStore();
        //设置cookies信息
        //CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(store).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Cookie",cookiesStr);
        httpPost.setHeader("Referer","http://backtest.10jqka.com.cn/backtest/app.html");
        httpPost.setHeader("Accept","application/json, text/plain, */*");
                httpPost.setHeader("Accept-Encoding","gzip, deflate");
                        httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.9");
                                httpPost.setHeader("Connection","keep-alive");
                                                httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
                                                        httpPost.setHeader("Cookie","etrade_robot_session=sihMXw6k9d40veXAhE2EA72jKIMGQFymkX6BHLxf; v=A6iyvSHcXpaVZXPDQIrcxa-neZ2_0Q_Z7jHgGGLS8RQWLUaDCuHcaz5FsOCx");
                                                                httpPost.setHeader("Host","backtest.10jqka.com.cn");
                                                                        httpPost.setHeader("Origin","http://backtest.10jqka.com.cn");
                                                                                httpPost.setHeader("Referer"," http://backtest.10jqka.com.cn/backtest/app.html");
                                                                                        httpPost.setHeader("User-Agent"," Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36");
                                                                                                httpPost.setHeader("X-Requested-With","XMLHttpRequest");
                                                                                                        httpPost.setHeader("hexin-v","A6iyvSHcXpaVZXPDQIrcxa-neZ2_0Q_Z7jHgGGLS8RQWLUaDCuHcaz5FsOCx");
        //JSONObject jsonObject = getKeys();
        //jsonObject.put("key", "hjkhkdsa");
        try {
            JSONObject keys = getKeys();
            keys.put("start_date",start);
            keys.put("end_date",end);
            if(StringUtils.isNotBlank(menv)){
                keys.put("menv",menv);
            }
            StringEntity stringEntity = new StringEntity((JSONObject.toJSONString(keys)), "application/json", "utf-8");
            //StringEntity stringEntity = new StringEntity((keys88), "application/json", "utf-8");
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse closeableHttpResponse =httpClient.execute(httpPost);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String res = EntityUtils.toString(httpEntity);//响应内容
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deal(){
        JDateTime startDateTime = new JDateTime();
        JDateTime endDateTime = new JDateTime();
        JDateTime startDateNewTime = new JDateTime();
        JDateTime endDateNewTime = new JDateTime();

        startDateTime.set(2019,12,1);
        endDateTime.set(2019,12,31);
        StringBuilder sb=new StringBuilder();

        while (true) {

            String start = startDateTime.toString("YYYY-MM-DD");
            String end = endDateTime.toString("YYYY-MM-DD");
            String res=doPostPayload(url,start,end,null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject reportData=jsonObject.getJSONObject("result");
            if(reportData!=null){
                reportData=reportData.getJSONObject("reportData");
                sb.append(start).append("=").append(end).append("结果：").append(reportData.toJSONString()).append("\r\n");
                if(reportData.getInteger("level")<3){
                    System.out.println("=============>>date = [" + start + "]-" + end + "]");
                    String reportMenv=reportData.getJSONArray("maxAnnualYield").get(1).toString();

                    startDateNewTime.set(endDateTime.getYear(),endDateTime.getMonth(),1);
                    startDateNewTime.addMonth(1);

                    endDateNewTime.set(endDateTime.getYear(),endDateTime.getMonth(),endDateTime.getDay());
                    endDateNewTime.addMonth(1);
                    //endDateNewTime.setDateTime(getLastDay(endDateNewTime.getYear(), endDateNewTime.getMonth()));

                    String startNew = startDateNewTime.toString("YYYY-MM-DD");
                    String endNew = endDateNewTime.toString("YYYY-MM-DD");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    res=doPostPayload(url,startNew,endNew,reportMenv);
                    JSONObject jsonWin = JSONObject.parseObject(res);
                    JSONObject reportDataWinJson=jsonWin.getJSONObject("result");
                    if(reportDataWinJson!=null){
                        JSONObject reportDataWin=reportDataWinJson.getJSONObject("reportData");
                        System.out.println(startNew + "==" + endNew + "<<==win 結果:"+reportDataWinJson.toJSONString());
                        sb.append(startNew).append("=").append(endNew).append(" WIN结果：").append(reportDataWin.getInteger("level")).append(reportDataWin.toJSONString()).append("\r\n");
                    }else {
                        sb.append(startNew).append("=").append(endNew).append("WIN结果：").append(res).append("\r\n");
                    }
                }
                System.out.println(start + "==" + end + " 結果:"+reportData.toJSONString());
            }else {
                sb.append(start).append("=").append(end).append("结果：").append(res).append("\r\n");
            }

            if (end.equals("2022-08-31")) {
                writeFile("D://user//2022-czx.txt",sb.toString());
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startDateTime.addMonth(1);
            endDateTime.addMonth(1);
            endDateTime.setDateTime(getLastDay(endDateTime.getYear(), endDateTime.getMonth()));

        }

    }
    public static void dealYield(){
        JDateTime startDateTime = new JDateTime();
        JDateTime endDateTime = new JDateTime();
        JDateTime startDateNewTime = new JDateTime();
        JDateTime endDateNewTime = new JDateTime();

        startDateTime.set(2020,5,1);
        endDateTime.set(2020,5,31);
        StringBuilder sb=new StringBuilder();

        while (true) {

            String start = startDateTime.toString("YYYY-MM-DD");
            String end = endDateTime.toString("YYYY-MM-DD");
            String res=doPostPayload(yield_url,start,end,null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject reportData=jsonObject.getJSONObject("result");
            if(reportData!=null){
                reportData=reportData.getJSONObject("reportData");
                sb.append(start).append("=").append(end).append("结果：").append(reportData.toJSONString()).append("\r\n");
                if(reportData.getInteger("level")<4){
                    System.out.println("=============>>date = [" + start + "]-" + end + "]");
                    BigDecimal report= (BigDecimal) reportData.getJSONArray("maxAnnualYield").get(0);
                    if(report.intValue()>0){
                        System.out.println(report+"=============>>date = [" + start + "]-" + end + "]");
                        startDateNewTime.set(endDateTime.getYear(),endDateTime.getMonth(),1);
                        startDateNewTime.addMonth(1);

                        endDateNewTime.set(endDateTime.getYear(),endDateTime.getMonth(),endDateTime.getDay());
                        endDateNewTime.addMonth(1);

                        String startNew = startDateNewTime.toString("YYYY-MM-DD");
                        String endNew = endDateNewTime.toString("YYYY-MM-DD");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        res=doPostPayload(yield_url,startNew,endNew,null);
                        JSONObject jsonWin = JSONObject.parseObject(res);
                        JSONObject reportDataWinJson=jsonWin.getJSONObject("result");
                        if(reportDataWinJson!=null){
                            JSONObject reportDataWin=reportDataWinJson.getJSONObject("reportData");
                            System.out.println(startNew + "==" + endNew + "<<==win 結果:"+reportDataWinJson.toJSONString());
                            sb.append(startNew).append("=").append(endNew).append(" WIN结果：").append(reportDataWin.getInteger("level")).append(reportDataWin.toJSONString()).append("\r\n");
                        }else {
                            sb.append(startNew).append("=").append(endNew).append("WIN结果：").append(res).append("\r\n");
                        }
                    }
                }
                System.out.println(start + "==" + end + " 結果:"+reportData.toJSONString());
            }else {
                sb.append(start).append("=").append(end).append("结果：").append(res).append("\r\n");
            }

            if (end.equals("2021-12-31")) {
                writeFile("D://user//2021-y.txt",sb.toString());
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startDateTime.addMonth(1);
            endDateTime.addMonth(1);
            endDateTime.setDateTime(getLastDay(endDateTime.getYear(), endDateTime.getMonth()));

        }

    }

    public static Date getLastDay(int year, int month) {


        // 获取Calendar类的实例
        Calendar c = Calendar.getInstance();
        // 设置年份
        c.set(Calendar.YEAR, year);
        // 设置月份，因为月份从0开始，所以用month - 1
        //c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.DATE,-1);
        // 获取当前时间下，该月的最大日期的数字
        // int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 将获取的最大日期数设置为Calendar实例的日期数
        //c.set(Calendar.DAY_OF_MONTH, lastDay);

        return c.getTime();
    }
    public static boolean writeFile(String path,String content) {

        File file = new File(path);
        boolean isSuccess = true;
        System.out.println(path);
        // if file doesnt exists, then create it
        if (!file.exists()) {
            try {
                isSuccess = file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isSuccess = false;
            }
        }else {
            file.delete();
        }
        FileWriter fw;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            System.out.println("写入成功.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("写入失败.");
            isSuccess = false;
        }
        return isSuccess;
    }

    public static void dealYieldDay(){
        Date startDate = MyUtils.getFormatDate("20210301");
        Date endDate = MyUtils.getFormatDate("20210331");
        Date startDateNew = MyUtils.getFormatDate("20210301");
        String startNew = MyUtils.getDayFormat2(startDateNew);
        StringBuilder sb=new StringBuilder();
        boolean changePass=true;

        while (true) {

            String start = MyUtils.getDayFormat2(startDate);
            String end = MyUtils.getDayFormat2(endDate);
            String res=doPostPayload(yield_url,start,end,null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject reportData=jsonObject.getJSONObject("result");
            if(reportData!=null){
                reportData=reportData.getJSONObject("reportData");
                sb.append(start).append("=").append(end).append("结果：").append(reportData.toJSONString()).append("\r\n");
                if(reportData.getInteger("level")<4){
                    System.out.println("=============>>date = [" + start + "]-" + end + "]");
                    BigDecimal report= (BigDecimal) reportData.getJSONArray("maxAnnualYield").get(0);
                    if(report.intValue()>0){
                        //好结果需要跟进。。获取跟进日期 需要判断是否重新进入（是否有结束时间）
                        System.out.println(report+"=============>>date = [" + start + "]-" + end + "]");
                        if(changePass){
                            startDateNew.setTime(endDate.getTime());
                            startDateNew =addNum(startDateNew,1);
                            //作为开始时间；
                            startNew = MyUtils.getDayFormat2(startDateNew);
                            //反面作为结束时间
                            changePass=false;
                        }
                        sb.append(startNew).append("=").append(" 进入 WIN结果：").append("\r\n");

                    }else {
                        //需要结束日期
                        if(!changePass){
                            changePass=true;
                            res=doPostPayload(yield_url,startNew,end,null);
                            JSONObject jsonWin = JSONObject.parseObject(res);
                            JSONObject reportDataWinJson=jsonWin.getJSONObject("result");
                            if(reportDataWinJson!=null){
                                JSONObject reportDataWin=reportDataWinJson.getJSONObject("reportData");
                                System.out.println(startNew + "==" + end + "<<==win 結果:"+reportDataWinJson.toJSONString());
                                sb.append(startNew).append("=").append(end).append(" end1 WIN结果：").append(reportDataWin.getInteger("level")).append(reportDataWin.toJSONString()).append("\r\n");
                            }else {
                                sb.append(startNew).append("=").append(end).append("end1 WIN结果：").append(res).append("\r\n");
                            }
                        }
                    }
                }else {
                    //第一级结束日期
                    if(!changePass){
                        changePass=true;
                        res=doPostPayload(yield_url,startNew,end,null);
                        JSONObject jsonWin = JSONObject.parseObject(res);
                        JSONObject reportDataWinJson=jsonWin.getJSONObject("result");
                        if(reportDataWinJson!=null){
                            JSONObject reportDataWin=reportDataWinJson.getJSONObject("reportData");
                            System.out.println(startNew + "==" + end + "<<==win 結果:"+reportDataWinJson.toJSONString());
                            sb.append(startNew).append("=").append(end).append(" end2 WIN结果：").append(reportDataWin.getInteger("level")).append(reportDataWin.toJSONString()).append("\r\n");
                        }else {
                            sb.append(startNew).append("=").append(end).append("end2 WIN结果：").append(res).append("\r\n");
                        }
                    }
                }
                System.out.println(start + "==" + end + " 結果:"+reportData.toJSONString());
            }else {
                sb.append(start).append("=").append(end).append("结果：").append(res).append("\r\n");
            }

            if (end.equals("2022-08-31")||end.equals("2022-08-01")||end.equals("2022-08-02")) {
                //第一级结束日期
                if(!changePass){
                    changePass=true;
                    res=doPostPayload(yield_url,startNew,end,null);
                    JSONObject jsonWin = JSONObject.parseObject(res);
                    JSONObject reportDataWinJson=jsonWin.getJSONObject("result");
                    if(reportDataWinJson!=null){
                        JSONObject reportDataWin=reportDataWinJson.getJSONObject("reportData");
                        System.out.println(startNew + "==" + end + "<<==win 結果:"+reportDataWinJson.toJSONString());
                        sb.append(startNew).append("=").append(end).append(" end2 WIN结果：").append(reportDataWin.getInteger("level")).append(reportDataWin.toJSONString()).append("\r\n");
                    }else {
                        sb.append(startNew).append("=").append(end).append("end2 WIN结果：").append(res).append("\r\n");
                    }
                }
                writeFile("D://user//2022-3Day-y.txt",sb.toString());
                break;
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //加3天
            startDate =addNum(startDate,3);
            endDate =addNum(endDate,3);

        }
    }

    public static Date addNum(Date date,Integer num){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        System.out.println(calendar.get(Calendar.DAY_OF_MONTH));//今天的日期
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+num);//让日期加1
        calendar.get(Calendar.DATE);//加1之后的日期Top
        return calendar.getTime();
    }

    public static void dealYieldMounth(){
        JDateTime startDateTime = new JDateTime();
        JDateTime endDateTime = new JDateTime();

        startDateTime.set(2021,5,1);
        endDateTime.set(2021,5,31);
        StringBuilder sb=new StringBuilder();

        while (true) {

            String start = startDateTime.toString("YYYY-MM-DD");
            String end = endDateTime.toString("YYYY-MM-DD");
            String res=doPostPayload(yield_url,start,end,null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject reportData=jsonObject.getJSONObject("result");
            if(reportData!=null){
                reportData=reportData.getJSONObject("reportData");
                sb.append(start).append("=").append(end).append("结果：").append(reportData.toJSONString()).append("\r\n");
                System.out.println(start + "==" + end + " 結果:"+reportData.toJSONString());
            }else {
                sb.append(start).append("=").append(end).append("结果：").append(res).append("\r\n");
            }

            if (end.equals("2021-07-31")) {
                writeFile("D://user//2021-涨停后高开收阳.txt",sb.toString());
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startDateTime.addMonth(1);
            endDateTime.addMonth(1);
            endDateTime.setDateTime(getLastDay(endDateTime.getYear(), endDateTime.getMonth()));

        }

    }


    public static void dealYieldDays(){
        String fileName = "2021-10均线涨停后高开收阳";
        String start = "";String end = "";
        StringBuilder sb=new StringBuilder();
        StringBuilder incomeSb=new StringBuilder();
        TreeMap<String,String> map=getDays();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            start=entry.getKey();end=entry.getValue();
            String res=doPostPayload(yield_url,start,end,null);
            JSONObject jsonObject = JSONObject.parseObject(res);
            JSONObject result=jsonObject.getJSONObject("result");
            if(result!=null){
                String id = result.getString("id");
                JSONArray profitData=result.getJSONArray("profitData").getJSONObject(0).getJSONArray("everydayIncome");
                JSONArray everydayIncome =profitData.getJSONArray(profitData.size()-1);
                sb.append(start).append("=").append(end).append(" income").append(everydayIncome.toJSONString());
                incomeSb.append(everydayIncome.get(0)).append(",").append(everydayIncome.get(1)).append("\r\n");
                JSONObject reportData=result.getJSONObject("reportData");
                sb.append(" level:").append(reportData.getInteger("level")).append(" maxYield").append(reportData.getJSONArray("maxAnnualYield").toJSONString());

                JSONObject backtestData=result.getJSONArray("backtestData").getJSONObject(0);
                sb.append(" times:").append(backtestData.getInteger("totalTradeTimes")).append(" maxDown:").append(backtestData.getBigDecimal("maxDrawDown"));

                sb.append("\r\n");

            }else {
                sb.append(start).append("=").append(end).append("结果：").append(res).append("\r\n");
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        writeFile("D://user//"+fileName+".txt",sb.toString());
        writeFile("D://user//"+fileName+"_income.txt",incomeSb.toString());

    }
    public static void main(String[] args) {
        dealYieldDays();
        //dealYieldMounth();
        //dealYieldDay();
        //dealYield();
        //deal();
        //Date date =getLastDay(2019,9);
        //System.out.println("args = [" + date + "]");
    }


}
