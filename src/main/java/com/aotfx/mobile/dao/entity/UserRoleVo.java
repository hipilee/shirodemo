package com.aotfx.mobile.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author : lqf
 * @Description :
 * @date : Create in 18:14 2018/10/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "shiro_user_role")
public class UserRoleVo {
    private String userId;
    private String roleId;
    private Date createTime;
    private Date updateTime;
}
