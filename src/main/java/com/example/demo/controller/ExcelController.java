package com.example.demo.controller;

import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.exception.NormalException;
import com.example.demo.utils.FileExcelUtil;
import com.example.demo.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ExcelController {
    public Log log = LogFactory.getLog(ExcelController.class);
    @Autowired
    StockTradeValInfoJobRepository stockTradeValInfoJobRepository;
    @RequestMapping("import")
    public String export(ModelMap modelMap)  {
        return "export";
    }
    @ResponseBody
    @RequestMapping("importExcel.action")
    public String importExcel(MultipartFile file) throws NormalException {
       List<StockTradeValInfoJob> personList = FileExcelUtil.importExcel(file, StockTradeValInfoJob.class);
        System.out.println("导入数据一共【"+personList.size()+"】行");
        int i =0;
        for(StockTradeValInfoJob stockZy :personList){
            i++;
            System.out.println(i+"《===============第，导入数据的电话【"+stockZy.getCode()+"】");
            stockZy.setCode(stockZy.getCode().substring(0,6));
            stockZy.setYesterdayClosePrice(MyUtils.getCentByYuanStr(stockZy.getYesterdayClosePriceStr()));
            stockZy.setYesterdayGains(MyUtils.getCentByYuanStr(stockZy.getYesterdayGainsStr()));
            stockZy.setYn(1);
            String yesterdayVolumeStr =stockZy.getYesterdayVolumeStr();
            if(yesterdayVolumeStr.lastIndexOf("亿")>0){
                yesterdayVolumeStr=yesterdayVolumeStr.substring(0,yesterdayVolumeStr.length()-1);
                Integer yestemp =MyUtils.getCentByYuanStr(yesterdayVolumeStr)*100000;
                stockZy.setYesterdayVolume(yestemp);
            }else {
                yesterdayVolumeStr=yesterdayVolumeStr.substring(0,yesterdayVolumeStr.length()-1);
                Integer yestemp =MyUtils.getCentByYuanStr(yesterdayVolumeStr.replace(",",""));
                stockZy.setYesterdayVolume(yestemp);
            }
            String tradeAmountStr =stockZy.getTradeAmountStr();
            tradeAmountStr =tradeAmountStr.substring(0,tradeAmountStr.length()-4);
            stockZy.setTradeAmount(Integer.parseInt(tradeAmountStr));

            String yesterdayTurnoverStr =stockZy.getYesterdayTurnoverStr();
            yesterdayTurnoverStr =yesterdayTurnoverStr.substring(0,yesterdayTurnoverStr.length()-4);
            stockZy.setYesterdayTurnover(Integer.parseInt(yesterdayTurnoverStr));
            try {
                stockTradeValInfoJobRepository.save(stockZy);
            }catch (Exception e){
                log.error("失败，可能重复"+e.getMessage(),e);
            }
        }
        //TODO 保存数据库
        return "导入数据一共【"+personList.size()+"】行,已存在的手机号未再次导入";
    }



}
