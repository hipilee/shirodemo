package com.aotfx.mobile.service;

import com.aotfx.mobile.dao.entity.User;
import com.aotfx.mobile.dao.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface IUserService extends IService<User> {

    String registerUser(String telephone, String username, String password,String captcha) throws Exception;

}
