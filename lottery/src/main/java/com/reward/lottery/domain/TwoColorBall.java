package com.reward.lottery.domain;

/**
 * 双色球
 */
public class TwoColorBall {

    private String pkId;
    private String issueNumber;//期号
    private String number;
    private String red1;
    private String red2;
    private String red3;
    private String red4;
    private String red5;
    private String red6;
    private String red7;
    private String red8;
    private String red9;
    private String red10;
    private String red11;
    private String red12;
    private String red13;
    private String red14;
    private String red15;
    private String blue1;
    private String blue2;
    private String blue3;
    private String blue4;
    private String blue5;
    private Long bonusPool; //奖池金额
    private Long totalBets; //总投注额
    private Integer price; //彩票价格
    private Integer firstPrizeNumber; //一等奖注数
    private Integer firstPrizeAmount; //一等奖金额
    private Integer secondPrizeNumber; //二等奖注数
    private Integer secondPrizeAmount; //二等奖金额
    private Integer winningAmount; //中奖金额
    private String type; //0开奖号码，1自购号码， 2推荐号码
    private String awardDate;//开奖日期
    private Integer sort;//序号

    public TwoColorBall() {
    }

    public TwoColorBall(String number, String red1, String red2, String red3, String red4, String red5, String red6, String blue1, String type, Integer sort){
        this.number = number;
        this.red1 = red1;
        this.red2 = red2;
        this.red3 = red3;
        this.red4 = red4;
        this.red5 = red5;
        this.red6 = red6;
        this.blue1 = blue1;
        this.type = type;
        this.sort = sort;
    }

    public TwoColorBall(String number, String red1, String red2, String red3, String red4, String red5, String red6, String blue1, String type, String issueNumber, String awardDate, Integer sort){
        this.number = number;
        this.red1 = red1;
        this.red2 = red2;
        this.red3 = red3;
        this.red4 = red4;
        this.red5 = red5;
        this.red6 = red6;
        this.blue1 = blue1;
        this.type = type;
        this.issueNumber = issueNumber;
        this.awardDate = awardDate;
        this.sort = sort;
    }

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRed1() {
        return red1;
    }

    public void setRed1(String red1) {
        this.red1 = red1;
    }

    public String getRed2() {
        return red2;
    }

    public void setRed2(String red2) {
        this.red2 = red2;
    }

    public String getRed3() {
        return red3;
    }

    public void setRed3(String red3) {
        this.red3 = red3;
    }

    public String getRed4() {
        return red4;
    }

    public void setRed4(String red4) {
        this.red4 = red4;
    }

    public String getRed5() {
        return red5;
    }

    public void setRed5(String red5) {
        this.red5 = red5;
    }

    public String getRed6() {
        return red6;
    }

    public void setRed6(String red6) {
        this.red6 = red6;
    }

    public String getRed7() {
        return red7;
    }

    public void setRed7(String red7) {
        this.red7 = red7;
    }

    public String getRed8() {
        return red8;
    }

    public void setRed8(String red8) {
        this.red8 = red8;
    }

    public String getRed9() {
        return red9;
    }

    public void setRed9(String red9) {
        this.red9 = red9;
    }

    public String getRed10() {
        return red10;
    }

    public void setRed10(String red10) {
        this.red10 = red10;
    }

    public String getRed11() {
        return red11;
    }

    public void setRed11(String red11) {
        this.red11 = red11;
    }

    public String getRed12() {
        return red12;
    }

    public void setRed12(String red12) {
        this.red12 = red12;
    }

    public String getRed13() {
        return red13;
    }

    public void setRed13(String red13) {
        this.red13 = red13;
    }

    public String getRed14() {
        return red14;
    }

    public void setRed14(String red14) {
        this.red14 = red14;
    }

    public String getRed15() {
        return red15;
    }

    public void setRed15(String red15) {
        this.red15 = red15;
    }

    public String getBlue1() {
        return blue1;
    }

    public void setBlue1(String blue1) {
        this.blue1 = blue1;
    }

    public String getBlue2() {
        return blue2;
    }

    public void setBlue2(String blue2) {
        this.blue2 = blue2;
    }

    public String getBlue3() {
        return blue3;
    }

    public void setBlue3(String blue3) {
        this.blue3 = blue3;
    }

    public String getBlue4() {
        return blue4;
    }

    public void setBlue4(String blue4) {
        this.blue4 = blue4;
    }

    public String getBlue5() {
        return blue5;
    }

    public void setBlue5(String blue5) {
        this.blue5 = blue5;
    }

    public Long getBonusPool() {
        return bonusPool;
    }

    public void setBonusPool(Long bonusPool) {
        this.bonusPool = bonusPool;
    }

    public Long getTotalBets() {
        return totalBets;
    }

    public void setTotalBets(Long totalBets) {
        this.totalBets = totalBets;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getFirstPrizeNumber() {
        return firstPrizeNumber;
    }

    public void setFirstPrizeNumber(Integer firstPrizeNumber) {
        this.firstPrizeNumber = firstPrizeNumber;
    }

    public Integer getFirstPrizeAmount() {
        return firstPrizeAmount;
    }

    public void setFirstPrizeAmount(Integer firstPrizeAmount) {
        this.firstPrizeAmount = firstPrizeAmount;
    }

    public Integer getSecondPrizeNumber() {
        return secondPrizeNumber;
    }

    public void setSecondPrizeNumber(Integer secondPrizeNumber) {
        this.secondPrizeNumber = secondPrizeNumber;
    }

    public Integer getSecondPrizeAmount() {
        return secondPrizeAmount;
    }

    public void setSecondPrizeAmount(Integer secondPrizeAmount) {
        this.secondPrizeAmount = secondPrizeAmount;
    }

    public Integer getWinningAmount() {
        return winningAmount;
    }

    public void setWinningAmount(Integer winningAmount) {
        this.winningAmount = winningAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(String awardDate) {
        this.awardDate = awardDate;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "TwoColorBall{" +
                "pkId='" + pkId + '\'' +
                ", issueNumber='" + issueNumber + '\'' +
                ", number='" + number + '\'' +
                ", red1='" + red1 + '\'' +
                ", red2='" + red2 + '\'' +
                ", red3='" + red3 + '\'' +
                ", red4='" + red4 + '\'' +
                ", red5='" + red5 + '\'' +
                ", red6='" + red6 + '\'' +
                ", blue1='" + blue1 + '\'' +
                ", type='" + type + '\'' +
                ", awardDate=" + awardDate +
                ", sort=" + sort +
                '}';
    }

    public String toAllString() {
        return "TwoColorBall{" +
                "pkId='" + pkId + '\'' +
                ", issueNumber='" + issueNumber + '\'' +
                ", number='" + number + '\'' +
                ", red1='" + red1 + '\'' +
                ", red2='" + red2 + '\'' +
                ", red3='" + red3 + '\'' +
                ", red4='" + red4 + '\'' +
                ", red5='" + red5 + '\'' +
                ", red6='" + red6 + '\'' +
                ", red7='" + red7 + '\'' +
                ", red8='" + red8 + '\'' +
                ", red9='" + red9 + '\'' +
                ", red10='" + red10 + '\'' +
                ", red11='" + red11 + '\'' +
                ", red12='" + red12 + '\'' +
                ", red13='" + red13 + '\'' +
                ", red14='" + red14 + '\'' +
                ", red15='" + red15 + '\'' +
                ", blue1='" + blue1 + '\'' +
                ", blue2='" + blue2 + '\'' +
                ", blue3='" + blue3 + '\'' +
                ", blue4='" + blue4 + '\'' +
                ", blue5='" + blue5 + '\'' +
                ", bonusPool=" + bonusPool +
                ", totalBets=" + totalBets +
                ", price=" + price +
                ", firstPrizeNumber=" + firstPrizeNumber +
                ", firstPrizeAmount=" + firstPrizeAmount +
                ", secondPrizeNumber=" + secondPrizeNumber +
                ", secondPrizeAmount=" + secondPrizeAmount +
                ", winningAmount=" + winningAmount +
                ", type='" + type + '\'' +
                ", awardDate='" + awardDate + '\'' +
                ", sort=" + sort +
                '}';
    }
}
