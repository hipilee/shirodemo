package com.aotfx.mobile.service.impl;


import com.aotfx.mobile.dao.mapper.UserMapper;
import com.aotfx.mobile.service.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;



    @Override
    public Map<String, Object> queryInfoByTelephone(String telephone) {
        return userMapper.queryInfoByTelphone(telephone);
    }

    @Override
    public boolean registerUser(String telephone, String user_name, String password) {


        // 将电话号码（主键唯一不变）作为盐值
        ByteSource salt = ByteSource.Util.bytes(telephone);

        /*
        * MD5加密：使用SimpleHash类对原始密码进行加密。
        * @param algrithm Name 代表使用MD5方式加密
        * @param password 原始密码
        * @param salt 盐值，即电话号码
        * @param hashIterations 加密次数
        * 最后用toHex()方法将加密后的密码转成String
        * */
        String newPs = new SimpleHash("MD5", password, salt, 1024).toHex();

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("telephone", telephone);
        dataMap.put("user_name", user_name);
        dataMap.put("password", newPs);

        // 看数据库中是否存在该账户
        Map<String, Object> userInfo = queryInfoByTelephone(telephone);
        if(userInfo == null) {
            userMapper.insertUser(dataMap);
            return true;
        }
        return false;
    }
}
