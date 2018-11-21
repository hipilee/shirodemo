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
public class AccountAssetBean {
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
}
