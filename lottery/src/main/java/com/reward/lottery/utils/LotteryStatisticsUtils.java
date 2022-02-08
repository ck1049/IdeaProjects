package com.reward.lottery.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LotteryStatisticsUtils {
    /**
     *
     * @param url 访问路径
     * @return
     */
    public static Document getDocument(String url){
        //5000是设置连接超时时间，单位ms
        try {
            return Jsoup.connect(url).timeout(10000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Map<String, String>> historyList(){
        return historyList(null);
    }

    public static List<Map<String, String>> historyList(String type){
        return historyList( type, null, null);
    }

    public static List<Map<String, String>> historyList(String type, String start, String end){
        String typeStr = StringUtils.isBlank(type) ? "ssq" : type;
        String startNumber = StringUtils.isNotBlank(start) ? start : "03001";//03001代表03年第一期彩票
        List<Map<String, String>> list = new ArrayList<>();
        String url = "https://datachart.500.com/" + typeStr + "/history/newinc/history.php?start=" + startNumber;
        if (StringUtils.isNotBlank(end)){
            url += "&end=" + end;
        }
        Document doc = getDocument(url);
        //获取目标HTML代码
        Elements trs = doc.select("tbody[id=tdata]").select("tr");
        trs.forEach(e -> {
            Map<String, String> map = new HashMap<>();
            Elements tds = e.select("td");
            map.put("number", tds.get(1).text()+tds.get(2).text()+tds.get(3).text()+tds.get(4).text()+tds.get(5).text()+tds.get(6).text()+tds.get(7).text());
            map.put("issueNumber", tds.get(0).text());
            if ("ssq".equals(typeStr)){
                map.put("awardDate", tds.get(15).text());
                map.put("firstPrizeNumber", tds.get(10).text());
                map.put("firstPrizeAmount", tds.get(11).text().replaceAll(",", ""));
                map.put("secondPrizeNumber", tds.get(12).text());
                map.put("secondPrizeAmount", tds.get(13).text().replaceAll(",", ""));
                map.put("bonusPool", tds.get(9).text().replaceAll(",", ""));
                map.put("totalBets", tds.get(14).text().replaceAll(",", ""));
            }else if ("dlt".equals(typeStr)){
                map.put("awardDate", tds.get(14).text());
                map.put("firstPrizeNumber", tds.get(9).text());
                map.put("firstPrizeAmount", tds.get(10).text().replaceAll(",", ""));
                map.put("secondPrizeNumber", tds.get(11).text());
                map.put("secondPrizeAmount", tds.get(12).text().replaceAll(",", ""));
                map.put("bonusPool", tds.get(8).text().replaceAll(",", ""));
                map.put("totalBets", tds.get(13).text().replaceAll(",", ""));
            }
            list.add(map);
        });
        return list;
    }

    public static Map<String, String> getLastLotteryInfo() {
        return historyList().get(0);
    }

    public static Map<String, String> getLastLotteryInfo(String type) {
        return historyList(type).get(0);
    }
}
