package com.example.demo.enums;


public enum RankTypeEnum {
    TGB(10,"淘股吧"),
    THS(20,"同花顺"),
    HOLD(30,"持仓"),
    WAN(40,"比赛"),
    TRADE(100,"成交前十"),
            ;

    private RankTypeEnum(int code, String desc) {
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
        for(RankTypeEnum d : RankTypeEnum.values()){
            if(d.getCode()==code){
                return d.getDesc();
            }
        }
        return "";
    }
}
