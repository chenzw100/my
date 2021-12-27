package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.table.StockOpt;
import com.example.demo.service.StockOptService;
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
@RequestMapping("/opt")
public class StockOptController {
    private static String PRE_END="";

    @Autowired
    StockOptService stockOptService;
    @RequestMapping("/list.html")
    public String index(ModelMap modelMap){
        return "opt/list";
    }
    @RequestMapping("/list.action")
    @ResponseBody
    public String list(Integer page, Integer rows, StockOpt obj){
        if(StringUtils.isBlank(obj.getDayFormat())){
            if(StringUtils.isBlank(obj.getCode())){
                obj.setDayFormat(getQueryDate(obj.getHotValue()));
            }
        }
        Page<StockOpt> list =stockOptService.findALl(page,rows,obj);
        Map map = new HashMap<>();
        map.put("total",list.getTotalElements());
        map.put("rows",list.getContent());
        return JSON.toJSONString(map);
    }
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
}
