package com.aotfx.mobile.dao.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value="mt4_accounts")
public class Mt4Account {
    private Long userId;
    private String user;
    private String broker;
    private String password;
    private Integer status;
    private Integer timeZoneOffset;
    private Date createTime;
    private Date updateTime;
}
