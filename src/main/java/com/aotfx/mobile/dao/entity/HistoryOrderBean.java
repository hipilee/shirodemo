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
public class HistoryOrderBean {

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



}
