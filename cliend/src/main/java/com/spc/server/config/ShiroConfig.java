package com.spc.server.config;

import com.spc.server.dao.UserDao;
import com.spc.server.pojo.AuthToken;
import com.spc.server.pojo.User;
import com.spc.server.service.UserService;
import com.spc.server.service.impl.UserServiceImpl;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShiroConfig {
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        /**
         *Filter Name	Class
         * anon	    org.apache.shiro.web.filter.authc.AnonymousFilter
         * authc	org.apache.shiro.web.filter.authc.FormAuthenticationFilter
         * authcBasic	org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter
         * perms	org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter
         * port	    org.apache.shiro.web.filter.authz.PortFilter
         * rest	    org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter
         * roles	org.apache.shiro.web.filter.authz.RolesAuthorizationFilter
         * ssl	    org.apache.shiro.web.filter.authz.SslFilter
         * user	    org.apache.shiro.web.filter.authc.UserFilter
         */

        filterMap.put("authc", new CORSAuthenticationFilter());
        //给filter设置安全管理器
        shiroFilter.setSecurityManager(securityManager);

        //拦截器.添加shiro内置过滤器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 顺序判断
        /*
         * anno：无需认证就可以访问
         * authc：必须认证才能访问
         * user：必须拥有记住我功能才能访问
         * perms：拥有某个资源的权限才能访问
         * role：拥有某个角色的权限才能访问
         * */

        filterChainDefinitionMap.put("/static/**", "anon");
        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        //  filterChainDefinitionMap.put("/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        filterChainDefinitionMap.put("/login.action", "anon");
        filterChainDefinitionMap.put("/user/selectByUserName", "perms[userInfo:view]");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilter.setLoginUrl("/login.action");
        shiroFilter.setUnauthorizedUrl("/noauth");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);
        //自定义过滤器
        shiroFilter.setFilters(filterMap);
        System.out.println("shirFilter");

        return shiroFilter;
    }



    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setSessionManager(sessionManager());
       // securityManager.setSubjectDAO();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    //自定义sessionManager
    @Bean("sessionManager")
    public SessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        //设置session过期时间3600s
        defaultWebSessionManager.setGlobalSessionTimeout(3600000L);
        return defaultWebSessionManager;
    }

    /**
     * 注册全局异常处理
     *
     * @return
     */
//    @Bean(name = "exceptionHandler")
//    public HandlerExceptionResolver handlerExceptionResolver() {
//        return new MyExceptionHandler();
//    }
//
//    public MyExceptionHandler MyExceptionHandler() {
//        return new MyExceptionHandler();
//    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     * ）
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }

    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();
        //myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }


    /**
     * Shiro生命周期处理器:
     * 用于在实现了Initializable接口的Shiro bean初始化时调用Initializable接口回调(例如:UserRealm)
     * 在实现了Destroyable接口的Shiro bean销毁时调用 Destroyable接口回调(例如:DefaultSecurityManager)
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 启用shrio授权注解拦截方式，AOP式方法级权限检查
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
                new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 解决 因为 @RequiresPermissions/Role 导致404
     */
    @Bean
    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {

        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 无权限异常捕捉
     * @return
     */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();

        properties.setProperty("org.apache.shiro.authz.UnauthorizedException", "/noauth");
        resolver.setExceptionMappings(properties);
        return resolver;
    }
}
