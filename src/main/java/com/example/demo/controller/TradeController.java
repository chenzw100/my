package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.MyDoTradeStock;
import com.example.demo.domain.MyTradeStock;
import com.example.demo.domain.StockTradeValCurrent;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.StockYyb;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.TradeService;
import com.example.demo.service.YybService;
import com.example.demo.service.ths.StockTradeValCurrentService;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/trade")
public class TradeController {
    @Autowired
    TradeService tradeService;
    @Autowired
    StockTradeValCurrentService stockTradeValCurrentService;
    @RequestMapping("/trade.html")
    public String doTrade(ModelMap modelMap){
        return "trade/trade";
    }
    @RequestMapping("/trade.action")
    @ResponseBody
    public String trade(StockTradeValInfoJob obj){
        if(StringUtils.isBlank(obj.getDayFormat())){
            obj.setDayFormat(MyUtils.getYesterdayDayFormat());
        }
        if(obj.getRank()==null){
            obj.setRank(6);
        }
        List<StockTradeValInfoJob> scs =tradeService.dododo(obj.getDayFormat(),obj.getRank());
        Map map = new HashMap<>();
        map.put("total",scs.size());
        map.put("rows",scs);
        return JSON.toJSONString(map);
    }

    @RequestMapping("/staMe.html")
    public String staMe(ModelMap modelMap){
        modelMap.put("title","低吸统计");
        return "trade/staMe";
    }
    @RequestMapping("/staMe.action")
    @ResponseBody
    public String staMe(StockTradeValInfoJob obj){
        if(obj.getRankType()==null){
            obj.setRankType(NumberEnum.StockTradeType.FIRST.getCode());
        }
        if(StringUtils.isBlank(obj.getDayFormat())){
            obj.setDayFormat(MyUtils.getPreMonthDayFormat());
        }
        if(StringUtils.isBlank(obj.getCode())){
            obj.setCode(MyUtils.getDayFormat());
        }
        List<MyDoTradeStock> list =tradeService.doMeSta(obj.getRankType(),obj.getDayFormat(),obj.getCode());
        Map map = new HashMap<>();
        map.put("total",list.size());
        map.put("rows",list);
        return JSON.toJSONString(map);
    }

    @RequestMapping("/listUp.html")
    public String listLimit(ModelMap modelMap){
        return "trade/listUp";
    }
    @RequestMapping("/listUp.action")
    @ResponseBody
    public String listUp(Integer page, Integer rows, StockTradeValInfoJob obj){
        if(obj.getRankType()==null){
            obj.setRankType(NumberEnum.StockTradeType.HARDEN.getCode());
        }
        if(obj.getOneOpenRate()==null){
            obj.setOneOpenRate(-700);
        }
        Map map = new HashMap<>();

        Page<StockTradeValInfoJob> list =tradeService.findLimitUP(page,rows,obj);
        map.put("total",list.getTotalElements());
        map.put("rows",list.getContent());
        return JSON.toJSONString(map);
    }
    @RequestMapping("/list.html")
    public String index(ModelMap modelMap){
        modelMap.put("title","数据记录");
        return "trade/list";
    }
    @RequestMapping("/list.action")
    @ResponseBody
    public String list(Integer page, Integer rows, StockTradeValInfoJob obj){
        if(obj.getRankType()==null){
            obj.setRankType(NumberEnum.StockTradeType.HARDEN.getCode());
            obj.setRank(5);
        }
        Map map = new HashMap<>();
        if(obj.getRankType()==-1){
            List<StockTradeValInfoJob> list =tradeService.statisticsList(obj.getRank());
            map.put("total",list.size());
            map.put("rows",list);
        }else {
            Page<StockTradeValInfoJob> list =tradeService.findList(page,rows,obj);
            map.put("total",list.getTotalElements());
            map.put("rows",list.getContent());
        }
        return JSON.toJSONString(map);
    }

    @RequestMapping("/sta.html")
    public String sta(ModelMap modelMap){
        return "trade/sta";
    }
    @RequestMapping("/sta.action")
    @ResponseBody
    public String sta(StockTradeValInfoJob obj){
        if(obj.getRankType()==null){
            obj.setRankType(NumberEnum.StockTradeType.FIFTY.getCode());
        }
        if(obj.getYesterdayTurnover()==null){
            obj.setYesterdayTurnover(8);
        }
        List<MyTradeStock> list =tradeService.statistics(obj.getRankType(),obj.getYesterdayTurnover(),obj.getRank());
        Map map = new HashMap<>();
        map.put("total",list.size());
        map.put("rows",list);
        return JSON.toJSONString(map);
    }
    @RequestMapping("/sta2.html")
    public String sta2(ModelMap modelMap){
        return "trade/sta2";
    }


    @RequestMapping("/current.html")
    public String current(ModelMap modelMap){
        return "trade/current";
    }
    @RequestMapping("/current.action")
    @ResponseBody
    public String current(StockTradeValCurrent obj){
        if(StringUtils.isBlank(obj.getDayFormat())){
            obj.setDayFormat(MyUtils.getDayFormat());
        }
        if(obj.getRankType()==null){
            obj.setRankType(NumberEnum.StockTradeType.FIFTY.getCode());
        }
        List<StockTradeValCurrent> scs =stockTradeValCurrentService.findByDayFormatAndRankType(obj.getDayFormat(),obj.getRankType());
        Map map = new HashMap<>();
        map.put("total",scs.size());
        map.put("rows",scs);
        return JSON.toJSONString(map);
    }

}
