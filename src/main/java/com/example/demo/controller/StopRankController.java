package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.dao.StockPlateStaRepository;
import com.example.demo.dao.StockTemperatureRepository;
import com.example.demo.domain.MyDoTradeStock;
import com.example.demo.domain.MyTrendStock;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockRank;
import com.example.demo.domain.table.StockTemperature;
import com.example.demo.enums.NumberEnum;
import com.example.demo.enums.RankTypeEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockUpService;
import com.example.demo.service.rank.StockRankService;
import com.example.demo.service.tgb.TgbDealService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/rank")
public class StopRankController {
    private static String UP_PRE_END="";
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockUpService stockUpService;
    @Autowired
    StockPlateStaRepository stockPlateStaRepository;
    @Autowired
    StockTemperatureRepository stockTemperatureRepository;
    @Autowired
    StockRankService stockRankService;
    @Autowired
    TgbDealService tgbDealService;


    @RequestMapping("/alllist.html")
    public String alllist(ModelMap modelMap){
        modelMap.put("title","排行ALL");
        return "rank/alllist";
    }
    @RequestMapping("/alllist.action")
    @ResponseBody
    public String alllist(StockRank obj){
        String queryEnd = getQueryDate(obj.getDayFormat());
        List<StockRank> infos =null;
        if(obj.getRankType()==null){
           infos =stockRankService.findByDayFormatOrderByStockType(queryEnd);
        }else {
            infos =stockRankService.findByDayFormatAndRankTypeOrderByShowCountDesc(queryEnd,obj.getRankType());
        }
        StockRank up= new StockRank();
        up.setDayFormat(queryEnd);
        up.setCode(queryEnd);
        up.setName("观察日");
        up.setPlateName("观察日");
        infos.add(0,up);
        Map map = new HashMap<>();
        map.put("total",infos.size());
        map.put("rows",infos);
        return JSON.toJSONString(map);
    }


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

    @RequestMapping("/dealRank.action")
    @ResponseBody
    public String dealRank() {
        stockRankService.dealRank();
        return "dealRank success";
    }
    @RequestMapping("/dealThsRank.action")
    @ResponseBody
    public String dealThsRank() {
        stockRankService.dealThsRank();
        return "dealThsRank success";
    }
    @RequestMapping("/dealThsTradesRank.action")
    @ResponseBody
    public String dealThsTradesRank() {
        stockRankService.dealThsTradesRank();
        return "dealThsRank success";
    }

    @RequestMapping("/staFlow.html")
    public String staFlow(ModelMap modelMap){
        modelMap.put("title","排行统计趋势");
        return "rank/staFlow";
    }
    @RequestMapping("/staFlow.action")
    @ResponseBody
    public String staFlow(StockRank obj){
        if(obj.getRankType()==null){
            obj.setRankType(RankTypeEnum.TGB.getCode());
        }
        if(StringUtils.isBlank(obj.getDayFormat())){
            obj.setDayFormat(MyUtils.getPreMonthDayFormat());
        }
        if(StringUtils.isBlank(obj.getCode())){
            obj.setCode(MyUtils.getDayFormat());
        }
        List<MyTrendStock> list =stockRankService.doStaFlow(obj.getRankType(),obj.getDayFormat(),obj.getCode());
        Map map = new HashMap<>();
        map.put("total",list.size());
        map.put("rows",list);
        return JSON.toJSONString(map);
    }

    @RequestMapping("/tgb7")
    @ResponseBody
    public String tgb7() {
        try {
            tgbDealService.day7Date();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "day7Date deal success";
    }


}
