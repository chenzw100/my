package com.example.demo.service;

import com.example.demo.dao.StockYybRecordRepository;
import com.example.demo.domain.table.StockYybRecord;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockYybRecordService {
    @Autowired
    StockYybRecordRepository stockYybRecordRepository;
    public void deal(){
        String start =MyUtils.getPreFiveDayFormat();
        String end = MyUtils.getDayFormat();
        List<StockYybRecord> stockYybRecords = stockYybRecordRepository.findRecord(start,end);
        for(StockYybRecord stockYybRecord :stockYybRecords){
            System.out.println(stockYybRecord.toString());
        }
    }
}
