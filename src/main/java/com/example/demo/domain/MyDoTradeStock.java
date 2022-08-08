package com.example.demo.domain;

/**
 * Created by laikui on 2019/1/10.
 */
public interface MyDoTradeStock {
    public String getDayFormat();
    public Integer getOneOpenRate();
    public Integer getOneCloseRate();
    public Integer getOneOpenIncomeRate();
    public Integer getOneCloseIncomeRate();
    public Integer getOneNextOpenIncomeRate();
    public Integer getOneNextCloseIncomeRate();
    public Integer getTwoOpenIncomeRate();
    public Integer getTwoCloseIncomeRate();
    public Long getYesterdayTurnover();
    public Integer getCountNum();

}
