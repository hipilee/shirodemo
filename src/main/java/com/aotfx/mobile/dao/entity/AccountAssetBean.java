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
    private String user;
    private BigDecimal balance;
    private BigDecimal equity;
    private BigDecimal margin;
    private BigDecimal credit;
    private BigDecimal free;
    private BigDecimal totalProfitLoss;
    private BigDecimal yesterdayProfitLoss;
    private BigDecimal todayProfitLoss;
    private BigDecimal deposit;
    private BigDecimal withdrawl;


}
