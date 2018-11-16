package com.aotfx.mobile.dao.mapper;

import com.aotfx.mobile.dao.entity.UserRoleVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleVoMapper extends BaseMapper<UserRoleVo> {
    @Select("SELECT * FROM fy_user u LEFT JOIN fy_role r ON u.role = r.id")
    List<UserRoleVo> selectUserListPage(Page<UserRoleVo> page);
}
