package com.aotfx.mobile.beans;


import com.aotfx.mobile.dao.entity.User;
import com.aotfx.mobile.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Description
 * @auther xiutao li
 * @email hipilee@gamil.com leexiutao@foxmail.com
 * @create 2018-11-17 13:04
 */
public class ShiroRealm extends AuthorizingRealm {
    @Autowired

    private UserService baseService;

    private SimpleAuthenticationInfo info = null;

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
        Map<String, Object> userInfo = baseService.queryInfoByTelephone(telephone);

        if (userInfo != null) {
            // 如果查询到了，封装查询结果，返回给我们的调用
            Object principal = userInfo.get("telephone");
            Object credentials = userInfo.get("password");

            // 获取盐值，即用户名
            ByteSource salt = ByteSource.Util.bytes(telephone);
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
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        //获取session中的用户
//        User user = (User) principal.fromRealm(this.getClass().getName()).iterator().next();
//        List<String> permissions = new ArrayList<>();
//        Set<Role> roles = user.getRoles();
//        if (roles.size() > 0) {
//            for (Role role : roles) {
//                Set<Module> modules = role.getModules();
//                if (modules.size() > 0) {
//                    for (Module module : modules) {
//                        permissions.add(module.getMname());
//                    }
//                }
//            }
//        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //将权限放入shiro中.
//        info.addStringPermissions(permissions);
        return info;
    }


}
