package com.example.demo.controller;

import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.exception.NormalException;
import com.example.demo.utils.FileExcelUtil;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
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

            int temp= 6-stockZy.getCode().length();
            if(temp>0){
                if(temp==5){
                    stockZy.setCode("00000"+stockZy.getCode());
                }else if(temp==4){
                    stockZy.setCode("0000"+stockZy.getCode());
                }
                else if(temp==3){
                    stockZy.setCode("000"+stockZy.getCode());
                }
                else if(temp==2){
                    stockZy.setCode("00"+stockZy.getCode());
                }
                else if(temp==1){
                    stockZy.setCode("0"+stockZy.getCode());
                }
            }
            stockZy.setCode(stockZy.getCode().substring(0,6));
            stockZy.setYn(1);
            System.out.println(i+"《===============第，导入数据的电话【"+stockZy.getCode()+"】");
            String yesterdayVolumeStr =stockZy.getYesterdayVolumeStr();
            if(yesterdayVolumeStr.lastIndexOf("亿")>0){
                yesterdayVolumeStr=yesterdayVolumeStr.substring(0,yesterdayVolumeStr.length()-1);
                Integer yestemp =MyUtils.getCentByYuanStr(yesterdayVolumeStr.replace(",",""))*100;
                stockZy.setYesterdayVolume(yestemp);
            }else {
                yesterdayVolumeStr=yesterdayVolumeStr.substring(0,yesterdayVolumeStr.length()-4);
                Integer yestemp =Integer.parseInt(yesterdayVolumeStr.replace(",",""));
                stockZy.setYesterdayVolume(yestemp);
            }
            String tradeAmountStr =stockZy.getTradeAmountStr();
            if(StringUtils.isNotBlank(tradeAmountStr)){
                tradeAmountStr =tradeAmountStr.substring(0,tradeAmountStr.length()-4);
                stockZy.setTradeAmount(Integer.parseInt(tradeAmountStr.replace(",","")));
            }

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
        return "导入数据一共【"+personList.size()+"】行";
    }



}
