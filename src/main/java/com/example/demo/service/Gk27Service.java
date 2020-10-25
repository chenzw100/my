package com.example.demo.service;

import com.example.demo.dao.StockZyRepository;
import com.example.demo.domain.table.StockZy;
import org.apache.commons.lang.StringUtils;
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
public class Gk27Service {
    private static Map cache = new HashMap();
    @Autowired
    StockZyRepository stockZyRepository;
    static String urlId ="http://www.27guakao.com/city/hb/information.php?id=";
    static String pageId ="http://www.27guakao.com/category.php?catid=2&page=";
    static String cookiesStr = "UM_distinctid=1755d86f15d242-05748ed3145656-3e604000-100200-1755d86f15e239; Hm_lvt_881d4cf37741783ebdba3d130d30cb84=1603595052; CNZZDATA1259217558=643901521-1603591927-null%7C1603603587; jskx__s_uid=17623430884; jskx__s_pwd=VlcGBwUNCAMFBAMLVAYDWwBRAFMHBQEEBVFXBgRWAQM%3D9e65575921; Hm_lpvt_881d4cf37741783ebdba3d130d30cb84=1603603744";
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

            System.out.println("jieshao =" +doc.getElementsByClass("jieshao").text() );
            System.out.println("tel =" +doc.getElementsByClass("tel").get(1).text() );
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public String getPhoneInfo(String url){

        Document doc = null;
        try {
            doc = Jsoup.connect(url).cookies(getCookies()).get();
            return doc.getElementsByClass("tel").get(1).text();
        } catch (Exception e) {
            System.out.println("----------------------------------------------err url = [" + url + "]");
            e.printStackTrace();
        }
        return "";

        //stockZyRepository.save(stockZy);

    }

    public void infoPages(Integer page){
        Document doc = null;
        try {
            doc = Jsoup.connect(pageId+page).cookies(getCookies()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementsByClass("hover");
        Integer size = elements.size();
        for (int i =1 ;i<size;i++){
            Element ine = elements.get(i);
            Elements names = ine.getElementsByClass("name");
            String info = names.get(0).getElementsByClass("content").text();
            String phone = getPhone(info);
            String url =names.get(0).children().get(1).getElementsByAttribute("href").attr("href");
            if(StringUtils.isBlank(phone)){
                phone=getPhoneInfo(url);
            }
            String name = names.get(0).children().get(1).text();
            if(cache.get(name+phone)!=null){
                continue;
            }
            Elements zhuanyes = ine.getElementsByClass("zhuanye");
            String zhuanye = zhuanyes.text();
            //System.out.println("name = [" + name + "]"+"zhuanye = [" + zhuanye +"][phone:"+phone+ "]url:"+url);
            StockZy stockZy = new StockZy();
            stockZy.setName(name);
            stockZy.setZy(zhuanye);
            stockZy.setPhone(phone);
            stockZy.setInfoDesc(info);
            try {
                stockZyRepository.save(stockZy);
            }catch (Exception e){
                System.out.println(page+stockZy.toString());
            }
            cache.put(name+phone,stockZy);


        }


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
