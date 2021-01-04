package com.example.demo.service.dfcf;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.base.BaseService;
import com.example.demo.utils.HttpClientUtil;
import com.example.demo.utils.MyUtils;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class DfcfService extends BaseService {
    private static String current_Continue="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0816&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";
    private static String current_Yesterday="http://push2.eastmoney.com/api/qt/stock/get?secid=90.BK0815&ut=bd1d9ddb04089700cf9c27f6f7426281&fields=f170";

    public String currentContinueVal() {
        Object str = getRequest(current_Continue);
        JSONObject jsonObject = JSONObject.parseObject(str.toString());
        int temp = jsonObject.getJSONObject("data").getInteger("f170");
        MyUtils.getYuanByCent(temp);
        return MyUtils.getYuanByCent(temp);
    }
    public String currentYesterdayVal() {
        Object str = getRequest(current_Yesterday);
        JSONObject jsonObject = JSONObject.parseObject(str.toString());
        int temp = jsonObject.getJSONObject("data").getInteger("f170");
        MyUtils.getYuanByCent(temp);
        return MyUtils.getYuanByCent(temp);
    }
}
