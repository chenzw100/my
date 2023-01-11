package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.dao.StockPlateStaRepository;
import com.example.demo.dao.StockTemperatureRepository;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.domain.table.StockTemperature;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.StockInfoService;
import com.example.demo.service.StockUpService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/stop")
public class StopInfoController {
    private static String UP_PRE_END="";
    @Autowired
    StockInfoService stockInfoService;
    @Autowired
    StockUpService stockUpService;
    @Autowired
    StockPlateStaRepository stockPlateStaRepository;
    @Autowired
    StockTemperatureRepository stockTemperatureRepository;


    @RequestMapping("/alllist.html")
    public String alllist(ModelMap modelMap){
        modelMap.put("title","类型ALL");
        return "stop/alllist";
    }
    @RequestMapping("/alllist.action")
    @ResponseBody
    public String alllist(StockInfo obj){
        String queryEnd = getQueryDate(obj.getDayFormat());
        List<StockInfo> infos =stockInfoService.findByDayFormatOrderByStockType(queryEnd);
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        StockInfo up= new StockInfo();
        up.setStockType(10);
        String day = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        up.setDayFormat(queryEnd);
        up.setCode(queryEnd);
        up.setName("观察日");
        up.setPlateName("观察日");
        up.setTodayOpenRate(queryEnd);
        up.setTodayClose("是否跌停");
        up.setTomorrowOpen("是否跌停");
        up.setTomorrowOpenEarnings(day);
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

    @RequestMapping("/optlist.html")
    public String index(ModelMap modelMap){
        modelMap.put("title","操作标的");
        return "stop/optlist";
    }
    @RequestMapping("/optlist.action")
    @ResponseBody
    public String list(StockInfo obj){

        String queryEnd = getQueryDate(obj.getDayFormat());
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        ChineseWorkDay tenDay=new ChineseWorkDay(endDate);
        String startTen =MyUtils.getDayFormat(tenDay.preDaysWorkDay(9,endDate));
        List<StockInfo> ten4 =stockInfoService.optCodeNew4(startTen,queryEnd);
        List<StockInfo> focus = stockInfoService.findByDayFormatAndStockTypeOrderByIdAsc(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
        List<StockInfo> focus2 = stockInfoService.findByDayFormatAndStockTypeOrderByIdAsc(queryEnd, NumberEnum.StockType.STOCK_BUY.getCode());
        ten4.addAll(focus);
        ten4.addAll(focus2);
        StockInfo up= new StockInfo();
        up.setStockType(10);
        String day = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        up.setDayFormat(queryEnd);
        up.setCode(queryEnd);
        up.setName("观察日");
        up.setPlateName("观察日");
        up.setTodayOpenRate(queryEnd);
        up.setTodayClose("是否跌停");
        up.setTomorrowOpen("是否跌停");
        up.setTomorrowOpenEarnings(day);
        ten4.add(0,up);
        Map map = new HashMap<>();
        map.put("total",ten4.size());
        map.put("rows",ten4);
        return JSON.toJSONString(map);
    }
    @RequestMapping("/tlist.html")
    public String tindex(ModelMap modelMap){
        modelMap.put("title","市场温度");
        return "stop/tlist";
    }
    @RequestMapping("/tlist.action")
    @ResponseBody
    public String tlist(Integer page, Integer rows, StockTemperature obj){
        String queryEnd = getQueryDate(obj.getDayFormat());
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        String start =MyUtils.getDayFormat(MyChineseWorkDay.preDaysWorkDay(5, endDate));
        List<StockTemperature> temperaturesClose=stockTemperatureRepository.close(start, queryEnd);
        Map map = new HashMap<>();
        map.put("total",temperaturesClose.size());
        map.put("rows",temperaturesClose);
        return JSON.toJSONString(map);
    }

    @RequestMapping("/clist.html")
    public String cindex(ModelMap modelMap){
        modelMap.put("title","市场实时温度");
        return "stop/clist";
    }
    @RequestMapping("/clist.action")
    @ResponseBody
    public String clist(Integer page, Integer rows, StockTemperature obj){
        String queryEnd = getQueryDate(obj.getDayFormat());
        List<StockTemperature> dayTemperatures=stockTemperatureRepository.findByDayFormat(queryEnd);
        Date endDate =  MyUtils.getFormatDate(queryEnd);
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
        Map map = new HashMap<>();
        map.put("total",temperatures.size());
        map.put("rows",temperatures);
        return JSON.toJSONString(map);
    }


    @RequestMapping("/slist.html")
    public String slist(ModelMap modelMap){
        modelMap.put("title","简洁-操作标的");
        return "stop/slist";
    }
    @RequestMapping("/slist.action")
    @ResponseBody
    public String slist(StockInfo obj){

        String queryEnd = getQueryDate(obj.getDayFormat());
        Date endDate =  MyUtils.getFormatDate(queryEnd);
        ChineseWorkDay tenDay=new ChineseWorkDay(endDate);
        String startTen =MyUtils.getDayFormat(tenDay.preDaysWorkDay(9,endDate));
        List<StockInfo> ten4 =stockInfoService.optCodeNew4(startTen,queryEnd);
        List<StockInfo> focus = stockInfoService.findByDayFormatAndStockTypeOrderByIdAsc(queryEnd, NumberEnum.StockType.STOCK_KPL.getCode());
        List<StockInfo> focus2 = stockInfoService.findByDayFormatAndStockTypeOrderByIdAsc(queryEnd, NumberEnum.StockType.STOCK_BUY.getCode());
        focus.addAll(focus2);
        focus.addAll(ten4);
        StockInfo up= new StockInfo();
        up.setStockType(10);
        String day = MyUtils.getDayFormat(MyChineseWorkDay.nextWorkDay(endDate));
        up.setDayFormat(queryEnd);
        up.setCode(queryEnd);
        up.setName("观察日");
        up.setPlateName("观察日");
        up.setTodayOpenRate(queryEnd);
        up.setTodayClose("是否跌停");
        up.setTomorrowOpen("是否跌停");
        up.setTomorrowOpenEarnings(day);
        List<StockInfo> ups = new ArrayList<>();
        Map temp = new HashMap();
        for(StockInfo s:focus){
            if(temp.get(s.getCode())==null){
                temp.put(s.getCode(),s.getName());
                ups.add(s);
            }
        }
        ups.add(0,up);
        Map map = new HashMap<>();
        map.put("total",ups.size());
        map.put("rows",ups);
        return JSON.toJSONString(map);
    }


}
