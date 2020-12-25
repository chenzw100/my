package com.example.demo.utils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class TestProxyIp {

    private static final Logger log = Logger.getLogger(TestProxyIp.class);

    public static void setProxyIp()  {
        System.setProperty("http.maxRedirects", "50");
        System.getProperties().setProperty("proxySet", "true");
        // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
        String ip = "110.228.45.103";
        System.getProperties().setProperty("http.proxyHost", ip);
        System.getProperties().setProperty("http.proxyPort", "8118");

    }
    public static void setHttpsProxyIp()  {
        System.setProperty("https.maxRedirects", "50");
        System.getProperties().setProperty("proxySet", "true");
        // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
        System.getProperties().setProperty("https.proxyHost", "183.195.106.118");
        System.getProperties().setProperty("https.proxyPort", "8118");

    }
    public static void main(String[] args) throws IOException {
        System.setProperty("http.maxRedirects", "50");
        System.getProperties().setProperty("proxySet", "true");
        // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以

        String ip = "110.228.45.103";
        System.getProperties().setProperty("http.proxyHost", ip);
        System.getProperties().setProperty("http.proxyPort", "8118");

        //确定代理是否设置成功
        System.out.println(getHtml("https://www.baidu.com/"));

    }

    private static String getHtml(String address){
        StringBuffer html = new StringBuffer();
        String result = null;
        try{
            URL url = new URL(address);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; NT 5.1; GTB5; .NET CLR 2.0.50727; CIBA)");
            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

            try{
                String inputLine;
                byte[] buf = new byte[4096];
                int bytesRead = 0;
                while (bytesRead >= 0) {
                    inputLine = new String(buf, 0, bytesRead, "ISO-8859-1");
                    html.append(inputLine);
                    bytesRead = in.read(buf);
                    inputLine = null;
                }
                buf = null;
            }finally{
                in.close();
                conn = null;
                url = null;
            }
            result = new String(html.toString().trim().getBytes("ISO-8859-1"), "gb2312").toLowerCase();

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            html = null;
        }
       // System.out.println("address = [" + result + "]");
        return result;
    }


}
