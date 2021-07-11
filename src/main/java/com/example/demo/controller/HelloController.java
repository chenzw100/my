package com.example.demo.controller;

import com.example.demo.dao.*;
import com.example.demo.domain.MyTotalStock;
import com.example.demo.domain.SinaTinyInfoStock;
import com.example.demo.domain.StaStockPlate;
import com.example.demo.domain.StaStockPlateImpl;
import com.example.demo.domain.table.*;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockPlateService;
import com.example.demo.service.StockYybRecordService;
import com.example.demo.service.dfcf.DfcfRecordService;
import com.example.demo.service.dfcf.DfcfService;
import com.example.demo.service.sina.SinaService;
import com.example.demo.service.tgb.TgbDealService;
import com.example.demo.service.xgb.XgbCurrentService;
import com.example.demo.service.xgb.XgbService;
import com.example.demo.task.PanService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class HelloController {
    private static String PRE_END="";
    @Autowired
    SinaService sinaService;
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockPlateService stockPlateService;
    @Autowired
    StockTemperatureRepository stockTemperatureRepository;
    @Autowired
    PanService panService;
    @Autowired
    XgbService xgbService;
    @Autowired
    XgbCurrentService xgbCurrentService;

    @Autowired
    StockInfoRepository stockInfoRepository;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    StockTruthRepository stockTruthRepository;
    @Autowired
    StockPlateStaRepository stockPlateStaRepository;
    @Autowired
    DfcfService dfcfService;
    @Autowired
    StockMoodRepository stockMoodRepository;
    @Autowired
    StockYybRecordService stockYybRecordService;
    @Autowired
    DfcfRecordService dfcfRecordService;
    @Autowired
    TgbDealService tgbDealService;
    @Autowired
    private RestTemplate restTemplate;
    private static String current_Continue="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08161&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
    private static String c_cUrl ="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0816&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    @RequestMapping("/tgb3")
    public String tgb3() {
        try {
            tgbDealService.dayDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "tgb3 deal success";
    }
    @RequestMapping("/tes3/{code}")
    public String tes3(@PathVariable("code")Integer code) {
        dfcfRecordService.yyb(code);
        return "deal success";
    }
    @RequestMapping("/tes2")
    public String tes2() {
        stockYybRecordService.deal();
        return "deal success";
    }
    @RequestMapping("/test/{code}")
    public String test(@PathVariable("code")String code) {
        StockLimitUp stockLimitUp = stockLimitUpRepository.findTop1ByCodeAndPlateNameIsNotNullOrderByIdDesc(code);
        return "yyb success";
    }
    @RequestMapping("/yybTest/{yybId}")
    public String yyb(@PathVariable("yybId")Integer yybId) {
        dfcfService.yyb(yybId);
        return "yyb success";
    }
    @RequestMapping("/yybTest1/{yybId}")
    public String yyb1(@PathVariable("yybId")Integer yybId) {
        dfcfService.yybJob();
        return "yyb success";
    }
    @RequestMapping("/mood/{info}/{masterLine}")
    public String mood(@PathVariable("info")Integer info,@PathVariable("masterLine")String masterLine) {
        if (info ==0) {
            return "success";
        }
        StockMood stockTruth = new StockMood();
        stockTruth.setMoodType(info);
        stockTruth.setMasterLine(masterLine);
        stockMoodRepository.save(stockTruth);
        return "add success";
    }
    @RequestMapping("/mood/{dayFormat}/{info}/{masterLine}")
    public String mood(@PathVariable("info")Integer info,@PathVariable("dayFormat")String dayFormat,@PathVariable("masterLine")String masterLine) {
        if (info ==0) {
            return "success"+dayFormat;
        }
        StockMood stockTruth = new StockMood();
        stockTruth.setMoodType(info);
        stockTruth.setDayFormat(dayFormat);
        stockTruth.setMasterLine(masterLine);
        stockMoodRepository.save(stockTruth);
        return "add success";
    }

    @RequestMapping("/high/{code}")
    @ResponseBody
    String high(@PathVariable("code")String code){
        if ("0".equals(code)) {
            return "success";
        }
        if (code.indexOf("6") == 0) {
            code = "sh" + code;
        } else {
            code = "sz" + code;
        }
        List<StockLimitUp> stockLimitUps = stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(code);
        List<StockLimitUp> stockUps = new ArrayList<>();
        for(StockLimitUp stockLimitUp :stockLimitUps){
            stockUps.add(stockLimitUp);
            List<StockPlateSta> stockPlates =stockPlateStaRepository.findByDayFormatAndPlateType(stockLimitUp.getDayFormat(), 1);
            for(StockPlateSta stockPlateSta:stockPlates){
                StockLimitUp sp = new StockLimitUp();
                sp.setCode("-板块-");
                sp.setYesterdayClosePrice(100);
                sp.setContinueBoardCount(1);
                sp.setPlateName(stockPlateSta.getDescription());
                sp.setName(stockPlateSta.getPlateName());
                sp.setDayFormat(stockPlateSta.getDayFormat());
                stockUps.add(sp);
            }
        }
        return stockUps.toString();
    }
    @RequestMapping("/ideals2/{end}")
    String ideals2(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        PRE_END=queryEnd;
        Date yesterdayDate =  MyUtils.getFormatDate(PRE_END);
        String queryYesterday =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(yesterdayDate));
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        System.out.println("=============queryEnd = [" + queryEnd + "]"+"queryYesterday"+queryYesterday);
        List<StockInfo> fives = stockInfoService.findStockDayFivesHotSevenDesc(queryEnd);
        List<StockInfo> yesterdayOpens = stockInfoService.findStockFivesTomorrowOpenYield(queryYesterday);
        List<StockInfo> yesterdayOpensResult = new ArrayList<>();
        for(StockInfo stockInfo :yesterdayOpens){
            yesterdayOpensResult.add(stockInfo);
            StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
            if(stockInfoOpen!=null){
                yesterdayOpensResult.add(stockInfoOpen);
            }
        }
        List<StockInfo> yesterdayCloses = stockInfoService.findStockFivesTomorrowCloseYield(queryYesterday);
        List<StockInfo> yesterdayClosesResult = new ArrayList<>();
        for(StockInfo stockInfo :yesterdayCloses){
            yesterdayClosesResult.add(stockInfo);
            StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
            if(stockInfoOpen!=null){
                yesterdayClosesResult.add(stockInfoOpen);
            }
        }
        List<StockTruth> stockTruths = stockTruthRepository.findByDayFormat(queryEnd);
        StockTruth stockTruth = null;
        if(stockTruths==null){
            stockTruth =new StockTruth();
            stockTruths.add(stockTruth);
        }
        String desc ="信念[人气反抽模式，条件行情转暖，且大跌过的人气票] 提供20191015以后的数据=====>当前查询日期";
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start, queryEnd);

        List<StockTemperature> dayTemperatures=stockTemperatureRepository.findByDayFormat(queryEnd);
        endDate =  MyUtils.getFormatDate(queryEnd);
        String preDate =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        List<StockTemperature> preTemperatures=stockTemperatureRepository.findByDayFormat(preDate);
        List<StockTemperature> temperatures = new ArrayList<>();
        int i=0;
        int size = preTemperatures.size();
        for(StockTemperature st:dayTemperatures){
            if(i<size){
                temperatures.add(preTemperatures.get(i));
            }
            temperatures.add(st);
            i++;
        }
        return desc+queryEnd+"===>【复盘情况】:<br>"+temperaturesClose+queryEnd
                +"===>【早盘修复情况】:<br>"+yesterdayOpensResult+queryEnd+
                "===>【数据情况】:<br>"+fives+queryEnd+"" +
                "===>【尾盘修复情况】:<br>"+yesterdayClosesResult+queryEnd+
                "===>【盘面实时运行情况】:<br>"+temperatures;
    }
    @RequestMapping("/ideals1/{end}")
    String ideals1(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        PRE_END=queryEnd;
        Date yesterdayDate =  MyUtils.getFormatDate(PRE_END);
        String queryYesterday =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(yesterdayDate));
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        List<StockInfo> dayOpens = stockInfoService.findStockDaysByDayFormatOpen(queryEnd,NumberEnum.StockType.STOCK_DAY.getCode());
        List<StockInfo> fiveOpens = stockInfoService.findStockDaysByDayFormatOpen(queryEnd, NumberEnum.StockType.STOCK_DAY_FIVE.getCode());
        System.out.println("=============queryEnd = [" + queryEnd + "]"+"queryYesterday"+queryYesterday);
        List<StockInfo> yesterdayOpensFive = stockInfoService.findStockFivesTomorrowOpenYield(queryYesterday);
        List<StockInfo> yesterdayOpensResultFive = new ArrayList<>();
        for(StockInfo stockInfo :yesterdayOpensFive){
            yesterdayOpensResultFive.add(stockInfo);
            StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
            if(stockInfoOpen!=null){
                yesterdayOpensResultFive.add(stockInfoOpen);
            }
        }
        List<StockInfo> yesterdayOpens = stockInfoService.findStockDaysByDayFormatTomorrowOpenYield(queryYesterday);
        List<StockInfo> yesterdayOpensResult = new ArrayList<>();
        for(StockInfo stockInfo :yesterdayOpens){
            yesterdayOpensResult.add(stockInfo);
            StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
            if(stockInfoOpen!=null){
                yesterdayOpensResult.add(stockInfoOpen);
            }
        }
        List<StockInfo> yesterdayCloses = stockInfoService.findStockFivesTomorrowCloseYield(queryYesterday);
        List<StockInfo> yesterdayClosesResult = new ArrayList<>();
        for(StockInfo stockInfo :yesterdayCloses){
            yesterdayClosesResult.add(stockInfo);
            StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
            if(stockInfoOpen!=null){
                yesterdayClosesResult.add(stockInfoOpen);
            }
        }
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockInfo> highCurrents = stockInfoService.fiveHeightSpace(start, queryEnd);
        List<StockInfo> kpls = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
        String desc ="信念[空间与新题材模式，趋势持股看看一下，条件双十逻辑，涨停倍增逻辑] 提供20191015以后的数据=====>当前查询日期";
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start, queryEnd);
        List<StockTemperature> dayTemperatures=stockTemperatureRepository.findByDayFormat(queryEnd);
        endDate =  MyUtils.getFormatDate(queryEnd);
        String preDate =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        List<StockTemperature> preTemperatures=stockTemperatureRepository.findByDayFormat(preDate);
        List<StockTemperature> temperatures = new ArrayList<>();
        List<StockTemperature> temperaturesFives = new ArrayList<>();
        int i=0;
        int size = preTemperatures.size();
        for(StockTemperature st:dayTemperatures){
            if(i<size){
                temperatures.add(preTemperatures.get(i));
                if(i<5){
                    temperaturesFives.add(preTemperatures.get(i));
                    temperaturesFives.add(st) ;
                }
            }

            temperatures.add(st);
            i++;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(desc).
                append(queryEnd).append("===>【复盘情况】:<br>").append(temperaturesClose).
                append(queryEnd).append("===>【空间板数据情况】:<br>").append(highCurrents).
                append(queryEnd).append("===>【灵魂股情况】:<br>").append(kpls).
                append(queryEnd).append("===>【应变决策情况】:<br>").append(temperaturesFives).
                append(queryEnd).append("===>【早盘当日冲击情况】:<br>").append(yesterdayOpensResult).
                append(queryEnd).append("===>【早盘当日最强竞价】:<br>").append(dayOpens).
                append(queryEnd).append("===>【早盘五日最强竞价】:<br>").append(fiveOpens).
                append(queryEnd).append("===>【早盘五日冲击情况】:<br>").append(yesterdayOpensResultFive).
                append(queryEnd).append("===>【尾盘当日冲击情况】:<br>").append(yesterdayClosesResult).
                append(queryEnd).append("===>【盘面实时运行情况】:<br>").append(temperatures);
        return sb.toString();
    }

    @RequestMapping("/deal")
    public String deal() {
        List<StockInfo> ss = stockInfoRepository.findAll();
        for(StockInfo s :ss){
            s.getTodayCloseYield();
            s.getTomorrowCloseYield();
            s.getTomorrowOpenYield();
            s.getFiveHighYield();
            s.getFiveLowYield();
            stockInfoRepository.save(s);
        }
        return "success:";
    }
    @RequestMapping("/con")
    public String con() {
        String ss = dfcfService.currentContinueVal();
        return "success:"+ss;
    }
    @RequestMapping("/plates")
    public String plates() {
       xgbService.staPlates();
        return "staPlates success";
    }
    @RequestMapping("/truth/{info}")
    public String truth(@PathVariable("info")String info) {
        if ("1".equals(info)) {
            return "success";
        }
        StockTruth stockTruth = new StockTruth();
        stockTruth.setTruthInfo(info);
        stockTruthRepository.save(stockTruth);
        return "add success";
    }
    @RequestMapping("/truth/{dayFormat}/{info}")
    public String truth2(@PathVariable("info")String info,@PathVariable("dayFormat")String dayFormat) {
        if ("1".equals(dayFormat)) {
            return "success"+dayFormat;
        }
        StockTruth stockTruth = new StockTruth();
        stockTruth.setTruthInfo(info);
        stockTruth.setDayFormat(dayFormat);
        stockTruthRepository.save(stockTruth);
        return "add success";
    }

    @RequestMapping("/xxx")
    public String xxx() {
        xgbCurrentService.closeNewAndNearly();
        return "pre success";
    }
    @RequestMapping("/pre")
    public String pre() {
        panService.preTgb();
        return "pre success";
    }
    @RequestMapping("/doFive")
    public String doFive() {
        String end = MyUtils.getDayFormat();
        String start =MyUtils.getPreFiveDayFormat();
        List<MyTotalStock> totalStocks =  stockInfoService.fiveDayInfo(start, end);

        while (totalStocks.size()>0){
            for(MyTotalStock myTotalStock : totalStocks){
                StockInfo stockInfo =stockInfoRepository.findByCodeAndDayFormatAndStockType(myTotalStock.getCode(),end,70);
                if(stockInfo!=null && StringUtils.isNotEmpty(stockInfo.getCode())){
                    stockInfo.setHotSort(myTotalStock.getTotalCount());
                    stockInfo.setHotSeven(myTotalStock.getHotSeven());
                    stockInfo.setHotValue(myTotalStock.getHotValue());
                    stockInfoRepository.save(stockInfo);
                }
            }
            Date endDate =  MyUtils.getFormatDate(end);
            end =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
            start =MyUtils.getPreFiveDayFormat(endDate);
            totalStocks =  stockInfoService.fiveDayInfo(start, end);
            System.out.println("==========================>end:"+end);
        }

        return "doFive success";
    }
    @RequestMapping("/add/{code}")
    public String add(@PathVariable("code")String code) {
        if ("1".equals(code)) {
            return "success";
        }
        if (code.indexOf("6") == 0) {
            code = "sh" + code;
        } else {
            code = "sz" + code;
        }
        SinaTinyInfoStock sinaStock = sinaService.getTiny(code);
        if (sinaStock == null) {
            return "fail";
        }
        StockInfo myStock = new StockInfo(code, sinaStock.getName(), NumberEnum.StockType.STOCK_KPL.getCode());
        myStock.setYesterdayClosePrice(sinaStock.getYesterdayPrice());
        myStock.setContinuous(1);
        myStock.setOpenCount(-1);
        myStock.setHotSort(-1);
        myStock.setOneFlag(-1);
        StockInfo fiveTgbStockTemp =stockInfoService.findStockKplByCodeAndYesterdayFormat(myStock.getCode());
        if(fiveTgbStockTemp!=null){
            myStock.setShowCount(fiveTgbStockTemp.getShowCount() + 1);
        }else {
            myStock.setShowCount(1);
        }
        List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myStock.getCode(),MyUtils.getDayFormat(MyUtils.getYesterdayDate()));
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
        stockInfoRepository.save(myStock);
        return myStock.toString();
    }
    @RequestMapping("/add2/{code}")
    public String add2(@PathVariable("code")String code) {
        if ("1".equals(code)) {
            return "success";
        }
        if (code.indexOf("6") == 0) {
            code = "sh" + code;
        } else {
            code = "sz" + code;
        }
        SinaTinyInfoStock sinaStock = sinaService.getTiny(code);
        if (sinaStock == null) {
            return "fail";
        }
        StockInfo myStock = new StockInfo(code, sinaStock.getName(), NumberEnum.StockType.STOCK_KPL.getCode());
        myStock.setDayFormat(MyUtils.getTomorrowDayFormat());
        myStock.setYesterdayClosePrice(sinaStock.getCurrentPrice());
        myStock.setContinuous(1);
        myStock.setOpenCount(-1);
        myStock.setHotSort(-1);
        myStock.setOneFlag(-1);
        StockInfo fiveTgbStockTemp =stockInfoService.findStockKplByCodeAndTodayFormat(myStock.getCode());
        if(fiveTgbStockTemp!=null){
            myStock.setShowCount(fiveTgbStockTemp.getShowCount() + 1);
        }else {
            myStock.setShowCount(1);
        }
        List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myStock.getCode(),MyUtils.getDayFormat());
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
        stockInfoRepository.save(myStock);
        return myStock.toString();
    }

    @RequestMapping("/p")
    public String plate(){
        xgbService.platesClose();
        return "close success";
    }
    @RequestMapping("/hello")
    public String hello(){
        panService.currentPan();
        return "hello";
    }
    @RequestMapping("/close")
    public String close(){
        panService.closePan();
        return "close success";
    }

    @RequestMapping("/chance/{end}")
    String chance(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;

        List<StockInfo> fives = stockInfoService.findStockDayFivesByDayFormatTomorrowOpenYield(queryEnd);
        List<StockInfo> top3 =stockInfoService.findByDayFormatOrderByFiveHighYieldDescTop3(queryEnd);

        List<StockTruth> stockTruths = stockTruthRepository.findByDayFormat(queryEnd);
        StockTruth stockTruth = null;
        if(stockTruths==null){
            stockTruth =new StockTruth();
            stockTruths.add(stockTruth);
        }
        String desc ="【主流板块】注意[1,4,8,10月披露+月底提金，还有一些莫名的反常！！！]查询日期20191015以后的数据=====>当前查询日期";
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start,queryEnd);
        List<StockInfo> days = stockInfoService.findStockDaysByDayFormatTomorrowOpenYield(queryEnd);
        return desc+queryEnd+"<br>心理历程<br>:"+stockTruths+"<br>===>【竞价情况】:<br>"+days+"<br>"+top3+"<br>"+fives+temperaturesClose;
    }
    @RequestMapping("/chance3/{end}")
    String chance3(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date yesterdayDate =  MyUtils.getFormatDate(queryEnd);
        String queryYesterday =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(yesterdayDate));
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;
        System.out.println("=============queryEnd = [" + queryEnd + "]"+"queryYesterday"+queryYesterday);
        List<StockInfo> fives = stockInfoService.findStockDayFivesHotSevenDesc(queryEnd);
        List<StockInfo> yesterdayOpens = stockInfoService.findStockFivesTomorrowOpenYield(queryYesterday);
        List<StockInfo> yesterdayCloses = stockInfoService.findStockFivesTomorrowCloseYield(queryYesterday);
        List<StockTruth> stockTruths = stockTruthRepository.findByDayFormat(queryEnd);
        StockTruth stockTruth = null;
        if(stockTruths==null){
            stockTruth =new StockTruth();
            stockTruths.add(stockTruth);
        }
        String desc ="先趋势，后图形，再竞价,核心挂，当小心》》提供20191015以后的数据=====>当前查询日期";
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start, queryEnd);
        return desc+queryEnd+"<br>心理历程<br>:"+stockTruths+"===>【早盘修复情况】:<br>"+yesterdayOpens+"===>【复盘情况】:<br>"+temperaturesClose+"===>【数据情况】:<br>"+fives+"===>【尾盘修复情况】:<br>"+yesterdayCloses;
    }
    @RequestMapping("/chance2/{end}")
    String chance2(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        PRE_END=queryEnd;
        Date yesterdayDate =  MyUtils.getFormatDate(PRE_END);
        String queryYesterday =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(yesterdayDate));
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        List<StockInfo> dayOpens = stockInfoService.findStockDaysByDayFormatOpen(queryEnd,NumberEnum.StockType.STOCK_DAY.getCode());
        System.out.println("=============queryEnd = [" + queryEnd + "]"+"queryYesterday"+queryYesterday);
        List<StockInfo> yesterdayOpens = stockInfoService.findStockDaysByDayFormatTomorrowOpenYield(queryYesterday);
        List<StockInfo> yesterdayOpensResult = new ArrayList<>();
        for(StockInfo stockInfo :yesterdayOpens){
            yesterdayOpensResult.add(stockInfo);
            StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
            if(stockInfoOpen!=null){
                yesterdayOpensResult.add(stockInfoOpen);
            }
        }
        List<StockInfo> yesterdayCloses = stockInfoService.findStockFivesTomorrowCloseYield(queryYesterday);
        List<StockInfo> yesterdayClosesResult = new ArrayList<>();
        for(StockInfo stockInfo :yesterdayCloses){
            yesterdayClosesResult.add(stockInfo);
            StockInfo stockInfoOpen = stockInfoService.findFirst1ByCodeAndDayFormat(stockInfo.getCode(),queryEnd);
            if(stockInfoOpen!=null){
                yesterdayClosesResult.add(stockInfoOpen);
            }
        }
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockInfo> highCurrents = stockInfoService.fiveHeightSpace(start, queryEnd);
        List<StockInfo> kpls = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
        List<StockInfo> news = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_NEW.getCode());
        List<StockInfo> nearlys = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_NEARLY.getCode());
        String desc ="信念[空间与新题材模式，趋势持股看看一下，条件双十逻辑，涨停倍增逻辑] 提供20191015以后的数据=====>当前查询日期";
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start, queryEnd);
        List<StockTemperature> dayTemperatures=stockTemperatureRepository.findByDayFormat(queryEnd);
        endDate =  MyUtils.getFormatDate(queryEnd);
        String preDate =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        List<StockTemperature> preTemperatures=stockTemperatureRepository.findByDayFormat(preDate);
        List<StockTemperature> temperatures = new ArrayList<>();
        List<StockTemperature> temperaturesFives = new ArrayList<>();
        int i=0;
        int size = preTemperatures.size();
        for(StockTemperature st:dayTemperatures){
            if(i<size){
                temperatures.add(preTemperatures.get(i));
                if(i<5){
                    temperaturesFives.add(preTemperatures.get(i));
                    temperaturesFives.add(st) ;
                }
            }
            temperatures.add(st);
            i++;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(desc).
                append(queryEnd).append("===>【复盘情况】:<br>").append(temperaturesClose).
                append(queryEnd).append("===>【空间板数据情况】:<br>").append(highCurrents).
                append(queryEnd).append("===>【灵魂股情况】:<br>").append(kpls).
                append(queryEnd).append("===>【应变决策情况】:<br>").append(temperaturesFives).
                append(queryEnd).append("===>【早盘当日冲击情况】:<br>").append(yesterdayOpensResult).
                append(queryEnd).append("===>【早盘当日最强竞价】:<br>").append(dayOpens).
                append(queryEnd).append("===>【新股】:<br>").append(news).
                append(queryEnd).append("===>【次新股】:<br>").append(nearlys).
                append(queryEnd).append("===>【盘面实时运行情况】:<br>").append(temperatures);
        return sb.toString();
    }

    @RequestMapping("/risk/{end}")
    String risk(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;
        List<StockTruth> stockTruths = stockTruthRepository.findByDayFormat(queryEnd);
        StockTruth stockTruth = null;
        if(stockTruths==null){
            stockTruth =new StockTruth();
            stockTruths.add(stockTruth);
        }
        String desc ="【主流板块】注意[1,4,8,10月披露+月底提金，还有一些莫名的反常！！！]查询日期20191015以后的数据<br>===>当前查询日期";
        List<StockInfo> downs =stockInfoService.findStockInfosByDayFormatOrderByOpenBidRate(queryEnd);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockInfo> kpls = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
        List<StockInfo> highCurrents = stockInfoService.fiveHeightSpace(start, queryEnd);
        highCurrents.addAll(kpls);
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start,queryEnd);

        List<StockTemperature> temperaturesOpen=stockTemperatureRepository.open(start,queryEnd);
        List<StockTemperature> temperatures=stockTemperatureRepository.findByDayFormat(queryEnd);

       /* List<StaStockPlate> staStockPlatesWeek = stockPlateService.weekStatistic();
        List<StaStockPlateImpl> staStockPlatesWeekImpl = new ArrayList<>();
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesWeek){
                staStockPlatesWeekImpl.add(new StaStockPlateImpl(s));
            }
        }
        List<StaStockPlate> staStockPlatesWeek2 = stockPlateService.week2Statistic();
        List<StaStockPlateImpl> staStockPlatesWeek2Impl = new ArrayList<>();
        if(staStockPlatesWeek2.size()>0){
            for(StaStockPlate s: staStockPlatesWeek2){
                staStockPlatesWeek2Impl.add(new StaStockPlateImpl(s));
            }
        }*/
       List<StockInfo> days = stockInfoService.findStockDaysByDayFormat(queryEnd);
       List<StockInfo> fives = stockInfoService.findStockDayFivesByDayFormat(queryEnd);
        //List<StockInfo> alls = stockInfoService.findByDayFormatOrderByOpenBidRateDesc(queryEnd);

        return desc+queryEnd+"<br>===>【核心股的大低开】:<br>"+downs+"<br>===>【近5天开盘情况】:<br>"+temperaturesOpen+"<br>===>【复盘】:<br>"+stockTruths+"<br>===>【当天市场情况】:<br>"+temperatures+"<br>===>【近5日空间版和目标股】:<br>"+highCurrents+"<br>===>【近5天市场情况】:<br>"+temperaturesClose+"<br>===>【数据情况】:<br>"+fives+"<br>===>【竞价情况】:<br>"+days;
    }
    @RequestMapping("/info3/{end}")
    String info(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;


        String desc ="【主流板块 大科技】注意[不参与竞价，核心股的大低开，连板指数上6+]；查询日期20191015";
        List<StockInfo> stockInfos = stockInfoService.findStockInfosByDayFormatOrderByStockType(queryEnd);
        List<StockInfo> downs =stockInfoService.findStockInfosByDayFormatOrderByOpenBidRate(queryEnd);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5,endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start,queryEnd);
        List<StockTemperature> temperaturesOpen=stockTemperatureRepository.open(start,queryEnd);
        List<StockTemperature> temperatures=stockTemperatureRepository.findByDayFormat(queryEnd);
        List<StaStockPlate> staStockPlatesWeek = stockPlateService.weekStatistic();
        List<StaStockPlateImpl> staStockPlatesWeekImpl = new ArrayList<>();
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesWeek){
                staStockPlatesWeekImpl.add(new StaStockPlateImpl(s));
            }
        }
        List<StaStockPlate> staStockPlatesWeek2 = stockPlateService.week2Statistic();
        List<StaStockPlateImpl> staStockPlatesWeek2Impl = new ArrayList<>();
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesWeek2){
                staStockPlatesWeek2Impl.add(new StaStockPlateImpl(s));
            }
        }
        List<StaStockPlate> staStockPlatesMonth = stockPlateService.monthStatistic();
        List<StaStockPlateImpl> staStockPlatesMonthImpl = new ArrayList<>();
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesMonth){
                staStockPlatesMonthImpl.add(new StaStockPlateImpl(s));
            }
        }

        return desc+queryEnd+"<br>月:"+staStockPlatesMonthImpl+"<br>半月:"+staStockPlatesWeek2Impl+"周:"+staStockPlatesWeekImpl+"<br>最近5天市场情况<br>"+temperaturesClose+"<br>"+temperaturesOpen+"大低开:<br>"+downs+"<br>"+temperatures+"<br>【相信数据，相信市场】:<br>"+stockInfos;
    }
    @RequestMapping("/info/{end}")
    String pan(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;
        String preDate =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));

        String desc ="【主流板块 大科技】注意[不参与竞价，核心股的大低开，连板指数上6+]；日期20191015<br>查询日期:";
        List<StockInfo> stockInfos = stockInfoService.findStockInfosByDayFormatOrderByStockType(queryEnd);
        List<StockInfo> downs =stockInfoService.findStockInfosByDayFormatOrderByOpenBidRate(queryEnd);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start, queryEnd);
        List<StockTemperature> temperaturesOpen=stockTemperatureRepository.open(start, queryEnd);
        List<StockTemperature> dayTemperatures=stockTemperatureRepository.findByDayFormat(queryEnd);
        List<StockTemperature> preTemperatures=stockTemperatureRepository.findByDayFormat(preDate);
        List<StockPlateSta> stockPlates =stockPlateStaRepository.findByDayFormatAndPlateType(queryEnd, 1);
        List<StockTemperature> temperatures = new ArrayList<>();
        int i=0;
        int size = preTemperatures.size();
        for(StockTemperature st:dayTemperatures){
            if(i<size){
                temperatures.add(preTemperatures.get(i));
            }
            temperatures.add(st);
            i++;
        }
        List<StockTruth> stockTruths = stockTruthRepository.findByDayFormat(queryEnd);
        StockTruth stockTruth = null;
        if(stockTruths==null){
            stockTruth =new StockTruth();
            stockTruths.add(stockTruth);
        }
        List<StockInfo> kpls = stockInfoService.findByDayFormatAndStockTypeOrderByOpenBidRate(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
        List<StockInfo> highCurrents = stockInfoService.fiveHeightSpace(start, queryEnd);
        highCurrents.addAll(kpls);
        return desc+queryEnd+"<br>心理历程:"+stockTruths+"<br>月度主题:"+stockPlates+"<br>最近5天市场情况<br>"+temperaturesClose+"大低开:<br>"+downs+"<br>"+temperatures+"<br>【相信数据，相信市场】:<br>"+highCurrents+"<br>"+stockInfos+"<br>"+temperaturesOpen;
    }
    @RequestMapping("/info2/{end}")
    String info2(@PathVariable("end")String end) {
        String queryEnd = end;
        if("1".equals(end)){
            if(isWorkday()){
                queryEnd= MyUtils.getDayFormat();
            }else {
                queryEnd=MyUtils.getYesterdayDayFormat();
            }
        }else if("2".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        }else if("3".equals(end)){
            Date endDate =  MyUtils.getFormatDate(PRE_END);
            queryEnd =MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        PRE_END=queryEnd;
        String desc ="【主流板块 大科技】注意[不参与竞价，核心股的大低开，连板指数上6+]；查询日期20191015";
        List<StockInfo> stockInfos = stockInfoService.findStockInfosByDayFormatOrderByStockType(queryEnd);
        List<StockInfo> downs =stockInfoService.findStockInfosByDayFormatOrderByOpenBidRate(queryEnd);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5,endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start,queryEnd);
        List<StockTemperature> temperaturesOpen=stockTemperatureRepository.open(start,queryEnd);
        List<StockTemperature> temperatures=stockTemperatureRepository.findByDayFormat(queryEnd);
        List<StaStockPlate> staStockPlatesWeek = stockPlateService.weekStatistic();
        List<StaStockPlateImpl> staStockPlatesWeekImpl = new ArrayList<>();
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesWeek){
                staStockPlatesWeekImpl.add(new StaStockPlateImpl(s));
            }
        }
        List<StaStockPlate> staStockPlatesWeek2 = stockPlateService.week2Statistic();
        List<StaStockPlateImpl> staStockPlatesWeek2Impl = new ArrayList<>();
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesWeek2){
                staStockPlatesWeek2Impl.add(new StaStockPlateImpl(s));
            }
        }
        List<StaStockPlate> staStockPlatesMonth = stockPlateService.monthStatistic();
        List<StaStockPlateImpl> staStockPlatesMonthImpl = new ArrayList<>();
        if(staStockPlatesWeek.size()>0){
            for(StaStockPlate s: staStockPlatesMonth){
                staStockPlatesMonthImpl.add(new StaStockPlateImpl(s));
            }
        }

        return desc+queryEnd+"<br>月:"+staStockPlatesMonthImpl+"<br>半月:"+staStockPlatesWeek2Impl+"周:"+staStockPlatesWeekImpl+"<br>最近5天市场情况<br>"+temperaturesClose+"<br>"+temperaturesOpen+"大低开:<br>"+downs+"<br>"+temperatures+"<br>【相信数据，相信市场】:<br>"+stockInfos;
    }
    public boolean isWorkday(){
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
