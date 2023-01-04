package com.example.demo.domain;

/**
 * Created by laikui on 2019/1/10.
 */
public interface MyTrendStock {
    public String getDayFormat();
    public String getHot();
    public Integer getFirstOpenRate();
    public Integer getFirstCloseRate();
    public Integer getSecondOpenRate();
    public Integer getSecondCloseRate();
    public Integer getThirdOpenRate();
    public Integer getThirdCloseRate();
    public Integer getCountNum();

}
