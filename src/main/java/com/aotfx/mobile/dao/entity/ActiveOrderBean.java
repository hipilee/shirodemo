package com.aotfx.mobile.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("active_orders")
public class ActiveOrderBean implements Cloneable {

    private Long userId;
    private String user;

    //    订单编号
    private Long orderNumber;

    //    开仓信息
    private Date openTime;
    private Integer type;
    private Double size;
    private String symbol;
    private BigDecimal openPrice;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;

    //    平仓信息
    private BigDecimal currentPrice;
    private BigDecimal commission;

    private BigDecimal taxes;
    private BigDecimal swap;
    private BigDecimal profit;
    private String comment;

    private Date createTime;
    private Date updateTime;

    public static class Builder {
        private ActiveOrderBean activeOrderBean;

        public Builder(Long userId, String user, Long orderNumber) {
            this.activeOrderBean = new ActiveOrderBean();
            this.activeOrderBean.setUserId(userId);
            this.activeOrderBean.setUser(user);
            this.activeOrderBean.setOrderNumber(orderNumber);
        }

        public ActiveOrderBean.Builder openTime(Date openTime) {
            this.activeOrderBean.setOpenTime(openTime);
            return this;
        }

        public ActiveOrderBean.Builder type(Integer type) {
            this.activeOrderBean.setType(type);
            return this;
        }

        public ActiveOrderBean.Builder size(double size) {
            this.activeOrderBean.setSize(size);
            return this;
        }

        public ActiveOrderBean.Builder symbol(String symbol) {
            this.activeOrderBean.setSymbol(symbol);
            return this;
        }

        public ActiveOrderBean.Builder openPrice(BigDecimal openPrice) {
            this.activeOrderBean.setOpenPrice(openPrice);
            return this;
        }

        public ActiveOrderBean.Builder stopLoss(BigDecimal bigDecimal) {
            this.activeOrderBean.setStopLoss(bigDecimal);
            return this;
        }

        public ActiveOrderBean.Builder takeProfit(BigDecimal takaProfit) {
            this.activeOrderBean.setTakeProfit(takaProfit);
            return this;
        }

        public ActiveOrderBean.Builder currentPrice(BigDecimal currentPrice) {
            this.activeOrderBean.setCurrentPrice(currentPrice);
            return this;
        }

        public ActiveOrderBean.Builder commission(BigDecimal commission) {
            this.activeOrderBean.setCommission(commission);
            return this;
        }

        public ActiveOrderBean.Builder taxes(BigDecimal taxes) {
            this.activeOrderBean.setTaxes(taxes);
            return this;
        }

        public ActiveOrderBean.Builder swap(BigDecimal swap) {
            this.activeOrderBean.setSwap(swap);
            return this;
        }

        public ActiveOrderBean.Builder profit(BigDecimal profit) {
            this.activeOrderBean.setProfit(profit);
            return this;
        }

        //comment最多只允许45个字符，如果设计传入的字符过长，则会被截断。
        public ActiveOrderBean.Builder comment(String comment) {
            int endInx = comment.length() > 45 ? 44 : comment.length();
            this.activeOrderBean.setComment(comment.substring(0, endInx));
            return this;
        }

        public ActiveOrderBean build() {
            try {
                try {
                    return (ActiveOrderBean) this.activeOrderBean.clone();
                } finally {

                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
