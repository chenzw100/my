package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockPlateStaRepository;
import com.example.demo.dao.StockTemperatureRepository;
import com.example.demo.domain.table.*;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockUpService;
import com.example.demo.service.qt.QtService;
import com.example.demo.service.xgb.XgbService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/up")
public class StockUpController {
    private static String UP_PRE_END="";
    private static String HOT_PRE_END="";
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockUpService stockUpService;
    @Autowired
    StockPlateStaRepository stockPlateStaRepository;
    @Autowired
    StockTemperatureRepository stockTemperatureRepository;
    @Autowired
    QtService qtService;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    XgbService xgbService;
    private String getQueryDate(String end) {
        String queryEnd = end;
        if (end==null) {
            if (MyChineseWorkDay.isWorkday()) {
                queryEnd = MyUtils.getDayFormat();
            } else {
                queryEnd = MyUtils.getYesterdayDayFormat();
            }
        } else if (end.equals("2")) {
            Date endDate = MyUtils.getFormatDate(UP_PRE_END);
            queryEnd = MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        } else if (end.equals("3")) {
            Date endDate = MyUtils.getFormatDate(UP_PRE_END);
            queryEnd = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        UP_PRE_END = queryEnd;
        return queryEnd;
    }

    @RequestMapping("/list.html")
    public String index(ModelMap modelMap){
        modelMap.put("title","连板梯队");
        return "up/list";
    }
    @RequestMapping("/list.action")
    @ResponseBody
    public String list(Integer page, Integer rows, StockLimitUp obj){
        obj.setDayFormat(getQueryDate(obj.getDayFormat()));
        Map map = new HashMap<>();
        List<StockLimitUp> list =stockUpService.findByDayFormat(obj.getDayFormat());
        StockLimitUp up= new StockLimitUp();
        Date endDate = MyUtils.getFormatDate(UP_PRE_END);
        String day = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        up.setDayFormat(day);
        up.setCode(day);
        up.setName("观察日");
        up.setPlateName("观察日");
        up.setTodayOpenRate(day);
        String dayt = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        up.setTomorrowOpenEarnings(dayt);
        up.setTodayCloseRate("是否跌停");
        up.setTomorrowOpen("是否跌停");
        list.add(0,up);
        map.put("total",list.size());
        map.put("rows",list);
        return JSON.toJSONString(map);
    }
    @RequestMapping("/limit.action")
    @ResponseBody
    public String limitUp() {
        stockUpService.doLimitUp();
        return "success";
    }
    @RequestMapping("/opt.action")
    @ResponseBody
    public String opt() {
        stockUpService.doOpt();
        return "success";
    }

    @RequestMapping("/hot/{end}")
    @ResponseBody
    String hot(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(MyChineseWorkDay.isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(HOT_PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(HOT_PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        HOT_PRE_END=queryEnd;

        StringBuilder sb =new StringBuilder();
        String desc ="【主流板块】注意[1,4,8,10月披露+月底提金，还有一些莫名的反常！！！]查询日期20191015以后的数据<br>=====>当前查询日期";
        sb.append(desc).append(queryEnd);

        ChineseWorkDay tenDay=new ChineseWorkDay(endDate);
        String startTen =MyUtils.getDayFormat(tenDay.preDaysWorkDay(9,endDate));
        ChineseWorkDay fifteenDay=new ChineseWorkDay(endDate);
        String startFifteen =MyUtils.getDayFormat(fifteenDay.preDaysWorkDay(14,endDate));
        ChineseWorkDay twentyDay=new ChineseWorkDay(endDate);
        String startTwenty =MyUtils.getDayFormat(twentyDay.preDaysWorkDay(19,endDate));
        System.out.println("startTen = [" + startTen + "]"+"startFifteen = [" + startFifteen + "]"+"startTwenty = [" + startTwenty + "]");
        List<StockOpt> ten =stockInfoService.optCode(startTen,queryEnd);
        List<StockInfo> ten4 =stockInfoService.optCodeNew4(startTen,queryEnd);
        List<StockOpt> fifteen =stockInfoService.hotCode(startFifteen,queryEnd);
        //List<StockOpt> twenty =stockInfoService.hotCode(startTwenty,queryEnd);
        ChineseWorkDay yesterdayDate=new ChineseWorkDay(endDate);
        String yesterday =MyUtils.getDayFormat(yesterdayDate.preWorkDay());
        List<StockPlateSta> stockPlates =stockPlateStaRepository.findByDayFormatAndPlateType(yesterday, 1);

        for(StockPlateSta stockPlateSta:stockPlates){
            sb.append("<br>").append(yesterday).append(" 板块【").append(stockPlateSta.getPlateName()).append("】").append(stockPlateSta.getDescription());
        }
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start, queryEnd);

        return sb.toString()+"<br>===>【fifteen】:<br>"+fifteen+"<br>===>【ten】:<br>"+ten+"<br>===>【ten4】:<br>"+ten4+"<br>===>【复盘】:<br>"+temperaturesClose;
    }


    @RequestMapping("/add/{day}/{type}/{hot}/{code}")
    @ResponseBody
    public String add(@PathVariable("day")String day,@PathVariable("type")Integer type,@PathVariable("hot")Integer hot,@PathVariable("code")String code) {
        if ("1".equals(code)) {
            return "success 持仓-1 买入-2 顽主-3 同花顺-4 热7-5  DAY/TYPE/HOT/CODE";
        }
        if (code.indexOf("6") == 0) {
            code = "sh" + code;
        } else if (code.indexOf("0") == 0){
            code = "sz" + code;
        }
        StockInfo myStock = qtService.getInfo(code);
        if (myStock == null) {
            return "fail";
        }
        if(type>1){
            myStock.setStockType(type);
        }
        StockInfo fiveTgbStockTemp =stockInfoService.findStockKplByCodeAndYesterdayFormat(myStock.getCode());
        if(fiveTgbStockTemp!=null){
            myStock.setShowCount(fiveTgbStockTemp.getShowCount() + 1);
        }else {
            myStock.setShowCount(1);
        }
        String dayFormat = MyUtils.getDayFormat(MyUtils.getYesterdayDate());
        if(!day.equals("1")){
            dayFormat=day;
            Date yesterdayDate = MyUtils.getFormatDate(dayFormat);
            ChineseWorkDay nowWorkDay = new ChineseWorkDay(yesterdayDate);
            Date now = nowWorkDay.nextWorkDay();
            myStock.setDayFormat(MyUtils.getDayFormat(now));
        }
        List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myStock.getCode(),dayFormat);
        if(xgbStocks!=null && xgbStocks.size()>0){
            StockLimitUp xgbStock =xgbStocks.get(0);
            myStock.setPlateName(xgbStock.getPlateName());
            myStock.setOneFlag(xgbStock.getOpenCount());
            myStock.setContinuous(xgbStock.getContinueBoardCount());
            myStock.setLimitUp(1);
        }else {
            xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(myStock.getCode());
            if(xgbStocks!=null && xgbStocks.size()>0){
                myStock.setPlateName(xgbStocks.get(0).getPlateName());
            }else {
                myStock.setPlateName("");
            }
            myStock.setOneFlag(1);
            myStock.setContinuous(0);
            myStock.setLimitUp(0);
        }
        myStock.setHotSeven(hot);
        myStock.setHotValue(hot);
        stockInfoService.save(myStock);
        return myStock.toString();
    }

    @RequestMapping("/doUp")
    @ResponseBody
    public String doUp() {
        xgbService.limitUp();
        return "success";
    }

    @RequestMapping("/optlist.html")
    public String optlist(ModelMap modelMap){
        modelMap.put("title","涨停操作标的");
        return "up/optlist";
    }
    @RequestMapping("/optlist.action")
    @ResponseBody
    public String optlist(StockInfo obj){

        String queryEnd = getQueryDate(obj.getDayFormat());
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        ChineseWorkDay tenDay=new ChineseWorkDay(endDate);
        String startTen =MyUtils.getDayFormat(tenDay.preDaysWorkDay(9,endDate));
        List<StockInfo> ten4 =stockInfoService.optCodeNew4(startTen,queryEnd);
        List<StockInfo> focus = stockInfoService.findByDayFormatAndStockTypeOrderByIdAsc(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
        List<StockInfo> focus2 = stockInfoService.findByDayFormatAndStockTypeOrderByIdAsc(queryEnd, NumberEnum.StockType.STOCK_BUY.getCode());
        ten4.addAll(focus);
        ten4.addAll(focus2);
        StockInfo up= new StockInfo();
        up.setStockType(10);
        String day = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        up.setDayFormat(queryEnd);
        up.setCode(queryEnd);
        up.setName("观察日");
        up.setPlateName("观察日");
        up.setTodayOpenRate(queryEnd);
        up.setTodayClose("是否跌停");
        up.setTomorrowOpen("是否跌停");
        up.setTomorrowOpenEarnings(day);
        ten4.add(0,up);
        Map map = new HashMap<>();
        List<StockInfo> ups = new ArrayList<>();
        for(StockInfo s:ten4){
            if(s.getContinuous()>0){
                ups.add(s);
            }
        }
        map.put("total",ups.size());
        map.put("rows",ups);
        return JSON.toJSONString(map);
    }

    @RequestMapping("/slist.html")
    public String slist(ModelMap modelMap){
        modelMap.put("title","简洁涨停操作标的");
        return "up/slist";
    }
    @RequestMapping("/slist.action")
    @ResponseBody
    public String slist(StockInfo obj){

        String queryEnd = getQueryDate(obj.getDayFormat());
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        ChineseWorkDay tenDay=new ChineseWorkDay(endDate);
        String startTen =MyUtils.getDayFormat(tenDay.preDaysWorkDay(9,endDate));
        List<StockInfo> focus = stockInfoService.findByDayFormatAndStockTypeOrderByIdAsc(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
        List<StockInfo> focus2 = stockInfoService.findByDayFormatAndStockTypeOrderByIdAsc(queryEnd, NumberEnum.StockType.STOCK_BUY.getCode());
        List<StockInfo> ten4 =stockInfoService.optCodeNew4(startTen,queryEnd);
        focus.addAll(focus2);
        focus.addAll(ten4);
        StockInfo up= new StockInfo();
        up.setStockType(10);
        String day = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        up.setDayFormat(queryEnd);
        up.setCode(queryEnd);
        up.setName("观察日");
        up.setPlateName("观察日");
        up.setTodayOpenRate(queryEnd);
        up.setTodayClose("是否跌停");
        up.setTomorrowOpen("是否跌停");
        up.setTomorrowOpenEarnings(day);
        Map map = new HashMap<>();
        List<StockInfo> ups = new ArrayList<>();
        Map temp = new HashMap();
        for(StockInfo s:focus){
            if(s.getContinuous()>0){
                if(temp.get(s.getCode())==null){
                    temp.put(s.getCode(),s.getName());
                    ups.add(s);
                }
            }
        }
        ups.add(0,up);
        map.put("total",ups.size());
        map.put("rows",ups);
        return JSON.toJSONString(map);
    }

}
