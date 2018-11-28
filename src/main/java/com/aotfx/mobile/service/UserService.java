package com.aotfx.mobile.service;

import java.util.Map;

public interface UserService {

    /**
     * 根据用户名查询用户信息
     * @param telephone 用户名
     * @return 将数据封装到Map类型中
     */
    public Map<String, Object> queryInfoByTelephone(String telephone);

    /**
     * 注册功能
     * @param telephone
     * @param user_name 用户名
     * @param password 密码
     * @return
     */
    public boolean registerUser(String telephone, String user_name, String password);
}
