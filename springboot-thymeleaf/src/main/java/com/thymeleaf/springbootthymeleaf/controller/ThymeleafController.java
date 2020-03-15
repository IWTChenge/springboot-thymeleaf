package com.thymeleaf.springbootthymeleaf.controller;

import com.thymeleaf.springbootthymeleaf.entiy.Student;
import com.thymeleaf.springbootthymeleaf.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@Controller
public class ThymeleafController {
    private StudentService ss;
    @Autowired
    public void setStudentService(StudentService studentservice){
        this.ss=studentservice;
    }
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String show(Model model){
      List<Student> ls= ss.selectAll();
        model.addAttribute("ls",ls);
        //model.addAttribute("name","chenge");
        return "index";
    }
    @RequestMapping(value = "welcome", method = RequestMethod.GET)
    public String welcome(Model model){

        return "login";
    }
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(Model model){

        return "login";
    }
}
