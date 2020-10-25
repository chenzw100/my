package com.example.demo.service;

import com.example.demo.dao.StockZyRepository;
import com.example.demo.domain.table.StockZy;
import com.example.demo.service.base.BaseService;
import com.example.demo.utils.MyUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by laikui on 2019/9/2.
 *  * v_s_sh600519="1~贵州茅台~600519~358.74~-2.55~-0.71~27705~99411~~4506.49";
 1  0: 未知
 2  1: 股票名称
 3  2: 股票代码
 4  3: 当前价格
 5  4: 涨跌
 6  5: 涨跌%
 7  6: 成交量（手）
 8  7: 成交额（万）
 9  8:
 10  9: 总市值
 */
@Component
public class KpwService{
    @Autowired
    StockZyRepository stockZyRepository;
    static String url ="https://www.zgzckpw.com/personal_show.php?showid=";
    static String urlId ="https://www.zgzckpw.com/personal.php?keyqua=1&PageNo=";
    static String cookiesStr = "LiveWSLZT56322652=4ada08df8c654646940358879183441a1; NLZT56322652fistvisitetime=1601541039833; UM_distinctid1=174e348e85a560-0f9969abec791a-3c634103-100200-174e348e85b21a; _qddaz=QD.ipqr6h.h5105o.kfqk6o3m; tencentSig=3897116672; PHPSESSID=4146a7796e19091519806fc4dc93fc41; LiveWSLZT56322652sessionid=aedd0b3354d5418582a89cc5d79b8782; NLZT56322652visitecounts=2; CNZZDATA1274190612=180194105-1601537784-https%253A%252F%252Fwww.baidu.com%252F%7C1601595129; IESESSION=alive; _qdda=3-1.1; _qddab=3-ul4u0h.kfrgt45g; _qddamta_4000088650=3-0; NLZT56322652lastvisitetime=1601595841875; NLZT56322652visitepages=46";
    private static Map<String, String> cookies= new HashMap<>();

    public static Map<String, String> getCookies() {
        if(cookies.size()!=0){
            return cookies;
        }
        String parameStr[] = cookiesStr.split("; ");
        Map<String, String> cookies = new HashMap<>();
        for(String coo : parameStr){
            String parame[] =coo.split("=");
            cookies.put(parame[0],parame[1]);
        }
        return cookies;
    }
    public void infos(String id,StockZy stockZy){

        Document doc = null;
        try {
            doc = Jsoup.connect(url+id).cookies(getCookies()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //doc.getElementsByTag("table").get(2).childNodes.get(0).childNode(0).text()
        //手机： 18502981238 E-mail： QQ：
        Elements elements = doc.getElementsByTag("table").get(2).children();
        Element element =elements.first().children().first();
        String desc = element.text();
        if(element.childNodeSize()==1){
            System.out.println("失败手机================ = [" + desc + "]");
            throw new RuntimeException("登录失败");
        }
        String phone =element.child(1).text();
        int index =desc.lastIndexOf("E");
        String desc1 = desc.substring(index,desc.length());
        stockZy.setInfoDesc(desc1);
        stockZy.setPhone(phone);
        System.out.println("desc = [" + stockZy.toString() + "]");

        stockZyRepository.save(stockZy);

    }

    public void infoPages(Integer page){
        Document doc = null;
        try {
            doc = Jsoup.connect(urlId+page).cookies(getCookies()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
       // doc.getElementsByTag("table").get(0).childNodes.get(1).childNodes().get(2).attributes().get("onclick").substring(50,56)
        Element element = doc.getElementsByTag("table").get(0).child(0);
        Elements elements = element.children();
        Integer size = elements.size();
        for (int i =1 ;i<size;i++){
            Element ine = elements.get(i);
            String id = ine.attributes().get("onclick").substring(50,56);
            String name = ine.child(0).text();
            String jibie = ine.child(3).text();
            String zy = ine.child(4).text();
            String city = ine.child(5).text();
            //System.out.println("id:"+id+",姓名"+name+",级别"+jibie);
            StockZy stockZy = new StockZy();
            stockZy.setInfoLevel(jibie);
            stockZy.setName(name);
            stockZy.setZy(zy);
            stockZy.setInfoCity(city);
            infos(id,stockZy);
        }


    }


}
