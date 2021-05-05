package com.spc.server.service.impl;

import com.spc.server.dao.UserDao;
import com.spc.server.pojo.*;
import com.spc.server.service.UserService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    //5小时后过期
    private final static int EXPIRE = 1000 * 60 * 15;

//    @Autowired
//    public void setUserDao(UserDao ud) {
//        this.userDao = ud;
//    }

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public User verifyUser(String username, String password) {

        if (password != null && username != null) {
            if (!"".equals(password.trim()) && !"".equals(username.trim())) {
//                User user = new User("", "", "", "");
//                user.setUsername(username);
//                user.setPassword(password);
                // return userDao.searchUser(user);
            }

        }
        return null;

    }

    @Override
    public String addUsers(User[] users) {
        userDao.addUser(users);
        return "ok";
    }

    @Override
    public int addOneUser(User user) {
        int i = userDao.addOneUser(user);
        System.out.println(">>>>>>>>>>>" + i);
        return i;
    }


    @Override
    public String DeleteUser(int id) {
        User us = userDao.selectByUserId(id);
        if (us == null) {
            return "该用户不存在";
        }
        int isSuccess = userDao.DeleteUser(id);
        if (isSuccess < 1) {
            return "删除失败";
        }
        return "删除成功";
    }

    @Override
    public String update(User user) {
        return null;
    }

    @Override
    public User selectUser(int id) {

        return userDao.selectByUserId(id);

    }

    @Override
    public User selectUserByColumn(String column, String value) {
        return userDao.selectByColumn(column, value);
    }

    @Override
    public Set<String> findRoleByUsername(String userName) {
        return null;
    }

    @Override
    public Set<String> findPermissionByRole(Set<String> roles) {

        Set<String> set ;
        set=userDao.findPermissionByRole(roles);
        return set;
    }

    @Override
    public AuthToken createToken(String userName) {
        long salt = System.currentTimeMillis();
        System.out.println("salt>>>>>>>>>>>>>>>>>>>" + salt);
        String token = DigestUtils.md5DigestAsHex((userName + salt).getBytes());
        AuthToken authToken = new AuthToken(token);
        authToken.setExpireTime(salt + EXPIRE);

        try {
            User u = userDao.selectByColumn("username", userName);
            int uid = u.getUserId();

            authToken.setUserId(uid);

            userDao.save(authToken);
        } catch (Exception e) {
            e.printStackTrace();
            //抛出异常 事务回滚
            throw new RuntimeException();
        }

        return authToken;
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public AuthToken findByToken(String accessToken) {
        return userDao.findByToken(accessToken);
    }

    @Override
    public Set<String> getRoleByUid(int userId) {
        Set<String> set = new HashSet<String>();
        Set<String> st = userDao.getRoleByUid(userId);
//        SysRole sysRole=new SysRole(1,"admin","admin",true);
//        set.add(sysRole);
        set.add("admin");
        return set;
    }

    @Override
    public void updateExpireTime(AuthToken token) {
        userDao.updateExpireTime(token);
    }

    @Override
    public User verify(String Token) {
        //首先检验token的有效性
        AuthToken at = userDao.findByToken(Token);
        if (at == null) {
            throw new IncorrectCredentialsException("失效token，请登录");
        }
        long now = System.currentTimeMillis();
        if (at.getExpireTime() < now) {
            throw new IncorrectCredentialsException("token过期，请重新登录");
        }
        //更新token过期时间
        at.setExpireTime(now);
        userDao.updateExpireTime(at);
        return selectUser(at.getUserId());
    }

    @Override
    public User selectUserByToken(String token) {
        return userDao.searchUserByToken(token);
    }

    @Override
    public int cleanToken(String token) {
      return   userDao.cleanToken(token);
    }
}
