package com.example.demo.service.base;

import com.example.demo.domain.SinaTinyInfoStock;
import com.example.demo.utils.RequestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by laikui on 2019/9/2.
 */
@Component
public class BaseService {
    public Log log = LogFactory.getLog(BaseService.class);
    public final static HashMap<String,String> PRICE_CACHE = new HashMap();
    public final static HashMap<String, SinaTinyInfoStock> SINA_CACHE= new HashMap<>();
    @Autowired
    RequestUtils requestUtils;

    public Object getRequest(String url){
        return requestUtils.request(url);
    }

}
