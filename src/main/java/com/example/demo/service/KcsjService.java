package com.example.demo.service;

import com.example.demo.dao.StockZyRepository;
import com.example.demo.domain.table.StockZy;
import com.example.demo.utils.TestProxyIp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class KcsjService {
    @Autowired
    StockZyRepository stockZyRepository;
    static String urlId ="https://www.kpwhr.com/index/index/jianlishow/id/";
    static String pageId ="http://www.kcsjgcsw.com/personal.php?PageNo=";
    static String cookiesStr = "PHPSESSID=v5vo58659j7k2qdpvf2qlhoov7; LiveWSLZT73369086=6b4beaabf8a24f83ba6475cd2fcddc8c; LiveWSLZT73369086sessionid=6b4beaabf8a24f83ba6475cd2fcddc8c; NLZT73369086fistvisitetime=1606615551848; NLZT73369086visitecounts=1; UM_distinctid=17611bfd0bf38a-080a953480922e-594a2011-100200-17611bfd0c0416; IESESSION=alive; pgv_pvi=6827934720; pgv_si=s6676731904; tencentSig=3500160000; _qddaz=QD.xqio8d.wmvfcl.ki2hf3fp; _qdda=3-1.1; _qddab=3-80wk5c.ki2hf3ft; _qddamta_4000088650=3-0; CNZZDATA1274190688=493980554-1606610746-%7C1606616150; NLZT73369086lastvisitetime=1606617741299; NLZT73369086visitepages=13";
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

    public String getPhoneInfo(String url){
       //TestProxyIp.setHttpsProxyIp();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)").cookies(getCookies()).get();
            //System.out.println("id =" + doc.html());
            String phone = doc.getElementsByTag("table").get(2).getElementsByTag("td").get(1).text();
            return phone;
        } catch (IOException e) {
            System.out.println("err---------url = [" + url + "]");
            e.printStackTrace();
        }
        return "";

    }

    public void infoPages(Integer page){
        //TestProxyIp.setHttpsProxyIp();
        Document doc = null;
        try {
            doc = Jsoup.connect(pageId+page).userAgent("Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)").cookies(getCookies()).get();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
       // doc.getElementsByTag("table").get(0).childNodes.get(1).childNodes().get(2).attributes().get("onclick").substring(50,56)
        Elements elements = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Integer size = elements.size();
        for (int i =1 ;i<size;i++){
            Element ine = elements.get(i);
            String name = ine.child(0).text();
            String zy = ine.child(1).text();
            String jibie = ine.child(2).text();
            String city = ine.child(3).text();
            //System.out.println("id:"+id+",姓名"+name+",级别"+jibie);
            StockZy stockZy = new StockZy();
            stockZy.setName(name);
            stockZy.setZy(zy);
            stockZy.setInfoLevel(jibie);
            stockZy.setInfoCity(city);
            String url =ine.getElementsByAttribute("onclick").attr("onclick");
            int index = url.indexOf("("); int end = url.indexOf(")");
            url = url.substring(index+2,end-1);
            String phone = getPhoneInfo("http://www.kcsjgcsw.com/"+url);
            stockZy.setPhone(phone);
            System.out.println(stockZy.toString());
            stockZyRepository.save(stockZy);
        }


    }

    public static void main(String[] args) {
        String text = "手上有本17年的结构中级工程师，不转社保，单证网查，有需要的联系：朱工 18002577001 联系我时，请说明是在27建筑网上看到的，谢谢！";
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[34578]\\d{9})|(?:861[358]\\d{9}))(?!\\d)");
        Matcher matcher = pattern.matcher(text);
        StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }

        System.out.println(bf.toString());
    }

    String getPhone(String content){
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[34578]\\d{9})|(?:861[358]\\d{9}))(?!\\d)");
        Matcher matcher = pattern.matcher(content);
        StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }

        return bf.toString();
    }



}
