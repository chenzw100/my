package com.example.demo.utils;

import org.apache.commons.lang.StringUtils;
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

    public static Date getFormat2Date(String format)  {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

    public static String get2DayFormat(String dayFormat){
       return MyUtils.getDayFormat(MyUtils.getFormat2Date(dayFormat));
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
    public static String getPre2DayFormat(){
        return getDayFormat(MyChineseWorkDay.preDaysWorkDay(2, new Date()));
    }
    public static String getDayHHFormat(Date date){
        return DateFormatUtils.format(date, "MMdd HH");
    }
    public static String getDayFormat(Date date){
        return DateFormatUtils.format(date, "yyyyMMdd");
    }
    public static String getDayFormat2(Date date){
        return DateFormatUtils.format(date, "yyyy-MM-dd");
    }
    public static String getPreFiveDayFormat(){
        return getDayFormat(MyChineseWorkDay.preDaysWorkDay(4, MyUtils.getCurrentDate()));
    }
    public static String getPreThreeDayFormat(){
        return getDayFormat(MyChineseWorkDay.preDaysWorkDay(2, MyUtils.getCurrentDate()));
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
    public static String getPreTwoMonthDayFormat(){
        return getDayFormat2(MyChineseWorkDay.preDaysWorkDay(30, MyUtils.getCurrentDate()));
    }
    public static int getCentBySinaPriceStr(String sinaPriceStr){
        return new BigDecimal(Double.parseDouble(sinaPriceStr)).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue();
    }
    public static int getCentByYuanStr(String sinaPriceStr){
        if(StringUtils.isBlank(sinaPriceStr)){
            return 0;
        }
        return new BigDecimal(Double.parseDouble(sinaPriceStr)).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue();
    }
    public static int getCentBy4Point(String sinaPriceStr){
        return new BigDecimal(Double.parseDouble(sinaPriceStr)).multiply(new BigDecimal(10000)).setScale(2, RoundingMode.HALF_UP).intValue();
    }
    public static String getYuanByCent(Integer cent){
        if(cent==null){
            return "";
        }
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
    public static Integer getYuanByFen(int cent){
        return new BigDecimal(cent).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP).intValue();
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
    public static int getYuanPriceStr(String sinaPriceStr){
        if(StringUtils.isBlank(sinaPriceStr)){
            return 0;
        }
        return new BigDecimal(Double.parseDouble(sinaPriceStr)).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    public static BigDecimal getIncreaseRate(Integer increase,Integer base){
        if(increase==null || increase==0||base==0 || base==null){
            return new BigDecimal(0);
        }
        return new BigDecimal(increase-base).multiply(new BigDecimal(100)).divide(new BigDecimal(base), 2, RoundingMode.HALF_UP);

    }
    public static BigDecimal getIncreaseRateCent(Integer increase,Integer base){
        if(increase==null||base==null){
            return new BigDecimal(0);
        }
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
       /* int cp =MyUtils.getCentBy4Point("0.0996");
        String sy ="300265";*/
        System.out.println( MyUtils.getDayFormat(MyUtils.getFormat2Date("2021-07-08")));
    }
    public static String getIncreaseRateCentForTwoPiont(Integer increase,Integer base){
        BigDecimal result = null;
        if(increase==null||base==null){
            result =  new BigDecimal(0);
            return getYuanByCent(result.intValue());
        }
        if(increase==0||base==0){
            result =  new BigDecimal(0);
            return getYuanByCent(result.intValue());
        }
        result = new BigDecimal(increase-base).multiply(new BigDecimal(10000)).divide(new BigDecimal(base), 0, RoundingMode.HALF_UP);
        return getYuanByCent(result.intValue());
    }
}
