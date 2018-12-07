package com.aotfx.mobile.dao.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")
public class User {
    private Long userId;
    private String userName;
    private String password;
    private String telephone;
    private String encryptTelephone;
    private Date createTime;
    private Date updateTime;

    @TableField(exist = false)
    private List<Role> roleList;
}
