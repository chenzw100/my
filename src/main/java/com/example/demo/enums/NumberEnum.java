package com.example.demo.enums;

/**
 * Created by chenzw on 2018/10/22.
 */
public class NumberEnum {
    public enum TemperatureType{
        OPEN(1,"开盘"),
        CLOSE(2,"收盘"),
        NORMAL(3,"正常");

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
        STOCK_DAY(10,"当天"),
        STOCK_CURRENT(20,"当天实时"),
        STOCK_DOWN(30,"弱势"),
        STOCK_SPACE_HEIGHT(40,"空间"),
        STOCK_LIMIT_UP_FIVE(50,"五版"),
        STOCK_CURRENT_FIVE(60,"五日实时"),
        STOCK_DAY_FIVE(70,"五日当天"),
        STOCK_KPL(80,"聚焦"),
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
        FALL(4,"非连板"),//2021年8月6日  跌幅排名前40  成交额大于1千万
        FIRST(1,"首板"),//2021年8月6日 涨停 成交额从大到小排名前50 非连板 成交额大于10亿
        CYB(3,"创业板"),//2021年08月6日 涨停  成交额从大到小排名前50  成交额大于1亿 创业板
        RISE(5,"20亿"),//2021年08月6日   成交额大于20亿 涨幅大于5% 流通值 100
        HARDEN(10,"涨停"),//2021年08月6日 涨停   成交额大于10亿  流通值 100
        FIFTY(50,"50排名"),//2020年12月31日  成交额从大到小排名前50 流通值大于10
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

        public static String getPlateType(int code){
            for(PlateType d : PlateType.values()){
                if(d.getCode()==code){
                    return d.getDesc();
                }
            }
            return "";
        }
    }
}
