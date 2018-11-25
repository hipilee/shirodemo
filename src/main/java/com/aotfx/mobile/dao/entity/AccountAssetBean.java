package com.aotfx.mobile.dao.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("assets")
public class AccountAssetBean implements Cloneable {
    @TableField(value = "telephone")
    private Long telephone;

    @TableField(value = "user")
    private String user;

    @TableField(value = "balance")
    private BigDecimal balance;

    @TableField(value = "equity")
    private BigDecimal equity;

    @TableField(value = "free")
    private BigDecimal free;

    @TableField(value = "margin")
    private BigDecimal margin;

    @TableField(value = "credit")
    private BigDecimal credit;

    @TableField(value = "deposit")
    private BigDecimal deposit;

    @TableField(value = "withdrawal")
    private BigDecimal withdrawal;

    @TableField(value = "profit_loss")
    private BigDecimal profitLoss;

    @TableField(value = "yesterday_profit_loss")
    private BigDecimal yesterdayProfitLoss;

    public static class Builder {

        private AccountAssetBean accountAssetBean;

        public Builder(Long telephone, String user) {
            accountAssetBean = new AccountAssetBean();
            accountAssetBean.setTelephone(telephone);
            accountAssetBean.setUser(user);
        }

        public Builder balance(BigDecimal balance) {
            accountAssetBean.setBalance(balance);
            return this;
        }

        public Builder equity(BigDecimal equity) {
            accountAssetBean.setEquity(equity);
            return this;
        }

        public Builder free(BigDecimal free) {
            accountAssetBean.setFree(free);
            return this;
        }

        public Builder margin(BigDecimal margin) {
            accountAssetBean.setMargin(margin);
            return this;
        }

        public Builder credit(BigDecimal credit) {
            accountAssetBean.setCredit(credit);
            return this;
        }

        public Builder deposit(BigDecimal deposit) {
            accountAssetBean.setDeposit(deposit);
            return this;
        }

        public Builder withdrawal(BigDecimal withdrawal) {
            accountAssetBean.setWithdrawal(withdrawal);
            return this;
        }

        public Builder profitLoss(BigDecimal profitLoss) {
            accountAssetBean.setProfitLoss(profitLoss);
            return this;
        }

        public Builder yesterdayProfitLoss(BigDecimal yesterdayProfitLoss) {
            accountAssetBean.setYesterdayProfitLoss(yesterdayProfitLoss);
            return this;
        }

        public AccountAssetBean build() {
            try {
                try {
                    return (AccountAssetBean) this.accountAssetBean.clone();
                } finally {

                }
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
