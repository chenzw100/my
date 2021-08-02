package com.example.demo.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.enums.NumberEnum;
import com.example.demo.service.dfcf.DfcfPankService;
import com.example.demo.service.dfcf.DfcfService;
import com.example.demo.service.dfcf.DfcfYybRecordJobService;
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
    private static final String closeYybCron ="5 2 17 ? * MON-FRI";
    private static final String closeYybJobCron ="45 01 18 ? * MON-FRI";
    private static final String closeTradeJobCron ="15 01 18 ? * MON-FRI";
    //private static final String choiceMy="1 1 9 ? * MON-FRI";
    //private static final String currentTimeCron="1 55 0/1 ? * MON-FRI";
    private static final String temperatureCron="10 35 9,10,11,13,14 ? * MON-FRI";
    private static final String temperatureCron2="10 45 9,13,14 ? * MON-FRI";
    private static final String temperatureCron3="10 30 9,14 ? * MON-FRI";
    private static final String temperatureCron4="10 01 10 ? * MON-FRI";
    private static final String temperatureCron5="10 33 9 ? * MON-FRI";
    private static final String temperatureCron6="10 08 11,13,14 ? * MON-FRI";
    private static final String temperatureOpenCron="39 25 9 ? * MON-FRI";
    @Autowired
    DealPanDataService dealPanDataService;
    @Autowired
    TgbService tgbService;
    @Autowired
    XgbService xgbService;
    @Autowired
    XgbCurrentService xgbCurrentService;
    @Autowired
    DfcfService dfcfService;
    @Autowired
    DfcfYybRecordJobService dfcfYybRecordJobService;
    @Autowired
    DfcfPankService dfcfPankService;
    //营业部处理
    @Scheduled(cron = closeTradeJobCron)
    public void tradeJob(){
        //获取数据
        if(isWorkday()){
            dfcfPankService.getAllList();
        }
    }
    //营业部处理
    @Scheduled(cron = closeYybJobCron)
    public void yyb2Job(){
        //获取数据
        if(isWorkday()){
            dfcfYybRecordJobService.yybJob();
        }
    }
    //营业部处理
    @Scheduled(cron = closeYybCron)
    public void yybJob(){
        //获取数据
        if(isWorkday()){
            dfcfService.yybJob();
        }
    }
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
            dfcfPankService.oneOpenIncomeRate();

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
            log.info("currentPan-ready5 data");
            xgbCurrentService.currentPan();
            //xgbService.temperature(NumberEnum.TemperatureType.NORMAL.getCode());
        }
    }
    @Scheduled(cron = temperatureCron6)
    public void currentPan6(){
        if(isWorkday()) {
            log.info("currentPan-ready6 data");
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

    public static void main(String[] args) {
        String s = "jQuery1123026748595128041175_1625483208174({\"Message\":\"\",\"Status\":0,\"Data\":[{\"TableName\":\"RptLHBYYBJYMXMap\",\"TotalPage\":9,\"ConsumeMSecond\":0,\"SplitSymbol\":\"|\",\"FieldName\":\"BMoney,RChange3M,RChange20DC,RChange15DC,RChange10DC,SalesName,RChange3DC,ChgRadio,SName,CPrice,SCode,RChange5DC,RChange20DO,RChange2DC,ActSellNum,SMoney,RChange6M,PBuy,RChange30DC,RChange15DO,ActBuyNum,RChange3DO,TDate,RChange10DO,RChange1M,CTypeDes,RChange5DO,RChange1DO,RChange30DO,SalesCode,RChange1DC,RChange2DO,RChange1Y\",\"Data\":[\"15851016|150.29585734|-32.8605|-34.1608|-31.6785|财信证券有限责任公司杭州西湖国贸中心证券营业部|-3.4279|10.013|山东墨龙|8.46|002490|-17.2577|-31.9149|-12.1749|14542226|14542226|140.34090863|1308790|-38.7707|-34.9882|15851016|-8.2742|2021-05-21|-32.1513|98.12646366|连续三个交易日内，涨幅偏离值累计达到20%的证券|-18.6761|-8.8652||80623462|-7.4468|-10.2837|171.15384545\",\"99753800.41|273.67021277|15.1601|13.9502|11.7438|财信证券有限责任公司杭州西湖国贸中心证券营业部|8.2562|10.0235|财达证券|14.05|600906|12.5267|16.3421|14.5196|||273.67021277|99753800.41|6.0498|10.2921|99753800.41|4.3115|2021-05-21|6.0501|273.67021277|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|7.8581|0.904||80623462|9.8932|11.2656|273.67021277\",\"21189678|69.07020984|-17.2899|-14.9567|-8.8322|财信证券有限责任公司杭州西湖国贸中心证券营业部|-9.4276|10|特一药业|17.82|002728|4.0965|-18.576|-12.0651|6631000.2|6631000.2|33.98496331|14558677.8|-14.7234|-15.9644|27217860|-10.4411|2021-05-20|-9.9287|58.82352942|日涨幅偏离值达到7%的前5只证券|6.086|-8.9894|-16.4287|80623462|-4.0965|-12.8978|29.88338278\",\"27217860|69.07020984|-17.2899|-14.9567|-8.8322|财信证券有限责任公司杭州西湖国贸中心证券营业部|-9.4276|10|特一药业|17.82|002728|4.0965|-18.576|-12.0651|6631000.2|6631000.2|33.98496331|20586859.8|-14.7234|-15.9644|27217860|-10.4411|2021-05-20|-9.9287|58.82352942|连续三个交易日内，涨幅偏离值累计达到20%的证券|6.086|-8.9894|-16.4287|80623462|-4.0965|-12.8978|29.88338278\",\"13241600.53|180.79207921|-3.5966|-5.8533|10.7193|财信证券有限责任公司杭州西湖国贸中心证券营业部|5.9238|10.0078|华通线缆|14.18|605196|12.4824|-10.0067|8.9563|||180.79207921|13241600.53|-12.835|-11.8079|13241600.53|8.7392|2021-05-20|6.8045|180.79207921|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|10.0067|3.4023|-18.2789|80623462|10.0141|-0.6671|180.79207921\",\"|60.33700585|6.4673|4.2846|28.7389|财信证券有限责任公司杭州西湖国贸中心证券营业部|0.1617|-10.0036|拉芳家化|24.74|603630|6.2652|12.0426|-8.9329|30633038|30633038|41.61419575|-30633038|-0.2425|8.0851||8.0851|2021-05-20|31.8723|46.21749409|有价格涨跌幅限制的日收盘价格跌幅偏离值达到7%的前三只证券|11.8723|-2.9362|3.8298|80623462|-6.9927|-4.6809|44.63662682\",\"11687290|208.77659574|27.907|30.8355|36.9509|财信证券有限责任公司杭州西湖国贸中心证券营业部|32.9888|10.0474|财达证券|11.61|600906|31.0078|15.74|21.0164|||208.77659574|11687290|31.0939|20.047|11687290|13.6257|2021-05-19|23.101|208.77659574|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|17.4628|10.0235|19.1073|80623462|9.9914|12.6077|208.77659574\",\"42155585|204.03225801|7.6923|24.0053|32.0955|财信证券有限责任公司杭州西湖国贸中心证券营业部|15.7825|10.073|华银电力|7.54|600744|26.6578|-3.0266|20.9549|||177.20588252|42155585|0|11.3801|42155585|7.2639|2021-05-19|21.1864|28.66894202|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|11.138|5.2058|-9.4431|80623462|9.9469|12.2276|235.11110675\",\"0|-23.74581885|26.7544|22.807|7.0175|财信证券有限责任公司杭州西湖国贸中心证券营业部|1.7544|-0.8696|*ST华英|2.28|002321|1.7544|26.7782|0.4386|3320523.8|3320523.8|-38.21138164|-3320523.8|29.8246|17.9916|0|-2.5105|2021-05-19|4.6025|-28.97196212|连续三个交易日内，跌幅偏离值累计达到12%的ST证券、*ST证券和未完成股改证券|-3.3473|0.4184|23.8494|80623462|4.8246|-2.9289|-51.69491492\",\"3558666.06|-6.24999939|5.098|3.9216|-6.6667|财信证券有限责任公司杭州西湖国贸中心证券营业部|-10.5882|4.9383|*ST华英|2.55|002321|-10.1961|10.3306|-9.8039|0|0|-27.96610118|3558666.06|21.1765|14.876|3558666.06|-1.2397|2021-05-14|-2.0661|-29.16666612|连续三个交易日内，涨幅偏离值累计达到12%的ST证券、*ST证券和未完成股改证券|-4.1322|-4.9587|25.2066|80623462|-5.098|-6.1983|-47.31404921\",\"12735|126.27272727|-12.6557|5.3435|4.9819|财信证券有限责任公司杭州西湖国贸中心证券营业部|-1.366|-7.1961|奥园美谷|24.89|000615|-1.7678|-7.6923|1.2455|319043945.05|319043945.05|463.12216772|-319031210.05|-15.3475|4.0748|12735|-1.6216|2021-05-14|6.4449|105.70247934|日跌幅偏离值达到7%的前5只证券|2.5364|2.2869|-12.6819|80623462|-1.3258|2.2869|488.41607109\",\"34058617.4|66.31509557|2.2592|26.2386|4.2013|财信证券有限责任公司杭州西湖国贸中心证券营业部|8.9972|9.9826|拉芳家化|25.23|603630|-1.9421|-2.3077|-0.9116|||49.11347517|34058617.4|-5.7868|19.1923|34058617.4|8.7308|2021-05-13|1.1154|49.82185274|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|-9.6154|-4.6154|-9.2308|80623462|4.677|5.7692|86.92161463\",\"0|103.96270398|-11.4286|-14|-2.9714|财信证券有限责任公司杭州西湖国贸中心证券营业部|-0.6057|-15.0238|国科微|87.5|300672|-7.3714|-14.8571|-5.0857|25788337.4|25788337.4|70.56530215|-25788337.4|15.3143|-15.1429|0|1.5314|2021-05-13|-5.1429|107.78912375|日跌幅达到15%的前5只证券|-7.4629|-4.1943|10.5143|80623462|1.5771|-3.9886|67.39048528\",\"|137.6|4.3771|14.8896|3.2548|财信证券有限责任公司杭州西湖国贸中心证券营业部|1.272|2.453|佳禾食品|26.73|605300|0.0748|8.0385|8.9787|9565710|9565710|137.6|-9565710|-2.9555|17.6538||0|2021-05-13|11.5385|137.6|有价格涨跌幅限制的日价格振幅达到15%的前三只证券|0.7692|7.6923|0.3077|80623462|9.9888|7.5|137.6\",\"|137.6|4.3771|14.8896|3.2548|财信证券有限责任公司杭州西湖国贸中心证券营业部|1.272|2.453|佳禾食品|26.73|605300|0.0748|8.0385|8.9787|9565710|9565710|137.6|-9565710|-2.9555|17.6538||0|2021-05-13|11.5385|137.6|有价格涨跌幅限制的日换手率达到20%的前三只证券|0.7692|7.6923|0.3077|80623462|9.9888|7.5|137.6\",\"187470227|129|-11.4331|12.6638|-2.3422|财信证券有限责任公司杭州西湖国贸中心证券营业部|-2.501|10|奥园美谷|25.19|000615|-2.5407|-14.981|-1.1909|47965821|76923321|488.55139745|110546906|-16.9909|6.4639|159897627|-6.4639|2021-05-12|-6.616|115.48331907|连续三个交易日内，涨幅偏离值累计达到20%的证券|-10.038|-0.9886|-23.0038|80623462|6.4708|-8.5551|492.70587776\",\"33900|129|-11.4331|12.6638|-2.3422|财信证券有限责任公司杭州西湖国贸中心证券营业部|-2.501|10|奥园美谷|25.19|000615|-2.5407|-14.981|-1.1909|47965821|47965821|488.55139745|-47931921|-16.9909|6.4639|159897627|-6.4639|2021-05-12|-6.616|115.48331907|日涨幅偏离值达到7%的前5只证券|-10.038|-0.9886|-23.0038|80623462|6.4708|-8.5551|492.70587776\",\"8042176.2|110.32831737|25.2033|38.8618|53.9512|财信证券有限责任公司杭州西湖国贸中心证券营业部|7.6098|10.0179|百龙创园|30.75|605016|23.5772|22.9904|-2.1789|6461776.5|6461776.5|110.32831737|1580399.7|15.8374|35.3376|8042176.2|11.9614|2021-05-11|47.91|110.32831737|有价格涨跌幅限制的日换手率达到20%的前三只证券|18.9711|-0.418|13.3441|80623462|2.7642|-5.0161|110.32831737\",\"0|713.13993174|3.106|19.2025|-10.1364|财信证券有限责任公司杭州西湖国贸中心证券营业部|4.0923|0.1261|顺控发展|47.65|003039|0.7345|9.8126|0.4827|24124172.66|24124172.66|713.13993174|-24124172.66|-3.9526|21.301|0|8.0485|2021-05-11|-5.1819|-16.15344008|日换手率达到20%的前5只证券|5.4024|-0.7718|-0.2089|80623462|-3.5887|3.3517|713.13993174\",\"|501.93236715|1.1236|7.9454|-4.0128|财信证券有限责任公司杭州西湖国贸中心证券营业部|9.6308|9.9735|英利汽车|12.46|601279|2.488|0.7223|21.0273|13233432.11|13233432.11|501.93236715|-13233432.11|-11.236|6.7416||4.4141|2021-05-11|-5.2167|501.93236715|有价格涨跌幅限制的日换手率达到20%的前三只证券|0.8828|9.9518|-11.1557|80623462|10.0321|11.557|501.93236715\",\"35651725|78.30985962|-6.1611|3.9494|4.5814|财信证券有限责任公司杭州西湖国贸中心证券营业部|12.1643|10.087|澳洋健康|6.33|002172|0.6319|-5.7751|11.2164|37913125.83|37913125.83|116.78082236|-2261400.83|-16.2717|7.4468|35651725|6.9909|2021-05-10|1.3678|29.18367341|连续三个交易日内，涨幅偏离值累计达到20%的证券|-3.1915|-3.7994|-20.5167|80623462|1.1058|6.383|122.10526368\",\"27572600|95.17743402|10.0233|33.986|14.1725|财信证券有限责任公司杭州西湖国贸中心证券营业部|25.035|10|奥园美谷|21.45|000615|14.4988|8.746|17.4359|28957500|28957500|397.67981057|-1384900|-1.5385|32.994|27572600|20.4998|2021-05-10|15.2244|83.49016254|日涨幅偏离值达到7%的前5只证券|13.8362|4.5812|0.8792|80623462|6.7599|21.7029|416.8674659\",\"23693860|712.11604096|5.0851|31.7294|-10.0651|财信证券有限责任公司杭州西湖国贸中心证券营业部|0.6094|5.1945|顺控发展|47.59|003039|0.3362|4.2316|-3.4671|0|0|712.11604096|23693860|-1.0086|31.9789|23693860|-1.3263|2021-05-10|-10.9053|-16.25901812|日振幅值达到15%的前5只证券|-0.4632|-4.5263|-1.6864|80623462|0.1261|-5.2632|712.11604096\",\"23693860|712.11604096|5.0851|31.7294|-10.0651|财信证券有限责任公司杭州西湖国贸中心证券营业部|0.6094|5.1945|顺控发展|47.59|003039|0.3362|4.2316|-3.4671|0|0|712.11604096|23693860|-1.0086|31.9789|23693860|-1.3263|2021-05-10|-10.9053|-16.25901812|日换手率达到20%的前5只证券|-0.4632|-4.5263|-1.6864|80623462|0.1261|-5.2632|712.11604096\",\"22841587.96|98.78928987|-7.8238|-6.7346|-2.3073|财信证券有限责任公司杭州西湖国贸中心证券营业部|2.483|20|国科微|85.38|300672|-2.729|-14.3696|20.602|0|0|56.20197585|22841587.96|-16.5964|-15.2065|22841587.96|-4.8913|2021-05-10|-8.6957|95.42229343|日涨幅达到15%的前5只证券|-8.6848|3.7174|-22.8043|80623462|18.8452|1.1957|77.4656898\",\"|447.34299517|7.8553|22.5066|11.2092|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.098|10|英利汽车|11.33|601279|9.8853|0.9167|21.0062|23618518|23618518|447.34299517|-23618518|-4.4131|15.8333||15.8333|2021-05-10|3.0833|447.34299517|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|2.5|3.8333|-11.5833|80623462|9.9735|14.1667|447.34299517\",\"|447.34299517|7.8553|22.5066|11.2092|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.098|10|英利汽车|11.33|601279|9.8853|0.9167|21.0062|23618518|23618518|447.34299517|-23618518|-4.4131|15.8333||15.8333|2021-05-10|3.0833|447.34299517|有价格涨跌幅限制的日换手率达到20%的前三只证券|2.5|3.8333|-11.5833|80623462|9.9735|14.1667|447.34299517\",\"34607478|15.73395841|-22.2785|-10.1013|-1.3924|财信证券有限责任公司杭州西湖国贸中心证券营业部|-5.2152|2.8914|麦迪科技|39.5|603990|-14.0253|-21.0942|-7.6962|||-24.05306672|34607478|-33.038|-8.5481|34607478|-3.4982|2021-05-07|-5.313|76.1034329|有价格涨跌幅限制的日价格振幅达到15%的前三只证券|-11.2309|2.4198|-30.0894|80623462|2.557|-3.9453|6.63124713\",\"28380000|270.78384811|35.4901|45.5477|30.8136|财信证券有限责任公司杭州西湖国贸中心证券营业部|3.4593|10.007|锦泓集团|15.61|603518|21.2684|30.0813|3.139|||161.91275161|28380000|37.0275|36.3352|28380000|-1.4384|2021-05-06|27.7048|165.02546696|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|28.2051|9.4434|32.833|80623462|9.9936|-0.813|146.99367092\",\"31017947|352.17391304|45.2991|34.6154|34.8291|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.1197|9.9882|英利汽车|9.36|601279|61.1111|33.6893|21.047|||352.17391304|31017947|11.9658|21.068|31017947|20.9709|2021-05-06|22.5243|352.17391304|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|34.9515|10|0|80623462|10.0427|16.5049|352.17391304\",\"13331680|31.19188368|50.6443|30.3479|30.2191|财信证券有限责任公司杭州西湖国贸中心证券营业部|22.4227|9.9929|未名医药|15.52|002581|22.0361|50.1877|16.6237|0|0|-19.71029572|13331680|17.3969|31.1014|13331680|18.8986|2021-04-29|26.9712|62.85414312|异常期间价格涨幅偏离值累计达到28.93%|14.831|0.3129|14.1427|80623462|9.9871|22.1527|-22.28342595\",\"11722962|186.66666666|62.5581|71.3953|54.4961|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.1008|9.9744|锦泓集团|12.9|603518|25.1938|47.7801|21.0078|||89.70588239|11722962|56.4341|52.1494|11722962|23.3263|2021-04-29|34.1085|134.5454545|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|11.0641|10.007|42.8471|80623462|10|12.685|112.52059304\",\"0|112.12458285|52.5433|35.6581|32.1447|财信证券有限责任公司杭州西湖国贸中心证券营业部|2.2549|9.9769|奥园美谷|19.07|000615|20.0839|49.739|6.345|31844387|31844387|370.86419388|-31844387|8.495|31.8372|0|1.9311|2021-04-29|28.3925|67.72207565|日价格涨幅偏离值达到9.80%|17.9541|18.4238|9.1336|80623462|10.0157|6.1065|370.86419388\",\"0|112.12458285|52.5433|35.6581|32.1447|财信证券有限责任公司杭州西湖国贸中心证券营业部|2.2549|9.9769|奥园美谷|19.07|000615|20.0839|49.739|6.345|31844387|31844387|370.86419388|-31844387|8.495|31.8372|0|1.9311|2021-04-29|28.3925|67.72207565|日价格振幅达到16.64%|17.9541|18.4238|9.1336|80623462|10.0157|6.1065|370.86419388\",\"|157.24423421|33.5402|32.5747|34.069|财信证券有限责任公司杭州西湖国贸中心证券营业部|11.4023|3.4975|小康股份|43.5|601127|28.023|29.717|1.2644|149743756|149743756|376.4512595|-149743756|54.7586|34.7877||17.9245|2021-04-27|37.2642|76.82926825|有价格涨跌幅限制的日价格振幅达到15%的前三只证券|30.4245|9.6698|62.7358|80623462|8.046|2.8538|432.20175684\",\"76991002.66|201.02347983|-4.72|-8|-11.9|财信证券有限责任公司杭州西湖国贸中心证券营业部|-5.99|201.0235|华利集团|100|300979|2|-4.2424|-6.89|0|0|201.02347983|76991002.66|3|-6.5657|76991002.66|-5.0505|2021-04-26|-10.101|201.02347983|无价格涨跌幅限制的证券|3.0303|-9.101|3.303|80623462|-12.2|-7.0707|201.02347983\",\"0|-10.06616008|7.0294|-3.5733|-11.876|财信证券有限责任公司杭州西湖国贸中心证券营业部|-1.8917|10|歌尔股份|38.06|002241|-2.3384|6.8329|-2.1808|144198952|144198952|-10.55228894|-144198952|-2.7557|-4.8256|0|-2.5702|2021-04-22|-11.7231|45.93558687|日价格涨幅偏离值达到9.52%|-2.9635|-2.7537|-4.1715|80623462|-2.1808|-3.0947|107.58726818\",\"128175558|129.50581386|71.6276|91.5453|93.9835|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.0906|-10.0028|小康股份|31.58|601127|48.8284|77.1242|20.9943|||232.42105272|128175558|84.0722|95.4575|128175558|40.5229|2021-04-21|108.4641|41.2975391|有价格涨跌幅限制的日收盘价格跌幅偏离值达到7%的前三只证券|51.9608|13.4314|88.2353|80623462|10.0063|34.8693|275.83730297\",\"26209397.31|29.33985329|-6.8683|-8.3806|-6.8683|财信证券有限责任公司杭州西湖国贸中心证券营业部|0.8192|9.9792|常熟汽饰|15.87|603035|9.1367|-8.75|-2.8355|||13.43817016|26209397.31|-4.671|-8.875|26209397.31|-1|2021-04-20|-8.8125|41.5700268|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|3.125|1.3125|-6.6586|80623462|6.2382|1|39.07039005\",\"2286198|273.64967188|-27.9249|-23.4126|-19.7784|财信证券有限责任公司杭州西湖国贸中心证券营业部|8.8895|10.0015|华亚智能|74.02|003043|-11.7941|-30.75|20.9943|793904.14|39864332|273.64967188|-37578134|-26.2767|-26.2895|1576626|0|2021-04-20|-22.1974|273.64967188|异常期间价格涨幅偏离值累计达到27.02%|-14.4211|12.6184|-27.3684|80623462|9.9973|26.3158|273.64967188\",\"|19.59183674|32.9352|48.1229|29.3515|财信证券有限责任公司杭州西湖国贸中心证券营业部|7.1672|-5.0243|江苏吴中|5.86|600200|2.5597|33.3904|3.9249|56961902.67|56961902.67|-4.248366|-56961902.67|38.2253|44.6918||6.6781|2021-04-19|32.5342|14.0077821|非ST、*ST和S证券连续三个交易日内收盘价格跌幅偏离值累计达到20%的证券|2.3973|-2.3973|36.8151|80623462|-1.0239|3.7671|-19.83584013\",\"709572|208.78344271|-9.4981|-8.0105|2.256|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.1045|9.9982|华亚智能|61.17|003043|31.7639|-13.5938|21.007|39070427.86|39070427.86|208.78344271|-38360855.86|1.3078|-12.4844|709572|33.7344|2021-04-16|-4.3438|208.78344271|日换手率达到20%的前5只证券|18.75|4.6875|1.5625|80623462|10.0049|18.75|208.78344271\",\"|46.05954467|-17.9856|-16.307|-32.494|财信证券有限责任公司杭州西湖国贸中心证券营业部|-20.9832|10.0264|大湖股份|8.34|600257|-26.9784|-11.3307|-16.9065|27528725|27528725|101.937046|-27528725|-8.2734|-11.726||-13.1752|2021-04-16|-24.3742|75.21008404|有价格涨跌幅限制的日价格振幅达到15%的前三只证券|-19.8946|-6.8511|0.6588|80623462|-9.952|-11.726|105.41871384\",\"35574221.06|180.71680969|0.8272|5.8623|11.8864|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.1056|10.0099|华亚智能|55.61|003043|61.0502|0.3597|21.0034|0|0|180.71680969|35574221.06|1.3127|3.3993|35574221.06|36.6906|2021-04-15|10.6655|180.71680969|连续三个交易日内，涨幅偏离值累计达到20%的证券|72.6619|15.1079|1.3669|80623462|9.9982|20.5036|180.71680969\",\"35574221.06|180.71680969|0.8272|5.8623|11.8864|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.1056|10.0099|华亚智能|55.61|003043|61.0502|0.3597|21.0034|0|0|180.71680969|35574221.06|1.3127|3.3993|35574221.06|36.6906|2021-04-15|10.6655|180.71680969|日换手率达到20%的前5只证券|72.6619|15.1079|1.3669|80623462|9.9982|20.5036|180.71680969\",\"35574221.06|180.71680969|0.8272|5.8623|11.8864|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.1056|10.0099|华亚智能|55.61|003043|61.0502|0.3597|21.0034|0|0|180.71680969|35574221.06|1.3127|3.3993|35574221.06|36.6906|2021-04-15|10.6655|180.71680969|连续三个交易日内，日均换手率与前五个交易日的日均换手率的比值达到30倍，且换手率累计达20%的证券|72.6619|15.1079|1.3669|80623462|9.9982|20.5036|180.71680969\",\"35574221.06|180.71680969|0.8272|5.8623|11.8864|财信证券有限责任公司杭州西湖国贸中心证券营业部|33.1056|10.0099|华亚智能|55.61|003043|61.0502|0.3597|21.0034|0|0|180.71680969|35574221.06|1.3127|3.3993|35574221.06|36.6906|2021-04-15|10.6655|180.71680969|日涨幅偏离值达到7%的前5只证券|72.6619|15.1079|1.3669|80623462|9.9982|20.5036|180.71680969\",\"26703805.49|36.57657658|-14.7757|-12.0053|-22.6913|财信证券有限责任公司杭州西湖国贸中心证券营业部|-8.5752|10.0145|大湖股份|7.58|600257|-11.8734|-12.6359|-0.9235|||81.33971295|26703805.49|3.562|-8.9674|26703805.49|-8.9674|2021-04-15|-22.4185|58.57740587|非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券|-10.1902|3.125|4.6196|80623462|10.0264|-3.9402|77.93426762\",\"6845612|60.94291658|168.5095|168.5095|171.2378|财信证券有限责任公司杭州西湖国贸中心证券营业部|72.803|20.0069|热景生物|69.64|688068|85.0661|124.988|43.9977|||34.44015445|6845612|163.4406|119.0043|6845612|59.1671|2021-04-13|127.3815|75.41561712|有价格涨跌幅限制的日收盘价格涨幅达到15%的前五只证券|58.3293|2.0823|138.236|80623462|20.0029|25.6582|47.18066665\",\"3577669.16|139.79798368|33.1929|51.9798|31.4238|财信证券有限责任公司杭州西湖国贸中心证券营业部|28.2224|10.0093|金发拉比|11.87|002762|22.9149|47.191|16.5965|38910649.66|38910649.66|87.81645873|-35332980.5|57.0947|61.5169|3577669.16|40.4494|2021-04-13|43.7266|128.70905958|连续三个交易日内，涨幅偏离值累计达到20%的证券|29.7753|29.588|66.2397|80623462|5.9815|32.9588|135.08678338\"]}]})";
        int i = s.indexOf("(");
        s=s.substring(i+1,s.length()-1);
        JSONObject j = JSON.parseObject(s);

        JSONArray jsonArray=j.getJSONArray("Data");
        JSONObject dataObj= (JSONObject) jsonArray.get(0);
        jsonArray=dataObj.getJSONArray("Data");
        for(Object data:jsonArray){
            String dataStr = data.toString();
            String dataArray[] = dataStr.split("\\|");
            System.out.println("买入:"+dataArray[0]+"净额"+dataArray[17]+",code"+dataArray[10]+":"+dataArray[8]+dataArray[22]+":"+"第一日:"+dataArray[30]+"第二日:"+dataArray[13]+"第三日:"+dataArray[6]);
        }

    }
}
