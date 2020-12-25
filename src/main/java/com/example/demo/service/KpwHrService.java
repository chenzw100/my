package com.example.demo.service;

import com.example.demo.dao.StockZyRepository;
import com.example.demo.domain.table.StockZy;
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
public class KpwHrService {
    @Autowired
    StockZyRepository stockZyRepository;
    static String urlId ="https://www.kpwhr.com/index/index/jianlishow/id/";
    static String pageId ="https://www.kpwhr.com/index/usercenter/jianlidown/p/";
    static String cookiesStr = "PHPSESSID=0dgi0lm5s043r8qol52f0o1277; UM_distinctid=1755d85750f2d9-054e48e2373629-3e604000-100200-1755d8575104dc; think_var=zh-cn; CNZZDATA1253509078=1093696009-1603591829-%7C1603607671; lee_user_id=147158; lee_user_phone=18815199002; lee_user_lastlogintime=2020-10-25+10%3A14%3A26; lee_user_lastloginip=171.93.167.218; lee_user_sessionCode=Session7E4401023452m59367";
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
    public void testId(String id){

        Document doc = null;
        try {
            doc = Jsoup.connect(urlId+id+".html").cookies(getCookies()).get();
            //System.out.println("id =" + doc.html());
            String phone = doc.getElementsByClass("telbox").get(0).getElementsByTag("li").get(1).getElementsByTag("b").text();
            System.out.println("phone = [" + phone + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public String getPhoneInfo(String url){
        Document doc = null;
        try {
            doc = Jsoup.connect(url).cookies(getCookies()).get();
            //System.out.println("id =" + doc.html());
            String phone = doc.getElementsByClass("telbox").get(0).getElementsByTag("li").get(1).getElementsByTag("b").text();
            return phone;
        } catch (IOException e) {
            System.out.println("err---------url = [" + url + "]");
            e.printStackTrace();
        }
        return "";

    }

    public void infoPages(Integer page){
        Document doc = null;
        try {
            doc = Jsoup.connect(pageId+page+".html").cookies(getCookies()).get();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
       // doc.getElementsByTag("table").get(0).childNodes.get(1).childNodes().get(2).attributes().get("onclick").substring(50,56)
        Elements elements = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr");
        Integer size = elements.size();
        for (int i =0 ;i<size;i++){
            Element ine = elements.get(i);
            String name = ine.child(0).text();
            //String jibie = ine.child(3).text();
            String zy = ine.child(3).text();
            String city = ine.child(4).text();
            //System.out.println("id:"+id+",姓名"+name+",级别"+jibie);
            StockZy stockZy = new StockZy();
            stockZy.setName(name);
            stockZy.setZy(zy);
            stockZy.setInfoCity(city);
            String url =ine.child(6).getElementsByAttribute("href").attr("href");
            String phone = getPhoneInfo("https://www.kpwhr.com/"+url);
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
