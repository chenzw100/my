package com.example.demo.service.dfcf;

import com.example.demo.service.base.BaseService;
import com.example.demo.utils.HttpClientUtil;
import org.springframework.stereotype.Component;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class DfcfService extends BaseService {
    private static String current_Continue="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08161&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";
    private static String current_Yesterday="http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx?type=CT&cmd=BK08151&sty=FDPBPFB&token=7bc05d0d4c3c22ef9fca8c2a912d779c";

    public String currentContinueVal() {
        String str = HttpClientUtil.doGet(current_Continue);;
        String[] stockObj = str.split(",");
        if(stockObj.length<7){
            log.error( ":err=" + str);
            return "0";
        }
        str =stockObj[5];
        return str;
    }
    public String currentYesterdayVal() {
        Object response =  getRequest(current_Yesterday);
        if(response==null){
            return "0";
        }
        String str = response.toString();
        String[] stockObj = str.split(",");
        if(stockObj.length<7){
            log.error( ":err=" + str);
            return "0";
        }
        str =stockObj[5];
        return str;
    }
}
