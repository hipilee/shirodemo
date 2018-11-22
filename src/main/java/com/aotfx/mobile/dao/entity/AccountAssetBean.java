package com.aotfx.mobile.dao.entity;


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
    private Long telephone;
    private String user;
    private BigDecimal balance;
    private BigDecimal equity;
    private BigDecimal free;
    private BigDecimal margin;
    private BigDecimal credit;
    private BigDecimal deposit;
    private BigDecimal withdrawal;
    private BigDecimal profitLoss;
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
            accountAssetBean.setBalance(free);
            return this;
        }

        public Builder margin(BigDecimal margin) {
            accountAssetBean.setEquity(margin);
            return this;
        }

        public Builder credit(BigDecimal credit) {
            accountAssetBean.setBalance(credit);
            return this;
        }

        public Builder deposit(BigDecimal deposit) {
            accountAssetBean.setEquity(deposit);
            return this;
        }

        public Builder withdrawal(BigDecimal withdrawal) {
            accountAssetBean.setEquity(withdrawal);
            return this;
        }

        public Builder profitLoss(BigDecimal profitLoss) {
            accountAssetBean.setBalance(profitLoss);
            return this;
        }

        public Builder yesterdayProfitLoss(BigDecimal yesterdayProfitLoss) {
            accountAssetBean.setEquity(yesterdayProfitLoss);
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
