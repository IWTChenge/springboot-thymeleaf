package com.spc.server.Controller;


import com.spc.server.pojo.User;
import com.spc.server.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 使用set方式注入
     * 缺点：将不能将属性设置为final
     */

//    @Autowired
//    public void setUserService(UserService us) {
//        userService = us;
//    }
    /**
     * 使用构造器注入
     * 缺点 ：依赖多的时候，构造函数的参数个数可能会长到无法想像

     @Autowired public UserController(UserService userService) {
     this.userService = userService;
     }
     */

    /**
     * 属性（field注入） 不推荐
     缺点：如果不使用Spring框架，这个属性只能通过反射注入，太麻烦了！这根本不符合JavaBean规范。

     还有，当你不是用过Spring创建的对象时，还可能引起NullPointerException。
     并且，你不能用final修饰这个属性
     @Autowired private UserService userService;
     */

    /**
     * @return users
     */
    @RequiresPermissions("userInfo:view")
    @RequestMapping(value = "/get_all_user", method = RequestMethod.GET)
    public JsonResult<List<User>> getAllUser() {
        Session session = SecurityUtils.getSubject().getSession();
        System.out.println(session.getAttributeKeys());
        List<User> users;
        users = userService.getAllUser();

        return new JsonResult<>(users);
    }


    /**
     * 增加用户
     *
     * @param user
     * @return
     */
    @RequiresRoles(value = {"admin", "user"}, logical = Logical.OR)
    @RequestMapping(value = "/adduser", method = {RequestMethod.PUT})
    public int addUsers(User user) {

        //List<User> users = new ArrayList<>(Arrays.asList(user));
        return userService.addOneUser(user);
    }

    /**
     * 增加用户
     *
     * @param users
     * @return
     */
    @RequestMapping(value = "/addusers", method = {RequestMethod.PUT})
    public String addListUsers(@RequestBody User[] users) {

        //List<User> users = new ArrayList<>(Arrays.asList(user));
        return userService.addUsers(users);
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteUser/{userId}", method = {RequestMethod.PUT})
    public String deleteUser(@PathVariable(value = "userId") int id) {

        //List<User> users = new ArrayList<>(Arrays.asList(user));
        return userService.DeleteUser(id);
    }

    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/selectByUserId", method = {RequestMethod.GET})
    public User getUserById(@RequestParam(value = "userId") int id) {

        //List<User> users = new ArrayList<>(Arrays.asList(user));
        return userService.selectUser(id);
    }

    /**
     * 查询用户
     *
     * @param name
     * @return
     */

    @RequestMapping(value = "/selectByUserName", method = {RequestMethod.GET})
    public JsonResult<User> getUserByName(@RequestParam(value = "username") String name, HttpServletResponse response) {

        //List<User> users = new ArrayList<>(Arrays.asList(user));
        User us = userService.selectUserByColumn("username", name);
        if (us == null) {
            return new JsonResult<>(response.getStatus(), "不存在该用户");
        }
        return new JsonResult<>(us);
    }
}
