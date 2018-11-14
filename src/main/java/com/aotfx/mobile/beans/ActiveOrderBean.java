package com.aotfx.mobile.beans;

import java.util.Date;

public class ActiveOrderBean {
    public ActiveOrderBean(String user, long orderNumber, Date openTime, String type, double size, String symbol, double openPrice, double sellLoss, double takeProfit, double currentPrice, String comment, double commission, double swap, double profit) {
        this.user = user;
        this.orderNumber = orderNumber;
        this.openTime = openTime;
        this.type = type;
        this.size = size;
        this.symbol = symbol;
        this.openPrice = openPrice;
        this.sellLoss = sellLoss;
        this.takeProfit = takeProfit;
        this.currentPrice = currentPrice;
        this.comment = comment;
        this.commission = commission;
        this.swap = swap;
        this.profit = profit;
    }

    private String user;

    //    订单编号
    private long orderNumber;

    //    开仓信息
    private Date openTime;
    private String type;
    private double size;
    private String symbol;
    private double openPrice;


    //    平仓信息
    private double sellLoss;
    private double takeProfit;
    private double currentPrice;
    private String comment;
    private double commission;
    private double taxes;
    private double swap;
    private double profit;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getSellLoss() {
        return sellLoss;
    }

    public void setSellLoss(double sellLoss) {
        this.sellLoss = sellLoss;
    }

    public double getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(double takeProfit) {
        this.takeProfit = takeProfit;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public double getSwap() {
        return swap;
    }

    public void setSwap(double swap) {
        this.swap = swap;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
