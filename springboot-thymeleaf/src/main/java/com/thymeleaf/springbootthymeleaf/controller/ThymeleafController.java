package com.thymeleaf.springbootthymeleaf.controller;

import com.thymeleaf.springbootthymeleaf.entiy.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ThymeleafController {
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String show(Model model){
        User user=new User();
        user.setId("9527");
        user.setName("007");
        model.addAttribute("user",user);
        //model.addAttribute("name","chenge");
        return "index";
    }
}
