package com.spc.myfeign.API;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi")
public interface Login {
    @RequestMapping(value = "/home/login", method = RequestMethod.GET)
    //@RequestParam 注解必须写
     String login_home(@RequestParam String name,@RequestParam String password);
}
