package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.dao.StockPlateStaRepository;
import com.example.demo.dao.StockTemperatureRepository;
import com.example.demo.domain.table.*;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockUpService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String getQueryDate(Integer end) {
        String queryEnd = MyUtils.getDayFormat();
        if (end==null) {
            if (MyChineseWorkDay.isWorkday()) {
                queryEnd = MyUtils.getDayFormat();
            } else {
                queryEnd = MyUtils.getYesterdayDayFormat();
            }
        } else if (end==2) {
            Date endDate = MyUtils.getFormatDate(UP_PRE_END);
            queryEnd = MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        } else if (end==3) {
            Date endDate = MyUtils.getFormatDate(UP_PRE_END);
            queryEnd = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        UP_PRE_END = queryEnd;
        return queryEnd;
    }

    @RequestMapping("/list.html")
    public String index(ModelMap modelMap,String code,Integer stockType){
        /*modelMap.put("code",code);
        modelMap.put("stockType",stockType);*/
        return "up/list";
    }
    @RequestMapping("/list.action")
    @ResponseBody
    public String list(Integer page, Integer rows, StockLimitUp obj){
        if(StringUtils.isBlank(obj.getDayFormat())){
            obj.setDayFormat(getQueryDate(obj.getOpenCount()));
        }else {
            Date endDate = MyUtils.getFormatDate(obj.getDayFormat());
            UP_PRE_END = MyUtils.getDayFormat(endDate);
        }
        Map map = new HashMap<>();
        Page<StockLimitUp> list =stockUpService.findALl(page,rows,obj);
        map.put("total",list.getTotalElements());
        map.put("rows",list.getContent());
        return JSON.toJSONString(map);
    }
    @RequestMapping("/limit.action")
    @ResponseBody
    public String limitUp() {
        stockUpService.doLimitUp();
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



}
