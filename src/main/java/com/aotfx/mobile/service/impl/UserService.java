package com.aotfx.mobile.service.impl;


import com.aotfx.mobile.common.utils.RSA;
import com.aotfx.mobile.dao.entity.User;
import com.aotfx.mobile.dao.mapper.UserMapper;
import com.aotfx.mobile.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public String registerUser(String telephone, String username, String password, String captcha) throws Exception{
        StringBuffer res = new StringBuffer();
        String encrypt_telephone;
        try {
            encrypt_telephone = Base64.encodeBase64String(RSA.encryptByPrivateKey(telephone.getBytes(), RSA.getAotfxPrivateKey()));

        } catch (Exception e) {
            logger.error("对 " + telephone + " 私钥加密异常！");
            throw new Exception(e);
        }
        QueryWrapper<User> qw_1 = new QueryWrapper<>();
        qw_1.eq("encrypt_telephone", encrypt_telephone);
        User user_1 = this.baseMapper.selectOne(qw_1);

        QueryWrapper<User> qw_2 = new QueryWrapper<>();
        qw_2.eq("user_name", username);
        User user_2 = this.baseMapper.selectOne(qw_2);
        if(user_1 != null){
            res.append("手机号码已被注册!");
        }
        if(user_2 != null){
            res.append("昵称已经存在!");
        }

        if(res.length() == 0){
            /*
             * MD5加密：使用SimpleHash类对原始密码进行加密。
             * @param algrithm Name 代表使用MD5方式加密
             * @param password 原始密码
             * @param salt 盐值，即电话号码
             * @param hashIterations 加密次数
             * 最后用toHex()方法将加密后的密码转成String
             * */

            User registerUser = new User();

            //user表的主键是自增的
            registerUser.setUserId(null);
            registerUser.setUserName(username);

            //对电话号码中间4位加密
            int length = telephone.length();
            registerUser.setTelephone(telephone.substring(0, 3) + "****" + telephone.substring(length - 4, length));

            //生成盐值，构造新密码
            ByteSource salt = ByteSource.Util.bytes(registerUser.getTelephone());
            String newPs = new SimpleHash("MD5", password, salt, 1024).toHex();
            registerUser.setPassword(newPs);

            registerUser.setCreateTime(new Date());
            registerUser.setUpdateTime(null);
            registerUser.setEncryptTelephone(encrypt_telephone);
            this.baseMapper.insert(registerUser);
            res.append("注册成功");
        }

        return res.toString();
    }
}
