package com.aotfx.mobile.dao.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="mt4_accounts")
public class Mt4Account {
    private Long telephone;
    private String user;
    private String broker;
    private String password;
    private Integer status;
    private Integer timeZoneOffset;

}
