package com.aotfx.mobile.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-12-03 20:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "shiro_role")
public class Role {
    @TableId(value = "role_id")
    private String roleId;
    private String roleName;
    private String roleRemarks;

    @TableField(exist = false)
    private List<User> userList;

    @TableField(exist = false)
    private List<Permission> permissionList;

    private Date createTime;
    private Date updateTime;
}
