package com.aotfx.mobile.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-12-03 21:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "shiro_role_permission")
public class RolePermissionVo {
    private String roleId;
    private String permissionId;
    private Date createTime;
    private Date updateTime;
}
