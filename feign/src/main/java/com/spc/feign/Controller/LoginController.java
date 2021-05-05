package com.spc.feign.Controller;

import com.spc.feign.API.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private Login login;
    @RequestMapping(value = "/login.action")
    public JsonResult login(String username, String password){
        return login.login_home(username,password);
    }
    @RequestMapping(value = "/user/get_all_user")
    public JsonResult getAllUser(){
        return login.getAllUser();
    }
    @RequestMapping("/logout")
    public JsonResult logout(String token){
        return login.logout(token);
    }

}
