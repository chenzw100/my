package com.example.demo.controller;

import com.example.demo.dao.StockLimitUpRepository;
import com.example.demo.dao.StockRankRepository;
import com.example.demo.dao.StockTradeValInfoJobRepository;
import com.example.demo.dao.StockTradeValInfoTestRepository;
import com.example.demo.domain.StockTradeValInfoJob;
import com.example.demo.domain.StockTradeValInfoTest;
import com.example.demo.domain.table.StockInfo;
import com.example.demo.domain.table.StockLimitUp;
import com.example.demo.domain.table.StockRank;
import com.example.demo.exception.NormalException;
import com.example.demo.service.qt.QtService;
import com.example.demo.utils.CsvUtils;
import com.example.demo.utils.FileExcelUtil;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ExcelController {
    public Log log = LogFactory.getLog(ExcelController.class);
    @Autowired
    StockTradeValInfoJobRepository stockTradeValInfoJobRepository;
    @Autowired
    StockTradeValInfoTestRepository stockTradeValInfoTestRepository;
    @Autowired
    QtService qtService;
    @Autowired
    StockLimitUpRepository stockLimitUpRepository;
    @Autowired
    StockRankRepository stockRankRepository;

    @RequestMapping("importRank")
    public String importRank(ModelMap modelMap)  {
        return "exportRank";
    }
    @ResponseBody
    @RequestMapping("importExcelRankT.action")
    public String importExcelT(MultipartFile file) throws NormalException {
        List<StockRank> personList = FileExcelUtil.importExcel(file, StockRank.class);
        System.out.println("导入数据一共【"+personList.size()+"】行");
        int i =0;
        for(StockRank stockZy :personList){
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

            String code = stockZy.getCode();
            if (code.indexOf("6") == 0) {
                code = "sh" + code;
            } else if (code.indexOf("0") == 0){
                code = "sz" + code;
            }else if (code.indexOf("3") == 0){
                code = "sz" + code;
            }

            StockRank myStock = qtService.getRankInfo(code);
            if (myStock == null) {
                log.error("导入失败，可能重复"+code);
                continue;
            }
            myStock.setRankType(stockZy.getRankType());
            myStock.setHotSort(stockZy.getHotSort());
            myStock.setYn(1);
            if(stockZy.getCode().substring(0,3).equals("688")){
                myStock.setYn(-1);
            }
            myStock.setDayFormat(MyUtils.getTomorrowDayFormat());

            log.info(i+"《===============第，导入数据的code【"+stockZy.getCode()+"】");
            List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myStock.getCode(),MyUtils.getDayFormat());
            if(xgbStocks!=null && xgbStocks.size()>0){
                StockLimitUp xgbStock =xgbStocks.get(0);
                myStock.setPlateName(xgbStock.getPlateName());
                myStock.setContinuous(xgbStock.getContinueBoardCount());
            }else {
                xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(myStock.getCode());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    myStock.setPlateName(xgbStocks.get(0).getPlateName());
                }else {
                    myStock.setPlateName("");
                }
                myStock.setContinuous(0);
            }

            try {
                stockRankRepository.save(myStock);
            }catch (Exception e){
                log.error("失败，可能重复"+e.getMessage(),e);
            }
        }
        //TODO 保存数据库
        return "导入数据一共【"+personList.size()+"】行";
    }
    @ResponseBody
    @RequestMapping("importExcelRankY.action")
    public String importExcelRankY(MultipartFile file) throws NormalException {
        List<StockRank> personList = FileExcelUtil.importExcel(file, StockRank.class);
        System.out.println("导入数据一共【"+personList.size()+"】行");
        int i =0;
        for(StockRank stockZy :personList){
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
            if(StringUtils.isBlank(stockZy.getDayFormat())){
                stockZy.setDayFormat(MyUtils.getDayFormat());
            }


            String code = stockZy.getCode();
            if (code.indexOf("6") == 0) {
                code = "sh" + code;
            } else if (code.indexOf("0") == 0){
                code = "sz" + code;
            }else if (code.indexOf("3") == 0){
                code = "sz" + code;
            }
            StockRank myStock = qtService.getRankInfo(code);
            if (myStock == null) {
                log.error("导入失败，可能重复"+code);
                continue;
            }
            myStock.setRankType(stockZy.getRankType());
            myStock.setHotSort(stockZy.getHotSort());
            myStock.setYn(1);
            if(stockZy.getCode().substring(0,3).equals("688")){
                myStock.setYn(-1);
            }
            myStock.setDayFormat(MyUtils.getDayFormat());
            log.info(i+"《===============第，导入数据的code【"+stockZy.getCode()+"】");
            List<StockLimitUp> xgbStocks = stockLimitUpRepository.findByCodeAndDayFormat(myStock.getCode(),MyUtils.getYesterdayDayFormat());
            if(xgbStocks!=null && xgbStocks.size()>0){
                StockLimitUp xgbStock =xgbStocks.get(0);
                myStock.setPlateName(xgbStock.getPlateName());
                myStock.setContinuous(xgbStock.getContinueBoardCount());
            }else {
                xgbStocks =stockLimitUpRepository.findByCodeAndPlateNameIsNotNullOrderByIdDesc(myStock.getCode());
                if(xgbStocks!=null && xgbStocks.size()>0){
                    myStock.setPlateName(xgbStocks.get(0).getPlateName());
                }else {
                    myStock.setPlateName("");
                }
                myStock.setContinuous(0);
            }

            try {
                stockRankRepository.save(myStock);
            }catch (Exception e){
                log.error("失败，可能重复"+e.getMessage(),e);
            }
        }
        //TODO 保存数据库
        return "导入数据一共【"+personList.size()+"】行";
    }






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
            if(StringUtils.isBlank(stockZy.getDayFormat())){
                stockZy.setDayFormat(MyUtils.getDayFormat());
            }
            if(stockZy.getRankType()==null || stockZy.getRankType()==0){
                stockZy.setRankType(1);
            }
            if(stockZy.getHot()==null || stockZy.getHot()==0){
                stockZy.setHot(stockZy.getRank());
            }
            stockZy.setYn(1);
            if(stockZy.getCode().substring(0,3).equals("688")){
                stockZy.setYn(-1);
            }
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
            if(StringUtils.isNotBlank(stockZy.getPriceStr())){
                stockZy.setYesterdayClosePrice(MyUtils.getCentByYuanStr(stockZy.getPriceStr()));
            }
            try {
                stockTradeValInfoJobRepository.save(stockZy);
            }catch (Exception e){
                log.error("失败，可能重复"+e.getMessage(),e);
            }
        }
        //TODO 保存数据库
        return "导入数据一共【"+personList.size()+"】行";
    }



    @ResponseBody
    @RequestMapping("importCsv.action")
    public String importCsv(@RequestPart("file")MultipartFile file){
        try {

            List<StockTradeValInfoTest> infos = CsvUtils.importCsv(file, StockTradeValInfoTest.class);
            for(StockTradeValInfoTest infoTest :infos){
                infoTest.setCode(infoTest.getCode().substring(1,7));
                infoTest.setYn(1);
                if(StringUtils.isNotBlank(infoTest.getPriceStr())){
                    infoTest.setYesterdayClosePrice(MyUtils.getCentByYuanStr(infoTest.getPriceStr()));
                }
                try {
                    stockTradeValInfoTestRepository.save(infoTest);
                }catch (Exception e){
                    log.error("失败，可能重复"+e.getMessage(),e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("importExcel2.action")
    public String importExcel2(MultipartFile file) throws NormalException {
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
            if(StringUtils.isBlank(stockZy.getDayFormat())){
                stockZy.setDayFormat(MyUtils.getYesterdayDayFormat());
            }
            if(stockZy.getRankType()==null || stockZy.getRankType()==0){
                stockZy.setRankType(1);
            }
            stockZy.setYn(1);
            if(stockZy.getCode().substring(0,3).equals("688")){
                stockZy.setYn(-1);
            }
            if(stockZy.getHot()==null || stockZy.getHot()==0){
                stockZy.setHot(stockZy.getRank());
            }
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
            if(StringUtils.isBlank(yesterdayTurnoverStr)){
                stockZy.setYesterdayTurnover(100);
            }else {
                stockZy.setYesterdayTurnover(Integer.parseInt(yesterdayTurnoverStr));
            }
            if(StringUtils.isNotBlank(stockZy.getPriceStr())){
                stockZy.setYesterdayClosePrice(MyUtils.getCentByYuanStr(stockZy.getPriceStr()));
            }
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
