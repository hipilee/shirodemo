package com.aotfx.mobile.dao.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("assets")
public class AccountAssetBean {
    private String user;
    private Double balance;
    private Double equity;
    private Double margin;
    private Double credit;
    private Double free;
    private Double profit_loss;
    private Double deposit;
    private Double withdrawl;
}
