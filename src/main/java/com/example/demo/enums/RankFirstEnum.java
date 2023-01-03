package com.example.demo.enums;


public enum RankFirstEnum {
    HIGH(1,"空间板"),
    TGB(11,"淘股吧"),
    THS(21,"同花顺"),
    TRADE(31,"成交前一"),
    WAN(41,"顽主杯前一"),
    HOLD(51,"持仓前一"),
    BUY(61,"买入前一"),
            ;

    private RankFirstEnum(int code, String desc) {
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
        for(RankFirstEnum d : RankFirstEnum.values()){
            if(d.getCode()==code){
                return d.getDesc();
            }
        }
        return "";
    }
}
