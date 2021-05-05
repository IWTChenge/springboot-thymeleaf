package com.spc.feign.API;

import com.spc.feign.Controller.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi")
public interface Login {
    @RequestMapping(value = "/login.action", method = RequestMethod.POST)
        //@RequestParam 注解必须写
    JsonResult login_home(@RequestParam String username, @RequestParam String password);

    @RequestMapping(value = "/user/get_all_user", method = RequestMethod.GET)
        //@RequestParam 注解必须写
    JsonResult getAllUser();

    @RequestMapping("/logout")
    public JsonResult logout(@RequestParam String token);
}
