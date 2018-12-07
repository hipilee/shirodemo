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
 * @create 2018-12-03 20:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "shiro_permission")
public class Permission {
    @TableId(value = "permission_id")
    private String permissionId;
    private String permissionName;
    private String permissionRemarks;

    @TableField(exist = false)
    private List<Role> permissionList;
    private Date createTime;
    private Date updateTime;
}
