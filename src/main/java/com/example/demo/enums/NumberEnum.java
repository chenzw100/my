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
}
