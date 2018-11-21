package com.aotfx.mobile.service.nj4x.impl;

import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.dao.mapper.Mt4UserMapper;
import com.aotfx.mobile.service.nj4x.IMT4UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MT4UserService extends ServiceImpl<Mt4UserMapper, Mt4Account> implements IMT4UserService {
}
