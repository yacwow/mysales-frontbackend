package com.duyi.readingweb.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class JsoupUtil {

    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.qidian.com/").get();
        System.out.println(doc.title());
    }
}
