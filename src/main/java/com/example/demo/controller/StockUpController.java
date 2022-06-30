package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.service.StockUpService;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/up")
public class StockUpController {
    private static String UP_PRE_END="";
    @Autowired
    StockUpService stockUpService;
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



}
