package com.aotfx.mobile.dao.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("histroy_orders")
public class HistoryOrderBean implements Cloneable {

    private Long telephone;
    private String user;

    //    订单编号
    private long orderNumber;

    //    开仓信息
    private Date openTime;
    private Integer type;
    private double size;
    private String symbol;
    private BigDecimal openPrice;
    private BigDecimal stopLoss;
    private BigDecimal takeProfit;


    //    平仓信息
    private Date closeTime;
    private BigDecimal closePrice;
    private BigDecimal commission;
    private BigDecimal taxes;
    private BigDecimal swap;
    private BigDecimal profit;
    private String comment;

    public static class Builder {

        private HistoryOrderBean historyOrderBean;

        public Builder(Long telephone, String user, long orderNumber) {
            historyOrderBean = new HistoryOrderBean();
            historyOrderBean.setTelephone(telephone);
            historyOrderBean.setUser(user);
            historyOrderBean.setOrderNumber(orderNumber);
        }

        public Builder openTime(Date openTime) {
            historyOrderBean.setOpenTime(openTime);
            return this;
        }

        public Builder type(Integer type) {
            historyOrderBean.setType(type);
            return this;
        }

        public Builder size(double size) {
            historyOrderBean.setSize(size);
            return this;
        }

        public Builder symbol(String symbol) {
            historyOrderBean.setSymbol(symbol);
            return this;
        }

        public Builder openPrice(BigDecimal openPrice) {
            historyOrderBean.setOpenPrice(openPrice);
            return this;
        }

        public Builder stopLoss(BigDecimal bigDecimal) {
            historyOrderBean.setStopLoss(bigDecimal);
            return this;
        }

        public Builder takeProfit(BigDecimal takaProfit) {
            historyOrderBean.setTakeProfit(takaProfit);
            return this;
        }

        public Builder closeTime(Date closeTime) {
            historyOrderBean.setCloseTime(closeTime);
            return this;
        }

        public Builder closePrice(BigDecimal closePrice) {
            historyOrderBean.setClosePrice(closePrice);
            return this;
        }

        public Builder commission(BigDecimal commission) {
            historyOrderBean.setCommission(commission);
            return this;
        }

        public Builder taxes(BigDecimal taxes) {
            historyOrderBean.setTaxes(taxes);
            return this;
        }

        public Builder swap(BigDecimal swap) {
            historyOrderBean.setSwap(swap);
            return this;
        }

        public Builder profit(BigDecimal profit) {
            historyOrderBean.setProfit(profit);
            return this;
        }

        public Builder comment(String comment) {
            int endInx = comment.length() > 45 ? 44 : comment.length();
            historyOrderBean.setComment(comment.substring(0, endInx));
            return this;
        }


        public HistoryOrderBean build() {
            try {
                try {
                    return (HistoryOrderBean) this.historyOrderBean.clone();
                } finally {

                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
