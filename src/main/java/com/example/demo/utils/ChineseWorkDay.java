package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by laikui on 2018/12/14.
 */
public class ChineseWorkDay {
    public static final long hour24 = 24*60*60*1000;
    private  String calendar;
    private  Date date;
    public ChineseWorkDay(Date date1){
        this.date = new Date(date1.getTime());
        System.out.printf(String.valueOf(date1==date));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.calendar= sdf.format(date);
    }
    // 法律规定的放假日期
    private static List<String> lawHolidays = new ArrayList<String>(Arrays.asList(
            "2018-12-31", "2019-01-01", "2019-02-04", "2019-02-05",
            "2019-02-06", "2019-02-07", "2019-02-08", "2019-04-05","2019-05-01", "2019-06-07", "2019-09-13", "2019-10-01","2019-10-02", "2019-10-03", "2019-10-04", "2019-10-07",
            "2020-01-01","2020-01-24","2020-01-27","2020-01-28","2020-01-29","2020-01-30","2020-01-31","2020-04-06","2020-05-01","2020-05-04","2020-05-05","2020-06-25","2020-06-26","2020-10-01","2020-10-02","2020-10-05","2020-10-06","2020-10-07","2020-10-08",
            "2021-01-01","2021-02-11","2021-02-12","2021-02-15","2021-02-16","2021-02-17","2021-04-05","2021-05-03","2021-05-04","2021-05-05","2021-06-14","2021-09-20","2021-09-21","2021-10-01","2021-10-04","2021-10-05","2021-10-06","2021-10-07",
            "2022-01-03","2022-01-31","2022-02-01","2022-02-02","2022-02-03","2022-02-04","2022-04-04","2022-04-05","2022-05-02","2022-05-03","2022-05-04","2022-06-03","2022-09-12","2022-10-03","2022-10-04","2022-10-05","2022-10-06","2022-10-07",
            "2023-01-02","2023-01-23","2023-01-24","2023-01-25","2023-01-26","2023-01-27","2023-04-04","2023-04-05","2023-05-02","2022-05-03","2022-05-04","2022-06-03","2022-09-12","2022-10-03","2022-10-04","2022-10-05","2022-10-06","2022-10-07")
    );
    // 由于放假需要额外工作的周末
    private List<String> extraWorkdays = new ArrayList<String>(Arrays.asList(
            "2018-01-01"));

    public Date preWorkDay(){
        try {
            date.setTime(date.getTime()-hour24);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar= sdf.format(date);
            while (isHoliday()){
                date.setTime(date.getTime()-hour24);
                calendar= sdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("上一个交易日:"+MyUtils.getDayFormat(date));;
        return date;
    }
    public Date preDaysWorkDay(int days,Date now){
        for(int i=0;i<days;i++){
            now=preWorkDay();
        }
        return now;
    }
    public Date nextWorkDay(){
        try {
            date.setTime(date.getTime()+hour24);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar= sdf.format(date);
            while (isHoliday()){
                date.setTime(date.getTime()+hour24);
                calendar= sdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("下一个交易日:"+MyUtils.getDayFormat(date));;
        return date;
    }
    public Date nextDaysWorkDay(int days,Date now){
        for(int i=0;i<days;i++){
            now=nextWorkDay();
        }
        return now;
    }
    /**
     * 判断是否是法定假日
     *
     * @param
     * @return
     * @throws Exception
     */
    public boolean isLawHoliday()  {

        if (lawHolidays.contains(calendar)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是周末
     *
     * @param
     * @return
     * @throws
     */
    public boolean isWeekends() {

        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        if (ca.get(Calendar.DAY_OF_WEEK) == 1
                || ca.get(Calendar.DAY_OF_WEEK) == 7) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是需要额外补班的周末
     *
     * @param
     * @return
     * @throws Exception
     */
    public boolean isExtraWorkday() {
        if (extraWorkdays.contains(calendar)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是休息日（包含法定节假日和不需要补班的周末）
     *
     * @param
     * @return
     * @throws Exception
     */
    public boolean isHoliday() {

        // 首先法定节假日必定是休息日
        if (this.isLawHoliday()) {
            return true;
        }
        // 排除法定节假日外的非周末必定是工作日
        if (!this.isWeekends()) {
            return false;
        }
        // 所有周末中只有非补班的才是休息日
        if (this.isExtraWorkday()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是工作日
     *
     * @param
     * @return
     * @throws Exception
     */
    public boolean isWorkday() throws Exception {

        return !(this.isHoliday());
    }

    public int getTotalDays() {
        return new GregorianCalendar().isLeapYear(Calendar.YEAR) ? 366 : 365;
    }

    public int getTotalLawHolidays() {
        return lawHolidays.size();
    }

    public int getTotalExtraWorkdays() {
        return extraWorkdays.size();
    }

    /**
     * 获取一年中所有周末的天数
     * @return
     */
    public int getTotalWeekends() {
        List<String> saturdays = new ArrayList<String>();
        List<String> sundays = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int nextYear = 1 + calendar.get(Calendar.YEAR);
        Calendar cstart = Calendar.getInstance();
        Calendar cend = Calendar.getInstance();
        cstart.set(currentYear, 0, 1);// 今年的元旦
        cend.set(nextYear, 0, 1);// 明年的元旦
        return this.getTotalSaturdays(saturdays, calendar, cstart, cend,
                currentYear)
                + this.getTotalSundays(sundays, calendar, cstart, cend,
                currentYear);
    }

    private int getTotalSaturdays(List<String> saturdays, Calendar calendar,
                                  Calendar cstart, Calendar cend, int currentYear) {
        // 将日期设置到上个周六
        calendar.add(Calendar.DAY_OF_MONTH, -calendar.get(Calendar.DAY_OF_WEEK));
        // 从上周六往这一年的元旦开始遍历，定位到去年最后一个周六
        while (calendar.get(Calendar.YEAR) == currentYear) {
            calendar.add(Calendar.DAY_OF_YEAR, -7);
        }
        // 将日期定位到今年第一个周六
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        // 从本年第一个周六往下一年的元旦开始遍历
        for (; calendar.before(cend); calendar.add(Calendar.DAY_OF_YEAR, 7)) {
            saturdays.add(calendar.get(Calendar.YEAR) + "-"
                    + calendar.get(Calendar.MONTH) + "-"
                    + calendar.get(Calendar.DATE));
        }
        return saturdays.size();
    }

    private int getTotalSundays(List<String> sundays, Calendar calendar,
                                Calendar cstart, Calendar cend, int currentYear) {
        // 将日期设置到上个周日
        calendar.add(Calendar.DAY_OF_MONTH,
                -calendar.get(Calendar.DAY_OF_WEEK) + 1);
        // 从上周日往这一年的元旦开始遍历，定位到去年最后一个周日
        while (calendar.get(Calendar.YEAR) == currentYear) {
            calendar.add(Calendar.DAY_OF_YEAR, -7);
        }
        // 将日期定位到今年第一个周日
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        // 从本年第一个周日往下一年的元旦开始遍历
        for (; calendar.before(cend); calendar.add(Calendar.DAY_OF_YEAR, 7)) {
            sundays.add(calendar.get(Calendar.YEAR) + "-"
                    + calendar.get(Calendar.MONTH) + "-"
                    + calendar.get(Calendar.DATE));
        }
        return sundays.size();
    }

    public int getTotalHolidays(){
        //先获取不需要补班的周末天数
        int noWorkWeekends = this.getTotalWeekends() - this.getTotalExtraWorkdays();
        return noWorkWeekends + this.getTotalLawHolidays();
    }

    public static void main(String[] args) throws Exception {

        ChineseWorkDay cc = new ChineseWorkDay(new Date());
        System.out.println("是否是法定节假日：" + cc.isLawHoliday());
        System.out.println("是否是周末：" + cc.isWeekends());
        System.out.println("是否是需要额外补班的周末：" + cc.isExtraWorkday());
        System.out.println("是否是休息日：" + cc.isHoliday());
        System.out.println("是否是工作日：" + cc.isWorkday());
        System.out.println("今年总共有" + cc.getTotalDays() + "天");
        System.out.println("今年总共有" + cc.getTotalLawHolidays() + "天法定节假日");
        System.out.println("今年总共有" + cc.getTotalExtraWorkdays() + "天需要补班的周末");
        System.out.println("今年总共有" + cc.getTotalWeekends() + "天周末");
        System.out.println("今年总共有" + cc.getTotalHolidays() + "天休息日");
    }

}
