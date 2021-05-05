package com.spc.server.Controller;

import com.spc.server.pojo.AuthToken;
import com.spc.server.pojo.User;
import com.spc.server.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.bouncycastle.jcajce.provider.symmetric.ARC4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    UserService us;
    @Value("${server.port}")
    String port;

    @RequestMapping(value = "/login.action", method = RequestMethod.POST)
    public JsonResult login(String username, String password) {

        // 获取当前用户
        Subject subject = SecurityUtils.getSubject();
        // 封装用户的登录数据
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println("passwd" + password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {

            subject.login(token);
            //创建认证用的token
            AuthToken au = us.createToken(token.getUsername());
            System.out.println("login.action" + au.getToken());
            return new JsonResult<>(au.getToken());

        } catch (UnknownAccountException e) {
            // model.addAttribute("msg", "用户名不存在");
            System.out.println("用户名不存在");
            return new JsonResult<>("用户名不存在");
        } catch (IncorrectCredentialsException e) {
            // model.addAttribute("msg", "密码错误");
            System.out.println("密码错误");
            return new JsonResult<>("密码错误");
        }

    }

    @RequestMapping("/noauth")
    public JsonResult unauthorized() {
        //  return "未授权无法访问此页面";
        return new JsonResult<>("未授权无法访问此页面");
    }

    @RequestMapping("/logout")
        public JsonResult logout(String token) throws AuthenticationException {
        try {
            us.verify(token);
        } catch (Exception e) {
            return new JsonResult<>("无效token");
        }
        us.cleanToken(token);

        return new JsonResult<>("您已安全退出系统");
    }

}
