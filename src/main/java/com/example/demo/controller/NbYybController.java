package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.domain.StockYybInfoJob;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.enums.NumberEnum;
import com.example.demo.enums.YybEnum;
import com.example.demo.service.dfcf.DfcfYybRecordJobService;
import com.example.demo.service.tgb.TGBRecordService;
import com.example.demo.service.yyb.NbYybService;
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
@RequestMapping("/nbyyb")
public class NbYybController {
    private static String PRE_END="";
    @Autowired
    NbYybService nbYybService;
    @Autowired
    DfcfYybRecordJobService dfcfYybRecordJobService;
    @RequestMapping("/yybDo")
    @ResponseBody
    public String yybJob() {
        try {
            dfcfYybRecordJobService.yybJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "yybDo deal success";
    }

    @RequestMapping("/list.html")
    public String index(ModelMap modelMap){
        return "nbyyb/list";
    }
    @RequestMapping("/list.action")
    @ResponseBody
    public String list(Integer page, Integer rows, StockYybInfoJob obj){
        Map map = new HashMap<>();
        if(obj.getYybId()==null){
            obj.setYybId(YybEnum.YzName.xhgm.getCode());
        }
        Page<StockYybInfoJob> list =nbYybService.findALl(page,rows,obj);
        map.put("total",list.getTotalElements());
        map.put("rows",list.getContent());
        return JSON.toJSONString(map);
    }



}
