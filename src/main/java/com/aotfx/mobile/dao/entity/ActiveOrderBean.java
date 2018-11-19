package com.aotfx.mobile.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("active_orders")
public class ActiveOrderBean implements Serializable {

    private String user;

    //    订单编号
    private Long orderNumber;

    //    开仓信息
    private Date openTime;
    private Integer type;
    private Double size;
    private String symbol;
    private Double openPrice;
    private Double stopLoss;
    private Double takeProfit;

    //    平仓信息
    private Double currentPrice;
    private String comment;
    private Double commission;

    private Double taxes;
    private Double swap;
    private Double profit;
}
