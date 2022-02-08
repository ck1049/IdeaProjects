package com.reward.lottery.utils;

import com.reward.lottery.domain.Lotto;
import com.reward.lottery.domain.TwoColorBall;

import java.util.*;

public class LotteryUtils {

    /**大乐透红球*/
    public static String[] LOTTO_RED ={
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35"};

    /**大乐透蓝球*/
    public static String[] LOTTO_BLUE = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12"};

    /**双色球红球*/
    public static String[] TWO_COLOR_BALL_RED = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33"};

    /**双色球蓝球*/
    public static String[] TWO_COLOR_BALL_BLUE = {
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
            "11", "12", "13", "14", "15", "16"};

    public static List<String> randomLottery(String type){
        Set<String> red = new HashSet<>();
        Set<String> blue = new HashSet<>();
        Random random = new Random();
        if ("ssq".equals(type)){
            while(red.size() < 6){
                int  ran = random.nextInt(33);
                red.add(TWO_COLOR_BALL_RED[ran]);
            }
            blue.add(TWO_COLOR_BALL_BLUE[random.nextInt(16)]);
        }else if ("dlt".equals(type)){
            while(red.size() < 5){
                int ran = random.nextInt(35);
                red.add(LOTTO_RED[ran]);
            }
            while(blue.size() < 2){
                int ran = random.nextInt(12);
                blue.add(LOTTO_BLUE[ran]);
            }
        }
        ArrayList<String> redList = new ArrayList<>(red);
        ArrayList<String> blueList = new ArrayList<>(blue);
        Collections.sort(redList);
        Collections.sort(blueList);
        redList.addAll(blueList);
        return redList;
    }

    public static Lotto setAndReturnLotto(Map<String, String> lottoMap){
        String issueNumber = lottoMap.get("issueNumber");//期号
        String awardDate = lottoMap.get("awardDate");
        String number = lottoMap.get("number");
        String bonusPool = lottoMap.get("bonusPool");//奖池金额
        String totalBets = lottoMap.get("totalBets");//总投注额
        String price = lottoMap.get("price");//彩票价格
        String firstPrizeNumber = lottoMap.get("firstPrizeNumber");//一等奖注数
        String firstPrizeAmount = lottoMap.get("firstPrizeAmount");//一等奖金额
        String secondPrizeNumber = lottoMap.get("secondPrizeNumber");//二等奖注数
        String secondPrizeAmount = lottoMap.get("secondPrizeAmount");//二等奖金额
        String winningAmount = lottoMap.get("winningAmount");//中奖金额
        String red1 = number.substring(0, 2);
        String red2 = number.substring(2, 4);
        String red3 = number.substring(4, 6);
        String red4 = number.substring(6, 8);
        String red5 = number.substring(8, 10);
        String blue1 = number.substring(10, 12);
        String blue2 = number.substring(12);
        Lotto lotto = new Lotto(number, red1, red2, red3, red4, red5, blue1, blue2, "0", 0);
        lotto.setPkId(KeyUtils.uuid());
        lotto.setAwardDate(awardDate);
        lotto.setIssueNumber(issueNumber);
        lotto.setBonusPool(Long.parseLong(bonusPool));
        lotto.setTotalBets(Long.parseLong(totalBets));
        lotto.setPrice(Integer.parseInt(StringUtils.isBlank(price) ? "0" : price));
        lotto.setFirstPrizeNumber(Integer.parseInt(firstPrizeNumber));
        lotto.setFirstPrizeAmount(Integer.parseInt(firstPrizeAmount));
        lotto.setSecondPrizeNumber(Integer.parseInt(secondPrizeNumber));
        lotto.setSecondPrizeAmount(Integer.parseInt(secondPrizeAmount));
        lotto.setWinningAmount(Integer.parseInt(StringUtils.isBlank(winningAmount) ? "0" : winningAmount));
        return lotto;
    }

    public static TwoColorBall setAndReturnTwoColorBall(Map<String, String> twoColorBallMap){
        String issueNumber = twoColorBallMap.get("issueNumber");//期号
        String awardDate = twoColorBallMap.get("awardDate");
        String number = twoColorBallMap.get("number");
        String bonusPool = twoColorBallMap.get("bonusPool");//奖池金额
        String totalBets = twoColorBallMap.get("totalBets");//总投注额
        String price = twoColorBallMap.get("price");//彩票价格
        String firstPrizeNumber = twoColorBallMap.get("firstPrizeNumber");//一等奖注数
        String firstPrizeAmount = twoColorBallMap.get("firstPrizeAmount");//一等奖金额
        String secondPrizeNumber = twoColorBallMap.get("secondPrizeNumber");//二等奖注数
        String secondPrizeAmount = twoColorBallMap.get("secondPrizeAmount");//二等奖金额
        String winningAmount = twoColorBallMap.get("winningAmount");//中奖金额
        String red1 = number.substring(0, 2);
        String red2 = number.substring(2, 4);
        String red3 = number.substring(4, 6);
        String red4 = number.substring(6, 8);
        String red5 = number.substring(8, 10);
        String red6 = number.substring(10, 12);
        String blue1 = number.substring(12);
        TwoColorBall twoColorBall = new TwoColorBall(number, red1, red2, red3, red4, red5, red6, blue1, "0", 0);
        twoColorBall.setPkId(KeyUtils.uuid());
        twoColorBall.setAwardDate(awardDate);
        twoColorBall.setIssueNumber(issueNumber);
        twoColorBall.setBonusPool(Long.parseLong(bonusPool));
        twoColorBall.setTotalBets(Long.parseLong(totalBets));
        twoColorBall.setPrice(Integer.parseInt(StringUtils.isBlank(price) ? "0" : price));
        twoColorBall.setFirstPrizeNumber(Integer.parseInt(firstPrizeNumber));
        twoColorBall.setFirstPrizeAmount(Integer.parseInt(firstPrizeAmount));
        twoColorBall.setSecondPrizeNumber(Integer.parseInt(secondPrizeNumber));
        twoColorBall.setSecondPrizeAmount(Integer.parseInt(secondPrizeAmount));
        twoColorBall.setWinningAmount(Integer.parseInt(StringUtils.isBlank(winningAmount) ? "0" : winningAmount));
        return twoColorBall;
    }

}
