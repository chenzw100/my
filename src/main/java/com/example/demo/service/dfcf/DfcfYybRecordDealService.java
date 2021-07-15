package com.example.demo.service.dfcf;

import com.example.demo.dao.StockYybInfoRecordDealRepository;
import com.example.demo.dao.StockYybInfoRecordRepository;
import com.example.demo.domain.StockYybInfoRecord;
import com.example.demo.domain.StockYybInfoRecordDeal;
import com.example.demo.enums.YybEnum;
import com.example.demo.service.base.BaseService;
import com.example.demo.utils.ChineseWorkDay;
import com.example.demo.utils.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by laikui on 2019/9/2.
 * ==5
 * 国泰君安证券股份有限公司南京太平南路证券营业部
 * ==8
 * 酒钢宏兴
 * ==10
 * 600307
 *===22
 * 2021-05-13
 * ====25
 * 非ST、*ST和S证券连续三个交易日内收盘价格涨幅偏离值累计达到20%的证券
 * 连续三个交易日内，涨幅偏离值累计达到20%的证券
 */
@Component
public class DfcfYybRecordDealService extends BaseService {

    @Autowired
    StockYybInfoRecordRepository stockYybInfoRecordRepository;
    @Autowired
    StockYybInfoRecordDealRepository stockYybInfoRecordDealRepository;


    public List<StockYybInfoRecord>  getAll(){
      return  stockYybInfoRecordRepository.findByYybIdOrderByDayFormatAscYzTypeAsc(YybEnum.YzName.xhgm.getCode());
    }
    public void deal(){
        List<StockYybInfoRecord> list = getAll();
        for(StockYybInfoRecord s:list){
            Date endDate = MyUtils.getFormatDate(s.getDayFormat());
            ChineseWorkDay nowWorkDay = new ChineseWorkDay(new Date());
            Date preDate = nowWorkDay.preDaysWorkDay(6,endDate);
            String start = MyUtils.getDayFormat(preDate);
            System.out.println(start+"-------------"+s.getDayFormat());
            List<StockYybInfoRecordDeal> listTemp =stockYybInfoRecordDealRepository.findByDayFormatBetweenAndYybIdAndCode(start,s.getDayFormat(),s.getYybId(),s.getCode());
            if(listTemp.size()>0){
                continue;
            }
            StockYybInfoRecordDeal stockYybInfoRecordDeal = new StockYybInfoRecordDeal();
            BeanUtils.copyProperties(s,stockYybInfoRecordDeal);
            stockYybInfoRecordDealRepository.save(stockYybInfoRecordDeal);
        }
    }
}
