package com.spc.server.config;

import com.spc.server.pojo.*;
import com.spc.server.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    // 设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("MyShiroRealm");
    }

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
//    @Override
//    public boolean supports(AuthenticationToken token) {
//        return token instanceof SysToken;
//    }
    //授权 访问控制。比如某个用户是否具有某个操作的使用权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // User userInfo = (User) principalCollection.getPrimaryPrincipal();
        String token = principalCollection.getPrimaryPrincipal().toString();
        User userInfo = userService.selectUserByToken(token);
        try {
            //获取用户角色集
            Set<String> listRole = userService.getRoleByUid(userInfo.getUserId());

            authorizationInfo.addRoles(listRole);
            //通过角色获取权限集

            Set<String> permission = userService.findPermissionByRole(listRole);
            authorizationInfo.addStringPermissions(permission);


        } catch (Exception e) {
            System.out.println("授权失败，请检查系统内部错误!!!" + e.getMessage());
        }

        return authorizationInfo;
    }

    //认证 用户身份识别(登录")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        String token = (String) authenticationToken.getPrincipal();
        System.out.println("token/auth" + authenticationToken.getCredentials());
        User userInfo;
        //区分是登录还是验证 登录token是用户名 验证token是凭证
        if (authenticationToken.getClass().equals(UsernamePasswordToken.class)) {
            System.out.println(">>>>>>>登录");
            userInfo = userService.selectUserByColumn("username", token);
            System.out.println("----->>userInfo=" + userInfo);
            if (userInfo == null) {
                return null;
            }
            if (userInfo.getState() == 2) { //账户冻结
                throw new LockedAccountException();
            }
            List<Object> principals = new ArrayList<>();
            principals.add(userInfo);
            return new SimpleAuthenticationInfo(
                    principals, //用户名
                    userInfo.getPassword(), //密码
                    ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt
                    getName()  //realm name
            );
        } else {
            System.out.println(">>>>>>>验证");
            userService.verify(token);

            return new SimpleAuthenticationInfo(token, token, getName());
        }


    }

}
