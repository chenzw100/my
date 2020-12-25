package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.*;
import com.example.demo.domain.SinaTinyInfoStock;
import com.example.demo.domain.StaStockPlate;
import com.example.demo.domain.StaStockPlateImpl;
import com.example.demo.domain.table.*;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockPlateService;
import com.example.demo.service.dfcf.DfcfService;
import com.example.demo.service.sina.SinaService;
import com.example.demo.service.xgb.XgbService;
import com.example.demo.task.PanService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.HttpClientUtil;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private RestTemplate restTemplate;
    private static String current_Continue="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08161&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
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

    @RequestMapping("/pre")
    public String pre() {
        panService.preTgb();
        return "pre success";
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
        List<StockTruth> stockTruths = stockTruthRepository.findByDayFormat(queryEnd);
        List<StockInfo> fives = stockInfoService.findStockDayFivesByDayFormat(queryEnd);
        List<StockPlateSta> stockPlates =stockPlateStaRepository.findByDayFormatAndPlateType(queryEnd, 1);
        StockTruth stockTruth = null;
        if(stockTruths!=null&& stockTruths.size()==1){
            stockTruth = stockTruths.get(0);
        }else {
            stockTruth =new StockTruth();
        }
        String desc ="【主流板块】注意[1,4,8,10月披露+月底提金，还有一些莫名的反常！！！]查询日期20191015以后的数据=====>当前查询日期";
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start,queryEnd);
        List<StockInfo> days = stockInfoService.findStockDaysByDayFormat(queryEnd);
        return desc+queryEnd+"<br>===>【复盘】:<br>"+stockTruth.getTruthInfo()+stockPlates+"<br>===>【竞价情况】:<br>"+days+"===>【近5天市场情况】:<br>"+temperaturesClose+"<br>"+fives;
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
