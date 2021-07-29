package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.MyTradeStock;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.StockYyb;
import com.example.demo.service.TradeService;
import com.example.demo.service.YybService;
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
    @RequestMapping("/list.html")
    public String index(ModelMap modelMap){
        return "trade/list";
    }
    @RequestMapping("/list.action")
    @ResponseBody
    public String list(Integer page, Integer rows, StockTradeValInfoJob obj){
        obj.setRankType(1);
        Page<StockTradeValInfoJob> list =tradeService.findList(page,rows,obj);

        Map map = new HashMap<>();
        map.put("total",list.getTotalElements());
        map.put("rows",list.getContent());
        return JSON.toJSONString(map);
    }

    @RequestMapping("/sta.html")
    public String sta(ModelMap modelMap){
        return "trade/sta";
    }
    @RequestMapping("/sta.action")
    @ResponseBody
    public String sta(StockTradeValInfoJob obj){
        obj.setRankType(1);
        List<MyTradeStock> list =tradeService.statistics();
        Map map = new HashMap<>();
        map.put("total",list.size());
        map.put("rows",list);
        return JSON.toJSONString(map);
    }
}
