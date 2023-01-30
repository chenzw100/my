package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.MyTradeStock;
import com.example.demo.domain.StockTradeValCurrent;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.TradeService;
import com.example.demo.service.tgb.TGBRecordService;
import com.example.demo.service.ths.StockTradeValCurrentService;
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
@RequestMapping("/tgb")
public class TGBController {
    private static String PRE_END="";
    @Autowired
    TGBRecordService tgbRecordService;
    private String getQueryDate(Integer end) {
        String queryEnd = MyUtils.getDayFormat();
        if (end==null) {
            if (MyChineseWorkDay.isWorkday()) {
                queryEnd = MyUtils.getDayFormat();
            } else {
                queryEnd = MyUtils.getYesterdayDayFormat();
            }
        } else if (end==2) {
            Date endDate = MyUtils.getFormatDate(PRE_END);
            queryEnd = MyUtils.getDayFormat(MyChineseWorkDay.preWorkDay(endDate));
        } else if (end==3) {
            Date endDate = MyUtils.getFormatDate(PRE_END);
            queryEnd = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        }
        PRE_END = queryEnd;
        return queryEnd;
    }

    @RequestMapping("/list.html")
    public String index(ModelMap modelMap,String code,Integer stockType){
        modelMap.put("code",code);
        modelMap.put("stockType",stockType);
        return "tgb/list";
    }
    @RequestMapping("/list.action")
    @ResponseBody
    public String list(Integer page, Integer rows, StockInfo obj){
        obj.setHotSort(-1);
        if(obj.getStockType()==null){
            obj.setStockType(NumberEnum.StockType.STOCK_DAY.getCode());
        }else if(obj.getStockType()==1){
            obj.setStockType(NumberEnum.StockType.STOCK_DAY.getCode());
            obj.setHotSort(1);
        }
        if(StringUtils.isBlank(obj.getDayFormat())){
            if(StringUtils.isBlank(obj.getCode())){
                obj.setDayFormat(getQueryDate(obj.getOpenCount()));
            }
        }
        Map map = new HashMap<>();
        if(StringUtils.isNotBlank(obj.getDayFormat())){
            PRE_END = obj.getDayFormat();
        }
        Page<StockInfo> list =tgbRecordService.findALl(page,rows,obj);
        map.put("total",list.getTotalElements());
        map.put("rows",list.getContent());
        return JSON.toJSONString(map);
    }



}
