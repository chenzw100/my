package com.example.demo.utils;

import org.apache.commons.lang.time.DateFormatUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenzw on 2018/10/16.
 */
public class MyUtils {
    public static final long eight_hour = 0*60*60*1000;
    public static Date getCurrentDate(){
        Date date = new Date();
        date.setTime(date.getTime()+eight_hour);
        return date;

    }
    public static String getWeek(Date date){
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        String currSun = dateFm.format(date);
        return currSun;
    }
    public static Date getFormatDate(String format)  {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            return simpleDateFormat.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
    public static Date getYesterdayDate(){
        return MyChineseWorkDay.preWorkDay();
    }
    public static Date getTomorrowDate(){
        return MyChineseWorkDay.nextWorkDay();
    }
    public static String getDayFormat(){
        return DateFormatUtils.format(getCurrentDate(), "yyyyMMdd");
    }
    public static String getYesterdayDayFormat(){
        return DateFormatUtils.format(MyChineseWorkDay.preWorkDay(), "yyyyMMdd");
    }
    public static String getTomorrowDayFormat(){
        return DateFormatUtils.format(MyChineseWorkDay.nextWorkDay(), "yyyyMMdd");
    }
    public static String getDayHHFormat(Date date){
        return DateFormatUtils.format(date, "MMdd HH");
    }
    public static String getDayFormat(Date date){
        return DateFormatUtils.format(date, "yyyyMMdd");
    }
    public static String getPreFiveDayFormat(){
        return getDayFormat(MyChineseWorkDay.preDaysWorkDay(4, MyUtils.getCurrentDate()));
    }
    public static String getPreFiveDayFormat(Date date){
        return getDayFormat(MyChineseWorkDay.preDaysWorkDay(4, date));
    }
    public static String getPre2WeekDayFormat(){
        return getDayFormat(MyChineseWorkDay.preDaysWorkDay(8, MyUtils.getCurrentDate()));
    }
    public static String getPreMonthDayFormat(){
        return getDayFormat(MyChineseWorkDay.preDaysWorkDay(16, MyUtils.getCurrentDate()));
    }
    public static int getCentBySinaPriceStr(String sinaPriceStr){
        return new BigDecimal(Double.parseDouble(sinaPriceStr)).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue();
    }
    public static int getCentByYuanStr(String sinaPriceStr){
        return new BigDecimal(Double.parseDouble(sinaPriceStr)).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue();
    }
    public static int getCentBy4Point(String sinaPriceStr){
        return new BigDecimal(Double.parseDouble(sinaPriceStr)).multiply(new BigDecimal(10000)).setScale(2, RoundingMode.HALF_UP).intValue();
    }
    public static String getYuanByCent(int cent){
        return new BigDecimal(cent).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).toString();
        /*Double faultRate = Double.parseDouble(sinaPriceStr);
        BigDecimal a = BigDecimal.valueOf(faultRate);
        BigDecimal b =a.setScale(2, RoundingMode.HALF_UP);//保留两位小数；
        System.out.println("结果是"+b);
        //下面将结果转化成百分比
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        System.out.println(percent.format(b.doubleValue()));
        */

    }

    public static BigDecimal getIncreaseRate(int increase,int base){
        if(increase==0||base==0){
            return new BigDecimal(0);
        }
        return new BigDecimal(increase-base).multiply(new BigDecimal(100)).divide(new BigDecimal(base), 2, RoundingMode.HALF_UP);

    }
    public static BigDecimal getIncreaseRateCent(int increase,int base){
        if(increase==0||base==0){
            return new BigDecimal(0);
        }
        return new BigDecimal(increase-base).multiply(new BigDecimal(10000)).divide(new BigDecimal(base), 0, RoundingMode.HALF_UP);
    }
    public static BigDecimal getAverageRateCent(int increase,int base){
        if(increase==0||base==0){
            return new BigDecimal(0);
        }
        return new BigDecimal(increase).divide(new BigDecimal(base), 2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        //System.out.println(MyUtils.getIncreaseRate(302,309).multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP));
        //System.out.println(MyUtils.getIncreaseRateCent(302, 309).intValue());
        //System.out.println(MyUtils.getAverageRateCent(-8000, 10).toString());
        int cp =MyUtils.getCentBy4Point("0.0996");
        String sy ="300265";

        System.out.println("cp:"+sy.indexOf("2300"));
    }
}
