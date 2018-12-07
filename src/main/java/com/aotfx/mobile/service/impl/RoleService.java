package com.aotfx.mobile.service.impl;

import com.aotfx.mobile.dao.entity.Permission;
import com.aotfx.mobile.dao.entity.Role;
import com.aotfx.mobile.dao.mapper.PermissionMapper;
import com.aotfx.mobile.dao.mapper.RoleMapper;
import com.aotfx.mobile.service.IPermissionService;
import com.aotfx.mobile.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-12-03 20:57
 */
@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> implements IRoleService {
}
