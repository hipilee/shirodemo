package com.aotfx.mobile.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : lqf
 * @Description :
 * @date : Create in 18:14 2018/10/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVo {

    private Long userId;

//    /**
//     * 创建时间
//     */
//    private LocalDateTime createTime;
//
//    /**
//     * 修改时间
//     */
//    private LocalDateTime updateTime;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

//    /**
//     * 姓名
//     */
//    private String realname;
//
//    /**
//     * 性别(0为女 1为男)
//     */
//    private Integer sex;
//
//    /**
//     * 手机号
//     */
//    private String mobile;
//
//    /**
//     * 密码加密串
//     */
//    private String passwordMd5;
//
//    /**
//     * 开账号人
//     */
//    private Long parentId;
//
//    /**
//     * 是否禁用 0否 1是
//     */
//    private Integer status;
//
//    /**
//     * 授权大区
//     */
//    private String authArea;
//
//    /**
//     * 授权城市
//     */
//    private String authCity;
//
//    private Integer role;
//
//    /**
//     * 用户剩余卡数
//     */
//    private Integer residueCardNumber;
//
//    /**
//     * 最后登录时间
//     */
//    private LocalDateTime lastLoginTime;
//
//    /**
//     * 最后登录ip地址
//     */
//    private String lastLoginIp;
//
//    /**
//     * 最后登录次数
//     */
//    private Integer lastLoginCount;
//
//    /**
//     * 渠道
//     */
//    private String authChannel;
//
//    /**
//     * 0 外网 1 内网
//     */
//    private Integer internet;
//
//    /**
//     * 金币
//     */
//    private Long goldCoin;
//
//    private Long id;
//
//    /**
//     * 状态
//     */
//    private String statusId;
//
//    /**
//     * 角色名
//     */
    private String roleName;
//
//    /**
//     * 角色值
//     */
//    private String roleValue;
//
//    /**
//     * 能添加的下属角色值
//     */
//    private String addibleValue;
}
