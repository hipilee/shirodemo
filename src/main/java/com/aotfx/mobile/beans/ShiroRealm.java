package com.aotfx.mobile.beans;


import com.aotfx.mobile.common.utils.RSA;
import com.aotfx.mobile.dao.entity.*;
import com.aotfx.mobile.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-17 13:04
 */
public class ShiroRealm extends AuthorizingRealm {
    private final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IUserRoleVoService iUserRoleVoService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IRolePermissionVoService iRolePermissionVoService;

    @Autowired
    private IPermissionService iPermissionService;

    private SimpleAuthenticationInfo info = null;

    private SimpleAuthorizationInfo simpleAuthorizationInfo = null;

    /**
     * 1.doGetAuthenticationInfo，获取认证消息，如果数据库中没有数，返回null，如果得到了正确的用户名和密码，
     * 返回指定类型的对象
     * <p>
     * 2.AuthenticationInfo 可以使用SimpleAuthenticationInfo实现类，封装正确的用户名和密码。
     * <p>
     * 3.token参数 就是我们需要认证的token
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 将token装换成UsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;

        // 获取用户名即可
        String telephone = upToken.getUsername();

        // 查询数据库，是否查询到用户名和密码的用户
        QueryWrapper<User> isExistByTelephone = new QueryWrapper<>();
        String encrypt_telephone;
        try {
            encrypt_telephone = Base64.encodeBase64String(RSA.encryptByPrivateKey(telephone.getBytes(), RSA.getAotfxPrivateKey()));

        } catch (Exception e) {
            logger.error("对 " + telephone + " 私钥加密异常！");
            throw new AuthenticationException("对 " + telephone + " 私钥加密异常！");
        }
        isExistByTelephone.eq("encrypt_telephone", encrypt_telephone);
        User user = iUserService.getOne(isExistByTelephone);

        if (user != null) {
            // 如果查询到了，封装查询结果，返回给我们的调用
            Object principal = null;
            try {
                principal = new String(RSA.decryptByPublicKey(Base64.decodeBase64(user.getEncryptTelephone()),RSA.getAotfxPublicKey()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Object credentials = user.getPassword();

            // 获取盐值，即用户名
            ByteSource salt = ByteSource.Util.bytes(user.getTelephone());
            String realmName = this.getName();

            // 将账户名，密码，盐值，realmName实例化到SimpleAuthenticationInfo中交给Shiro来管理
            info = new SimpleAuthenticationInfo(principal, credentials, salt, realmName);
        } else {
            // 如果没有查询到，抛出一个异常
            throw new AuthenticationException();
        }

        return info;
    }


    //授权
    //@RequiresPermissions注解;SecurityUtils.getSubject().isPermitted()触发该函数执行,在完成该函数的时候，
    // 没有通过级联的方式进行查询，而是完全使用mybatis-plus的查询方式。实现零XML。
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {

        //获取当前用户的主键
        simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        String telephone = principal.getPrimaryPrincipal().toString();


        //查找出当前电话对应在数据库里面的人
        QueryWrapper<User> isExistByTelephone = new QueryWrapper<>();
        String encrypt_telephone = null;
        try {
            encrypt_telephone = Base64.encodeBase64String(RSA.encryptByPrivateKey(telephone.getBytes(), RSA.getAotfxPrivateKey()));

        } catch (Exception e) {
            logger.error("对 " + telephone + " 私钥加密异常！");
        }
        isExistByTelephone.eq("encrypt_telephone", encrypt_telephone);
        final User userFromDb = iUserService.getOne(isExistByTelephone);


        //查询出用户角色表

        //1 查询出所有的用户角色
        QueryWrapper<UserRoleVo> userRoleVoQueryWrapper = new QueryWrapper<>();
        userRoleVoQueryWrapper.eq("user_id", userFromDb.getUserId());
        List<UserRoleVo> userRoleVoList = iUserRoleVoService.list(userRoleVoQueryWrapper);

        //2 组装出当前用户，所有角色的主键列表
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        List<String> rolesList = new Vector<>();
        for (UserRoleVo userRoleVo : userRoleVoList) {
            rolesList.add(userRoleVo.getRoleId());
        }

        //3 查询出当前用户，所有角色。这里要小心没有任何角色的情况。这个时候如果直接执行查询语句会查询出所有的信息。
        roleQueryWrapper.in("role_id", rolesList);
        List<Role> roleList = rolesList.size() == 0 ? new Vector<>() : iRoleService.list(roleQueryWrapper);

        //4 将所有的角色字符串添加到shiro中进行管理
        for (Role r : roleList) {
            //添加角色，将角色放入shiro中.
            simpleAuthorizationInfo.addRole(r.getRoleName());
        }

        List<String> permissionsNameList = new Vector<>();

        //5 依次获取每个角色的权限
        for (Role r : roleList) {

            //5 查询出当前角色的角色权限列表
            QueryWrapper<RolePermissionVo> rolePermissionVoUpdateWrapper = new QueryWrapper<>();
            rolePermissionVoUpdateWrapper.eq("role_id", r.getRoleId());
            List<RolePermissionVo> rolePermissionVoList = iRolePermissionVoService.list(rolePermissionVoUpdateWrapper);

            //6 组装出当前角色，所有权限的主键列表
            List<String> permissionIdList = new Vector<>();
            for (RolePermissionVo rolePermissionVo : rolePermissionVoList) {
                permissionIdList.add(rolePermissionVo.getPermissionId());
            }

            //7 查询出当前角色所有权限
            QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper();
            permissionQueryWrapper.in("permission_id", permissionIdList);
            List<Permission> permissionList = permissionIdList.size() == 0 ? new Vector<>() : iPermissionService.list(permissionQueryWrapper);
            for (Permission permission : permissionList) {
                permissionsNameList.add(permission.getPermissionName());
            }
        }
        //8 添加权限，将权限放入shiro中.
        simpleAuthorizationInfo.addStringPermissions(permissionsNameList);

        return simpleAuthorizationInfo;
    }
}
