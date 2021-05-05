package com.spc.myfeign.API;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "service-hi")
public interface SchedualServiceHi {
    @RequestMapping(value = "/hi/{name}", method = RequestMethod.GET)
    String sayHiFromClientOne(@PathVariable(value = "name") String name);
}
