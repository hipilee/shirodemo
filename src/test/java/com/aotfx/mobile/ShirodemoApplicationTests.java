package com.aotfx.mobile;

import com.aotfx.mobile.config.nj4x.Nj4xConfig;
import com.aotfx.mobile.dao.entity.*;
import com.aotfx.mobile.dao.mapper.RoleMapper;
import com.aotfx.mobile.dao.mapper.UserMapper;
import com.aotfx.mobile.service.IPermissionService;
import com.aotfx.mobile.service.IRolePermissionVoService;
import com.aotfx.mobile.service.IRoleService;
import com.aotfx.mobile.service.IUserRoleVoService;
import com.aotfx.mobile.service.impl.RoleService;
import com.aotfx.mobile.service.nj4x.IMT4AccountService;
import com.aotfx.mobile.service.quartz.IJobAndTriggerService;
import com.aotfx.mobile.service.quartz.UserRoleVoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigInteger;
import java.util.List;
import java.util.Vector;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShirodemoApplicationTests {
    @Autowired
    SqlSessionFactory mybatisPlusAutoConfiguration;

    @Autowired
    private IJobAndTriggerService iJobAndTriggerService;

    @Autowired
    private UserRoleVoService userRoleVoService;

    @Autowired
    private IMT4AccountService imt4UserService;

    @Autowired
    private IUserRoleVoService iUserRoleVoService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IRolePermissionVoService iRolePermissionVoService;

    @Autowired
    private IPermissionService iPermissionService;


    @Autowired
    Nj4xConfig nj4xConfig;

    @Autowired
    UserMapper userMapper;

    @Test
    public void contextLoads() {
//		// 当前页，总条数 构造 page 对象
//		Page<JobAndTriggerVo> page = new Page<>(1, 10);
//		page.setRecords(service.getJobAndTriggerDetails(page));
//		System.out.println(page.getRecords().get(0).getJobName());

        // 当前页，总条数 构造 page 对象
//		Page<UserRoleVo> page1 = new Page<>(1, 10);
//		page1.setRecords(service1.selectUserListPage(page1));
//		System.out.println(page1.getRecords().get(0));

//        boolean saveResult = imt4UserService.saveOrUpdate(new Mt4Account("3825688", "Ava-Real 5", "w3kvmbm", 1, "15708470013"));
//        System.out.println(saveResult);
//        Vector<Mt4Account> v = new Vector<>();
//        v.add(new Mt4Account("3825680", "Ava-Real 5", "w3kvmbm", 1, "15708470013"));
//        v.add(new Mt4Account("3825689", "Ava-Real 5", "w3kvmbm", 1, "15708470013"));
//        v.add(new Mt4Account("3825699", "Ava-Real 5", "w3kvmbm", 1, "15708470013"));
//        System.out.println(imt4UserService.saveOrUpdateBatch(v));
//                            imt4UserService.saveOrUpdate(new Mt4Account("80012391", "real5.ava-mt.com", "Lxtcfx8793", 1, "15708470013"));

//        System.out.println(saveResult);


        //查询出用户角色表
        UpdateWrapper<UserRoleVo> userRoleVoUpdateWrapper = new UpdateWrapper<>();
        userRoleVoUpdateWrapper.eq("telephone", 15708470013L);
        List<UserRoleVo> userRoleVoList = iUserRoleVoService.list(userRoleVoUpdateWrapper);


        //获取出角色列表
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        List<String> rolesList = new Vector<>();
        for (UserRoleVo userRoleVo : userRoleVoList) {
            rolesList.add(userRoleVo.getRoleId());
        }
        roleQueryWrapper.in("role_id", rolesList);
        List<Role> roleList = iRoleService.list(roleQueryWrapper);

        for (Role r : roleList) {
            System.out.println(r.getRoleId() + "  " + r.getRoleName());
        }

        List<String> permissionsNameList = new Vector<>();
        //获取出角色权限列表
        for (Role r : roleList) {

            QueryWrapper<RolePermissionVo> rolePermissionVoUpdateWrapper = new QueryWrapper<>();
            rolePermissionVoUpdateWrapper.eq("role_id", r.getRoleId());
            List<RolePermissionVo> rolePermissionVoList = iRolePermissionVoService.list(rolePermissionVoUpdateWrapper);

            List<String> permissionIdList = new Vector<>();
            for (RolePermissionVo rolePermissionVo : rolePermissionVoList) {
                permissionIdList.add(rolePermissionVo.getRoleId());
            }

            QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper();
            permissionQueryWrapper.in("permission_id", permissionIdList);
            List<Permission> permissionList = iPermissionService.list(permissionQueryWrapper);
            for (Permission permission : permissionList) {
                permissionsNameList.add(permission.getPermissionName());
            }
        }
    }

}
