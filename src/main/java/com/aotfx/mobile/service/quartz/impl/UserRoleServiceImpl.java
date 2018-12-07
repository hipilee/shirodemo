package com.aotfx.mobile.service.quartz.impl;

import com.aotfx.mobile.dao.entity.UserRoleVo;
import com.aotfx.mobile.dao.mapper.UserRoleVoMapper;
import com.aotfx.mobile.service.quartz.UserRoleVoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleVoMapper, UserRoleVo> implements UserRoleVoService {

}
