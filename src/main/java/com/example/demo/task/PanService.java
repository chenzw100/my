package com.example.demo.task;

import com.example.demo.enums.NumberEnum;
import com.example.demo.service.pan.DealPanDataService;
import com.example.demo.service.tgb.TgbService;
import com.example.demo.service.xgb.XgbService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class PanService {
    Log log = LogFactory.getLog(PanService.class);
    private static final String tgbCron = "50 35 6 ? * MON-FRI";
    private static final String openCron = "59 25 9 ? * MON-FRI";
    private static final String closeCron ="40 3 15 ? * MON-FRI";
    private static final String choiceMy="0 1 9 ? * MON-FRI";
    private static final String currentTimeCron="1 50 0/2 ? * MON-FRI";
    private static final String temperatureCron="0 46 9,10,11,13,14 ? * MON-FRI";
    private static final String temperatureOpenCron="40 33 9 ? * MON-FRI";
    @Autowired
    DealPanDataService dealPanDataService;
    @Autowired
    TgbService tgbService;
    @Autowired
    XgbService xgbService;
    //盘前处理数据 6:23点获取
    @Scheduled(cron = tgbCron)
    public void preTgb(){
        //获取数据
        if(isWorkday()){
            log.info("tgb-ready data");
            tgbService.dayDate();
            tgbService.dayFive();
        }
    }
    //盘前处理数据 9:03点获取
    @Scheduled(cron = choiceMy)
    public void preOpen(){
        //获取数据
        if(isWorkday()){
            log.info("preOne-ready data");
            tgbService.currentDate();
            tgbService.currentFive();
        }
    }
    //9:26处理数据
    @Scheduled(cron = openCron)
    public void openPan(){
        if(isWorkday()){
            log.info("openPan-ready data");
            dealPanDataService.open();

        }
    }
    //9:33处理数据
    @Scheduled(cron = temperatureOpenCron)
    public void openFivePan(){
        if(isWorkday()) {
            log.info("openFivePan-ready data");
            xgbService.temperature(NumberEnum.TemperatureType.OPEN.getCode());
        }
    }
    //15:08处理数据
    @Scheduled(cron = closeCron)
    public void closePan(){
        if(isWorkday()) {
            log.info("closePan-ready data");
            dealPanDataService.close();
            xgbService.closePan();
        }
    }
    //盘中每小时处理数据
    @Scheduled(cron = temperatureCron)
    public void currentPan(){
        if(isWorkday()) {
            log.info("currentPan-ready data");
            xgbService.temperature(NumberEnum.TemperatureType.NORMAL.getCode());
        }
    }
    //每2小时收集数据
    @Scheduled(cron = currentTimeCron)
    public void allDay(){
        if(isWorkday()) {
            log.info("currentDate-ready data");
            tgbService.currentDate();
        }
    }

    public boolean isWorkday(){
        ChineseWorkDay chineseWorkDay = new ChineseWorkDay(MyUtils.getCurrentDate());
        try {
            if(chineseWorkDay.isWorkday()){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
