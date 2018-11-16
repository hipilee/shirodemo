package com.aotfx.mobile.dao.mapper;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 将数据封装到Map类型中
     */
    public Map<String, Object> queryInfoByTelphone(String username);

    /**
     * 插入一条数据
     * @param data Map中包含id,username,password
     */
    public void insertUser(Map<String, String> data);
}
