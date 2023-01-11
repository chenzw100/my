package com.example.demo.enums;

/**
 * Created by chenzw on 2018/10/22.
 */
public class NumberEnum {
    public enum TemperatureType{
        OPEN(1,"开盘"),
        CLOSE(2,"收盘"),
        NORMAL(3,"正常"),
        FIVE(4,"五分钟");

        private TemperatureType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        private int code;
        private String desc;
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getTemperatureType(int code){
            for(TemperatureType d : TemperatureType.values()){
                if(d.getCode()==code){
                    return d.getDesc();
                }
            }
            return "";
        }
    }
    //stock_down，stock_space_height,stock_limit_up_five,stock_day，stock_current_five，stock_day_five
    public enum StockType{
        STOCK_KPL1(1,"持仓"),
        STOCK_BUY2(2,"买入"),
        STOCK_WAN3(3,"顽主"),
        STOCK_THS4(4,"同花顺"),
        STOCK_DAY_HOT(5,"热7"),
        STOCK_SPACE_HEIGHTS(6,"空间"),
        STOCK_DAY(10,"当天"),
        STOCK_DAY_THREE3(13,"三日当天"),
        STOCK_DAY_FIVE5(15,"五日当天"),
        STOCK_THS(11,"同花顺"),
        STOCK_CURRENT(20,"当天实时"),
        STOCK_DOWN(30,"弱势"),
        STOCK_DAY_THREE(31,"三日"),
        STOCK_SPACE_HEIGHT(40,"空间"),
        STOCK_LIMIT_UP_FIVE(50,"五版"),
        STOCK_CURRENT_FIVE(60,"五日实时"),
        STOCK_DAY_FIVE(70,"五日"),
        STOCK_KPL(80,"持仓"),
        STOCK_BUY(81,"买入"),
        STOCK_WAN(82,"顽主"),
        STOCK_TRADE(83,"成交额"),
        STOCK_NEARLY(90,"次新股"),
        STOCK_NEW(100,"新股"),
        ;

        private StockType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        private int code;
        private String desc;
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getStockType(int code){
            for(StockType d : StockType.values()){
                if(d.getCode()==code){
                    return d.getDesc();
                }
            }
            return "";
        }
    }
    public enum StockCurrentType{
        ONE_DAY(1,"一天"),
        FIVE_DAY(2,"五天"),
        ;

        private StockCurrentType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        private int code;
        private String desc;
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getStockCurrentType(int code){
            for(StockCurrentType d : StockCurrentType.values()){
                if(d.getCode()==code){
                    return d.getDesc();
                }
            }
            return "";
        }
    }

    public enum PlateType{
        MONTH(1,"月"),
        TWO_WEEK(2,"半月"),
        WEEK(3,"周");

        private PlateType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        private int code;
        private String desc;
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getPlateType(int code){
            for(PlateType d : PlateType.values()){
                if(d.getCode()==code){
                    return d.getDesc();
                }
            }
            return "";
        }
    }

    public enum MoodType{
        RISING(1,"情绪上升红盘"),
        HIGH_SIDEWAYS(2,"情绪高位红盘"),
        DOWN(3,"情绪下跌红盘"),
        LOW_SIDEWAYS(4,"情绪低位红盘"),
        CONTINUOUS(5,"情绪连续红盘"),
        JUMP_EMPTY(6,"情绪跳空红盘"),
        DOWN_DOWN(7,"情绪绿盘");

        private MoodType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        private int code;
        private String desc;
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getMoodType(int code){
            for(MoodType d : MoodType.values()){
                if(d.getCode()==code){
                    return d.getDesc();
                }
            }
            return "";
        }
    }

    public enum StockTradeType{
        HARDEN(10,"涨停"),//2021年8月18日 涨停   成交额大于10亿  流通值 100
        FALL(4,"跌幅"),//2021年8月12日  跌幅排名前30  成交额大于1亿 同花顺行业 流通值
        RISE(5,"上涨5"),//2021年8月12日 涨幅排名前100 涨幅大于5% 成交额大于20亿 行业  流通市值
        FIFTY(50,"全排名"),//2020年12月31日  成交额从大到小排名前50 流通值大于10 同花顺行业
        FIRST(1,"首板"),//2022年8月5日 涨停 成交额从大到小排名前50 非连板 成交额大于10亿
        CYB(3,"创业板"),//2021年08月6日 涨停  成交额从大到小排名前50  成交额大于1亿 创业板
        THS(20,"同花顺"),
        TRADE(30,"成交前十"),
        ;

        private StockTradeType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        private int code;
        private String desc;
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }

        public static String getDesc(int code){
            for(StockTradeType d : StockTradeType.values()){
                if(d.getCode()==code){
                    return d.getDesc();
                }
            }
            return "";
        }
    }
}
