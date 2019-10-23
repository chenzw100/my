package com.example.demo.domain;

/**
 * Created by laikui on 2019/1/10.
 */
public class StaStockPlateImpl {
    private StaStockPlate staStockPlate;
    public StaStockPlateImpl(StaStockPlate staStockPlate) {
        this.staStockPlate = staStockPlate;
    }

    @Override
    public String toString() {
        return staStockPlate.getPlateName()+","+staStockPlate.getTotalCount()+",ContinuousCount:"+staStockPlate.getContinuousCount()+",hot:"+staStockPlate.getHotSort()+","+staStockPlate.getDescription()+"<br>";
    }
}
