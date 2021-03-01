package com.example.demo.task;

import com.example.demo.enums.NumberEnum;
import com.example.demo.service.pan.DealPanDataService;
import com.example.demo.service.tgb.TgbService;
import com.example.demo.service.xgb.XgbCurrentService;
import com.example.demo.service.xgb.XgbService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyChineseWorkDay;
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
    private static final String tgbCron = "58 10  8 ? * MON-FRI";
    private static final String openCron = "30 25 9 ? * MON-FRI";
    private static final String closeCron ="58 2 15 ? * MON-FRI";
    //private static final String choiceMy="1 1 9 ? * MON-FRI";
    //private static final String currentTimeCron="1 55 0/1 ? * MON-FRI";
    private static final String temperatureCron="10 35 9,10,11,13,14 ? * MON-FRI";
    private static final String temperatureCron2="10 45 9,13,14 ? * MON-FRI";
    private static final String temperatureCron3="10 30 9,14 ? * MON-FRI";
    private static final String temperatureCron4="10 01 10 ? * MON-FRI";
    private static final String temperatureCron5="10 33 9 ? * MON-FRI";
    private static final String temperatureOpenCron="39 25 9 ? * MON-FRI";
    @Autowired
    DealPanDataService dealPanDataService;
    @Autowired
    TgbService tgbService;
    @Autowired
    XgbService xgbService;
    @Autowired
    XgbCurrentService xgbCurrentService;
    //盘前处理数据 6:23点获取
    @Scheduled(cron = tgbCron)
    public void preTgb(){
        //获取数据
        if(isWorkday()){
            tgbService.dayDate();
            tgbService.dayFive();
        }
    }
    //盘前处理数据 9:03点获取
    /*@Scheduled(cron = choiceMy)
    public void preOpen(){
        //获取数据
        if(isWorkday()){
            tgbService.currentDataDeal();
        }
    }*/
    //9:26处理数据
    @Scheduled(cron = openCron)
    public void openPan(){
        if(isWorkday()){
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
            xgbCurrentService.closePan();
            xgbService.closePan();
            xgbCurrentService.closeNewAndNearly();
        }
    }
    //盘中每小时处理数据
    @Scheduled(cron = temperatureCron)
    public void currentPan(){
        if(isWorkday()) {
            log.info("currentPan-ready1 data");
            xgbCurrentService.currentPan();
            //xgbService.temperature(NumberEnum.TemperatureType.NORMAL.getCode());
        }
    }
    @Scheduled(cron = temperatureCron2)
    public void currentPan2(){
        if(isWorkday()) {
            log.info("currentPan-ready2 data");
            xgbCurrentService.currentPan();
            //xgbService.temperature(NumberEnum.TemperatureType.NORMAL.getCode());
        }
    }
    @Scheduled(cron = temperatureCron3)
    public void currentPan3(){
        if(isWorkday()) {
            log.info("currentPan-ready3 data");
            xgbCurrentService.currentPan();
            //xgbService.temperature(NumberEnum.TemperatureType.NORMAL.getCode());
        }
    }
    @Scheduled(cron = temperatureCron4)
    public void currentPan4(){
        if(isWorkday()) {
            log.info("currentPan-ready4 data");
            xgbCurrentService.currentPan();
            //xgbService.temperature(NumberEnum.TemperatureType.NORMAL.getCode());
        }
    }
    @Scheduled(cron = temperatureCron5)
    public void currentPan5(){
        if(isWorkday()) {
            log.info("currentPan-ready4 data");
            xgbCurrentService.currentPan();
            //xgbService.temperature(NumberEnum.TemperatureType.NORMAL.getCode());
        }
    }
    //每2小时收集数据
   /* @Scheduled(cron = currentTimeCron)
    public void allDay(){
        if(isWorkday()) {
            log.info("currentDate-ready data");
            tgbService.currentDate();
        }
    }*/

    public boolean isWorkday(){
        return MyChineseWorkDay.isWorkday();
    }
}
