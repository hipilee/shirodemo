package com.aotfx.mobile.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("histroy_orders")
public class HistroyOrderBean {
//    订单编号
    private long orderNumber;

//    开仓信息
    private Date openTime;
    private String type;
    private double size;
    private String symbol;
    private double openPrice;


//    平仓信息
    private Date closeTime;
    private double closePrice;
    private double commission;
    private double taxes;
    private double swap;
    private double profit;
    private String comment;


}
