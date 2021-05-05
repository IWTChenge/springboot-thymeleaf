package com.spc.myfeign.Controller;

import com.spc.myfeign.API.SchedualServiceHi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @Autowired
    SchedualServiceHi schedualServiceHi;
    @RequestMapping(value = "/hi/{name}",method = RequestMethod.GET)
    public String sayHi(@PathVariable String name){
        return schedualServiceHi.sayHiFromClientOne(name);
    }
}