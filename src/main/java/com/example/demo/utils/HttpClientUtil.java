package com.example.demo.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpUtil;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import org.apache.commons.httpclient.*;

/**
 * Created by ShaoFan
 */
public class HttpClientUtil {
    public Log log = LogFactory.getLog(HttpClientUtil.class);
    static String cookiesStr = "etrade_robot_session=sihMXw6k9d40veXAhE2EA72jKIMGQFymkX6BHLxf; v=A8HbhgCDRhz626oo1zdVLv6w0Abe7jXgX2LZ9CMWvUgnCu94az5FsO-y6cCw";
    static String url = "http://backtest.10jqka.com.cn/tradebacktest/mebacktest";

    static String keysStr = "PHPSESSID=v5vo58659j7k2qdpvf2qlhoov7; LiveWSLZT73369086=6b4beaabf8a24f83ba6475cd2fcddc8c; LiveWSLZT73369086sessionid=6b4beaabf8a24f83ba6475cd2fcddc8c; NLZT73369086fistvisitetime=1606615551848; NLZT73369086visitecounts=1; UM_distinctid=17611bfd0bf38a-080a953480922e-594a2011-100200-17611bfd0c0416; IESESSION=alive; pgv_pvi=6827934720; pgv_si=s6676731904; tencentSig=3500160000; _qddaz=QD.xqio8d.wmvfcl.ki2hf3fp; _qdda=3-1.1; _qddab=3-80wk5c.ki2hf3ft; _qddamta_4000088650=3-0; CNZZDATA1274190688=493980554-1606610746-%7C1606616150; NLZT73369086lastvisitetime=1606617741299; NLZT73369086visitepages=13";
    static String keys88="{\"query\":\"非停牌；非st；涨停后高开收阳小于5%； 成交额大于10亿 5日均线上方 流通值大于200亿 流通值排序\",\"period\":\"2\",\"start_date\":\"2021-10-01\",\"end_date\":\"2021-12-31\",\"stock_hold\":\"1\",\"day_buy_stock_num\":\"1\",\"upper_income\":\"9\",\"lower_income\":\"5\",\"fall_income\":\"1\",\"bear_market_upper_income\":\"9\",\"bear_market_lower_income\":\"5\",\"bear_market_fall_income\":\"1\",\"buy_position\":\"0\",\"menv\":\"macd1,macd2,macd3,kdj1,jx1,jx2,bbi1,bbi2,bbi3,asi1,asi2,asi3,asi4,dpo1,dpo2,dpo3,dpo4,dma1,dma2,dma3\",\"capital\":\"100000\"}";
    private static JSONObject keys= new JSONObject();

    public static JSONObject getKeys() {
        keys = JSONObject.parseObject(keys88);
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
        startDateTime.set(2019,10,1);
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
                    endDateNewTime.addDay(1);
                    startDateNewTime=startDateTime;
                    startDateNewTime.addMonth(3);
                    endDateNewTime=endDateTime;
                    endDateNewTime.addMonth(1);
                    endDateNewTime.setDateTime(getLastDay(endDateNewTime.getYear(), endDateNewTime.getMonth()));
                    String startNew = startDateTime.toString("YYYY-MM-DD");
                    String endNew = endDateTime.toString("YYYY-MM-DD");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    res=doPostPayload(url,startNew,endNew,reportMenv);
                    JSONObject jsonWin = JSONObject.parseObject(res);
                    JSONObject reportDataWin=jsonWin.getJSONObject("result").getJSONObject("reportData");
                    System.out.println(startNew + "==" + endNew + "<<==win 結果:"+reportDataWin.toJSONString());
                    sb.append(startNew).append("=").append(endNew).append("WIN结果：").append(reportDataWin.toJSONString()).append("\r\n");
                }
                System.out.println(start + "==" + end + " 結果:"+reportData.toJSONString());
            }else {
                sb.append(start).append("=").append(end).append("结果：").append(res).append("\r\n");
            }

            if (end.equals("2020-01-31")) {
                writeFile("D://user//me.txt",sb.toString());
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
        c.set(Calendar.MONTH, month - 1);
        // 获取当前时间下，该月的最大日期的数字
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 将获取的最大日期数设置为Calendar实例的日期数
        c.set(Calendar.DAY_OF_MONTH, lastDay);

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
    public static void main(String[] args) {
        deal();
    }

}
