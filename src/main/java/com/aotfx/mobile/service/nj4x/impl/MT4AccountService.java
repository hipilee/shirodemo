package com.aotfx.mobile.service.nj4x.impl;

import com.aotfx.mobile.dao.entity.Mt4Account;
import com.aotfx.mobile.dao.mapper.Mt4AccountMapper;
import com.aotfx.mobile.service.nj4x.IMT4AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MT4AccountService extends ServiceImpl<Mt4AccountMapper, Mt4Account> implements IMT4AccountService {
}
