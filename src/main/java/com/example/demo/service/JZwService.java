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
public class JZwService {
    @Autowired
    StockZyRepository stockZyRepository;
    static String urlId ="http://jianzhurencaiwang.com/index.php/Person/resumeSelf/id/";
    static String pageId ="http://jianzhurencaiwang.com/index.php/Index/resumebox/id/2/majorid/150/p/";
    static String cookiesStr = "self=,22893,22883; PHPSESSID=ehvoprsmq4j2sfjoqb2fbaflo4; uid=24674; classid=3; phone=17623430884";
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
            doc = Jsoup.connect(urlId+id).cookies(getCookies()).get();
            //System.out.println("id =" + doc.html());
            String phone = doc.getElementById("selfPhoneShow").text();
            //String phone = doc.getElementsByClass("telbox").get(0).getElementsByTag("li").get(1).getElementsByTag("b").text();
            System.out.println("phone = [" + phone + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public String getPhoneInfo(String url){
        Document doc = null;
        try {
            doc = Jsoup.connect(url).cookies(getCookies()).get();
            String phone = doc.getElementById("selfPhoneShow").text();
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
            doc = Jsoup.connect(pageId+page).cookies(getCookies()).get();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
       // doc.getElementsByTag("table").get(0).childNodes.get(1).childNodes().get(2).attributes().get("onclick").substring(50,56)
        Elements elements = doc.getElementsByClass("resumebox").get(0).getElementsByTag("dl");
        Integer size = elements.size();
        for (int i =1 ;i<size;i++){
            Element ine = elements.get(i);
            String name = ine.child(0).text();
            String url =ine.child(0).getElementsByAttribute("href").attr("href");
            //String jibie = ine.child(3).text();
            String zy = ine.child(3).text();
            String city = ine.child(4).text();
            //System.out.println("id:"+id+",姓名"+name+",级别"+jibie);
            StockZy stockZy = new StockZy();
            stockZy.setName(name);
            stockZy.setZy(zy);
            stockZy.setInfoCity(city);
            String phone = getPhoneInfo("http://jianzhurencaiwang.com/"+url);
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
